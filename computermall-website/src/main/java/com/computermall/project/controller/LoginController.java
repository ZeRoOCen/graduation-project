package com.computermall.project.controller;


import com.alibaba.fastjson.JSON;
import com.computermall.project.POJO.User;
import com.computermall.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class LoginController {

    @Autowired
    UserMapper userMapper; //自动装配userMapper，从而可以使用userMapper中的接口

    @Autowired
    @Qualifier("redisTemplate") //由于我们自定义的redisTemplate与官方的重名了，所以一定要让配置的redisTemplate指向的是自己配置的
    private RedisTemplate redisTemplate;

//  一：接收前端传递过来的this.loginForm中的uname和upassword，封装到(@RequestBody User user)的user中
//  二：通过User allUser = userMapper.getUserByMessage(user.getUname(),user.getUpassword())，把user中的Uname和Upassword传递给userMapper中，
//     然后通过userMapper.xml中的查询语句方法将前端传递的Uname和Upassword与User表中的Uname和Upassword进行匹配，
//     如果存在，则把查询到的内容(此题为select *，即此用户的所有信息)赋给User对象的allUser上
//  三：最后转换成JSON字符串格式返回给前端(Login.vue)
    @PostMapping("/login")
    public Object login(@RequestBody User user){
        User loginUserInfo = userMapper.getUserByMessage(user.getUname(),user.getUpassword());
        HashMap<String, Object> res = new HashMap<>(); //这里使用HashMap来存储信息
        if(loginUserInfo!=null){
            //res.put("user",user); user仅返回uname和upassword
            res.put("loginUserInfo",loginUserInfo); //把allUser对象保存到HashMap函数中。allUSer返回查询到的所有信息，如uid, uname, upassword, usex, urole...
            redisTemplate.opsForValue().set("res_json",res);  //将res中的内容(此题为allUser对象信息)转换成json字符串并保存到res_json中
            Object res_json = redisTemplate.opsForValue().get("res_json"); //使用redis返回json字符串格式
            //String res_json = JSON.toJSONString(res);                    //使用fastjson返回json字符串格式
            return res_json;
        }
        return "用户信息匹配不上";
    }
}
//redisTemplate.opsForValue().set("res_json",res);   Object res_json = redisTemplate.opsForValue().get("res_json"); 使用redis返回Object对象
//一：public Object login(@RequestBody User user) ->返回Object对象
//   {allUser=User{uid=1, uname='admin', upassword='123456', usex=0, urole=10, umobile='123', uaddress='123'}}

//String res_json = JSON.toJSONString(res);  使用fastjson返回json字符串格式
//一：public User login(@RequestBody User user) -> 返回User对象
//   Object { uid: 0, uname: "admin", upassword: "123456", usex: 0, urole: 0, umobile: null, uaddress: null }
//二：public String login(@RequestBody User user) -> 返回Json字符串格式
//   user: Object { uid: 0, uname: "admin", upassword: "123456", usex: 0, urole: 0, umobile: null, uaddress: null}
//二：public HashMap login(@RequestBody User user) -> 返回HashMap格式
//   {allUser=User{uid=1, uname='admin', upassword='123456', usex=0, urole=10, umobile='123', uaddress='123'}}