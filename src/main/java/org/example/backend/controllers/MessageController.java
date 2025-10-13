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
import org.example.backend.models.Message;
import org.example.backend.services.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
    private SimpMessagingTemplate messagingTemplate;


    @Operation(
            summary = "Get message history via WebSocket",
            description = "Retrieve last 100 messages through WebSocket connection"
    )
    @MessageMapping("/history")
    @SendTo("/topic/history")
    public List<Message> message() {
        log.info("the last 100 messages has been sent");
        return messageService.findLast100Message();
    }

    @Operation(
            summary = "Send chat message via WebSocket",
            description = "Save new chat message and broadcast to all subscribers with Firebase notification"
    )
    @MessageMapping("/chat")
    public Message savePersonMessage(@Payload MessageDTO messageDTO) {
        Message message = new Message(messageDTO);
        messageService.save(message);
        log.debug("the new message with id = {} has been saved in the db  ", message.getId());
        firebaseConfig.sendNotification(message.getName(), message.getMessage());
        log.debug("the notification has been sent");

        messagingTemplate.convertAndSend("/topic/message", message);
        messagingTemplate.convertAndSendToUser(
                messageDTO.getName(),
                "/queue/receipt",
                "Message delivered successfully");
        return message;
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
    public Message messageOld(
            @Parameter(
                    description = "Message ID",
                    required = true,
                    example = "123"
            )
            @PathVariable int id) {
        return messageService.findById(id);
    }
}