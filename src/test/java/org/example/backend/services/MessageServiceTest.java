package org.example.backend.services;

import org.example.backend.exceptions.MessageCanNotBeNullException;
import org.example.backend.exceptions.MessageNotFoundException;
import org.example.backend.models.Message;
import org.example.backend.repositories.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
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
    public void testSaveMessage() {
        messageService.save(message);
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    public void testSaveNullMessage() {
        assertThatThrownBy(() -> messageService.save(null))
                .isInstanceOf(MessageCanNotBeNullException.class)
                .hasMessage("Message is null");

        verify(messageRepository, never()).save(null);
    }

    @Test
    public void testFindLast100MessageIsNotEmpty() {
        List<Message> list = List.of(message);
        when(messageRepository.findLast100Message())
                .thenReturn(list);

        List<Message> result = messageService.findLast100Message();

        assertThat(result).isEqualTo(list);

        verify(messageRepository, times(1)).findLast100Message();
    }

    @Test
    public void testFindLast100MessageEmpty() {
        List<Message> list = List.of();
        when(messageRepository.findLast100Message()).thenReturn(list);

        List<Message> result = messageService.findLast100Message();

        assertThat(result).isEqualTo(list);

        verify(messageRepository, times(1)).findLast100Message();
    }

    @Test
    public void testFindByIdExist() {
        when(messageRepository.findById(messageId))
                .thenReturn(message);

        assertThat(messageService.findById(messageId)).isEqualTo(message);

        verify(messageRepository, times(1)).findById(messageId);
    }

    @Test
    public void testFindByIdNotFound() {
        when(messageRepository.findById(123))
                .thenReturn(null);

        assertThatThrownBy(() -> messageService.findById(123))
                .isInstanceOf(MessageNotFoundException.class)
                .hasMessage("No message with this ID was found.");

        verify(messageRepository, times(1)).findById(123);
    }

    @Test
    public void testFindByIdZeroOrLess() {
        assertThatThrownBy(() -> messageService.findById(0))
                .isInstanceOf(MessageNotFoundException.class)
                .hasMessage("No message with this ID was found.");

        verify(messageRepository, never()).findById(0);
    }
}
