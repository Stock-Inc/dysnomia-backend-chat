package org.example.backend.services;

import lombok.Builder;
import lombok.Data;
import org.example.backend.models.Message;
import org.example.backend.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@Builder
public class MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> findLast100Message() {
        return messageRepository.findLast100Message();
    }


    public void save(Message message) {
        messageRepository.save(message);
    }

    public Message findById(int id) {
        return messageRepository.findById(id);
    }
}
