package com.lf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 分类实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tab {

    private Forum forum; // 版块
    private int forum_id; // 版块id

    private int tab_id; // 分类id
    private String tab_name; // 分类名
    // private int tab_status; // 分类状态
    private int tab_isDeleted; // 是否删除，0-否，1-是
    private Date tab_createTime; // 创建时间
    private Date tab_modifyTime; // 修改时间

}
