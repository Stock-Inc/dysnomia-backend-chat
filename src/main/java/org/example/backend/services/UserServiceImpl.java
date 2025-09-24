package org.example.backend.services;

import org.example.backend.dto.UserDTO;
import org.example.backend.models.User;
import org.example.backend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));
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
    public boolean existsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }
}