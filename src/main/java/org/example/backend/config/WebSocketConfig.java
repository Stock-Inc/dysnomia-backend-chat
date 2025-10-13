package org.example.backend.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Configuration
@EnableWebSocketMessageBroker
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private SimpMessageSendingOperations clientOutboundChannel;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
    @Bean
    public ApplicationListener<SessionSubscribeEvent> webSocketEventListener(
            final AbstractSubscribableChannel clientOutboundChannel) {
        return event -> {
            Message<byte[]> message = event.getMessage();
            StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);
            if (stompHeaderAccessor.getReceipt() != null) {
                stompHeaderAccessor.setHeader("stompCommand", StompCommand.RECEIPT);
                stompHeaderAccessor.setReceiptId(stompHeaderAccessor.getReceipt());
                clientOutboundChannel.send(
                        MessageBuilder.createMessage(new byte[0], stompHeaderAccessor.getMessageHeaders())
                );
            }
        };
    }
}