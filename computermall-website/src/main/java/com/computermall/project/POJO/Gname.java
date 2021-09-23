package com.computermall.project.POJO;


import java.io.Serializable;

public class Gname implements Serializable {
    private String gname;

    public Gname() {
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    @Override
    public String toString() {
        return "Gname{" +
                "gname='" + gname + '\'' +
                '}';
    }
}
