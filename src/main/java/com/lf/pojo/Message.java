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
public class Message {

    private User user; // 用户

    private int message_id; // 通信id
    private int user_id; // 发信人的id
    private int buser_id; // 收信人的id
    private String message_content; // 信内容
    private int message_type; // 信的状态
    private Date user_regTime; // 发信时间


}