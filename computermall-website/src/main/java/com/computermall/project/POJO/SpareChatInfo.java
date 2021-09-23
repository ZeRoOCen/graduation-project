package com.computermall.project.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpareChatInfo implements Serializable {
    private Integer id;
    private String connectid;
    private String name;
    private String message;
    private String time;
}
