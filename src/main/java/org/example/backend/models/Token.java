package org.example.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "token_table")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "access_token", nullable = false, length = 255)
    @NotNull
    @Size(max = 255)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false, length = 255)
    @NotNull
    @Size(max = 255)
    private String refreshToken;

    @Column(name = "is_logged_out", nullable = false, length = 1)
    @NotNull
    private boolean loggedOut;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}