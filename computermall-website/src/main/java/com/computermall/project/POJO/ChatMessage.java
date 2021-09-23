package com.computermall.project.POJO;


import java.io.Serializable;

//浏览器(前端)发送给服务器(后端)的Websocket数据
public class ChatMessage implements Serializable {
    private String toName; //保存要发送消息该哪个用户名
    private String message; //保存要发送的消息内容

    public ChatMessage() {
    }

    public ChatMessage(String toName, String message) {
        this.toName = toName;
        this.message = message;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "toName='" + toName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
