package org.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.backend.models.Role;

@Data
@Schema(description = "User information response")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
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
    private Role role;

    @Schema(
            description = "User biography",
            example = "Passionate about technology and coding"
    )
    private String bio;
}