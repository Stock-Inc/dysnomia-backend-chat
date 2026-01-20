package org.example.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.config.FirebaseConfig;
import org.example.backend.dto.MessageDTO;
import org.example.backend.dto.PrivateMessageDTO;
import org.example.backend.models.Conversation;
import org.example.backend.models.Message;
import org.example.backend.services.ConversationService;
import org.example.backend.services.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@Slf4j
@Tag(
        name = "Message Management",
        description = "WebSocket and REST operations for chat messages and notifications"
)
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final FirebaseConfig firebaseConfig;
    private final ConversationService conversationService;
    private final SimpMessagingTemplate messagingTemplate;

    @Operation(
            summary = "Get message history via WebSocket",
            description = "Retrieve last 100 messages through WebSocket connection"
    )
    @MessageMapping("/history")
    @SendTo("/topic/history")
    public List<Message> history() {
        List<Message> messages = messageService.findLast100Message();
        log.info("Sent last 100 messages");
        return messages;
    }

    @Operation(
            summary = "Send public chat message via WebSocket",
            description = "Save new public chat message and broadcast to all subscribers with Firebase notification"
    )
    @MessageMapping("/chat")
    @SendTo("/topic/message")
    public Message savePublicMessage(@Payload MessageDTO messageDTO, Principal principal) {
        String senderName = principal.getName(); // Username из security
        messageDTO.setName(senderName);

        // Для общего чата conversation = null (после изменения модели)
        Message message = new Message(messageDTO, null); // null для общего
        messageService.save(message);
        log.debug("Saved public message id = {}", message.getId());
        firebaseConfig.sendNotification(message.getName(), message.getMessage());
        log.debug("Notification sent for message id = {}", message.getId());
        return message;
    }

    @Operation(
            summary = "Send private message via WebSocket",
            description = "Save new private message, associate with conversation, and send to recipient via user queue"
    )
    @MessageMapping("/private-chat")
    public void sendPrivateMessage(@Payload PrivateMessageDTO privateMessageDTO, Principal principal) {
        String senderName = principal.getName();
        if (!senderName.equals(privateMessageDTO.getInitiator())) {
            throw new SecurityException("Unauthorized sender");
        }

        Optional<Conversation> existingConv = conversationService.findByUsers(privateMessageDTO.getInitiator(), privateMessageDTO.getReceiver());
        Conversation conversation;
        if (existingConv.isEmpty()) {
            conversation = new Conversation(privateMessageDTO.getInitiator(), privateMessageDTO.getReceiver());
            conversationService.save(conversation);
        } else {
            conversation = existingConv.get();
        }

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setName(privateMessageDTO.getInitiator());
        messageDTO.setMessage(privateMessageDTO.getMessage());
        messageDTO.setReply_id(privateMessageDTO.getReply_id());
        Message message = new Message(messageDTO, conversation);
        messageService.save(message);
        log.debug("Saved private message id = {}", message.getId());

        // Отправить получателю и отправителю
        messagingTemplate.convertAndSendToUser(privateMessageDTO.getReceiver(), "/queue/private", message);
        messagingTemplate.convertAndSendToUser(privateMessageDTO.getInitiator(), "/queue/private", message);

        // Firebase (пока global; добавьте targeted позже)
        firebaseConfig.sendNotification(privateMessageDTO.getInitiator() + " (private to " + privateMessageDTO.getReceiver() + ")", privateMessageDTO.getMessage());
    }

    @Operation(
            summary = "Get private conversation history",
            description = "Retrieve messages for a specific conversation"
    )
    @GetMapping("/private-history/{conversationId}")
    @ResponseBody
    public List<Message> getPrivateHistory(
            @Parameter(description = "Conversation ID", required = true)
            @PathVariable UUID conversationId, Principal principal) {
        Conversation conv = conversationService.findById(conversationId).orElseThrow(() -> new RuntimeException("Conversation not found"));
        String user = principal.getName();
        if (!user.equals(conv.getInitiatorName()) && !user.equals(conv.getReceiverName())) {
            throw new SecurityException("Access denied");
        }
        return messageService.findByConversationId(conversationId);
    }

    @Operation(
            summary = "Get message by ID",
            description = "Retrieve specific message by its unique identifier"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Message found and returned",
            content = @Content(
                    schema = @Schema(implementation = Message.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Message not found",
            content = @Content(schema = @Schema(hidden = true))
    )
    @GetMapping("/message/{id}")
    @ResponseBody
    public Message getOldMessage(
            @Parameter(
                    description = "Message ID",
                    required = true,
                    example = "123"
            )
            @PathVariable int id) {
        return messageService.findById(id);
    }
}