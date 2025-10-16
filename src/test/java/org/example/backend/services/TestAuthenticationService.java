package org.example.backend.services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.backend.dto.AuthenticationResponseDto;
import org.example.backend.dto.LoginRequestDto;
import org.example.backend.dto.RegistrationRequestDto;
import org.example.backend.exceptions.EmailAlreadyExistsException;
import org.example.backend.exceptions.TokenInvalidException;
import org.example.backend.exceptions.UsernameAlreadyExistsException;
import org.example.backend.models.Role;
import org.example.backend.models.Token;
import org.example.backend.models.User;
import org.example.backend.repositories.TokenRepository;
import org.example.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestAuthenticationService {
    RegistrationRequestDto request = RegistrationRequestDto.builder()
            .username("username").password("password")
            .email("email").build();
    User user = User.builder()
            .id(1L)
            .email("email")
            .role(Role.USER)
            .password("password")
            .username("username").build();
    LoginRequestDto loginRequestDto = LoginRequestDto.builder()
            .username("username").password("password").build();
    Token tokenLoggedOutFalse = Token.builder()
            .accessToken("access token")
            .refreshToken("refresh token")
            .loggedOut(false)
            .user(user)
            .build();
    Token tokenLoggedOutTrue = Token.builder()
            .accessToken("access token")
            .refreshToken("refresh token")
            .loggedOut(true)
            .user(user)
            .build();
    @Mock
    HttpServletRequest httpServletRequest;
    @Mock
    private AuthenticationResponseDto authenticationResponseDto;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void testRegisterExistByUsernameUser() {
        when(userRepository.findByUsername(request.getUsername()))
                .thenReturn(Optional.of(user));


        assertThatThrownBy(() -> authenticationService.register(request))
                .isInstanceOf(UsernameAlreadyExistsException.class);

        verify(userRepository, times(1)).findByUsername(request.getUsername());
    }

    @Test
    public void testRegisterExistByEmailUser() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authenticationService.register(request))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    public void testRegisterExistUser() {
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateAccessToken(user)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user)).thenReturn("refresh-token");

        AuthenticationResponseDto response = authenticationService.register(request);

        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());

        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateAccessToken(user);
        verify(jwtService, times(1)).generateRefreshToken(user);
    }

    @Test
    public void testAuthenticateUser() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        when(userRepository.findByUsername(request.getUsername()))
                .thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(user)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user)).thenReturn("refresh-token");

        AuthenticationResponseDto response = authenticationService.authenticate(loginRequestDto);

        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());

        verify(userRepository, times(1)).findByUsername(request.getUsername());
        verify(jwtService, times(1)).generateAccessToken(user);
        verify(jwtService, times(1)).generateRefreshToken(user);
    }



    @Test
    public void testRevokeAllTokenWhenListEmpty() {
        List<Token> list = List.of();
        when(tokenRepository.findAllAccessTokenByUser(user.getId())).thenReturn(list);

        authenticationService.revokeAllToken(user);

        verify(tokenRepository, times(1)).saveAll(list);
    }

    @Test
    public void testRevokeAllTokenWhenTheyLoggedOutTrue() {
        List<Token> list = List.of(tokenLoggedOutTrue);
        when(tokenRepository.findAllAccessTokenByUser(user.getId()))
                .thenReturn(list);

        authenticationService.revokeAllToken(user);

        assertTrue(tokenLoggedOutTrue.isLoggedOut());

        verify(tokenRepository, times(1)).saveAll(list);
    }

    @Test
    public void testRevokeAllTokenWhenUserNull(){
        assertThatThrownBy(() -> authenticationService.revokeAllToken(null))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    public void testRevokeAllTokenWhenTheyLoggedOutFalse() {
        List<Token> list = List.of(tokenLoggedOutFalse);
        when(tokenRepository.findAllAccessTokenByUser(user.getId()))
                .thenReturn(list);

        authenticationService.revokeAllToken(user);

        assertTrue(tokenLoggedOutFalse.isLoggedOut());

        verify(tokenRepository, times(1)).saveAll(list);
    }



    @Test
    public void testSaveUserToken() {
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);

        String accessToken = UUID.randomUUID().toString();
        String refreshToken = UUID.randomUUID().toString();

        authenticationService.saveUserToken(accessToken, refreshToken, user);

        verify(tokenRepository, times(1)).save(tokenCaptor.capture());

        Token savedToken = tokenCaptor.getValue();
        assertEquals(accessToken, savedToken.getAccessToken());
        assertEquals(refreshToken, savedToken.getRefreshToken());
        assertFalse(savedToken.isLoggedOut());
        assertEquals(user, savedToken.getUser());

    }

    @Test
    public void testRefreshTokenNotStartWithBearer(){
        when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("refresh-token");

        assertThatThrownBy(() -> authenticationService.refreshToken(httpServletRequest))
                .isInstanceOf(TokenInvalidException.class);

        verify(httpServletRequest, times(1)).getHeader("Authorization");
    }

    @Test
    public void testRefreshTokenWhereHeaderIsNull(){
        when(httpServletRequest.getHeader("Authorization"))
                .thenReturn(null);

        assertThatThrownBy(() -> authenticationService.refreshToken(httpServletRequest))
                .isInstanceOf(TokenInvalidException.class);

        verify(httpServletRequest, times(1)).getHeader("Authorization");
    }

    @Test
    public void testRefreshTokenWhenUserNotFound(){
        when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer refresh-token");
        when(jwtService.extractUsername("refresh-token"))
                .thenReturn(anyString());
        when(userRepository.findByUsername(request.getUsername()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.refreshToken(httpServletRequest))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessage("No user found");

        verify(httpServletRequest, times(1)).getHeader("Authorization");
        verify(jwtService, times(1)).extractUsername("refresh-token");
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    public void testRevokeAllTokenWhenTheyLoggedOutFalseAndTrue() {
        List<Token> list = List.of(tokenLoggedOutFalse, tokenLoggedOutTrue);
        when(tokenRepository.findAllAccessTokenByUser(user.getId()))
                .thenReturn(list);

        authenticationService.revokeAllToken(user);

        assertTrue(tokenLoggedOutFalse.isLoggedOut());
        assertTrue(tokenLoggedOutTrue.isLoggedOut());

        verify(tokenRepository, times(1)).saveAll(list);
    }

    @Test
    public void testAuthenticateUserNotFound() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        when(userRepository.findByUsername(request.getUsername()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.authenticate(loginRequestDto))
                .isInstanceOf(NoSuchElementException.class);
        ;

        verify(userRepository, times(1)).findByUsername(request.getUsername());
    }

    @Test
    public void testRefreshTokenWhenIsValid(){
        when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer refresh-token");
        when(jwtService.extractUsername("refresh-token"))
                .thenReturn("testUser");
        when(userRepository.findByUsername("testUser"))
                .thenReturn(Optional.of(user));
        when(jwtService.isValidRefresh("refresh-token", user))
                .thenReturn(true);
        when(jwtService.generateAccessToken(user))
                .thenReturn("new-access-token");
        when(jwtService.generateRefreshToken(user))
                .thenReturn("new-refresh-token");

        ResponseEntity<?> response = authenticationService.refreshToken(httpServletRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AuthenticationResponseDto responseBody = (AuthenticationResponseDto) response.getBody();
        assertNotNull(responseBody);
        assertThat(responseBody.accessToken()).isEqualTo("new-access-token");
        assertThat(responseBody.refreshToken()).isEqualTo("new-refresh-token");


        verify(httpServletRequest, times(1)).getHeader("Authorization");
        verify(jwtService, times(1)).extractUsername("refresh-token");
        verify(jwtService, times(1)).generateAccessToken(user);
        verify(jwtService, times(1)).generateRefreshToken(user);
        verify(userRepository, times(1)).findByUsername("testUser");
        verify(jwtService, times(1)).isValidRefresh("refresh-token", user);
    }

    @Test
    public void testRefreshTokenWhenIsNotValid(){
        when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer refresh-token");
        when(jwtService.extractUsername("refresh-token"))
                .thenReturn("username");
        when(userRepository.findByUsername("username"))
                .thenReturn(Optional.of(user));
        when(jwtService.isValidRefresh("refresh-token", user))
                .thenReturn(false);

        ResponseEntity<?> response = authenticationService.refreshToken(httpServletRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo("It isn't a valid refresh token");

        verify(httpServletRequest, times(1)).getHeader("Authorization");
        verify(jwtService, times(1)).extractUsername("refresh-token");
        verify(userRepository, times(1)).findByUsername("username");
        verify(jwtService, times(1)).isValidRefresh("refresh-token", user);
    }
}

