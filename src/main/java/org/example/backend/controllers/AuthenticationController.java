package org.example.backend.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.backend.dto.AuthenticationResponseDto;
import org.example.backend.dto.LoginRequestDto;
import org.example.backend.dto.RegistrationRequestDto;
import org.example.backend.services.AuthenticationService;
import org.example.backend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }


    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponseDto> register(
            @RequestBody RegistrationRequestDto registrationDto) {
        AuthenticationResponseDto registrationRequestDto = authenticationService.register(registrationDto);
        return ResponseEntity.ok(registrationRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity <AuthenticationResponseDto> authenticate(
            @RequestBody LoginRequestDto request) {
        AuthenticationResponseDto authenticationResponseDto = authenticationService.authenticate(request);
        return ResponseEntity.ok(authenticationResponseDto);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        return authenticationService.refreshToken(request, response);
    }
}