package org.example.backend.repositories;

import org.example.backend.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query(value = "SELECT * FROM Message ORDER BY id DESC LIMIT 100", nativeQuery = true)
    List<Message> findLast100Message();

    @Query(
            value = "SELECT * FROM message " +
                    "WHERE to_tsvector('russian', message || ' ' || name) @@ plainto_tsquery('russian', :query)",
            nativeQuery = true
    )
    List<Message> findByQuery(@Param("query") String query);

    Message findById(int id);

    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId ORDER BY m.date ASC")
    List<Message> findByConversationId(@Param("conversationId") UUID conversationId);
}