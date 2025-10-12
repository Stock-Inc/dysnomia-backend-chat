package org.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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