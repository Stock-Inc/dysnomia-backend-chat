package org.example.backend.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "email ~* '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$' AND username ~ '^[a-zA-Z0-9_]+$'")
@Table(name = "users_table")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", length = 64, unique = true)
    @NotNull
    @Size(min = 1, max = 64)
    private String username;

    @Column(name = "password", length = 255)
    @NotNull
    @Size(min = 1, max = 255)
    private String password;

    @Column(name = "email", length = 255, unique = true)
    @NotNull
    @Email
    @Size(min = 1, max = 255)
    private String email;

    @Column(name = "role", nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @Column(name = "display_name", length = 64, nullable = true)
    @Size(max = 64)
    private String displayName;

    @Column(name = "bio", length = 1024, nullable = true)
    @Size(max = 1024)
    private String bio;

    @Column(name = "profile_image", length = 255, nullable = true)
    @Size(max = 255)
    private String profileImage;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((role.name())));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}