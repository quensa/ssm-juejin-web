package com.lf.service;


import com.lf.pojo.Reply;
import com.lf.pojo.ReplyTwo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ReplyService {

    /**
     * 获取所有评论
     *
     * @return List<reply>
     */
    List<Reply> selReplyAll();


    /**
     * 发表回复
     *
     * @return
     */
    String addReply(Reply reply);

    /**
     * 由文章id获取回复
     *
     * @param post_id 文章id
     * @return List<Reply>
     */
    List<Reply> getReplyByTipId(int post_id);

    /**
     * 查询评论
     *
     * @param reply_id 评论id
     * @return Reply
     */
    Reply selReplyByreplyId(int reply_id);

    /**
     * 由用户id查询评论
     *
     * @param user_id 用户id
     * @return List<Reply>
     */
    List<Reply> selReplyByuserId(int user_id);

    /**
     * 由评论id查询二级评论
     *
     * @param reply_id 评论id
     * @return List<Reply>
     */
    List<ReplyTwo> selReplyByReplyIdTwo(int reply_id);


    /**
     * 更新评论点赞数
     *
     * @param
     * @return
     */
    int updReplygood(Reply reply);

    /**
     * 更新评论点踩数
     *
     * @param
     * @return
     */
    int updReplyBad(Reply reply);


    /**
     * 逻辑删评论
     *
     * @param reply_id 文章id
     * @return
     */
    int updreplyIsDeleted(int reply_id);

    /**
     * 取消逻辑删评论
     *
     * @param reply_id 文章id
     * @return
     */
    int updreplyIsNotDeleted(int reply_id);
}
