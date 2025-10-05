package org.example.backend.dto;

import lombok.Data;

@Data
public class ChangeUserPasswordDTO {
    private String current_password;
    private String new_password;
}
