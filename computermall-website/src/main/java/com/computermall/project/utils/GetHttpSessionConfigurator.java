package com.computermall.project.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

/*
* 修改握手请求的对象和握手的服务器端点配置的推送
* */

public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    //modify Handshake修改握手，ServerEndpointConfig服务器端点配置，HandshakeRequest握手请求，HandshakeResponse握手响应
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response){
        //获取HttpSession
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        //将httpSessiond对象通过put方法存储到配置对象中。
        //以httpSession字节码名称为键对象，httpSession为值对象存储起来（Map<String, Object> getUserProperties()）
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }





}
