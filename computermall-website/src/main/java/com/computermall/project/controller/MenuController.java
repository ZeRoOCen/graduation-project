package com.computermall.project.controller;

import com.alibaba.fastjson.JSON;
import com.computermall.project.POJO.MainMenu;
import com.computermall.project.POJO.SubMenu;
import com.computermall.project.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class MenuController {

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    @Qualifier("redisTemplate") //由于我们自定义的redisTemplate与官方的重名了，所以一定要让配置的redisTemplate指向的是自己配置的
    private RedisTemplate redisTemplate;

    @GetMapping("/getmenus")
    public Object getAllMenus(){
        HashMap<String, Object> data = new HashMap<>(); //使用HashMap来存储数据
        List<MainMenu> menus = menuMapper.getMenus(); //获取数据库中主菜单、子菜单、三级菜单的信息
        if( menus != null ){
            data.put("menus",menus); //将获取到的信息发送到HashMap函数中进行存储
//          data.put("subMenus",subMenus); //同上
            redisTemplate.opsForValue().set("data_json",data);  //将res中的内容(此题为allUser对象信息)转换成json字符串并保存到res_json中
            Object data_json = redisTemplate.opsForValue().get("data_json");
//            String s = JSON.toJSONString(data); 使用fastjson依赖中的toJSONString方法将HashMap函数(data)转换成JSON字符串形式
            return data_json;
        }
        return "无法获取到数据库中主菜单和子菜单的信息";
    }
}
