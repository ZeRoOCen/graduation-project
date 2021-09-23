package com.computermall.project.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpareCustomer implements Serializable {
    private String id; //用户与商品连接唯一ID
    private String uname; //用户名
    private String cname; //商品客服名
}
