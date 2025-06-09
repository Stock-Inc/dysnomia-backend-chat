package org.example.backend.repositories;

import org.example.backend.models.ConsoleCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsoleRepository extends JpaRepository<ConsoleCommand, Integer> {
    ConsoleCommand findConsoleCommandByCommand(String command);
}