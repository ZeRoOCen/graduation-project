package com.computermall.project.POJO;

import java.io.Serializable;

public class Good implements Serializable {
    private int gid; //商品主键id
    private String gname; //商品名称
    private float gprice; //商品价格
    private String sgsort; //关联子商品分类
    private String mgsort; //关联主商品分类
    private int gstate; //商品状态
    private String gimgurl; //商品图片路径
    private String gmessage; //商品信息描述

    public Good() {
    }

    public Good(int gid, String gname, float gprice, String sgsort, String mgsort, int gstate, String gimgurl, String gmessage) {
        this.gid = gid;
        this.gname = gname;
        this.gprice = gprice;
        this.sgsort = sgsort;
        this.mgsort = mgsort;
        this.gstate = gstate;
        this.gimgurl = gimgurl;
        this.gmessage = gmessage;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public float getGprice() {
        return gprice;
    }

    public void setGprice(float gprice) {
        this.gprice = gprice;
    }

    public String getSgsort() {
        return sgsort;
    }

    public void setSgsort(String sgsort) {
        this.sgsort = sgsort;
    }

    public String getMgsort() {
        return mgsort;
    }

    public void setMgsort(String mgsort) {
        this.mgsort = mgsort;
    }

    public int getGstate() {
        return gstate;
    }

    public void setGstate(int gstate) {
        this.gstate = gstate;
    }

    public String getGimgurl() {
        return gimgurl;
    }

    public void setGimgurl(String gimgurl) {
        this.gimgurl = gimgurl;
    }

    public String getGmessage() {
        return gmessage;
    }

    public void setGmessage(String gmessage) {
        this.gmessage = gmessage;
    }

    @Override
    public String toString() {
        return "Good{" +
                "gid=" + gid +
                ", gname='" + gname + '\'' +
                ", gprice=" + gprice +
                ", sgsort='" + sgsort + '\'' +
                ", mgsort='" + mgsort + '\'' +
                ", gstate=" + gstate +
                ", gimgurl='" + gimgurl + '\'' +
                ", gmessage='" + gmessage + '\'' +
                '}';
    }
}
