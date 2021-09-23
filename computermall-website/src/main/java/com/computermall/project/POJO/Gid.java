package com.computermall.project.POJO;

import java.io.Serializable;

public class Gid implements Serializable {
    private Integer gid;

    public Gid() {
    }

    public Gid(Integer gid) {
        this.gid = gid;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    @Override
    public String toString() {
        return "Gid{" +
                "gid=" + gid +
                '}';
    }
}
