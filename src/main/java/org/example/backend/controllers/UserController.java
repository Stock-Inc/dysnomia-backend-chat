package org.example.backend.controllers;

import org.example.backend.dto.UserDTO;
import org.example.backend.services.UserServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserServiceImpl userService;


    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{username}")
    public UserDTO getUser(@PathVariable String username) {
        return userService.findUsersByUsername(username);
    }
}
