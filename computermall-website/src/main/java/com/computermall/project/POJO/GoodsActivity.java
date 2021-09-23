package com.computermall.project.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsActivity {
    private String gname;
    private Integer gprice;
    private String gimgurl;
    private String gmessage;
}
