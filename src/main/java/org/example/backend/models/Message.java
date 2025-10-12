package org.example.backend.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
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

    public Message(MessageDTO messageDTO) {
        this.name = messageDTO.getName();
        this.message = messageDTO.getMessage();
        this.reply_id = messageDTO.getReply_id();
    }
}