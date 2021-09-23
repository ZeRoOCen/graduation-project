package com.computermall.project.POJO;

public class Uname {
    private String uname;

    @Override
    public String toString() {
        return "Uname{" +
                "uname='" + uname + '\'' +
                '}';
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Uname(String uname) {
        this.uname = uname;
    }

    public Uname() {
    }
}
