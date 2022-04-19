package com.lf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 文章实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    private User user; // 用户
    private Tab tab; // 分类对象
    private int user_id; // 楼主id（发贴人id）
    private int tab_id; // 分类id

    private int post_id; // 文章id
    private String post_title; // 标题
    private String post_content; // 内容
    private Date post_publishTime; // 发表时间
    private Date post_modifyTime; // 修改时间
    private int post_click; // 点击量
    private int post_good_count; // 点赞量
    private int post_bad_count; // 踩量
    private int post_collect; // 收藏量
    private int post_isDeleted; // 是否逻辑删除，0-否，1-是
    private int post_isHide; // 状态，0-正常，1-匿名
    private int post_replies; // 文章评论数
    private int post_isTop; // 是否置顶，0-否，1-是
    private Date post_topTime; // 置顶时间

}