package org.example.backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.backend.models.Message;
import org.example.backend.services.SearchService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@AllArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/message")
    public ResponseEntity<List<Message>> searchMessages(
            HttpServletRequest request,
            @RequestParam("q") String query
    ) {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        List<Message> messages = searchService.searchMessages(authHeader, query);


        return ResponseEntity.ok(messages);
    }
}
