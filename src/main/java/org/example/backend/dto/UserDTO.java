package org.example.backend.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String username;
    private String displayName;
    private String role;
    private String bio;
}
