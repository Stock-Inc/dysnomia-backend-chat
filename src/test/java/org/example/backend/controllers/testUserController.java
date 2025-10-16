package org.example.backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.backend.dto.ChangeUserPasswordDTO;
import org.example.backend.dto.EditUserProfileDTO;
import org.example.backend.dto.UserDTO;
import org.example.backend.services.JwtService;
import org.example.backend.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class testUserController {

    @Mock
    private UserServiceImpl userServiceImpl;
    @Mock
    private JwtService jwtService;
    @Mock
    private HttpServletRequest request;
    @InjectMocks
    private UserController userController;

    String username = "name";
    String token = "token_value";

    @Test
    public void testGetExistUser() {
        UserDTO userDTO = new UserDTO();
        when(userServiceImpl.findUserInfo(username)).thenReturn(userDTO);
        ResponseEntity<?> response = userController.getUser(username, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());

        verify(jwtService, times(1)).validateAccessToken(username, request);
        verify(userServiceImpl, times(1)).findUserInfo(username);
    }

    @Test
    void testGetUserThrowsExceptionFromJwtService() {
        doThrow(RuntimeException.class).when(jwtService)
                .validateAccessToken(username, request);

        assertThatThrownBy(() -> userController.getUser(username, request))
                .isInstanceOf(RuntimeException.class);

        verify(jwtService).validateAccessToken(username, request);
        verify(userServiceImpl, never()).findUserInfo(username);
    }

    @Test
    public void testEditProfile() {
        EditUserProfileDTO editUserProfileDTO = new EditUserProfileDTO();

        when(jwtService.extractUsernameByToken(request)).thenReturn(token);
        when(jwtService.extractUsername(token)).thenReturn(username);


        ResponseEntity<?> response = userController.editProfile(editUserProfileDTO, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        verify(jwtService, times(1)).extractUsernameByToken(request);
        verify(jwtService, times(1)).extractUsername(token);
        verify(userServiceImpl, times(1)).updateProfile(editUserProfileDTO, username);
    }

    @Test
    public void testUnSuccessEditProfile() {
        EditUserProfileDTO editUserProfileDTO = new EditUserProfileDTO();

        doThrow(RuntimeException.class).when(jwtService).extractUsernameByToken(request);

        assertThatThrownBy(() -> userController.editProfile(editUserProfileDTO, request))
        .isInstanceOf(RuntimeException.class);

        verify(jwtService, times(1)).extractUsernameByToken(request);
        verify(jwtService, never()).extractUsername(token);
    }

    @Test
    public void testChangePassword() {
        ChangeUserPasswordDTO dto = new ChangeUserPasswordDTO();

        when(jwtService.extractUsernameByToken(request)).thenReturn(token);
        when(jwtService.extractUsername(token)).thenReturn(username);

        ResponseEntity<?> response = userController.changePassword(dto, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        verify(jwtService, times(1)).extractUsernameByToken(request);
        verify(jwtService, times(1)).extractUsername(token);
        verify(userServiceImpl, times(1)).changePassword(dto, username);
    }

    @Test
    public void testChangePasswordThrowsExceptionFromJwtService() {
        ChangeUserPasswordDTO dto = new ChangeUserPasswordDTO();
        doThrow(RuntimeException.class).when(jwtService).extractUsernameByToken(request);

        assertThatThrownBy(() -> userController.changePassword(dto, request))
                .isInstanceOf(RuntimeException.class);

        verify(jwtService, times(1)).extractUsernameByToken(request);
        verify(jwtService, never()).extractUsername(token);
        verify(userServiceImpl, never()).changePassword(dto, username);

    }
}
