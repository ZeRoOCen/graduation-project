package com.computermall.project.POJO;

import java.io.Serializable;

public class ConnectId implements Serializable {
    private String connectid;

    public ConnectId() {
    }

    public ConnectId(String connectid) {
        this.connectid = connectid;
    }

    public String getConnectid() {
        return connectid;
    }

    public void setConnectid(String connectid) {
        this.connectid = connectid;
    }

    @Override
    public String toString() {
        return "ConnectId{" +
                "connectid='" + connectid + '\'' +
                '}';
    }
}
