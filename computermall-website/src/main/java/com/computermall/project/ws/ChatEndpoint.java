package com.computermall.project.ws;

import com.computermall.project.utils.GetHttpSessionConfigurator;
import com.computermall.project.utils.ResultMessageConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/*
* @description websocket的具体实现类
* */

@ServerEndpoint(value = "/websocket", configurator = GetHttpSessionConfigurator.class)
@Component
public class ChatEndpoint {

    //以"用户名"为key，ChatEndpoint为对象值存储起来
    //用来存储每一个客服端对象对应的ChaEndPoint对象，即会有多个ChatEndpoint对象，但只会有这一个容器(static特性)
    //static和concurrentHashMap的特性在笔记上有
    private static Map<String, ChatEndpoint> onlineUsers = new ConcurrentHashMap<>();

    //声明session对象，通过该对象可以发送消息给指定的用户
    //每个ChatEndpoint都会有一个session，所以不使用static
    private Session session;

    //声明HttpSession对象，我们之前登录用户时通过sessionStorage给HttpSession对象中存储了用户名
    private HttpSession httpSession;

    //当WebSocket的连接状态readyState变为1时调用;这意味着当前连接已经准备好发送和接受数据。这个事件处理程序通过事件（建立连接时）触发。
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws JsonProcessingException {
        //将局部session对象赋值给成员session
        this.session = session;
        //通过get方式获取httpSession对象
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.httpSession = httpSession;

        //从httpSession对象中获取用户名（前端的login页面保存用户名在session上，命名为userName）
        String username = (String) httpSession.getAttribute("userName");
//        String username = (String) httpSession.getAttribute("uname");
        System.out.println("username===>" + username);
        //将当前登录的对象存储到容器中(用户名为键，ChatEndpoint方法类为值)
        onlineUsers.put(username,this);

        //将当前在线用户推送给所有客服端
        //1.获取信息（由于ResultMessageConfig类中的getMessage是静态方法，可以全局直接调用，不需要创建new实例）
        //由于是系统消息，所以第三个参数要为数组/集合形式，getNames()在下面已经定义为set单链集合（单链集合只需要键）
        String message = ResultMessageConfig.getMessage(true,null, getNames());
        //2.调用下面的broadcastAllUsers方法进行系统消息的推送
        broadcastAllUsers(message);
    }

    //onlineUsers.ketSet():将Map中所有的键存入到set集合中。因为set具备迭代器。可以通过迭代方式取出所有的键，再根据.get()方法。获取每一个键对应的值。
    private Set<String> getNames(){
        return onlineUsers.keySet();
    }

    //系统消息的推送给所有客服端方法
    private void broadcastAllUsers(String message){
        try {
            //onlineUsers.ketSet():将Map中所有的键存入到set集合中。因为set具备迭代器。通过迭代方式取出所有的键，再根据.get()方法。获取每一个键对应的值。
            Set<String> names = onlineUsers.keySet();
            for (String name : names) {
                //获取onlineUsers的值
                ChatEndpoint chatEndpoint = onlineUsers.get(name);

                //getAsyncRemote() 异步推送消息， getBasicRemote() 同步推送消息
                //暂时的理解：当分别发送两条同步消息时，当第二条消息加载完毕需要发送时，如果第一条消息还没发送完，第二条消息需要等待第一条消息发送完才可以发送
                chatEndpoint.session.getAsyncRemote().sendText(message);
//              chatEndpoint.session.getBasicRemote().sendText(message);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //当收到来自服务器的消息时被调用的 EventHandler 。它由一个MessageEvent调用（前端相应的参数调用）
    @OnMessage
    public void onMessage(String message, Session session){

    }
    //在WebSocket连接的readyState变为CLOSED时被调用，它接收一个名字为“close”的 CloseEvent 事件
    @OnClose
    public void onClose(Session session){

    }




    //@Component
//public class ChatEndpoint extends TextWebSocketHandler {
//
//    //    //以"用户名"为key，ChatEndpoint为对象值存储起来
////    //用来存储每一个客服端对象对应的ChaEndPoint对象，即会有多个ChatEndpoint对象，但只会有这一个容器(static特性)
////    //static和concurrentHashMap的特性在笔记上有
//    private static Map<String, ChatEndpoint> onlineUsers = new ConcurrentHashMap<>();
////
////    //声明session对象，通过该对象可以发送消息给指定的用户
////    //每个ChatEndpoint都会有一个session，所以不使用static
//    private WebSocketSession webSocketSession;
////
////    //声明HttpSession对象，我们之前登录用户时通过sessionStorage给HttpSession对象中存储了用户名
//    private HttpSession httpSession;
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception
//    {
//        super.afterConnectionClosed(session, status);
//        System.out.println("close....");
//    }
//
////连接后确立。当WebSocket的连接状态readyState变为1时调用;这意味着当前连接已经准备好发送和接受数据。这个事件处理程序通过事件（建立连接时）触发。
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session, EndpointConfig config) throws Exception
//    {
//        super.afterConnectionEstablished(session);
//        //将局部session对象赋值给成员session
////        //通过get方式获取httpSession对象
//        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
//        this.webSocketSession = webSocketSession;
//        this.httpSession = httpSession;
////
////        //从httpSession对象中获取用户名（前端的login页面保存用户名在session上，命名为userName）
//        String username = (String) httpSession.getAttribute("userName");
////        //将当前登录的对象存储到容器中(用户名为键，ChatEndpoint方法类为值)
//        onlineUsers.put(username,this);
////
////        //将当前在线用户推送给所有客服端
////        //1.获取信息（由于ResultMessageConfig类中的getMessage是静态方法，可以全局直接调用，不需要创建new实例）
////        //由于是系统消息，所以第三个参数要为数组/集合形式，getNames()在下面已经定义为set单链集合（单链集合只需要键）
//        WebSocketMessage message = ResultMessageConfig.getMessage(true,null, getNames());
////        //2.调用下面的broadcastAllUsers方法进行系统消息的推送
//        broadcastAllUsers(message);
//    }
//
//        private Set<String> getNames(){
//        return onlineUsers.keySet();
//    }
//
//        //系统消息的推送给所有客服端方法
//    private void broadcastAllUsers(WebSocketMessage message){
//        try {
//            //onlineUsers.ketSet():将Map中所有的键存入到set集合中。因为set具备迭代器。通过迭代方式取出所有的键，再根据.get()方法。获取每一个键对应的值。
//            Set<String> names = onlineUsers.keySet();
//            for (String name : names) {
//                //获取onlineUsers的值
//                ChatEndpoint chatEndpoint = onlineUsers.get(name);
//
//                //getAsyncRemote() 异步推送消息， getBasicRemote() 同步推送消息
//                //暂时的理解：当分别发送两条同步消息时，当第二条消息加载完毕需要发送时，如果第一条消息还没发送完，第二条消息需要等待第一条消息发送完才可以发送
//                chatEndpoint.webSocketSession.sendMessage(message);
////              chatEndpoint.session.getBasicRemote().sendText(message);
//            }
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception
//    {
//        System.out.println(message.getPayload());
//        TextMessage msg=new TextMessage(message.getPayload());
//        session.sendMessage(msg);
//
//    }
//
//    @Override
//    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception
//    {
//        super.handleMessage(session, message);
//    }
//
//    @Override
//    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception
//    {
//        super.handleTransportError(session, exception);
//    }






}
