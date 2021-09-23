package com.computermall.project.POJO;

public class Cname {
    public String cname;

    public Cname() {
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    @Override
    public String toString() {
        return "Cname{" +
                "cname='" + cname + '\'' +
                '}';
    }
}
