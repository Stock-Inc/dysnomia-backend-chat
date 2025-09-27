package org.example.backend.services;

import lombok.Data;
import org.example.backend.models.Message;
import org.example.backend.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class MessageService {
    private final MessageRepository MessageRepository;

    @Autowired
    public MessageService(MessageRepository MessageRepository) {
        this.MessageRepository = MessageRepository;
    }

    public List<Message> findLast100Message() {
        return MessageRepository.findLast100Message();
    }


    public void save(Message message) {
        MessageRepository.save(message);
    }

    public Message findById(int id) {
        return MessageRepository.findById(id);
    }
}
