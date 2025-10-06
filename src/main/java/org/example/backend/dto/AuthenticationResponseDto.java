package org.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(
        description = "Authentication response containing access and refresh tokens",
        example = """
        {
          "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
          "refreshToken": "dGhpcyBpcyBhIHNhbXBsZSByZWZyZXNoIHRva2VuIGZvciBkZW1vbnN0cmF0aW9uIHB1cnBvc2Vz",
        }
        """
)
public class AuthenticationResponseDto {

    @Schema(
            description = "JWT access token for API authorization",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private final String accessToken;

    @Schema(
            description = "Refresh token to obtain new access token",
            example = "dGhpcyBpcyBhIHNhbXBsZSByZWZyZXNoIHRva2VuIGZvciBkZW1vbnN0cmF0aW9uIHB1cnBvc2Vz",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private final String refreshToken;


    public AuthenticationResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}