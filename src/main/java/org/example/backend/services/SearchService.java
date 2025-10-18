package org.example.backend.services;

import lombok.AllArgsConstructor;
import org.example.backend.exceptions.HeaderIsInvalidException;
import org.example.backend.exceptions.TokenInvalidException;
import org.example.backend.models.Message;
import org.example.backend.repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SearchService {
    private final MessageRepository messageRepository;
    private final JwtService jwtService;

    public List<Message> searchMessages(String authHeader, String query) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new HeaderIsInvalidException();
        }

        String token = authHeader.substring(7);
        if (!jwtService.isAccessTokenExpired(token)) {
            throw new TokenInvalidException();
        }

        return messageRepository.findByQuery(query);
    }
}
