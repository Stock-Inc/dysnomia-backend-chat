package org.example.backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.backend.dto.AuthenticationResponseDto;
import org.example.backend.dto.LoginRequestDto;
import org.example.backend.dto.RegistrationRequestDto;
import org.example.backend.models.User;
import org.example.backend.repositories.UserRepository;
import org.example.backend.services.AuthenticationService;
import org.example.backend.services.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {
    AuthenticationResponseDto responseDto
            = new AuthenticationResponseDto("access-token", "refresh-token");

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    public void testRegisterUser() {
        RegistrationRequestDto registrationDto = new RegistrationRequestDto();
        registrationDto.setUsername("user");
        registrationDto.setEmail("user@example.com");
        registrationDto.setPassword("password");

        when(authenticationService.register(registrationDto)).thenReturn(responseDto);

        ResponseEntity<AuthenticationResponseDto> response =
                authenticationController.register(registrationDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(authenticationService, times(1)).register(registrationDto);
        verifyNoMoreInteractions(authenticationService);
    }

    @Test
    public void testLoginUser() {
        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setUsername("user");
        loginDto.setPassword("password");

        when(authenticationService.authenticate(loginDto)).thenReturn(responseDto);

        ResponseEntity<AuthenticationResponseDto> response =
                authenticationController.authenticate(loginDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(authenticationService, times(1)).authenticate(loginDto);
        verifyNoMoreInteractions(authenticationService);
    }

    @Test
    public void shouldRefreshTokenAndReturnNewTokens() {

        AuthenticationResponseDto responseDto =
                new AuthenticationResponseDto("new-access-token", "new-refresh-token");

        doReturn(ResponseEntity.ok(responseDto))
                .when(authenticationService).refreshToken(any(HttpServletRequest.class));

        ResponseEntity<?> response = authenticationController.refreshToken(httpServletRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(AuthenticationResponseDto.class, response.getBody());
        AuthenticationResponseDto body = (AuthenticationResponseDto) response.getBody();
        assertEquals("new-access-token", body.accessToken());
        assertEquals("new-refresh-token", body.refreshToken());

        verify(authenticationService, times(1)).refreshToken(httpServletRequest);
        verifyNoMoreInteractions(authenticationService);
    }



}
