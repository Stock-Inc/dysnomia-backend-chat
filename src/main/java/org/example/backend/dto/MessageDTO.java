package org.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Schema(name = "Модель, получаемая после сообщения пользователя")
public class MessageDTO {
    private String username;

    @NotBlank(message = "message is mandatory")
    @Length(min = 1, message = "The name must be more than 0 characters long.")
    private String message;

    private int reply_id;

    public MessageDTO(String username, String message, int reply_id) {
        this.username = username;
        this.message = message;
        this.reply_id = reply_id;
    }
}