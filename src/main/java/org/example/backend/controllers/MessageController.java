package org.example.backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpHeaders;
import org.example.backend.config.FirebaseConfig;
import org.example.backend.dto.MessageDTO;
import org.example.backend.models.Message;
import org.example.backend.services.JwtService;
import org.example.backend.services.MessageServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
public class MessageController {

    private final MessageServices messageServices;
    private final FirebaseConfig firebaseConfig;
    private final JwtService jwtService;

    @Autowired
    public MessageController(MessageServices messageServices, FirebaseConfig firebaseConfig, JwtService jwtService) {
        this.messageServices = messageServices;
        this.firebaseConfig = firebaseConfig;
        this.jwtService = jwtService;
    }

    @MessageMapping("/history")
    @SendTo("/topic/history")
    public List<Message> message() {
        log.info("the last 100 messages has been sent");
        return messageServices.findLast100Message();
    }

    @MessageMapping("/chat")
    @SendTo("/topic/message")
    public Message savePersonMessage(@Payload MessageDTO messageDTO, HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);
        Message message = new Message(messageDTO);
        if (message.getName().equals(username)) {
            messageServices.save(message);
            log.debug("the new message with id = {} has been saved in the db  ", message.getId());
            firebaseConfig.sendNotification(message.getName(), message.getMessage());
            log.debug("the notification has been sent");
            message.setHttpCode(200);
            return message;
        } else {
            message.setHttpCode(401);
            return message;
        }
    }


    @GetMapping("/message/{id}")
    @ResponseBody
    public Message messageOld(@PathVariable int id) {
        return messageServices.findById(id);
    }
}