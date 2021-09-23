package com.computermall.project.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsComment implements Serializable {
    private String gname; //商品名
    private Integer uid; //用户ID
    private String uname; //用户名
    private String gcomment; //商品评论内容
    private Integer grade; //商品评分分数
    private Integer power;  //商品评论权限

}
