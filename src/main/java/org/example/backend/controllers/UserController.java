package org.example.backend.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.backend.dto.EditUserProfileDTO;
import org.example.backend.services.JwtService;
import org.example.backend.services.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Controller", description = "controller for user profile")
@RestController
public class UserController {
    private final UserServiceImpl userService;
    private final JwtService jwtService;

    public UserController(UserServiceImpl userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Tag(name = "get user inform", description = "all information about user")
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUser(
            @PathVariable String username
            , HttpServletRequest request) {
        jwtService.validateAccessToken(username, request);
        return ResponseEntity.ok(userService.findUsersByUsername(username));
    }

    @PatchMapping("/profile/edit_info")
    public ResponseEntity<?> editProfile(@RequestBody @Valid EditUserProfileDTO userDTO
            , HttpServletRequest request) {
        String token = jwtService.extractUsernameByToken(request);
        String username = jwtService.extractUsername(token);
        userService.updateProfile(userDTO, username);
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/profile/change_password")
//    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangeUserPasswordDTO userDTO,
//                                            HttpServletRequest request) {
//        userService.changePassword(userDTO, jwtService.extractUsernameByToken(request));
//        return ResponseEntity.ok().build();
//    }
}
