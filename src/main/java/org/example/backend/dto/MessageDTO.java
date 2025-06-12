package org.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(name = "Модель, получаемая после сообщения пользователя")
public class MessageDTO {
    private String name;

    @NotBlank(message = "message is mandatory")
    @Length(min = 1, message = "The name must be more than 0 characters long.")
    private String message;

    private int reply_id = 0;

    public MessageDTO(String name, String message) {
        this.name = name;
        this.message = message;
    }
}