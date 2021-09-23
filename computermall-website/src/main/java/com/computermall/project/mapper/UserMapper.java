package com.computermall.project.mapper;

import com.computermall.project.POJO.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    //登录验证用户名密码
    public User getUserByMessage(@Param("userName")String uname, @Param("userPassword") String upassword);

    //获取用户信息，根据用户信息判断limit索引
        //limit i,n 常用于分页查询
        //pagestart(i)：查询结果的索引值(默认从0开始)，当i=0时可省略i
        //pagesize(n)：查询结果返回的数量
        //例子：# 查询10条数据，索引从0到9，第1条记录到第10条记录：  select * from t_user limit 10;   &&   select * from t_user limit 0,10;
        //     # 查询8条数据，索引从5到12，第6条记录到第13条记录：  select * from t_user limit 5,8;
    public List<User> getAllUser(@Param("userName")String uname, @Param("pageStart")Integer pageStart, @Param("pageSize")Integer pageSize);

        //通过唯一标示性的用户名来获取用户的数量
    public Integer getUserCounts(@Param("userName")String uname);

        //通过头像绝对路径匹配数据库中的用户头像
//    public String getAvatar(@Param("userList")String userList);

        //获取新添加的购物车ID
    public Integer getCartId();
        //根据新添加的购物车ID将用户名和用户角色权限加入到购物车表中
    public Integer addNameIdAndRoleToCart(@Param("userId")Integer uid, @Param("userName")String uname, @Param("userRole")Integer urole, @Param("cartId")Integer cartid);

        //修改用户状态
    public Integer updateUserState(@Param("userId")Integer uid, @Param("userState")Integer ustate);

        //根据用户id修改用户名
    public Integer updateUname(@Param("userName") String uname, @Param("userId")Integer uid);

        //根据用户id修改用户姓别
    public Integer updateUsex(@Param("userSex")  Integer usex, @Param("userId")Integer uid);

        //根据用户id修改用户手机号
    public Integer updateUmobile(@Param("userMobile") String umobile, @Param("userId")Integer uid);

        //根据用户id修改用户手机号
    public Integer updateUaddress(@Param("userAddress") String uaddress, @Param("userId")Integer uid);

        //添加用户
    public Integer addUser(User user);

        //查询得到新添加的用户的ID
    public Integer getNewUserId();

        //修改用户头像
    public Integer updateUserAvatar(@Param("userId") Integer uid, @Param("userAvatar") String uavatar);

        //删除用户
    public Integer deleteUser(@Param("userId") Integer uid);

        //根据用户ID查询此用户全部信息
    public User getUserInfo(@Param("userId") Integer uid);

        //编辑用户信息
    public int editUser(User user);

        //根据用户名和商品客服名判断备用联系客服表中是否存在该用户对此商品的唯一id，若不存在，则添加唯一id
    public Integer selectConnectionId(@Param("customerName") String cname, @Param("userName") String uname);

        //根据用户名和商品客服名添加唯一ID
    public Integer addContactInfo(@Param("customerName")String cname, @Param("userName")String uname, @Param("onlyId") String onlyId);

        //根据唯一ID查询对应的联系客服相关信息
    public List<SpareCustomer> selectContactInfo(@Param("userName") String uname);

        //获取客服头像
//    public String getCustomerAvatar(@Param("customerName") String cname);

        //保存发送消息相关的所有信息
    public Integer saveChatInfo(@Param("connectid") String id, @Param("sendName") String name,
                                @Param("sendMessage") String message, @Param("sendTime") String time);

        //获取到指定连接id的唯一消息id，发送名称，发送信息和发送时间
    public List<SpareChatInfo> getChatMessage(@Param("connectid") String id);

        //客服获取用户列表
    public List<UnameAndId> getUserList(@Param("customerName") String cname);
}
