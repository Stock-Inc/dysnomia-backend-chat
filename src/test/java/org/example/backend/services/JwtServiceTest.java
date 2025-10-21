package org.example.backend.services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.backend.exceptions.HeaderIsInvalidException;
import org.example.backend.exceptions.TokenInvalidException;
import org.example.backend.exceptions.UserNotExistsException;
import org.example.backend.exceptions.UsernameNotEqualsTokenException;
import org.example.backend.models.Role;
import org.example.backend.models.User;
import org.example.backend.repositories.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    @Mock
    private UserServiceImpl userService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private JwtService jwtService;

    private User user;

    @BeforeEach
    public void setUp() {
        String secret = "VGhpcy1pcy1hLXRlc3Qtc2VjcmV0LWtleS1mb3ItSldUUw";

        jwtService = new JwtService(tokenRepository, userService);
        ReflectionTestUtils.setField(jwtService, "secretKey", secret);
        ReflectionTestUtils.setField(jwtService, "accessTokenExpiration", 360000L);
        ReflectionTestUtils.setField(jwtService, "refreshTokenExpiration", 2520000L);

        user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setRole(Role.USER);
    }

    @Test
    public void testGenerateAccessToken_Success() {
        String token = jwtService.generateAccessToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals("username", jwtService.extractUsername(token));
    }

    @Test
    public void testGenerateRefreshToken_Success() {
        String token = jwtService.generateRefreshToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals("username", jwtService.extractUsername(token));
    }

    @Test
    public void testExtractUsername_TokenInvalid() {
        assertThatThrownBy(() -> jwtService.extractUsername("invalidToken"))
                .isInstanceOf(TokenInvalidException.class);
    }

    @Test
    public void testExtractUsernameByToken_Success() {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");

        String token = jwtService.extractUsernameByToken(request);

        assertEquals("token", token);
    }

    @Test
    public void testExtractUsernameByToken_HeaderIsNull() {
        when(request.getHeader("Authorization")).thenReturn(null);

        assertThatThrownBy(() -> jwtService.extractUsernameByToken(request))
                .isInstanceOf(HeaderIsInvalidException.class);
    }

    @Test
    public void testExtractUsernameByToken_HeaderNotStartsWithBearer() {
        when(request.getHeader("Authorization")).thenReturn("notBearer token");

        assertThatThrownBy(() -> jwtService.extractUsernameByToken(request))
                .isInstanceOf(HeaderIsInvalidException.class);
    }

    @Test
    public void testValidateAccessToken_Success() {
        String token = jwtService.generateAccessToken(user);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(userService.existsByUsername("username")).thenReturn(true);

        assertDoesNotThrow(() -> jwtService.validateAccessToken(user.getUsername(), request));
    }

    @Test
    public void testValidateAccessToken_UserNotExists() {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(userService.existsByUsername("username")).thenReturn(false);

        assertThatThrownBy(() -> jwtService.validateAccessToken(user.getUsername(), request))
                .isInstanceOf(UserNotExistsException.class);
    }

    @Test
    public void testValidateAccessToken_UsernameNotEqualToken() {
        String token = jwtService.generateAccessToken(user);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(userService.existsByUsername(anyString())).thenReturn(true);

        assertThatThrownBy(() -> jwtService.validateAccessToken("notUsername", request))
                .isInstanceOf(UsernameNotEqualsTokenException.class);
    }

    @Test
    public void testIsAccessTokenExpired_True() {
        String token = jwtService.generateAccessToken(user);
        assertTrue(jwtService.isAccessTokenExpired(token));
    }

    @Test
    public void testIsAccessTokenExpired_TokenExpired() {
        ReflectionTestUtils.setField(jwtService, "accessTokenExpiration", -1000L);
        String token = jwtService.generateAccessToken(user);
        assertThatThrownBy(() -> jwtService.isAccessTokenExpired(token))
                .isInstanceOf(TokenInvalidException.class);
    }
}
