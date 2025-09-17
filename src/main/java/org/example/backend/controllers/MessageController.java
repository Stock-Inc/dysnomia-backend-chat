package org.example.backend.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.backend.config.FirebaseConfig;
import org.example.backend.dto.MessageDTO;
import org.example.backend.models.Message;
import org.example.backend.services.MessageServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
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
        log.info("the last 100 messages has been sent");
        return messageServices.findLast100Message();
    }

    @MessageMapping("/chat")
    @SendTo("/topic/message")
    public Message savePersonMessage(@Payload MessageDTO messageDTO) {
        Message message = new Message(messageDTO);
        messageServices.save(message);
        log.debug("the new message with id = {} has been saved in the db  ", message.getId());
        firebaseConfig.sendNotification(message.getName(), message.getMessage());
        log.debug("the notification has been sent");
        return message;
    }

    @PostMapping("/save")
    @ResponseBody
    public Message savePersonMessage2(@RequestBody MessageDTO messageDTO) {
        Message message = new Message(messageDTO);
        messageServices.save(message);
        log.debug("the new message with id = {} has been saved in the db  ", message.getId());
        firebaseConfig.sendNotification(message.getName(), message.getMessage());
        log.debug("the notification has been sent");
        return message;
    }

    @GetMapping("/message/{id}")
    @ResponseBody
    public Message messageOld(@PathVariable int id) {
        return messageServices.findById(id);
    }
}