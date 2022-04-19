package com.lf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 回复实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyTwo {

    private User user; // 用户
    private Reply reply; // 贴子

    private int user_id; // 回复用户的id
    private int reply_id; // 被回复的文章id

    private int replyTwo_id; // 回复id
    private String reply_content; // 回复内容
    private int reply_good_count; // 点赞量
    private int r_bad_count; // 踩量
    private Date reply_publishTime; // 回复发表时间
    private int reply_isDeleted; // 是否删除，0-否，1-是
}
