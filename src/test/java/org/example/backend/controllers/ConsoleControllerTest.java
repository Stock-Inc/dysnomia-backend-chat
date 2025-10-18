package org.example.backend.controllers;

import org.example.backend.dto.ConsoleCommandDTO;
import org.example.backend.services.ConsoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConsoleControllerTest {
    String command;
    List<ConsoleCommandDTO> list = new ArrayList<>();

    @Mock
    private ConsoleService consoleService;

    @InjectMocks
    private ConsoleController consoleController;


    @Test
    public void testReceiveCommand() {
        command = "wheel a,b,c";

        ResponseEntity<String> responseEntity
                = consoleController.receiveCommand(command);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(consoleService, times(1))
                .findConsoleCommandByCommand(command);
        verifyNoMoreInteractions(consoleService);
    }

    @Test
    public void testGetAllCommandsWhenListEmpty() {
        List<ConsoleCommandDTO> resultList
                = consoleController.getAllCommands();

        assertEquals(list.size(), resultList.size());

        verify(consoleService, times(1)).findAllCommands();
        verifyNoMoreInteractions(consoleService);
    }

    @Test
    public void testGetAllCommandsWhenListNotEmpty() {
        list.add(new ConsoleCommandDTO());
        when(consoleService.findAllCommands())
                .thenReturn(list);
        List<ConsoleCommandDTO> resultList
                = consoleController.getAllCommands();

        assertEquals(list.size(), resultList.size());

        verify(consoleService, times(1)).findAllCommands();
        verifyNoMoreInteractions(consoleService);
    }
}
