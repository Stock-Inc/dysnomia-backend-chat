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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping("/reply/{id}")
    @ResponseBody
    public Message message(@PathVariable int id, @RequestBody MessageDTO messageDTO) {
        messageDTO.setReply_id(id);
        Message message = new Message(messageDTO);
        messageServices.save(message);
        return message;
    }

    @GetMapping("/message/{id}")
    @ResponseBody
    public Message messageOld(@PathVariable int id) {
        return messageServices.findById(id);
    }
}