package org.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Console command execution request")
public class ConsoleCommandDTO {

    @Schema(
            description = "Command to execute",
            example = "list-users",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String command;

    @Schema(
            description = "Command description or parameters",
            example = "List all registered users with their roles"
    )
    private String description;
}