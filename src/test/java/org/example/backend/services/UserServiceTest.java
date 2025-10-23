package org.example.backend.services;

import org.example.backend.dto.ChangeUserPasswordDTO;
import org.example.backend.dto.EditUserProfileDTO;
import org.example.backend.exceptions.UserNotExistsException;
import org.example.backend.exceptions.UserPasswordNotMatchException;
import org.example.backend.models.Role;
import org.example.backend.models.User;
import org.example.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email");
        user.setRole(Role.USER);
        user.setDisplayName("displayName");
    }

    @Test
    public void testLoadUserByUsername_Success() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("username");

        assertEquals("username", result.getUsername());
        verify(userRepository, times(1)).findByUsername("username");
    }

    @Test
    public void testLoadUserByUsername_NotFound() {
        when(userRepository.findByUsername("notUsername")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUsername("notUsername"))
                .isInstanceOf(UsernameNotFoundException.class);

        verify(userRepository, times(1)).findByUsername("notUsername");
    }

    @Test
    public void testExistsByUsername_True() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        assertTrue(userService.existsByUsername("username"));
    }

    @Test
    public void testExistsByUsername_False() {
        when(userRepository.findByUsername("notUsername")).thenReturn(Optional.empty());

        assertFalse(userService.existsByUsername("notUsername"));
    }

    @Test
    public void testExistsByEmail_True() {
        when(userRepository.findByEmail("email")).thenReturn(Optional.of(user));

        assertTrue(userService.existsByEmail("email"));
    }

    @Test
    public void testExistsByEmail_False() {
        when(userRepository.findByEmail("notEmail")).thenReturn(Optional.empty());

        assertFalse(userService.existsByEmail("notEmail"));
    }

    @Test
    public void testFindUsersByUsername_Success() {
        when(userRepository.findUsersByUsername("username")).thenReturn(user);

        User result = userService.findUsersByUsername("username");

        assertEquals("username", result.getUsername());
    }

    @Test
    public void testFindUsersByUsername_NotExists() {
        when(userRepository.findUsersByUsername("notUsername")).thenReturn(null);

        assertThatThrownBy(() -> userService.findUsersByUsername("notUsername"))
                .isInstanceOf(UserNotExistsException.class);

        verify(userRepository, times(1)).findUsersByUsername("notUsername");
    }

    @Test
    public void testUpdateProfile_Success() {
        EditUserProfileDTO editDto = new EditUserProfileDTO();
        editDto.setDisplayName("newDisplayName");
        editDto.setBio("newBio");

        when(userRepository.findUsersByUsername("username")).thenReturn(user);

        userService.updateProfile(editDto, "username");

        assertEquals("newDisplayName", user.getDisplayName());
        assertEquals("newBio", user.getBio());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testChangePassword_Success() {
        ChangeUserPasswordDTO changeDto = new ChangeUserPasswordDTO();
        changeDto.setCurrent_password("password");
        changeDto.setNew_password("newPassword");

        when(userRepository.findUsersByUsername("username")).thenReturn(user);
        when(passwordEncoder.matches(changeDto.getCurrent_password(), user.getPassword()))
                .thenReturn(true);
        when(passwordEncoder.encode(changeDto.getNew_password())).thenReturn("encodedNewPassword");

        userService.changePassword(changeDto, "username");

        assertEquals("encodedNewPassword", user.getPassword());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testChangePassword_PasswordNotMatch() {
        ChangeUserPasswordDTO changeDto = new ChangeUserPasswordDTO();
        changeDto.setCurrent_password("wrongPassword");
        changeDto.setNew_password("newPassword");

        when(userRepository.findUsersByUsername("username")).thenReturn(user);

        assertThatThrownBy(() -> userService.changePassword(changeDto, "username"))
                .isInstanceOf(UserPasswordNotMatchException.class);

        verify(userRepository, never()).save(user);
    }
}
