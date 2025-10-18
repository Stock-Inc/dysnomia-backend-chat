package org.example.backend.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.backend.dto.AuthenticationResponseDto;
import org.example.backend.dto.LoginRequestDto;
import org.example.backend.dto.RegistrationRequestDto;
import org.example.backend.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name = "Authentication Controller",
        description = "Provides endpoints for user registration, login, and access token refresh operations")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Register new user",
            description = "Creates a new user account in the system and returns authentication tokens"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User successfully registered and authentication tokens returned",
            content = @Content(
                    schema = @Schema(implementation = AuthenticationResponseDto.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                                      "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4gZXhhbXBsZQ==",
                                    }
                                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid registration data: missing required fields or invalid format",
            content = @Content(schema = @Schema(hidden = true))
    )
    @ApiResponse(
            responseCode = "401",
            description = "User already exists with provided data",
            content = @Content(schema = @Schema(hidden = true))
    )
    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponseDto> register(
            @Parameter(
                    description = "User registration credentials",
                    required = true,
                    example = """
                            {
                              "username": "user",
                              "email": "user@example.com",
                              "password": "password"
                            }
                            """
            )
            @RequestBody @Valid RegistrationRequestDto registrationDto) {
        AuthenticationResponseDto registrationRequestDto = authenticationService.register(registrationDto);
        return ResponseEntity.ok(registrationRequestDto);
    }

    @Operation(
            summary = "Authenticate user",
            description = "Login with existing credentials and receive authentication tokens"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully authenticated and tokens returned",
            content = @Content(
                    schema = @Schema(implementation = AuthenticationResponseDto.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                                      "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4gZXhhbXBsZQ==",
                                    }
                                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid login data format",
            content = @Content(schema = @Schema(hidden = true))
    )
    @ApiResponse(
            responseCode = "401",
            description = "Invalid email or password",
            content = @Content(schema = @Schema(hidden = true))
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(schema = @Schema(hidden = true))
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @Parameter(
                    description = "User login credentials",
                    required = true,
                    example = """
                            {
                              "username": "user",
                              "password": "password"
                            }
                            """
            )
            @RequestBody @Valid LoginRequestDto request) {
        AuthenticationResponseDto authenticationResponseDto = authenticationService.authenticate(request);
        return ResponseEntity.ok(authenticationResponseDto);
    }

    @Operation(
            summary = "Refresh access token",
            description = "Generate new access token using valid refresh token"
    )
    @ApiResponse(
            responseCode = "200",
            description = "New access token successfully generated",
            content = @Content(
                    schema = @Schema(implementation = AuthenticationResponseDto.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                                      "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4gZXhhbXBsZQ=="
                                    }
                                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Refresh token missing or invalid format",
            content = @Content(schema = @Schema(hidden = true))
    )
    @ApiResponse(
            responseCode = "401",
            description = "Invalid or expired refresh token",
            content = @Content(schema = @Schema(hidden = true))
    )
    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(
            @Parameter(
                    description = "Refresh token in Authorization header",
                    required = true,
                    example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            )
            HttpServletRequest request) {
        return authenticationService.refreshToken(request);
    }
}