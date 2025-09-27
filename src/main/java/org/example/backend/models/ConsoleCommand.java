package org.example.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "console")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "description")
    private String description;
}