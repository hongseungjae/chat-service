package com.hong.chatservice.chat.config;

import com.hong.chatservice.member.application.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    private final JwtUtil jwtUtil;
    private final WebSocketHandShakeHandler webSocketHandShakeHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //config.enableSimpleBroker("/topic");
        //config.setApplicationDestinationPrefixes("/app");

        config.setApplicationDestinationPrefixes("/app")
                .enableStompBrokerRelay("/topic")
                .setUserDestinationBroadcast("/topic/unresolved-user")
                .setUserRegistryBroadcast("/topic/user-registry")
                .setRelayHost("localhost")
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");

        //config.setUserRegistryOrder(Ordered.HIGHEST_PRECEDENCE + 3);

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOriginPatterns("*")
                //.setHandshakeHandler(webSocketHandShakeHandler)
                .addInterceptors(customHttpSessionHandshakeInterceptor())
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtChannelInterceptor());
    }

    @Bean
    public JwtChannelInterceptor jwtChannelInterceptor(){
        return new JwtChannelInterceptor(jwtUtil);
    }

    @Bean
    public CustomHttpSessionHandshakeInterceptor customHttpSessionHandshakeInterceptor() {
        return new CustomHttpSessionHandshakeInterceptor();
    }

}