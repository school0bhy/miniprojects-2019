package com.wootecobook.turkey.websocket.a;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //해당 API를 구독하고 있는 클라이언트에게 메세지 전달
        registry.enableSimpleBroker("/topic", "/queue");

        //서버에서 클라이언트로부터의 메세지를 받을 api의 prefix 설정
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //클라이언트에서 websocket을 연결할 api를 설정
        registry.addEndpoint("/websocket")
                .setAllowedOrigins("*")
                .addInterceptors(new HttpHandshakeInterceptor())
                .setHandshakeHandler(new CustomHandshakeHandler())
                .withSockJS();
    }

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        //해당 API를 구독하고 있는 클라이언트에게 메세지 전달
//        registry.enableSimpleBroker("/secured/user/queue/specific-user");
//        registry.setApplicationDestinationPrefixes("/spring-security-mvc-socket");
//        registry.setUserDestinationPrefix("/secured/user");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        //클라이언트에서 websocket을 연결할 api를 설정
//        registry.addEndpoint("/secured/room").withSockJS();
//    }
}
