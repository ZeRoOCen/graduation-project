package com.computermall.project.POJO;

public class GnameAndUname {
    private String gname;
    private String uname;

    public GnameAndUname() {
    }

    public GnameAndUname(String gname, String uname) {
        this.gname = gname;
        this.uname = uname;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Override
    public String toString() {
        return "GnameAndUname{" +
                "gname='" + gname + '\'' +
                ", uname='" + uname + '\'' +
                '}';
    }


}
