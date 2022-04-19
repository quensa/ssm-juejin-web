package com.lf.service.impl;


import com.lf.mapper.ForumMapper;
import com.lf.mapper.TabMapper;
import com.lf.mapper.PostMapper;
import com.lf.mapper.UserMapper;
import com.lf.pojo.Forum;
import com.lf.pojo.Tab;
import com.lf.pojo.Post;
import com.lf.pojo.User;
import com.lf.service.PostService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Resource
    private PostMapper postMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private TabMapper tabMapper;

    @Resource
    private ForumMapper forumMapper;

    // 日志优化 不用每个方法都声明Logger
    private Logger logger = Logger.getLogger(PostServiceImpl.class);

    /**
     * 发文章
     *
     * @param post 文章对象
     * @return
     */
    @Override
    public String addpost(Post post) {
        logger.info("id为" + post.getUser_id() + "的用户尝试发表文章...");
        int result = postMapper.inspostV2(post);
        if (result > 0) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 在获取文章列表时同时获取其它模型的数据
     *
     * @param postList List<post>
     * @return
     */
    private List<Post> getpostsSolvedElseModel(List<Post> postList) {
        for (int i = 0; i < postList.size(); i++) {
            User user = userMapper.selUserByUserId(postList.get(i).getUser_id());
            Tab tab = tabMapper.selTabByTabId(postList.get(i).getTab_id());
            Forum forum = forumMapper.selForumByForumId(tab.getForum_id());
            tab.setForum(forum);
            // 注入到文章
            postList.get(i).setUser(user);
            postList.get(i).setTab(tab);
        }
        return postList;
    }

    /**
     * 获取所有文章信息（不排序）
     *
     * @return
     */
    @Override
    public List<Post> getAllpost() {
        logger.info("尝试获取所有文章信息...");
        List<Post> postList = this.getpostsSolvedElseModel(postMapper.selpostAll());
        if (postList != null) {
            return postList;
        }
        return null;
    }

    /**
     * 根据文章id获取文章信息
     *
     * @param post_id 文章id
     * @return
     */
    @Override
    public Post getpostBypostId(int post_id) {
        logger.info("尝试获取ID为" + post_id + "的文章信息...");
        Post post = postMapper.selpostBypostId(post_id);
        if (post != null) {
            // 获取发文章人信息
            User postOwner = userMapper.selUserByUserId(post.getUser_id());
            if (postOwner != null) {
                post.setUser(postOwner);
            }
        }
        // 判空
        if (post != null) {
            return post;
        }
        return null;
    }

    @Override
    public String addpostClick(int post_id) {
        logger.info("尝试给ID为" + post_id + "的文章阅读量+1...");
        int result = postMapper.updpostClick(post_id);
        if (result > 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @Override
    public String disablepost(int post_id) {
        Logger logger = Logger.getLogger(PostServiceImpl.class);
        logger.info("尝试逻辑删除id为" + post_id + "的文章...");
        int result = postMapper.updpostIsDeleted(post_id);
        if (result > 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @Override
    public String enablepost(int post_id) {
        Logger logger = Logger.getLogger(PostServiceImpl.class);
        logger.info("尝试将id为" + post_id + "的文章取消逻辑删除...");
        int result = postMapper.updpostIsNotDeleted(post_id);
        if (result > 0) {
            return "success";
        } else {
            return "error";
        }
    }


    @Override
    public int countReplyBypostId(int post_id) {
        Logger logger = Logger.getLogger(ReplyServiceImpl.class);
        logger.info("尝试统计文章id" + post_id + "的回复数...");
        int result = postMapper.selReplyCountedBypostId(post_id);
        return result;
    }

    @Override
    public int updpostgood(Post post) {
        Logger logger = Logger.getLogger(ReplyServiceImpl.class);
        logger.info("尝试更新文章id" + post.getPost_id() + "的点赞数...");
        return postMapper.updpostgood(post);
    }

    @Override
    public int updpostcollect(Post post) {
        Logger logger = Logger.getLogger(ReplyServiceImpl.class);
        logger.info("尝试更新文章id" + post.getPost_id() + "的收藏数...");
        return postMapper.updpostcollect(post);
    }

    /**
     * 刷新文章更新时间
     *
     * @param post 文章对象
     */
    @Override
    public int updateModifyTime(Post post) {
        Logger logger = Logger.getLogger(ReplyServiceImpl.class);
        logger.info("尝试刷新文章id为" + post.getPost_id() + "的修改时间：" + post.getPost_modifyTime());
        int result = postMapper.updModifyTime(post);
        return result;
    }

    @Override
    public int updateRepliesBypostId(int post_id) {
        Logger logger = Logger.getLogger(ReplyServiceImpl.class);
        logger.info("尝试更新文章id为" + post_id + "的回复数");
        int result = postMapper.updRepliesBypostId(post_id);
        return result;
    }

    @Override
    public int updateAllReplies() {
        Logger logger = Logger.getLogger(ReplyServiceImpl.class);
        int result = 0; // 保存更新结果
        logger.info("尝试获取所有文章的id");
        List<Integer> postIdsList = postMapper.selpostIds();
        // 逐个文章更新
        for (int i = 0; i < postIdsList.size(); i++) {
            logger.info("尝试更新文章id为" + postIdsList.get(i) + "的回复数");
            int tmpResult = postMapper.updRepliesBypostId(postIdsList.get(i));
            if (tmpResult > 0) {
                result++;
            }
        }
        return result;
    }

    /**
     * 【首页搜索】根据关键词搜索文章标题和内容
     *
     * @param keyword 关键词
     * @return
     */
    @Override
    public List<Post> searchpostByKeyword(String keyword) {
        Logger logger = Logger.getLogger(PostServiceImpl.class);
        logger.info("尝试搜索标题、内容包含关键词的文章：" + keyword);
        List<Post> finalpostList = new ArrayList<>(); // 保存最终返回的文章数组
        // 保存搜索得到的文章数组
        List<Post> keywordpostList = new ArrayList<>();
        List<Post> toppostList = new ArrayList<>();
        // 关键词判空
        if (keyword == null || keyword.equals("")) {
            // 先获取置顶文章
            toppostList = this.getpostsSolvedElseModel(postMapper.selAllToppostForTopTimeDesc());
            // 所有未置顶文章
            keywordpostList = this.getpostsSolvedElseModel(postMapper.selAllUnToppostForModifyTimeDesc());
        } else {
            // 关键词不为空时搜索到的文章
            keywordpostList = this.getpostsSolvedElseModel(postMapper.selpostByKeyword(keyword));
        }
        // 放入最终返回的文章数组
        finalpostList.addAll(toppostList);
        finalpostList.addAll(keywordpostList);
        // 判空
        if (finalpostList != null) {
            return finalpostList;
        }
        return null;
    }

    /**
     * 修改文章
     *
     * @param post 文章对象
     * @return
     */
    @Override
    public String modifypost(Post post) {
        Logger logger = Logger.getLogger(ReplyServiceImpl.class);
        String resultStr = null;
        // 检查文章是否存在
        Post tmppost = postMapper.selpostBypostId(post.getPost_id());
        if (tmppost != null) {
            // 检查修改信息是否相同
            if (post.getPost_title().equals(tmppost.getPost_title()) &&
                    post.getPost_content().equals(tmppost.getPost_content()) &&
                    (post.getTab_id() == tmppost.getTab_id())) {
                resultStr = new String("修改失败：修改后的分类、标题、内容没有变化");
                return resultStr;
            }
            // 开始修改
            logger.info("尝试修改id为" + post.getPost_id() + "的文章信息");
            if (postMapper.updpost(post) > 0) {
                resultStr = new String("修改成功！");
            } else {
                resultStr = new String("修改失败！");
            }
        }
        return resultStr;
    }

    /**
     * 置顶文章
     *
     * @param post_id 文章id
     * @return
     */
    @Override
    public String doToppost(int post_id) {
        Logger logger = Logger.getLogger(PostServiceImpl.class);
        logger.info("尝试置顶id为" + post_id + "的文章");
        int result = postMapper.updpostToTop(post_id);
        if (result > 0) {
            // 更新置顶时间
            Date date = new Date(); // 获取当前时间
            Post tmppost = new Post(); // 文章对象
            tmppost.setPost_id(post_id); // 保存文章id
            tmppost.setPost_topTime(date); // 保存置顶时间
            // 调用方法更新置顶时间
            int updToptimeResult = this.updateTopTime(tmppost);
            if (updToptimeResult > 0) {
                return "success";
            } else {
                System.err.println("更新置顶时间失败！");
            }
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 取消置顶
     *
     * @param post_id 文章id
     * @return
     */
    @Override
    public String disToppost(int post_id) {
        Logger logger = Logger.getLogger(PostServiceImpl.class);
        logger.info("尝试取消置顶id为" + post_id + "的文章");
        int result = postMapper.updpostToUnTop(post_id);
        if (result > 0) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 置顶时间刷新
     *
     * @param post 文章对象
     * @return
     */
    @Override
    public int updateTopTime(Post post) {
        Logger logger = Logger.getLogger(ReplyServiceImpl.class);
        logger.info("尝试刷新文章id为" + post.getPost_id() + "的置顶时间：" + post.getPost_modifyTime());
        int result = postMapper.updTopTime(post);
        return result;
    }

    /**
     * 主页显示文章用
     *
     * @return
     */
    @Override
    public List<Post> getMainPageposts() {
        Logger logger = Logger.getLogger(PostServiceImpl.class);
        logger.info("处理主页显示的文章");
        List<Post> finalpostList = new ArrayList<>(); // 保存最终返回的文章
        // 先获取置顶文章
        List<Post> toppostList = this.getpostsSolvedElseModel(postMapper.selAllToppostForTopTimeDesc());
        // 然后是未置顶的文章
        List<Post> unToppostList = this.getpostsSolvedElseModel(postMapper.selAllUnToppostForModifyTimeDesc());
        // 放入最终返回的文章数组
        finalpostList.addAll(toppostList);
        finalpostList.addAll(unToppostList);
        // 判空
        if (finalpostList != null) {
            return finalpostList;
        }
        return null;
    }

    /**
     * 模糊查询文章
     */
    @Override
    public List<Post> searchpostFuzzy(String keyword) {
        logger.info("尝试根据关键词搜索文章");
        // 调用数据库
        List<Post> postList = this.getpostsSolvedElseModel(postMapper.selpostFuzzy(keyword));
        if (postList != null) {
            return postList;
        }
        return null;
    }


    @Override
    public List<Post> selpostIdByCollectId(int uid) {
        List<Post> posts = this.getpostsSolvedElseModel(postMapper.selpostIdByCollectId(uid));
        return posts;
    }

    @Override
    public List<Post> selPostForPost_clickDesc() {
        List<Post> posts = this.getpostsSolvedElseModel(postMapper.selPostForPost_clickDesc());
        return posts;
    }

    @Override
    public List<Post> selAllpostIdBFocusId(int uid) {
        List<Post> posts = this.getpostsSolvedElseModel(postMapper.selAllpostIdBFocusId(uid));
        return posts;
    }
}
