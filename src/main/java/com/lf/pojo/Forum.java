package com.lf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 版块实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Forum {

    private int forum_id; // 版块id
    private String forum_name; // 版块名
    // private int forum_status; // 版块状态
    private int forum_isDeleted; // 是否删除，0-否，1-是
    private Date forum_createTime; // 创建时间
    private Date forum_modifyTime; // 修改时间

}
