package com.computermall.project.POJO;

public class CustomerName {
    private String cname;

    @Override
    public String toString() {
        return "CustomerName{" +
                "cname='" + cname + '\'' +
                '}';
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public CustomerName(String cname) {
        this.cname = cname;
    }

    public CustomerName() {
    }
}
