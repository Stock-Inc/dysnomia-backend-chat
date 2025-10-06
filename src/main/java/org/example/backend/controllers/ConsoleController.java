package org.example.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RestController
@Tag(
        name = "Console Commands",
        description = "Operations for managing and retrieving console commands"
)
public class ConsoleController {
    private final ConsoleService consoleService;

    @Autowired
    public ConsoleController(ConsoleService consoleService) {
        this.consoleService = consoleService;
    }

    @CrossOrigin(origins = "*")
    @Operation(
            summary = "Execute console command",
            description = "Retrieve and execute a specific console command by its name"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Command find or not"
    )
    @GetMapping("/console")
    public ResponseEntity<String> receiveCommand(
            @Parameter(
                    description = "Console command to execute",
                    required = true,
                    example = "help"
            )
            @RequestParam(value = "command") String command) {

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(consoleService.findConsoleCommandByCommand(command));
    }

    @Operation(
            summary = "Get all console commands",
            description = "Retrieve a list of all available console commands"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of commands retrieved successfully",
            content = @Content(
                    schema = @Schema(implementation = ConsoleCommandDTO.class),
                    examples = @ExampleObject(value = "[{\"command\": \"help\", \"description\": \"Show help\"}]")
            )
    )
    @GetMapping("/all_commands")
    public List<ConsoleCommandDTO> getAllCommands() {
        return consoleService.findAllCommands();
    }
}