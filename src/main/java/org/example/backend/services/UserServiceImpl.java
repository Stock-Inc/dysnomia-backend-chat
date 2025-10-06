package org.example.backend.services;

import org.example.backend.config.PasswordConfig;
import org.example.backend.config.SecurityConfig;
import org.example.backend.dto.ChangeUserPasswordDTO;
import org.example.backend.dto.EditUserProfileDTO;
import org.example.backend.dto.UserDTO;
import org.example.backend.exceptions.UserPasswordNotMatch;
import org.example.backend.models.User;
import org.example.backend.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));
    }

    @Override
    public boolean existsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return true;
        }
        return false;
    }
    public UserDTO findUsersByUsername(String username) {
        User user = userRepository.findUsersByUsername(username);
        UserDTO userDTO = new UserDTO();
        System.out.println(user);
        userDTO.setUsername(user.getUsername());
        userDTO.setRole(String.valueOf(user.getRole()));
        return userDTO;
    }

    @Override
    public boolean existsByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return true;
        }
        return false;
    }

    public void updateProfile(EditUserProfileDTO userDTO, String username) {
        User user = userRepository.findUsersByUsername(username);
        user.setDisplayName(userDTO.getDisplay_name());
        user.setBio(userDTO.getBio());
        userRepository.save(user);
    }

    public void changePassword(ChangeUserPasswordDTO userDTO, String username) {
        User user = userRepository.findUsersByUsername(username);

        if (!passwordEncoder.matches(userDTO.getCurrent_password(), user.getPassword())) {
            throw new UserPasswordNotMatch();
        }

        user.setPassword(passwordEncoder.encode(userDTO.getNew_password()));
        userRepository.save(user);
    }
}