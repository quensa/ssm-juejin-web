package com.lf.mapper;


import com.lf.pojo.Post;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface PostMapper {

    /**
     * 添加文章
     * 发文章时间和修改时间由数据库自动取当前时间
     *
     * @param post 文章对象
     * @return
     */
    @Insert("INSERT INTO post " +
            "(post_title, post_content, user_id, tab_id,post_isHide ) " +
            "VALUES (" + "#{post_title}, #{post_content}, #{user_id}, #{tab_id},#{post_isHide} " + ")")
    int inspostV2(Post post);

    /**
     * 获取所有文章
     *
     * @return List<post>
     */
    @Select("SELECT * FROM post")
    List<Post> selpostAll();

    /**
     * 查询文章
     *
     * @param post_id 文章id
     * @return 文章对象
     */
    @Select("SELECT * FROM post WHERE post_id = #{post_id}")
    Post selpostBypostId(int post_id);

    /**
     * 查询分区文章
     *
     * @param tab_id 分类id
     * @return 文章对象
     */
    @Select("SELECT * FROM post WHERE tab_id = #{tab_id}")
    Post selpostBytabId(int tab_id);

    /**
     * 文章点击量+1
     *
     * @param post_id 文章id
     * @return
     */
    @Update("UPDATE post SET " +
            "post_click = post_click + 1 " +
            "WHERE post_id = #{post_id}")
    int updpostClick(int post_id);

    /**
     * 更新文章点赞数
     *
     * @param
     * @return
     */
    @Update("UPDATE post SET post_good_count = #{post_good_count} WHERE post_id = #{post_id}")
    int updpostgood(Post post);

    /**
     * 更新文章收藏数
     *
     * @param
     * @return
     */
    @Update("UPDATE post SET post_collect = #{post_collect} WHERE post_id = #{post_id}")
    int updpostcollect(Post post);


    /**
     * 逻辑删文章
     *
     * @param post_id 文章id
     * @return
     */
    @Update("UPDATE post SET " +
            "post_isDeleted = 1 " +
            "WHERE post_id = #{post_id}")
    int updpostIsDeleted(int post_id);

    /**
     * 取消逻辑删文章
     *
     * @param post_id 文章id
     * @return
     */
    @Update("UPDATE post SET " +
            "post_isDeleted = 0 " +
            "WHERE post_id = #{post_id}")
    int updpostIsNotDeleted(int post_id);


    /**
     * 统计文章回复数
     *
     * @param post_id 文章id
     * @return
     */
    @Select("SELECT COUNT(reply_id) FROM reply " +
            "WHERE post_id = #{post_id} and reply_isDeleted=0")
    int selReplyCountedBypostId(int post_id);

    /**
     * 刷新文章更新时间
     *
     * @param post 文章对象（包含id和修改时间）
     * @return
     */
    @Update("UPDATE post SET " +
            "post_modifyTime = #{post_modifyTime} " +
            "WHERE post_id = ${post_id}")
    int updModifyTime(Post post);

    /**
     * 查询所有文章id
     *
     * @return List<Integer>含有所有文章的id
     */
    @Select("SELECT post_id FROM post")
    List<Integer> selpostIds();

    /**
     * 更新文章回复数
     *
     * @param post_id 文章id
     * @return
     */
    @Update("UPDATE post SET post_replies = " +
            "(SELECT COUNT(reply_id) FROM reply WHERE post_id = #{post_id} and reply_isDeleted=0)  " +
            "WHERE post_id = #{post_id}")
    int updRepliesBypostId(int post_id);

    /**
     * 根据关键词查询标题和内容的文章
     *
     * @param keyword 关键词
     * @return List<post>
     */
    @Select("SELECT * FROM post " +
            "WHERE (post_title LIKE '%${_parameter}%'OR post_content LIKE '%${_parameter}%') " +
            "AND post_isTop = 0")
    List<Post> selpostByKeyword(String keyword);

    /**
     * 修改文章信息
     *
     * @param post 文章对象
     * @return
     */
    @Update("UPDATE post SET " +
            "post_title = #{post_title}, " +
            "post_content = #{post_content}, " +
            "post_modifyTime = #{post_modifyTime}, " +
            "post.tab_id = ${tab_id} " +
            "WHERE post_id = ${post_id}")
    int updpost(Post post);

    /**
     * 修改文章回复数（+1）
     *
     * @param post 文章对象
     * @return
     */
    @Update("UPDATE post SET " +
            "post_replies = post_replies + 1 " +
            "WHERE post_id = ${post_id}")
    int updReplisAddOne(Post post);


    /**
     * 文章置顶
     *
     * @param post_id 文章id
     * @return
     */
    @Update("UPDATE post SET post_isTop = 1 WHERE post_id = #{post_id}")
    int updpostToTop(int post_id);

    /**
     * 取消置顶
     *
     * @param post_id 文章id
     * @return
     */
    @Update("UPDATE post SET post_isTop = 0 WHERE post_id = #{post_id}")
    int updpostToUnTop(int post_id);

    /**
     * 更新置顶时间
     *
     * @param post 文章对象（需包含id和时间）
     * @return
     */
    @Update("UPDATE post SET " +
            "post_topTime = #{post_topTime} " +
            "WHERE post_id = ${post_id}")
    int updTopTime(Post post);

    /**
     * 获取所有置顶文章（按置顶时间倒序排列）
     *
     * @return List<post>
     */
    @Select("SELECT * FROM post WHERE post_isDeleted = 0 AND post_isTop = 1 ORDER BY post_topTime DESC")
    List<Post> selAllToppostForTopTimeDesc();

    /**
     * 获取所有未置顶的文章（按更新时间倒序排列）
     *
     * @return List<post>
     */
    @Select("SELECT * FROM post WHERE post_isDeleted = 0 AND post_isTop = 0 ORDER BY post_modifyTime DESC")
    List<Post> selAllUnToppostForModifyTimeDesc();


    /**
     * 根据分类id查询所有文章id
     *
     * @param tab_Id 分类id
     * @return List<Integer>
     */
    @Select("SELECT post_id FROM post WHERE post_isDeleted = 0 AND tab_id = #{tab_id}")
    List<Integer> selAllpostIdByTabId(int tab_Id);

    /**
     * 根据分类id逻辑删除所有文章
     * 2020-03-05 18:28
     *
     * @param tab_id 分类id
     * @return
     */
    @Update("UPDATE post SET post_isDeleted = 1 WHERE tab_id = #{tab_id}")
    int updAllpostIsDeletedByTabId(int tab_id);

    /**
     * 根据分类id取消逻辑删除所有文章
     *
     * @param tab_id 分类id
     * @return
     */
    @Update("UPDATE post SET post_isDeleted = 0 WHERE tab_id = #{tab_id}")
    int updAllpostIsNotDeletedByTabId(int tab_id);


    /**
     * 根据版块id查询所有文章id
     *
     * @param forum_id 版块id
     * @return List<Integer>
     */
    @Select("SELECT post_id FROM post WHERE tab_id IN (SELECT tab_id FROM tab WHERE forum_id = #{forum_id})")
    List<Integer> selAllpostIdByForumId(int forum_id);

    /**
     * 根据版块id逻辑删除所有文章
     *
     * @param forum_id 版块id
     * @return
     */
    @Update("UPDATE post SET post_isDeleted = 1 WHERE tab_id IN " +
            "(SELECT tab_id FROM tab WHERE forum_id = #{forum_id})")
    int updAllpostIsDeletedByForumId(int forum_id);

    /**
     * 根据版块id取消逻辑删除所有文章
     *
     * @param forum_id 版块id
     * @return
     */
    @Update("UPDATE post SET post_isDeleted = 0 WHERE tab_id IN " +
            "(SELECT tab_id FROM tab WHERE forum_id = #{forum_id})")
    int updAllpostIsNotDeletedByForumId(int forum_id);

    /**
     * 模糊查询文章（ID/标题/正文内容）
     *
     * @param keyword 关键词
     * @return
     */
    @Select("SELECT * FROM post WHERE " +
            "post_id = #{keyword} OR " +
            "post_title LIKE '%${keyword}%' OR " +
            "post_content LIKE '%${keyword}%'")
    List<Post> selpostFuzzy(@Param(value = "keyword") String keyword);

    /**
     * 根据用户id查询收藏文章
     */
    @Select("SELECT * FROM post WHERE post_id IN (SELECT aid FROM collect WHERE uid = #{uid})")
    List<Post> selpostIdByCollectId(int uid);

    /**
     * 获取热榜文章（按更新时间倒序排列）
     *
     * @return List<post>
     */
    @Select("SELECT * FROM post where post_isDeleted=0 ORDER BY post_click DESC")
    List<Post> selPostForPost_clickDesc();


    /**
     * 根据版块id查询所有文章id
     *
     * @param
     * @return List<Integer>
     */
    @Select("SELECT * FROM post WHERE  post_isHide=0 and post_isDeleted=0 and user_id IN (SELECT buid FROM focus WHERE uid = #{uid}) ")
    List<Post> selAllpostIdBFocusId(int uid);


    /**
     * 根据版块id查询所有文章
     *
     * @param forum_id 版块id
     * @return List<post>
     */
    @Select("SELECT * FROM post WHERE tab_id IN (SELECT tab_id FROM tab WHERE forum_id = #{forum_id})")
    List<Post> selAllpostByForumId(int forum_id);

    /**
     * 根据分类id查询所有文章
     *
     * @param tab_Id 分类id
     * @return List<post>
     */
    @Select("SELECT * FROM post WHERE post_isDeleted = 0 AND tab_id = #{tab_id}")
    List<Post> selAllpostByTabId(int tab_Id);
}
