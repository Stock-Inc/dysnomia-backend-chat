package org.example.backend.services;

import org.assertj.core.api.Assertions;
import org.example.backend.models.Message;
import org.example.backend.repositories.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestMessageService {
    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    int messageId = 1;
    Message message = Message.builder()
            .id(messageId).name("test message")
            .message("test message")
            .reply_id(0).build();

    @Test
    public void testFindById() {
        when(messageRepository.findById(messageId))
                .thenReturn(message);

        assertThat(messageService.findById(messageId)).isEqualTo(message);

        verify(messageRepository).findById(messageId);
    }

    @Test
    public void testFindLast100Message(){
        List<Message> list = List.of(message);
        when(messageRepository.findLast100Message())
                .thenReturn(list);

        assertThat(messageService.findLast100Message())
                .isEqualTo(list);

        verify(messageRepository).findLast100Message();
    }
}
