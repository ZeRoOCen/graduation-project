package com.computermall.project.POJO;

import java.io.Serializable;

public class UidGnameCommentAndGrade implements Serializable {
    private Integer uid;
    private String gname;
    private String comment;
    private Integer grade;

    public UidGnameCommentAndGrade() {
    }

    public UidGnameCommentAndGrade(Integer uid, String gname, String comment, Integer grade) {
        this.uid = uid;
        this.gname = gname;
        this.comment = comment;
        this.grade = grade;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "UidGnameAndComment{" +
                "uid=" + uid +
                ", gname='" + gname + '\'' +
                ", comment='" + comment + '\'' +
                ", grade=" + grade +
                '}';
    }
}
