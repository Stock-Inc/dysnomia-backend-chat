package org.example.backend.services;

import lombok.Builder;
import lombok.Data;
import org.example.backend.exceptions.MessageCanNotBeNullException;
import org.example.backend.exceptions.MessageNotFoundException;
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
}
