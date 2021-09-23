package com.computermall.project.POJO;

import java.io.Serializable;

//服务端（后端）给浏览器（前端）的Websocket数据
public class ChatResultMessage implements Serializable {
    private boolean isSystem; //判断是系统消息(例如用户列表)还是某一个的消息格式(例如李四接收张三发送的消息)   暂定true为系统消息，false为某一个的消息格式
    private String formName; //如果是系统消息，取值为null；反之存放发送信息的用户名
        //由于不知道message存放的是数组类型还是字符串类型，这里使用Object对象进行存储
    private Object message; //如果是系统消息，则存放用户名（可能有多个用户名，所以要用到数组/集合）；反之存放某个用户发送的信息（用到String字符串）

    public ChatResultMessage() {
    }

    public ChatResultMessage(boolean isSystem, String formName, Object message) {
        this.isSystem = isSystem;
        this.formName = formName;
        this.message = message;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean system) {
        isSystem = system;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatResultMessage{" +
                "isSystem=" + isSystem +
                ", formName='" + formName + '\'' +
                ", message=" + message +
                '}';
    }
}
