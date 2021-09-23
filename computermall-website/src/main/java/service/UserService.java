package service;

import com.computermall.project.POJO.SpareChatInfo;
import com.computermall.project.POJO.SpareCustomer;
import com.computermall.project.POJO.UnameAndId;
import com.computermall.project.POJO.User;

import java.util.List;

public interface UserService {
    //获取最大列表数(用户总数)。用户名进行模糊查询并进行int类型的数字存储。
    public Integer getUserCounts(String query);

    //获取每页的用户信息
    public List<User> getAllUser(String query, Integer pageStart, Integer pageSize);

    //根据用户id查询此用户全部信息
    public User getUserInfo(Integer uid);

    //更新用户状态
    public Integer updateUserState(Integer uid, Integer ustate);

    //更新用户名
    public Integer updateUname(String uname, Integer uid);

    //修改用户姓别
    public Integer updateUsex(Integer usex, Integer uid);

    //修改手机号
    public Integer updateUmobile(String umobile, Integer uid);

    //修改地址
    public Integer updateUaddress(String uaddress, Integer uid);

    //添加用户
    public Integer addUser(User user);

    //获取到数据库中新添加的用户的id
    public Integer getNewUserId();

    //根据新添加的用户的id，把头像传给它
    public Integer updateUserAvatar(Integer uid, String uavatar);

    //编辑用户信息
    public Integer editUser(User user);

    //删除用户信息
    public Integer deleteUser(Integer uid);

    //判断是否存在用户对应的商品客服名的唯一id
    public Integer selectConnectionId(String cname, String uname);

    //根据用户名和商品客服名添加唯一ID
    public Integer addContactInfo(String cname, String uname, String onlyId);

    //保存所有消息信息
    public Integer saveChatInfo(String id, String name, String message, String time);

    //获取到指定连接id的唯一消息id，发送名称，发送信息和发送时间
    public List<SpareChatInfo> getChatMessage(String id);

    //客服获取用户列表
    public List<UnameAndId> getUserList(String cname);

    //获取该用户名的所有联系客服信息
    public List<SpareCustomer> selectContactInfo(String uname);

    //获取购物车id
    public Integer getCartId();

    //将用户名和用户角色权限加入到购物车数据库中
    public Integer addNameIdAndRoleToCart(Integer uid, String uname, Integer urole, Integer cartid);
}
