package com.computermall.project.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/*
*
* @description WebSocketConfig配置类，注入对象ServerEndpointExporter，
*              这个bean会自动注册使用了@ServerEndpoint注解声明的WebsocketEndpoint
*
* */

@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
    public class WebSocketConfig{
//    @Autowired
//    ChatEndpoint chatEndpoint;
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(chatEndpoint(), "/websocket").setAllowedOrigins("*");
//    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
