package org.example.backend.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.example.backend.dto.MessageDTO;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "message")
@Schema(name = "Модель, записываемая в бд")
public class Message {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "message")
    private String message;

    @Column(name = "reply_id")
    private int reply_id = 0;

    @Column(name = "date")
    private long date = LocalDateTime.now(ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC);

    public Message(MessageDTO messageDTO) {
        this.name = messageDTO.getName();
        this.message = messageDTO.getMessage();
    }
}