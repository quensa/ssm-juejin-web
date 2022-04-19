package com.lf.mapper;

import com.lf.pojo.Post;
import com.lf.pojo.Reply;
import com.lf.pojo.ReplyTwo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ReplyMapper {

    /**
     * 获取所有评论
     *
     * @return List<reply>
     */
    @Select("SELECT * FROM reply")
    List<Reply> selReplyAll();

    /**
     * 获取所有二级评论
     *
     * @return List<reply>
     */
    @Select("SELECT * FROM replytwo")
    List<ReplyTwo> selReplyTwoAll();

    /**
     * 添加评论
     *
     * @param reply 回复实体对象
     * @return
     */
    @Insert("INSERT INTO reply " +
            "(reply_content, reply_publishTime, reply_modifyTime, user_id, post_id,reply_isHide) " +
            "VALUES " +
            "(#{reply_content}, #{reply_publishTime}, #{reply_modifyTime}, " +
            "${user.user_id}, ${post.post_id},#{reply_isHide})")
    int insReply(Reply reply);

    /**
     * 由文章id查询评论
     *
     * @param post_id 文章id
     * @return List<Reply>
     */
    @Select("SELECT * FROM reply WHERE post_id = #{post_id} and reply_isDeleted = 0 ")
    List<Reply> selReplyBypostId(int post_id);

    /**
     * 由用户id查询评论
     *
     * @param user_id 用户id
     * @return List<Reply>
     */
    @Select("SELECT * FROM reply WHERE user_id = #{user_id} and reply_isDeleted=0")
    List<Reply> selReplyByuserId(int user_id);

    /**
     * 查询评论
     *
     * @param reply_id 评论id
     * @return Reply
     */
    @Select("SELECT * FROM reply WHERE reply_id = #{reply_id}")
    Reply selReplyByreplyId(int reply_id);


    /**
     * 添加二级评论
     *
     * @param reply 回复实体对象
     * @return
     */
    @Insert("INSERT INTO reply " +
            "(reply_content, reply_publishTime, reply_modifyTime, user_id, post_id) " +
            "VALUES " +
            "(#{reply_content}, #{reply_publishTime}, #{reply_modifyTime}, " +
            "${user.user_id}, ${post.post_id})")
    int insReplyTwo(Reply reply);

    /**
     * 由评论id查询二级评论
     *
     * @param reply_id 评论id
     * @return List<Reply>
     */
    @Select("SELECT * FROM replytwo WHERE reply_id = #{reply_id} and reply_isDeleted=0")
    List<ReplyTwo> selReplyByReplyIdTwo(int reply_id);


    /**
     * 更新评论点赞数
     *
     * @param
     * @return
     */
    @Update("UPDATE reply SET reply_good_count = #{reply_good_count} WHERE reply_id = #{reply_id}")
    int updReplygood(Reply reply);

    /**
     * 更新评论点踩数
     *
     * @param
     * @return
     */
    @Update("UPDATE reply SET r_bad_count = #{r_bad_count} WHERE reply_id = #{reply_id}")
    int updReplyBad(Reply reply);


    /**
     * 逻辑删评论
     *
     * @param reply_id 贴子id
     * @return
     */
    @Update("UPDATE reply SET " +
            "reply_isDeleted = 1 " +
            "WHERE reply_id = #{reply_id}")
    int updreplyIsDeleted(int reply_id);

    /**
     * 取消逻辑删评论
     *
     * @param reply_id 贴子id
     * @return
     */
    @Update("UPDATE reply SET " +
            "reply_isDeleted = 0 " +
            "WHERE reply_id = #{reply_id}")
    int updreplyIsNotDeleted(int reply_id);
}
