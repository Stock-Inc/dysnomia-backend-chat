package org.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Edit user profile information")
public class EditUserProfileDTO {

    @Schema(
            description = "User's display name",
            example = "John Doe"
    )
    private String display_name;

    @Schema(
            description = "User biography or description",
            example = "Software developer and open source enthusiast"
    )
    private String bio;
}
