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
            summary = "Send chat message via WebSocket",
            description = "Save new chat message and broadcast to all subscribers with Firebase notification"
    )
    @MessageMapping("/chat")
    @SendTo("/topic/message")
    public Message savePersonMessage(@Payload MessageDTO messageDTO) {
        Message message = new Message(messageDTO);
        messageService.save(message);
        log.debug("Saved message id = {}", message.getId());
        firebaseConfig.sendNotification(message.getName(), message.getMessage());
        log.debug("Notification sent for message id = {}", message.getId());
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