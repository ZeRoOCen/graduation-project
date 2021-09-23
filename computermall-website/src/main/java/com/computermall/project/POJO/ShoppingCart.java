package com.computermall.project.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart implements Serializable {
    private Integer cartid; //购物车商品对应ID
    private String gname; //商品名
    private Float gprice; //商品价格
    private String sgsort; //子商品分类
    private String mgsort; //主商品分类
    private Integer gstate; //商品状态（0：异常冻结；1：正常）
    private String gimgurl; //商品图片
    private String gmessage; //商品描述
    private String uname; //用户名
    private int urole; //用户角色权限
    private int favorableprice; //会员优惠价格
}
