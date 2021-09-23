package com.computermall.project.POJO;

import java.io.Serializable;


public class CommentQueryInfo implements Serializable {
    private String currentGoodsName; //当前商品名
    private String commentMessage; //商品评论内容查询
    private Integer currentPageNum; //商品评论当前页
    private Integer commentPageSize; //商品评论每页最大展示数量

    public CommentQueryInfo() {
    }

    public CommentQueryInfo(String currentGoodsName, String commentMessage, Integer currentPageNum, Integer commentPageSize) {
        this.currentGoodsName = currentGoodsName;
        this.commentMessage = commentMessage;
        this.currentPageNum = currentPageNum;
        this.commentPageSize = commentPageSize;
    }

    public String getCurrentGoodsName() {
        return currentGoodsName;
    }

    public void setCurrentGoodsName(String currentGoodsName) {
        this.currentGoodsName = currentGoodsName;
    }

    public String getCommentMessage() {
        return commentMessage;
    }

    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }

    public Integer getCurrentPageNum() {
        return currentPageNum;
    }

    public void setCurrentPageNum(Integer currentPageNum) {
        this.currentPageNum = currentPageNum;
    }

    public Integer getCommentPageSize() {
        return commentPageSize;
    }

    public void setCommentPageSize(Integer commentPageSize) {
        this.commentPageSize = commentPageSize;
    }

    @Override
    public String toString() {
        return "CommentQueryInfo{" +
                "currentGoodsName='" + currentGoodsName + '\'' +
                ", commentMessage='" + commentMessage + '\'' +
                ", currentPageNum=" + currentPageNum +
                ", commentPageSize=" + commentPageSize +
                '}';
    }
}
