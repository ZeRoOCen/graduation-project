package com.computermall.project.serviceImpl;

import com.computermall.project.POJO.SpareChatInfo;
import com.computermall.project.POJO.SpareCustomer;
import com.computermall.project.POJO.UnameAndId;
import com.computermall.project.POJO.User;
import com.computermall.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public Integer getUserCounts(String query) {
        return userMapper.getUserCounts(query);
    }

    @Override
    public List<User> getAllUser(String query, Integer pageStart, Integer pageSize) {
        return userMapper.getAllUser(query, pageStart, pageSize);
    }

    @Override
    public User getUserInfo(Integer uid){
        return userMapper.getUserInfo(uid);
    }

    @Override
    public Integer updateUserState(Integer uid, Integer ustate) {
        return userMapper.updateUserState(uid, ustate);
    }

    @Override
    public Integer updateUname(String uname, Integer uid){
        return userMapper.updateUname(uname, uid);
    }

    @Override
    public Integer updateUsex(Integer usex, Integer uid) {
        return userMapper.updateUsex(usex, uid);
    }

    @Override
    public Integer updateUmobile(String umobile, Integer uid) {
        return userMapper.updateUmobile(umobile, uid);
    }

    @Override
    public Integer updateUaddress(String uaddress, Integer uid) {
        return userMapper.updateUaddress(uaddress, uid);
    }

    @Override
    public Integer addUser(User user) {
        return userMapper.addUser(user);
    }

    @Override
    public Integer getNewUserId() {
        return userMapper.getNewUserId();
    }

    @Override
    public Integer updateUserAvatar(Integer uid, String uavatar) {
        return userMapper.updateUserAvatar(uid, uavatar);
    }

    @Override
    public Integer editUser(User user) {
        return userMapper.editUser(user);
    }

    @Override
    public Integer deleteUser(Integer uid){
        return userMapper.deleteUser(uid);
    }

    @Override
    public Integer selectConnectionId(String cname, String uname) {
        return userMapper.selectConnectionId(cname, uname);
    }

    @Override
    public Integer addContactInfo(String cname, String uname, String onlyId) {
        return userMapper.addContactInfo(cname, uname, onlyId);
    }

    @Override
    public Integer saveChatInfo(String id, String name, String message, String time) {
        return userMapper.saveChatInfo(id, name, message, time);
    }

    @Override
    public List<SpareChatInfo> getChatMessage(String id) {
        return userMapper.getChatMessage(id);
    }

    @Override
    public List<UnameAndId> getUserList(String cname) {
        return userMapper.getUserList(cname);
    }

    @Override
    public List<SpareCustomer> selectContactInfo(String uname) {
        return userMapper.selectContactInfo(uname);
    }

    @Override
    public Integer getCartId() {
        return userMapper.getCartId();
    }

    @Override
    public Integer addNameIdAndRoleToCart(Integer uid, String uname, Integer urole, Integer cartid) {
        return userMapper.addNameIdAndRoleToCart(uid, uname, urole, cartid);
    }

}
