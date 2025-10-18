package org.example.backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.backend.exceptions.HeaderIsInvalidException;
import org.example.backend.models.Message;
import org.example.backend.services.SearchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchControllerTest {
    String authHeader;
    String query = "find";
    List<Message> messages = new ArrayList<>();
    @Mock
    private SearchService searchService;
    @Mock
    private HttpServletRequest request;
    @InjectMocks
    private SearchController searchController;

    @Test
    public void testSearchMessagesSuccessEmptyList() {
        authHeader = "Bearer token";
        when(request.getHeader(HttpHeaders.AUTHORIZATION))
                .thenReturn(authHeader);
        when(searchService.searchMessages(authHeader, query))
                .thenReturn(messages);

        ResponseEntity<List<Message>> responseEntity = searchController.searchMessages(request, query);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(messages, responseEntity.getBody());

        verify(request, times(1)).getHeader(HttpHeaders.AUTHORIZATION);
        verify(searchService, times(1)).searchMessages(authHeader, query);
    }

    @Test
    public void testSearchMessagesSuccessWhenListIsNotEmpty() {
        messages.add(new Message());
        authHeader = "Bearer token";
        when(request.getHeader(HttpHeaders.AUTHORIZATION))
                .thenReturn(authHeader);
        when(searchService.searchMessages(authHeader, query))
                .thenReturn(messages);

        ResponseEntity<List<Message>> responseEntity = searchController.searchMessages(request, query);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(messages, responseEntity.getBody());

        verify(request, times(1)).getHeader(HttpHeaders.AUTHORIZATION);
        verify(searchService, times(1)).searchMessages(authHeader, query);
    }

    @Test
    public void testSearchMessagesWhenThrowException() {
        authHeader = "token";
        when(request.getHeader(HttpHeaders.AUTHORIZATION))
                .thenReturn(authHeader);
        doThrow(HeaderIsInvalidException.class).when(searchService).searchMessages(authHeader, query);

        assertThatThrownBy(() -> searchController.searchMessages(request, query))
                .isInstanceOf(HeaderIsInvalidException.class);

        verify(request, times(1)).getHeader(HttpHeaders.AUTHORIZATION);
        verify(searchService, times(1)).searchMessages(authHeader, query);
    }
}
