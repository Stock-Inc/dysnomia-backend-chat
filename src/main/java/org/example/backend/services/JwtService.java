package org.example.backend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.example.backend.exceptions.HeaderIsInvalidException;
import org.example.backend.exceptions.TokenInvalidException;
import org.example.backend.exceptions.UserNotExistsException;
import org.example.backend.exceptions.UsernameNotEqualsTokenException;
import org.example.backend.models.User;
import org.example.backend.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final UserServiceImpl userServiceImpl;
    private final TokenRepository tokenRepository;
    @Value("${token.signing.key}")
    private String secretKey;
    @Value("${access_token_expiration}")
    private long accessTokenExpiration;
    @Value("${refresh_token_expiration}")
    private long refreshTokenExpiration;

    public JwtService(TokenRepository tokenRepository, UserServiceImpl userServiceImpl) {
        this.tokenRepository = tokenRepository;
        this.userServiceImpl = userServiceImpl;
    }

    public boolean isValid(String token, UserDetails user) {

        String username = extractUsername(token);

        boolean isValidToken = tokenRepository.findByAccessToken(token)
                .map(t -> !t.isLoggedOut()).orElse(false);

        return username.equals(user.getUsername())
                && !isAccessTokenExpired(token)
                && isValidToken;
    }


    public boolean isValidRefresh(String token, User user) {
        String username = extractUsername(token);

        boolean isValidRefreshToken = tokenRepository.findByRefreshToken(token)
                .map(t -> !t.isLoggedOut()).orElse(false);

        return username.equals(user.getUsername())
                && isAccessTokenExpired(token)
                && isValidRefreshToken;
    }


    public boolean isAccessTokenExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (Exception ex) {
            throw new TokenInvalidException();
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        try {
            Claims claims = extractAllClaims(token);
            return resolver.apply(claims);
        } catch (Exception ex) {
            throw new TokenInvalidException();
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            JwtParserBuilder parser = Jwts.parser();

            parser.verifyWith(getSgningKey());

            return parser.build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception ex) {
            throw new TokenInvalidException();
        }
    }

    public String generateAccessToken(User user) {

        return generateToken(user, accessTokenExpiration);
    }


    public String generateRefreshToken(User user) {

        return generateToken(user, refreshTokenExpiration);
    }


    private String generateToken(User user, long expiryTime) {
        JwtBuilder builder = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiryTime))
                .signWith(getSgningKey());

        return builder.compact();
    }


    private SecretKey getSgningKey() {

        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsernameByToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new HeaderIsInvalidException();
        }

        return authHeader.substring(7);
    }

    public void validateAccessToken(String username, HttpServletRequest request) {

        String token = extractUsernameByToken(request);

        if (!userServiceImpl.existsByUsername(username)) {
            throw new UserNotExistsException();
        }

        if (!extractUsername(token).equals(username)) {
            throw new UsernameNotEqualsTokenException();
        }

    }
}