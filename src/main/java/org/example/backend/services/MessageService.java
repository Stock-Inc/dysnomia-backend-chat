package org.example.backend.services;

import org.example.backend.exceptions.MessageCanNotBeNullException;
import org.example.backend.exceptions.MessageNotFoundException;
import org.example.backend.models.Message;
import org.example.backend.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void save(Message message) {
        if (message == null)
            throw new MessageCanNotBeNullException("Message is null");
        messageRepository.save(message);
    }

    public List<Message> findLast100Message() {
        return messageRepository.findLast100Message();
    }

    public Message findById(int id) {
        if (id <= 0)
            throw new MessageNotFoundException("No message with this ID was found.");
        Message message = messageRepository.findById(id);
        if (message == null) {
            throw new MessageNotFoundException("No message with this ID was found.");
        }
        return message;
    }

    public List<Message> findByConversationId(UUID conversationId) {
        return messageRepository.findByConversationId(conversationId);
    }
}