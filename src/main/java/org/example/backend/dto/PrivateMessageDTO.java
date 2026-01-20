package org.example.backend.dto;

import lombok.Data;

@Data
public class PrivateMessageDTO {
    private String initiator;
    private String receiver;
    private String message;
    private int reply_id;
}