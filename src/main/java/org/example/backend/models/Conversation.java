package org.example.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Conversation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    @NotNull
    private UUID id = UUID.randomUUID();

    @Column(name = "initiatorName", nullable = false, updatable = false)
    @NotNull
    private String initiatorName;

    @Column(name = "receiverName", nullable = false, updatable = false)
    @NotNull
    private String receiverName;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<Message> messages;

    public Conversation(String initiatorName, String receiverName){
        this.initiatorName = initiatorName;
        this.receiverName = receiverName;
    }
}
