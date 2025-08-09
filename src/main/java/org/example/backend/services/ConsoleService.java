package org.example.backend.services;

import lombok.Getter;
import lombok.Setter;
import org.example.backend.repositories.ConsoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@Getter
@Setter
public class ConsoleService {
    private final ConsoleRepository consoleRepository;

    @Autowired
    public ConsoleService(ConsoleRepository consoleRepository) {
        this.consoleRepository = consoleRepository;
    }

    public String findConsoleCommandByCommand(String command) {
        LocalTime currentTime = LocalTime.now();
        if (command.isEmpty() || consoleRepository.findConsoleCommandByCommand(command) == null)
            return "Неправильная команда!";
        if (command.equals("картель")) {
            return consoleRepository.findConsoleCommandByCommand(command).getResult() + " " +
                    +currentTime.getHour() + ":" + currentTime.getMinute();

        }
        return consoleRepository.findConsoleCommandByCommand(command).getResult();
    }
}