package org.example.backend.services;

import org.example.backend.dto.ConsoleCommandDTO;
import org.example.backend.models.ConsoleCommand;
import org.example.backend.repositories.ConsoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ConsoleService {
    private final ConsoleRepository consoleRepository;

    @Autowired
    public ConsoleService(ConsoleRepository consoleRepository) {
        this.consoleRepository = consoleRepository;
    }

    public String findConsoleCommandByCommand(String command) {
        if (command.startsWith("wheel") && command.length() > 5) {
            command = command.substring(5);
            List<String> commands = List.of(command.replace(" ", "").split(","));
            int randomNumber = new Random().nextInt(commands.size());
            return commands.get(randomNumber);
        } else if (command.startsWith("wheel") && command.length() <= 5) {
            return "Вы не ввели кто играет!";
        }
        ConsoleCommand dbCommand = consoleRepository.findConsoleCommandByCommand(command);
        if (command.isEmpty() || dbCommand == null)
            return "Неправильная команда!";
        return dbCommand.getResult();
    }

    public List<ConsoleCommandDTO> findAllCommands() {
        List<ConsoleCommand> listCommands = consoleRepository.findAllCommands();
        List<ConsoleCommandDTO> listCommandsDTO = new ArrayList<>();
        for (ConsoleCommand consoleCommand : listCommands) {
            ConsoleCommandDTO consoleCommandDTO = new ConsoleCommandDTO();
            consoleCommandDTO.setCommand(consoleCommand.getCommand());
            consoleCommandDTO.setDescription(consoleCommand.getDescription());
            listCommandsDTO.add(consoleCommandDTO);
        }
        return listCommandsDTO;
    }
}