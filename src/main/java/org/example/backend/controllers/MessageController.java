package org.example.backend.controllers;

import org.example.backend.config.FirebaseConfig;
import org.example.backend.dto.MessageDTO;
import org.example.backend.models.Message;
import org.example.backend.services.MessageServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MessageController {

    private final MessageServices messageServices;
    private final FirebaseConfig firebaseConfig;

    @Autowired
    public MessageController(MessageServices messageServices, FirebaseConfig firebaseConfig) {
        this.messageServices = messageServices;
        this.firebaseConfig = firebaseConfig;
    }

    @MessageMapping("/history")
    @SendTo("/topic/history")
    public List<Message> message() {
        return messageServices.findLast100Message();
    }

    @MessageMapping("/chat")
    @SendTo("/topic/message")
    public Message savePersonMessage(@Payload MessageDTO messageDTO) {
        Message message = new Message(messageDTO);
        messageServices.save(message);
        firebaseConfig.sendNotification(message.getName(), message.getMessage());
        return message;
    }
}