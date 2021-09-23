package com.computermall.project.POJO;

import java.io.Serializable;

//如果使用lombok，LoginController中的User allUser = userMapper.getUserByMessage(user.getUname(),user.getUpassword())语句
//获取不到getUanme()和getUpassword()
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class User implements Serializable {
    private int uid;            //用户id（自增字段）
    private String uname;       //用户名
    private String upassword;   //密码
    private int usex;           //性别
    private int urole;          //角色
    private int uonline;        //是否在线
    private int ustate;          //用户状态
    private String umobile;     //手机号
    private String uaddress;    //家庭住址
    private String uavatar;     //用户头像

    public User() {
    }

    public User(int uid, String uname, String upassword, int usex, int urole, int uonline, int ustate, String umobile, String uaddress, String uavatar) {
        this.uid = uid;
        this.uname = uname;
        this.upassword = upassword;
        this.usex = usex;
        this.urole = urole;
        this.uonline = uonline;
        this.ustate = ustate;
        this.umobile = umobile;
        this.uaddress = uaddress;
        this.uavatar = uavatar;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }

    public int getUsex() {
        return usex;
    }

    public void setUsex(int usex) {
        this.usex = usex;
    }

    public int getUrole() {
        return urole;
    }

    public void setUrole(int urole) {
        this.urole = urole;
    }

    public int getUonline() {
        return uonline;
    }

    public void setUonline(int uonline) {
        this.uonline = uonline;
    }

    public int getUstate() {
        return ustate;
    }

    public void setUstate(int ustate) {
        this.ustate = ustate;
    }

    public String getUmobile() {
        return umobile;
    }

    public void setUmobile(String umobile) {
        this.umobile = umobile;
    }

    public String getUaddress() {
        return uaddress;
    }

    public void setUaddress(String uaddress) {
        this.uaddress = uaddress;
    }

    public String getUavatar() {
        return uavatar;
    }

    public void setUavatar(String uavatar) {
        this.uavatar = uavatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", uname='" + uname + '\'' +
                ", upassword='" + upassword + '\'' +
                ", usex=" + usex +
                ", urole=" + urole +
                ", uonline=" + uonline +
                ", ustate=" + ustate +
                ", umobile='" + umobile + '\'' +
                ", uaddress='" + uaddress + '\'' +
                ", uavatar='" + uavatar + '\'' +
                '}';
    }

}
