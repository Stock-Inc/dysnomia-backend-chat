package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.repositories.ConversationRepository;

@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;


}
