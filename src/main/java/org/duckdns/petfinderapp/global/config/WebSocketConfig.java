package org.duckdns.petfinderapp.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
				.setAllowedOriginPatterns("*") // Allow all origins for development; adjust for production
				.withSockJS(); // Enable SockJS fallback options
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 서버 → 클라이언트로 메시지를 보내는 주소 prefix
		registry.enableSimpleBroker("/queue", "/topic");

		// 클라이언트 → 서버로 메시지를 보낼 때 prefix
		registry.setApplicationDestinationPrefixes("/app");

		// 1:1 메시지 전송을 위한 유저별 prefix
		registry.setUserDestinationPrefix("/user");
	}
}
