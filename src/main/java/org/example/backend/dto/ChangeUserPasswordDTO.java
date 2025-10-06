package org.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Change user password request")
public class ChangeUserPasswordDTO {

    @Schema(
            description = "Current user password for verification",
            example = "CurrentPassword"
    )
    private String current_password;

    @Schema(
            description = "New password to set",
            example = "NewSecurePassword"
    )
    private String new_password;
}