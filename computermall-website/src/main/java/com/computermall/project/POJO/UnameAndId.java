package com.computermall.project.POJO;


public class UnameAndId {
    private String uname;
    private String id;

    public UnameAndId() {
    }

    public UnameAndId(String uname, String id) {
        this.uname = uname;
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UnameAndId{" +
                "uname='" + uname + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
