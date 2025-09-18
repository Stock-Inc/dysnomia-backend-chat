package org.example.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.example.backend.dto.ConsoleCommandDTO;
import org.example.backend.services.ConsoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Data
@Tag(name = "Console Controller", description = "Консоль")
public class ConsoleController {
    private final ConsoleService consoleService;

    @Autowired
    public ConsoleController(ConsoleService consoleService) {
        this.consoleService = consoleService;
    }

    @Operation(summary = "Отправить команду в консоль")
    @CrossOrigin(origins = "*")
    @GetMapping("/console")
    public ResponseEntity<String> receiveCommand(
            @RequestParam(value = "command") String command) {

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(consoleService.findConsoleCommandByCommand(command));
    }

    @GetMapping("/all_commands")
    public List<ConsoleCommandDTO> getAllCommands(){
        return consoleService.findAllCommands();
    }

}