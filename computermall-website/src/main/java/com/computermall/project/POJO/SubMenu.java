package com.computermall.project.POJO;

import lombok.*;

import java.io.Serializable;
import java.util.List;

//为了防止getter\setter无法解决list集合问题，这里就不使用了
//@Getter
//@Setter
//@ToString
//@AllArgsConstructor
//@NoArgsConstructor

//后台管理 —— 子菜单
public class SubMenu implements Serializable {
    private int sid;        //ids
    private String stitle;  //可管理信息
    private String spath;   //地址
    private int mid; //关联主菜单主键id，写不写都可以，不影响MenuMapper.xml的判断。where mm.mid = sm.mid
    private List<ThirdMenu> tList; //关联三级菜单信息

    //无tList
//    public SubMenu() {
//    }
//
//    public SubMenu(int sid, String stitle, String spath, int mid) {
//        this.sid = sid;
//        this.stitle = stitle;
//        this.spath = spath;
//        this.mid = mid;
//    }
//
//    public int getSid() {
//        return sid;
//    }
//
//    public void setSid(int sid) {
//        this.sid = sid;
//    }
//
//    public String getStitle() {
//        return stitle;
//    }
//
//    public void setStitle(String stitle) {
//        this.stitle = stitle;
//    }
//
//    public String getSpath() {
//        return spath;
//    }
//
//    public void setSpath(String spath) {
//        this.spath = spath;
//    }
//
//    public int getMid() {
//        return mid;
//    }
//
//    public void setMid(int mid) {
//        this.mid = mid;
//    }
//
//    @Override
//    public String toString() {
//        return "SubMenu{" +
//                "sid=" + sid +
//                ", stitle='" + stitle + '\'' +
//                ", spath='" + spath + '\'' +
//                ", mid=" + mid +
//                '}';
//    }

//有tList
    public SubMenu() {
    }

    public SubMenu(int sid, String stitle, String spath, int mid, List<ThirdMenu> tList) {
        this.sid = sid;
        this.stitle = stitle;
        this.spath = spath;
        this.mid = mid;
        this.tList = tList;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getStitle() {
        return stitle;
    }

    public void setStitle(String stitle) {
        this.stitle = stitle;
    }

    public String getSpath() {
        return spath;
    }

    public void setSpath(String spath) {
        this.spath = spath;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public List<ThirdMenu> gettList() {
        return tList;
    }

    public void settList(List<ThirdMenu> tList) {
        this.tList = tList;
    }

    @Override
    public String toString() {
        return "SubMenu{" +
                "sid=" + sid +
                ", stitle='" + stitle + '\'' +
                ", spath='" + spath + '\'' +
                ", mid=" + mid +
                ", tList=" + tList +
                '}';
    }

}
