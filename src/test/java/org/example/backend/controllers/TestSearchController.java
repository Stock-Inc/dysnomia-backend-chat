package org.example.backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.backend.exceptions.HeaderIsInvalidException;
import org.example.backend.models.Message;
import org.example.backend.services.SearchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestSearchController {
    @Mock
    private SearchService searchService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private SearchController searchController;

    @Test
    public void testServiceWhenTokenIsCorrect() {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer token");
        when(searchService.searchMessages("Bearer token"
                , "query")).thenReturn(List.of());
        ResponseEntity<List<Message>> responseEntity
                = searchController.searchMessages(request, "query");

        assertEquals(responseEntity.getStatusCode().value(), HttpStatus.OK.value());

        verify(searchService, times(1))
                .searchMessages("Bearer token", "query");
    }
}
