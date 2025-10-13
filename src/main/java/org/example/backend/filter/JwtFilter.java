package org.example.backend.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.backend.models.ErrorResponse;
import org.example.backend.repositories.UserService;
import org.example.backend.services.JwtService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Lazy
    private final JwtService jwtService;

    private final UserService userService;

    public JwtFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            try {
                filterChain.doFilter(request, response);
            } catch (Exception ex) {
                ErrorResponse errorResponse = new ErrorResponse("Token invalid");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.addHeader("error", ex.getMessage());
            }
            return;
        }
        String token = authHeader.substring(7);
        String username = null;

        try {
            username = jwtService.extractUsername(token);
        } catch (RuntimeException ex) {
            ErrorResponse errorResponse = new ErrorResponse("Token invalid");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.addHeader("error", ex.getMessage());
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            try {
                userDetails = userService.loadUserByUsername(username);
            } catch (RuntimeException ex) {
                ErrorResponse errorResponse = new ErrorResponse("Token invalid");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.addHeader("error", ex.getMessage());
            }
            if (jwtService.isValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        try {
            filterChain.doFilter(request, response);
        }catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse("Token invalid");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.addHeader("error", ex.getMessage());
        }
    }
}
