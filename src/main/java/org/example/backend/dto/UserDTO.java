package org.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "User information response")
public class UserDTO {

    @Schema(
            description = "Unique username",
            example = "johndoe"
    )
    private String username;

    @Schema(
            description = "User's display name",
            example = "John Doe"
    )
    private String displayName;

    @Schema(
            description = "User role in the system",
            example = "ROLE_USER",
            allowableValues = {"ROLE_USER", "ROLE_ADMIN"}
    )
    private String role;

    @Schema(
            description = "User biography",
            example = "Passionate about technology and coding"
    )
    private String bio;
}