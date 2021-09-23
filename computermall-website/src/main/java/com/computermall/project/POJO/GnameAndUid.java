package com.computermall.project.POJO;

import lombok.Data;

public class GnameAndUid {
    private String gname;
    private Integer uid;

    public GnameAndUid() {
    }

    public GnameAndUid(String gname, Integer uid) {
        this.gname = gname;
        this.uid = uid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "GnameAndUid{" +
                "gname='" + gname + '\'' +
                ", uid=" + uid +
                '}';
    }
}
