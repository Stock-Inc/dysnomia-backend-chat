package org.example.backend.repositories;

import org.example.backend.models.ConsoleCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsoleRepository extends JpaRepository<ConsoleCommand, Integer> {
    ConsoleCommand findConsoleCommandByCommand(String command);

    @Query("SELECT c FROM ConsoleCommand c")
    List<ConsoleCommand> findAllCommands();
}