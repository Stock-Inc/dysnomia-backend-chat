package org.example.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Column(name = "name", length = 64)
    @Size(max = 64)
    private String name;

    @Column(name = "message", nullable = false, length = 1024)
    @NotNull
    @Size(max = 1024)
    private String message;

    @Column(name = "reply_id", nullable = false)
    @NotNull
    private int reply_id;

    @Column(name = "date")
    @NotNull
    private long date = LocalDateTime.now(ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC);

    public Message(MessageDTO messageDTO) {
        this.name = messageDTO.getName();
        this.message = messageDTO.getMessage();
        this.reply_id = messageDTO.getReply_id();
    }
}