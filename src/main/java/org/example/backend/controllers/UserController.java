package org.example.backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.backend.dto.AuthenticationResponseDto;
import org.example.backend.dto.UserDTO;
import org.example.backend.models.User;
import org.example.backend.services.JwtService;
import org.example.backend.services.UserServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserServiceImpl userService;
    private final JwtService jwtService;

    public UserController(UserServiceImpl userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/user/{username}")
    public void getUser(@PathVariable String username, HttpServletRequest request) {
    }
}
