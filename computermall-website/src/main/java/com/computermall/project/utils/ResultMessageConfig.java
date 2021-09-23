package com.computermall.project.utils;

import com.computermall.project.POJO.ChatResultMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//消息工具类，将isSystemMessage, fromName, message封装到一个result中
public class ResultMessageConfig {
    public static String getMessage(boolean isSystemMessage, String fromName, Object message) throws JsonProcessingException {
        try {
            ChatResultMessage result = new ChatResultMessage();
            result.setIsSystem(isSystemMessage);
            result.setMessage(message);
            if (fromName != null) {
                result.setFormName(fromName);
            }
            //ObjectMapper类是Jackson库的主要类。它提供一些功能，这些功能将为Java对象转化成匹配的JSON结构(比如JSONString字符串类型结构)
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(result); //将result —— JAVA对象类型转换成JSONString字符串类型

        }catch(JsonProcessingException e){
            e.printStackTrace();
        }
        return null; //如果没有进入try，则返回null
    }
}
