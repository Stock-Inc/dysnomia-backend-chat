package org.example.backend.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.dto.MessageDTO;

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
    private int reply_id;

    @Column(name = "date")
    private long date = LocalDateTime.now(ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC);

    @Column(name = "httpCode")
    private int httpCode = 200;

    public Message(MessageDTO messageDTO) {
        this.name = messageDTO.getUsername();
        this.message = messageDTO.getMessage();
        this.reply_id = messageDTO.getReply_id();
    }
}