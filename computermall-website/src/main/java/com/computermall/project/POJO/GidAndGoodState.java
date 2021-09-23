package com.computermall.project.POJO;

public class GidAndGoodState {
    private Integer gid;
    private Integer gstate;

    public GidAndGoodState() {
    }

    public GidAndGoodState(Integer gid, Integer gstate) {
        this.gid = gid;
        this.gstate = gstate;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public Integer getGstate() {
        return gstate;
    }

    public void setGstate(Integer gstate) {
        this.gstate = gstate;
    }

    @Override
    public String toString() {
        return "GidAndGoodState{" +
                "gid=" + gid +
                ", gstate=" + gstate +
                '}';
    }

}
