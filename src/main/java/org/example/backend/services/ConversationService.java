package org.example.backend.services;

import org.example.backend.models.Conversation;
import org.example.backend.repositories.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;

    @Autowired
    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public boolean existsByInitiatorName(String initiatorName){
        return conversationRepository.existsByInitiatorName(initiatorName);
    }

    public boolean existsByReceiverName(String receiverName){
        return conversationRepository.existsByReceiverName(receiverName);
    }

    public void save(Conversation conversation){
        conversationRepository.save(conversation);
    }

    public Optional<Conversation> findByUsers(String user1, String user2) {
        return conversationRepository.findByUsers(user1, user2);
    }

    public Optional<Conversation> findById(UUID id) {
        return conversationRepository.findById(id);
    }
}