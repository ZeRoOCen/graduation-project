package com.computermall.project.POJO;

import java.io.Serializable;

public class Uid implements Serializable {
    private Integer uid; //用户id

    public Uid() {
    }

    public Uid(Integer uid) {
        this.uid = uid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Uname{" +
                "uname='" + uid + '\'' +
                '}';
    }
}
