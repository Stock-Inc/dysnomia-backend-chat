package org.example.backend.services;

import lombok.Getter;
import lombok.Setter;
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
        if (command.startsWith("wheel")){
            command = command.substring(5);
            List<String> commands = List.of(command.split(","));
            int randomNumber = new Random().nextInt(commands.size());
            return commands.get(randomNumber);
        }
        return consoleRepository.findConsoleCommandByCommand(command).getResult();
    }

    public List<ConsoleCommandDTO> findAllCommands(){
        List<ConsoleCommand> listCommands = consoleRepository.findAllCommands(0);
        List<ConsoleCommandDTO> listCommandsDTO = new ArrayList<>();
        for (ConsoleCommand consoleCommand : listCommands){
            ConsoleCommandDTO consoleCommandDTO = new ConsoleCommandDTO();
            consoleCommandDTO.setCommand(consoleCommand.getCommand());
            consoleCommandDTO.setDescription(consoleCommand.getDescription());
            listCommandsDTO.add(consoleCommandDTO);
        }
        return listCommandsDTO;
    }
}