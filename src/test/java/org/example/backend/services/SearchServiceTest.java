package org.example.backend.services;

import org.example.backend.exceptions.HeaderIsInvalidException;
import org.example.backend.models.Message;
import org.example.backend.repositories.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private SearchService searchService;

    @Test
    public void testSearchMessagesWhenHeaderIsNull() {
        assertThatThrownBy(() -> searchService.searchMessages(null, ""))
                .isInstanceOf(HeaderIsInvalidException.class);
    }

    @Test
    public void testSearchMessagesWhenHeaderNotStartWithBearer() {
        assertThatThrownBy(() -> searchService.searchMessages("not bearer", "not null"))
                .isInstanceOf(HeaderIsInvalidException.class);
    }

    @Test
    public void testSearchMessagesWhenTokenExpired() {
        when(jwtService.isAccessTokenExpired(anyString()))
                .thenReturn(true);


        assertThatThrownBy(() -> searchService
                .searchMessages("Bearer token expired", ""))
                .isInstanceOf(HeaderIsInvalidException.class);

        verify(jwtService, times(1)).isAccessTokenExpired(anyString());
    }

    @Test
    public void testSearchMessagesWhenTokenNotExpiredAndListEmpty() {
        when(jwtService.isAccessTokenExpired(anyString()))
                .thenReturn(false);
        when(messageRepository.findByQuery("string")).thenReturn(List.of());

        List<Message> list = searchService.searchMessages("Bearer token expired", "string");

        assertEquals(0, list.size());

        verify(jwtService, times(1)).isAccessTokenExpired(anyString());
        verify(messageRepository, times(1)).findByQuery("string");
    }

    @Test
    public void testSearchMessagesWhenTokenNotExpiredAndListNotEmpty() {
        Message message = new Message();
        when(jwtService.isAccessTokenExpired(anyString()))
                .thenReturn(false);
        when(messageRepository.findByQuery("string")).thenReturn(List.of(message));

        List<Message> list = searchService.searchMessages("Bearer token expired", "string");

        assertEquals(1, list.size());
        assertEquals(message, list.get(0));

        verify(jwtService, times(1)).isAccessTokenExpired(anyString());
        verify(messageRepository, times(1)).findByQuery("string");
    }
}
