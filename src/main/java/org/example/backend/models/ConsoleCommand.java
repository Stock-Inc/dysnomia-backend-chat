package org.example.backend.models;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "console")
@Data
@Entity
public class ConsoleCommand {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "command")
    private String command;

    @Column(name = "result")
    private String result;
}