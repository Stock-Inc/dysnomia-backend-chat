package org.example.backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.backend.dto.EditUserProfileDTO;
import org.example.backend.dto.UserDTO;
import org.example.backend.services.JwtService;
import org.example.backend.services.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserServiceImpl userService;
    private final JwtService jwtService;

    public UserController(UserServiceImpl userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username
            , HttpServletRequest request) {
        jwtService.validateAccessToken(username, request);
        return ResponseEntity.ok(userService.findUsersByUsername(username));
    }

    @PatchMapping("/profile/edit_info")
    public ResponseEntity<?> editProfile(@RequestBody @Valid EditUserProfileDTO userDTO
            , HttpServletRequest request) {
        String username =  jwtService.extractUsernameByToken(request);
        userService.updateProfile(userDTO, username);
        return ResponseEntity.ok().build();
    }
}
