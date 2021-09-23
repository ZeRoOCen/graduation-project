package com.computermall.project.POJO;

import java.io.Serializable;

//后台管理 —— 三级菜单
public class ThirdMenu implements Serializable {
    private int tid; //三级菜单主键id
    private String ttitle; //关联maingoodsort表的商品主分类mgsort作为自己的三级菜单名
    private String tpath; //三级菜单路径
    private int sid; //关联二级菜单主键sid，可以不写

    public ThirdMenu() {
    }

    public ThirdMenu(int tid, String ttitle, String tpath, int sid) {
        this.tid = tid;
        this.ttitle = ttitle;
        this.tpath = tpath;
        this.sid = sid;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTtitle() {
        return ttitle;
    }

    public void setTtitle(String ttitle) {
        this.ttitle = ttitle;
    }

    public String getTpath() {
        return tpath;
    }

    public void setTpath(String tpath) {
        this.tpath = tpath;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "ThirdMenu{" +
                "tid=" + tid +
                ", ttitle='" + ttitle + '\'' +
                ", tpath='" + tpath + '\'' +
                ", sid=" + sid +
                '}';
    }
}
