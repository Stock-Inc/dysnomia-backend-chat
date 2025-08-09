package org.example.backend.models;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Schema(description = "Этот метод возвращает коллекцию объектов GrantedAuthority, " +
            "которые представляют права доступа пользователя. " +
            "Это могут быть роли или конкретные разрешения," +
            " которые определяют, что пользователь может делать в системе.")
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((role.name())));
    }

    @Schema(description = "Возвращает true, если учетная запись пользователя не истекла. " +
            "Если учетная запись истекла, пользователь не может аутентифицироваться.")
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Schema(description = "Возвращает true, если учетная запись пользователя не заблокирована. " +
            "Заблокированные учетные записи не могут аутентифицироваться.")
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Schema(description = "Возвращает true, если учетные данные пользователя не истекли.")
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Schema(description = "озвращает true, если учетная запись пользователя включена. " +
            "Отключенные учетные записи не могут аутентифицироваться.")
    @Override
    public boolean isEnabled() {
        return true;
    }
}