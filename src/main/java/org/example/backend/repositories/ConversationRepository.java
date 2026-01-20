package org.example.backend.repositories;

import org.example.backend.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
    boolean existsByInitiatorName(String initiatorName);

    boolean existsByReceiverName(String receiverName);

    @Query("SELECT c FROM Conversation c WHERE (c.initiatorName = :user1 AND c.receiverName = :user2) OR (c.initiatorName = :user2 AND c.receiverName = :user1)")
    Optional<Conversation> findByUsers(@Param("user1") String user1, @Param("user2") String user2);
}