package org.example.backend.services;


import org.example.backend.dto.ConsoleCommandDTO;
import org.example.backend.models.ConsoleCommand;
import org.example.backend.repositories.ConsoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestConsoleService {
    @Mock
    private ConsoleRepository consoleRepository;

    @InjectMocks
    private ConsoleService consoleService;

    @Test
    public void testWhenWheelHasMultiplePlayers() {
        String resultWheelTrueFirstCase = consoleService.findConsoleCommandByCommand("wheel a, b, c");
        assertThat(resultWheelTrueFirstCase).isIn("a", "b", "c");
        verify(consoleRepository, never()).findConsoleCommandByCommand(any());
    }

    @Test
    public void testWhenWheelHasOnePlayer() {
        String resultWheelTrueSecondCase = consoleService.findConsoleCommandByCommand("wheel a");
        assertThat(resultWheelTrueSecondCase).isEqualTo("a");
        verify(consoleRepository, never()).findConsoleCommandByCommand(any());
    }

    @Test
    public void testWhenWheelHasNoPlayers() {
        String resultWheelFalse = consoleService.findConsoleCommandByCommand("wheel");
        assertThat(resultWheelFalse).isEqualTo("Вы не ввели кто играет!");
        verify(consoleRepository, never()).findConsoleCommandByCommand(any());
    }

    @Test
    public void testWhenEmptyCommand() {
        String testResult = "";
        String resultEmpty = consoleService.findConsoleCommandByCommand(testResult);
        assertThat(resultEmpty).isEqualTo("Неправильная команда!");
        verify(consoleRepository, never()).findConsoleCommandByCommand(testResult);
    }

    @Test
    public void testWhenCommandIsUnknow() {
        String resultNull = consoleService.findConsoleCommandByCommand("Unknow");
        assertThat(resultNull).isEqualTo("Неправильная команда!");
        verify(consoleRepository, times(1)).findConsoleCommandByCommand("Unknow");
    }

    @Test
    public void testWhenCommandIsExist() {
        ConsoleCommand command = ConsoleCommand.builder()
                .id(1).command("test")
                .description("test description").result("test result")
                .build();

        when(consoleRepository.findConsoleCommandByCommand("test"))
                .thenReturn(command);

        String resultCommand = consoleService.findConsoleCommandByCommand("test");

        assertThat(resultCommand).isEqualTo("test result");

        verify(consoleRepository, times(1))
                .findConsoleCommandByCommand("test");
    }

    @Test
    public void testFindAllCommands() {

        ConsoleCommand command = ConsoleCommand.builder()
                .id(1).command("test")
                .description("test description").result("test result")
                .build();

        ConsoleCommandDTO consoleCommandDTO = ConsoleCommandDTO.builder()
                .command("test")
                .description("test description")
                .build();

        when(consoleRepository.findAllCommands()).thenReturn(List.of(command));

        List<ConsoleCommandDTO> result = consoleService.findAllCommands();

        assertThat(result).isEqualTo(List.of(consoleCommandDTO));

        verify(consoleRepository, times(1)).findAllCommands();
    }
}
