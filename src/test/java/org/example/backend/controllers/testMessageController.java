package org.example.backend.controllers;

import org.example.backend.config.FirebaseConfig;
import org.example.backend.dto.MessageDTO;
import org.example.backend.models.Message;
import org.example.backend.services.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class testMessageController {
    Message message = new Message();
    MessageDTO messageDTO = new MessageDTO();
    List<Message> messages = new ArrayList<>();
    @Mock
    private MessageService messageService;
    @Mock
    private FirebaseConfig firebaseConfig;
    @InjectMocks
    private MessageController messageController;

    @Test
    public void testReturnEmptyHistory() {
        when(messageService.findLast100Message())
                .thenReturn(messages);

        List<Message> resultMessages = messageController.history();

        assertEquals(0, resultMessages.size());
        assertEquals(messages, resultMessages);

        verify(messageService, times(1))
                .findLast100Message();
    }

    @Test
    public void testReturn10Elements() {
        messages = IntStream.range(0, 10)
                .mapToObj(i -> new Message())
                .collect(Collectors.toList());

        when(messageService.findLast100Message())
                .thenReturn(messages);

        List<Message> resultMessages = messageController.history();

        assertEquals(10, resultMessages.size());


        verify(messageService, times(1))
                .findLast100Message();
    }

    @Test
    public void testGetOldMessageWhenItIsEmpty() {
        when(messageService.findById(1))
                .thenReturn(null);

        assertNull(messageController.getOldMessage(1));

        verify(messageService, times(1)).findById(1);
    }

    @Test
    public void testGetOldMessageWhenItIsNotEmpty() {
        when(messageService.findById(1))
                .thenReturn(message);

        assertEquals(message, messageController.getOldMessage(1));

        verify(messageService, times(1)).findById(1);
    }

    @Test
    public void testSavePersonMessageWhenItIsSuccess() {
        doNothing().when(firebaseConfig)
                .sendNotification(message.getName(), message.getMessage());

        Message testMessage = messageController
                .savePersonMessage(messageDTO);

        assertEquals(message.getMessage(), testMessage.getMessage());
        assertEquals(message.getName(), testMessage.getName());
        assertEquals(message.getReply_id(), testMessage.getReply_id());

        verify(messageService, times(1)).save(any(Message.class));
        verify(firebaseConfig, times(1)).sendNotification(message.getName(), message.getMessage());
    }

    @Test
    public void testSavePersonMessageWhenItIsException() {
        doThrow(RuntimeException.class).when(firebaseConfig)
                .sendNotification(message.getName(), message.getMessage());


        assertThrows(RuntimeException.class,
                () -> messageController.savePersonMessage(messageDTO));

        verify(messageService, times(1)).save(any(Message.class));
        verify(firebaseConfig, times(1)).sendNotification(message.getName(), message.getMessage());
    }
}
