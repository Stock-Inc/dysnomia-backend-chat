package org.example.backend.repositories;

import org.example.backend.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConversationRepository
        extends JpaRepository<Conversation, UUID> {

}
