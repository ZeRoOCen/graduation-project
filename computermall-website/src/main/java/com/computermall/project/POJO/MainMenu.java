package com.computermall.project.POJO;

import lombok.*;

import java.io.Serializable;
import java.util.List;

//getter\setter方法无法使用，初步判断是tList集合问题
//@Data
//@ToSring
//@AllArgsConstructor
//@NoArgsConstructor

//后台管理 —— 主菜单
public class MainMenu implements Serializable {
    private int mid;                //id
    private String mtitle;          //可管理信息
    private String mpath;           //地址
    private List<SubMenu> sList;    //关联子菜单信息
//    private List<ThirdMenu> tList;

//    无tList
    public MainMenu(){
    }
    public MainMenu(int mid, String mtitle, String mpath, List<SubMenu> sList) {
        this.mid = mid;
        this.mtitle = mtitle;
        this.mpath = mpath;
        this.sList = sList;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getMtitle() {
        return mtitle;
    }

    public void setMtitle(String mtitle) {
        this.mtitle = mtitle;
    }

    public String getMpath() {
        return mpath;
    }

    public void setMpath(String mpath) {
        this.mpath = mpath;
    }

    public List<SubMenu> getsList() {
        return sList;
    }

    public void setsList(List<SubMenu> sList) {
        this.sList = sList;
    }

    @Override
    public String toString() {
        return "MainMenu{" +
                "mid=" + mid +
                ", mtitle='" + mtitle + '\'' +
                ", mpath='" + mpath + '\'' +
                ", sList=" + sList +
                '}';
    }

    //有tList
//    public MainMenu() {
//    }
//
//    public MainMenu(int mid, String mtitle, String mpath, List<SubMenu> sList, List<ThirdMenu> tList) {
//        this.mid = mid;
//        this.mtitle = mtitle;
//        this.mpath = mpath;
//        this.sList = sList;
//        this.tList = tList;
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
//    public String getMtitle() {
//        return mtitle;
//    }
//
//    public void setMtitle(String mtitle) {
//        this.mtitle = mtitle;
//    }
//
//    public String getMpath() {
//        return mpath;
//    }
//
//    public void setMpath(String mpath) {
//        this.mpath = mpath;
//    }
//
//    public List<SubMenu> getsList() {
//        return sList;
//    }
//
//    public void setsList(List<SubMenu> sList) {
//        this.sList = sList;
//    }
//
//    public List<ThirdMenu> gettList() {
//        return tList;
//    }
//
//    public void settList(List<ThirdMenu> tList) {
//        this.tList = tList;
//    }
//
//    @Override
//    public String toString() {
//        return "MainMenu{" +
//                "mid=" + mid +
//                ", mtitle='" + mtitle + '\'' +
//                ", mpath='" + mpath + '\'' +
//                ", sList=" + sList +
//                ", tList=" + tList +
//                '}';
//    }

}

