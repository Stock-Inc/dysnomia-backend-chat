package org.example.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.backend.dto.ChangeUserPasswordDTO;
import org.example.backend.dto.EditUserProfileDTO;
import org.example.backend.dto.UserDTO;
import org.example.backend.services.JwtService;
import org.example.backend.services.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(
        name = "User Management",
        description = "Operations for user profile management and password changes"
)
@AllArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final JwtService jwtService;

    @Operation(
            summary = "Get user by username",
            description = "Retrieve user information by username with JWT validation"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User found and returned",
            content = @Content(
                    schema = @Schema(implementation = UserDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Invalid or expired access token",
            content = @Content(schema = @Schema(hidden = true))
    )
    @ApiResponse(
            responseCode = "403",
            description = "Access denied for this user",
            content = @Content(schema = @Schema(hidden = true))
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(schema = @Schema(hidden = true))
    )
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUser(
            @Parameter(
                    description = "Username to retrieve",
                    required = true,
                    example = "johndoe"
            )
            @PathVariable String username,
            HttpServletRequest request) {
        jwtService.validateAccessToken(username, request);
        return ResponseEntity.ok(userService.findUserInfo(username));
    }

    @Operation(
            summary = "Edit user profile",
            description = "Update user profile information (display name and bio)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Profile updated successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid profile data",
            content = @Content(schema = @Schema(hidden = true))
    )
    @ApiResponse(
            responseCode = "401",
            description = "Invalid or expired access token",
            content = @Content(schema = @Schema(hidden = true))
    )
    @PatchMapping("/profile/edit_info")
    public ResponseEntity<?> editProfile(
            @Parameter(
                    description = "Updated profile data",
                    required = true
            )
            @RequestBody @Valid EditUserProfileDTO userDTO,
            HttpServletRequest request) {
        String token = jwtService.extractUsernameByToken(request);
        String username = jwtService.extractUsername(token);
        userService.updateProfile(userDTO, username);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Change user password",
            description = "Change user password with current password verification"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Password changed successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid password data",
            content = @Content(schema = @Schema(hidden = true))
    )
    @ApiResponse(
            responseCode = "401",
            description = "Invalid current password or token",
            content = @Content(schema = @Schema(hidden = true))
    )
    @PostMapping("/profile/change_password")
    public ResponseEntity<?> changePassword(
            @Parameter(
                    description = "Password change data",
                    required = true
            )
            @RequestBody @Valid ChangeUserPasswordDTO userDTO,
            HttpServletRequest request) {
        String token = jwtService.extractUsernameByToken(request);
        String username = jwtService.extractUsername(token);
        userService.changePassword(userDTO, username);
        return ResponseEntity.ok().build();
    }
}