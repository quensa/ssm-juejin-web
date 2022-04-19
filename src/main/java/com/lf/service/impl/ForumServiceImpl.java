package com.lf.service.impl;

import com.lf.mapper.*;
import com.lf.pojo.*;
import com.lf.service.ForumService;
import com.lf.utils.TimeUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForumServiceImpl implements ForumService {
    @Resource
    private ForumMapper forumMapper;

    @Resource
    private TabMapper tabMapper;

    @Resource
    private PostMapper postMapper;

    @Override
    public List<Forum> getAllForum() {
        Logger logger = Logger.getLogger(ForumServiceImpl.class);
        logger.info("尝试获得所有大板块...");
        List<Forum> forumList = forumMapper.selForumAll();
        return forumList;
    }

    @Override
    public List<Forum> selForumAllMain() {
        List<Forum> forums = forumMapper.selForumAllMain();
        return forums;
    }

    @Override
    public Forum getForumByForumId(int forum_id) {
        Logger logger = Logger.getLogger(ForumServiceImpl.class);
        logger.info("尝试获得id为" + forum_id + "的大板块...");
        Forum forum = forumMapper.selForumByForumId(forum_id);
        if (forum != null) {
            return forum;
        }
        return null;
    }

    /**
     * 修改版块
     *
     * @param forum 版块对象
     * @return
     */
    @Override
    public String modifyForum(Forum forum) {
        Logger logger = Logger.getLogger(ForumServiceImpl.class);
        String resultStr = null;
        // 先检查Forum是否存在
        logger.info("尝试获得id为" + forum.getForum_id() + "的版块...");
        Forum tmpForum = this.getForumByForumId(forum.getForum_id());
        if (tmpForum != null) {
            if (forum.getForum_name().equals(tmpForum.getForum_name())) {
                // 如果新版块名与原来的相同
                resultStr = new String("修改失败：新版块名与旧版块名相同。");
            } else {
                logger.info("尝试修改id为" + forum.getForum_id() + "的版块...");
                LocalDateTime localDateTime = LocalDateTime.now();
                forum.setForum_modifyTime(TimeUtil.convertLocalDateTimeToDate(localDateTime));
                // 执行修改
                int result = forumMapper.updForum(forum);
                if (result > 0) {
                    resultStr = new String("修改成功！");
                } else {
                    resultStr = new String("修改失败！");
                }
            }
        }
        return resultStr;
    }

    @Override
    public String addForum(Forum forum) {
        Logger logger = Logger.getLogger(ForumServiceImpl.class);
        String resultStr = null;
        logger.info("尝试添加版块...");
        if (forumMapper.insForum(forum) > 0) {
            resultStr = new String("success");
        } else {
            resultStr = new String("error");
        }
        return resultStr;
    }

    @Override
    public Forum getForumByForumName(String forum_name) {
        Logger logger = Logger.getLogger(ForumServiceImpl.class);
        logger.info("尝试获得name为" + forum_name + "的版块...");
        Forum forum = forumMapper.selForumByForumName(forum_name);
        if (forum != null) {
            return forum;
        }
        return null;
    }

    /**
     * 逻辑删除版块
     *
     * @param forum_id 版块id
     * @return 0成功，-1分类不存在，-2删除版块失败，-3删除分类或文章失败
     */
    @Override
    public int deleteForumLogical(int forum_id) {
        Logger logger = Logger.getLogger(ForumServiceImpl.class);
        // 先检查版块是否存在
        logger.info("检查版块是否存在，id：" + forum_id);
        if (forumMapper.selForumByForumId(forum_id) == null) {
            return -1;
        }
        logger.info("逻辑删除版块，id：" + forum_id);
        if (forumMapper.updForumIsDeleted(forum_id) <= 0) {
            return -2;
        } else {
            // 删除版块成功，处理分类和文章
            if (this.deleteAllTabAndTipByForumId(forum_id) == 0) {
                // 删除分类和文章成功
                return 0;
            } else {
                return -3;
            }
        }
    }

    /**
     * 取消逻辑删除版块
     *
     * @param forum_id 版块id
     * @return 0成功，-1分类不存在，-2删除版块失败，-3取消删除分类或文章失败
     */
    @Override
    public int disDeleteForumLogical(int forum_id) {
        Logger logger = Logger.getLogger(ForumServiceImpl.class);
        // 先检查版块是否存在
        logger.info("检查版块是否存在，id：" + forum_id);
        if (forumMapper.selForumByForumId(forum_id) == null) {
            return -1;
        }
        logger.info("取消逻辑删除版块，id：" + forum_id);
        if (forumMapper.updForumIsNotDeleted(forum_id) <= 0) {
            return -2;
        } else {
            // 取消删除版块成功，处理分类和文章
            if (this.disDeleteAllTabAndTipByForumId(forum_id) == 0) {
                // 取消删除分类和文章成功
                return 0;
            } else {
                // 取消删除分类或文章失败
                return -3;
            }
        }
    }

    /**
     * 删除版块时，同时删除关联的分类和文章
     *
     * @param forum_id 版块id
     * @return 0删除分类和文章成功，-1删除关联分类失败，-2删除分类成功但删除文章失败
     */
    private int deleteAllTabAndTipByForumId(int forum_id) {
        // 该版块下的分类数
        int tabCount = tabMapper.selAllTabIdUnDeletedByForumId(forum_id).size();
        // 根据版块id删除分类
        int delTabResult = tabMapper.updAllTabIsDeletedByForumId(forum_id);
        if (delTabResult == tabCount) {
            // 删除所有关联分类成功，继续删除文章
            // 该版块下的文章数
            int tipCount = postMapper.selAllpostIdByForumId(forum_id).size();
            // 根据版块id删除文章
            int delTipResult = postMapper.updAllpostIsDeletedByForumId(forum_id);
            if (delTipResult == tipCount) {
                // 删除所有关联文章成功
                return 0;
            } else {
                // 删除关联文章失败
                return -2;
            }
        } else {
            // 删除关联分类失败
            return -1;
        }
    }

    /**
     * 取消删除版块时，同时取消删除关联的分类和文章
     *
     * @param forum_id 版块id
     * @return 0取消删除分类和文章成功，-1取消删除关联分类失败，-2取消删除分类成功但删除文章失败
     */
    private int disDeleteAllTabAndTipByForumId(int forum_id) {
        // 该版块下的分类数
        int tabCount = tabMapper.selAllTabIdIsDeletedByForumId(forum_id).size();
        // 根据版块id取消删除分类
        int disDelTabResult = tabMapper.updAllTabIsNotDeletedByForumId(forum_id);
        if (disDelTabResult == tabCount) {
            // 取消删除所有关联分类成功，继续取消删除文章
            // 该版块下的文章数
            int tipCount = postMapper.selAllpostIdByForumId(forum_id).size();
            // 根据版块id取消删除文章
            int disDelTipResult = postMapper.updAllpostIsNotDeletedByForumId(forum_id);
            if (disDelTipResult == tipCount) {
                // 取消删除所有关联文章成功
                return 0;
            } else {
                // 取消删除关联文章失败
                return -2;
            }
        } else {
            // 取消删除关联分类失败
            return -1;
        }
    }
}
