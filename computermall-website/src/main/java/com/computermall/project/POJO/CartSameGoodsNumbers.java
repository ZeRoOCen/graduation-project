package com.computermall.project.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartSameGoodsNumbers implements Serializable {
    private String gname;
    private Integer amount;
    private Float gprice;
    private String gimgurl;
    private int uid;
    private int urole;
    private int favorableprice;
}
