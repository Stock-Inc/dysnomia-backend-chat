package org.example.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Column(name = "command", length = 255, nullable = false)
    @NotNull
    @Size(max = 255)
    private String command;

    @Column(name = "result", length = 255, nullable = false)
    @NotNull
    @Size(max = 255)
    private String result;

    @Column(name = "description", length = 255)
    @Size(max = 255)
    private String description;
}