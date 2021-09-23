package com.computermall.project.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsOrder {
    private Integer uid; //用户id
    private Integer totalprice; //订单总价（购物车商品总价）
    private String ordernumber; //订单号
    private String ordertime; //也可以传Date类型，不过格式有点问题
    private String paytime; //同上
    private int paystate; //订单支付状态
}
