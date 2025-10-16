package org.example.backend.repositories;

import org.example.backend.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query(value = "SELECT * FROM message ORDER BY id DESC LIMIT 100", nativeQuery = true)
    List<Message> findLast100Message();

    @Query(
            value = "SELECT * FROM message " +
                    "WHERE to_tsvector('russian', message || ' ' || name) @@ plainto_tsquery('russian', :query)",
            nativeQuery = true
    )
    List<Message> findByQuery(@Param("query") String query);

    Message findById(int id);
}