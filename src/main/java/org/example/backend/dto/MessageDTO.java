package org.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "Chat message data transfer object")
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    @Schema(
            description = "Name of the message sender",
            example = "Alice"
    )
    private String name;

    @Schema(
            description = "Message content",
            example = "Hello everyone!",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 1
    )
    @NotBlank(message = "message is mandatory")
    @Length(min = 1, message = "The name must be more than 0 characters long.")
    private String message;

    @Schema(
            description = "ID of the message being replied to (0 if not a reply)",
            example = "123"
    )
    private int reply_id;

    public MessageDTO(String name, String message, Integer reply_id) {
        this.name = name;
        this.message = message;
        this.reply_id = reply_id;
    }
}