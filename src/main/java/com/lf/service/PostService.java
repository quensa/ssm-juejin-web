package com.lf.service;


import com.lf.pojo.Post;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PostService {

    /**
     * 添加文章
     *
     * @param post
     */
    String addpost(Post post);

    /**
     * 获取所有文章
     *
     * @return List<post>
     */
    List<Post> getAllpost();

    /**
     * 由文章id获得文章
     *
     * @param post_id 文章id
     * @return 文章对象
     */
    Post getpostBypostId(int post_id);

    /**
     * 给文章浏览量+1
     *
     * @param post_id 文章id
     * @return
     */
    String addpostClick(int post_id);

    /**
     * 逻辑删文
     *
     * @param post_id
     * @return
     */
    String disablepost(int post_id);

    /**
     * 取消逻辑删文
     *
     * @param post_id 文章id
     * @return
     */
    String enablepost(int post_id);


    /**
     * 统计回复数
     *
     * @param post_id 文章id
     * @return
     */
    int countReplyBypostId(int post_id);

    /**
     * 刷新文章更新时间
     *
     * @param post 文章对象
     * @return
     */
    int updateModifyTime(Post post);

    /**
     * 更新文章点赞数
     *
     * @param
     * @return
     */
    int updpostgood(Post post);

    /**
     * 更新文章收藏数
     *
     * @param
     * @return
     */
    int updpostcollect(Post post);


    /**
     * 更新文章回复量
     *
     * @param post_id 文章id
     * @return
     */
    int updateRepliesBypostId(int post_id);

    /**
     * 更新所有文章的回复数
     *
     * @return
     */
    int updateAllReplies();

    /**
     * 搜索标题、内容含某关键词的文章
     *
     * @param keyword 关键词
     * @return List<post>
     */
    List<Post> searchpostByKeyword(String keyword);

    /**
     * 修改文章信息
     *
     * @param post 文章对象
     * @return
     */
    String modifypost(Post post);

    /**
     * 文章置顶
     *
     * @param post_id 文章id
     * @return
     */
    String doToppost(int post_id);

    /**
     * 取消置顶
     *
     * @param post_id 文章id
     * @return
     */
    String disToppost(int post_id);

    /**
     * 置顶时间刷新
     *
     * @param post 文章对象
     * @return
     */
    int updateTopTime(Post post);

    /**
     * 主页显示文章用
     *
     * @return List<post>
     */
    List<Post> getMainPageposts();

    /**
     * 模糊查询文章（ID/标题/正文内容）
     *
     * @param keyword 关键词
     * @return
     */
    List<Post> searchpostFuzzy(String keyword);


    /**
     * 根据用户id查询所有文章
     */
    List<Post> selpostIdByCollectId(int uid);


    /**
     * 获取热榜文章（按更新时间倒序排列）
     *
     * @return List<post>
     */
    List<Post> selPostForPost_clickDesc();

    /**
     * 根据关注用户id查询所有文章
     *
     * @param
     * @return List<Post>
     */
    List<Post> selAllpostIdBFocusId(int uid);
}
