package org.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(name = "Модель, получаемая после сообщения пользователя")
public class MessageDTO {
    private String name;

    @NotBlank(message = "message is mandatory")
    @Length(min = 1, message = "The name must be more than 0 characters long.")
    private String message;

    private int reply_id;

    public MessageDTO(String name, String message, Integer reply_id) {
        this.name = name;
        this.message = message;
        this.reply_id = reply_id;
    }
}