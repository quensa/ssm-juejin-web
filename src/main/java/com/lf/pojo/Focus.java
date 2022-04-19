package com.lf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @description: 关注表实体类
 * focusId 关注表id
 * userId 用户id
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Focus {
    private Integer focusId;
    private Integer uid;
    private Integer buid;
}