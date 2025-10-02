package org.example.backend.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.backend.dto.AuthenticationResponseDto;
import org.example.backend.dto.LoginRequestDto;
import org.example.backend.dto.RegistrationRequestDto;
import org.example.backend.services.AuthenticationService;
import org.example.backend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }


    @PostMapping("/registration")
    public ResponseEntity<?> register(
            @RequestBody RegistrationRequestDto registrationDto
    ) {
        if (userService.existsByUsername(registrationDto.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } else if (userService.existsByEmail(registrationDto.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        return ResponseEntity.ok(authenticationService.register(registrationDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @RequestBody LoginRequestDto request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authenticationService.refreshToken(request, response);
    }
}