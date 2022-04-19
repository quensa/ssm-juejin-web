package com.lf.service.impl;


import com.lf.pojo.*;
import com.lf.service.ReplyService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.lf.mapper.*;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {
    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private PostMapper postMapper;


    @Resource
    private UserMapper userMapper;

    @Resource
    private TabMapper tabMapper;

    @Resource
    private ForumMapper forumMapper;

    /**
     * 在获取评论列表时同时获取其它模型的数据
     *
     * @param replyList List<Reply>
     * @return
     */
    private List<Reply> getreplysSolvedElseModel(List<Reply> replyList) {
        for (int i = 0; i < replyList.size(); i++) {
            User user = userMapper.selUserByUserId(replyList.get(i).getUser_id());
            // 注入到用户
            replyList.get(i).setUser(user);
        }
        return replyList;
    }

    private List<ReplyTwo> getreplysSolvedElseModel2(List<ReplyTwo> replyList) {
        for (int i = 0; i < replyList.size(); i++) {
            User user = userMapper.selUserByUserId(replyList.get(i).getUser_id());
            // 注入到用户
            replyList.get(i).setUser(user);
        }
        return replyList;
    }

    @Override
    public List<Reply> selReplyAll() {
        Logger logger = Logger.getLogger(ReplyServiceImpl.class);
        logger.info("获取所有评论");
        List finallist = new ArrayList<>();
        List<Reply> replieone = this.getreplysSolvedElseModel(replyMapper.selReplyAll());
        List<ReplyTwo> replyTwos = this.getreplysSolvedElseModel2(replyMapper.selReplyTwoAll());
        finallist.addAll(replieone);
        finallist.addAll(replyTwos);
        if (finallist != null) {
            return finallist;
        }
        return null;
    }

    @Override
    public String addReply(Reply reply) {
        Logger logger = Logger.getLogger(ReplyServiceImpl.class);
        logger.info("【用户评论文章】");
        // 插入数据到评论表
        if (replyMapper.insReply(reply) > 0) {
            // 发表回复成功则令文章评论数+1
            postMapper.updReplisAddOne(reply.getPost());
            // 刷新文章评论最新时间
            reply.getPost().setPost_modifyTime(reply.getReply_publishTime());
            postMapper.updModifyTime(reply.getPost());
            return "发表回复成功！";
        } else {
            return "发表回复失败！";
        }
    }

    @Override
    public List<Reply> getReplyByTipId(int post_id) {
        Logger logger = Logger.getLogger(ReplyServiceImpl.class);
        logger.info("尝试获取文章id为" + post_id + "的所有回复...");
        List<Reply> replyList = replyMapper.selReplyBypostId(post_id);
        if (replyList != null) {
            return replyList;
        }
        return null;
    }

    @Override
    public List<Reply> selReplyByuserId(int user_id) {
        Logger logger = Logger.getLogger(ReplyServiceImpl.class);
        logger.info("尝试获取文章id为" + user_id + "的所有评论...");
        List<Reply> replies = replyMapper.selReplyByuserId(user_id);
        if (replies != null) {
            return replies;
        }
        return null;
    }

    @Override
    public Reply selReplyByreplyId(int reply_id) {
        Logger logger = Logger.getLogger(ReplyServiceImpl.class);
        logger.info("尝试获取文章id为" + reply_id + "的所有评论...");
        return replyMapper.selReplyByreplyId(reply_id);
    }

    @Override
    public List<ReplyTwo> selReplyByReplyIdTwo(int reply_id) {
        Logger logger = Logger.getLogger(ReplyServiceImpl.class);
        logger.info("尝试获取评论id为" + reply_id + "的所有二级评论...");
        List<ReplyTwo> replyTwos = replyMapper.selReplyByReplyIdTwo(reply_id);
        if (replyTwos != null) {
            return replyTwos;
        }
        return null;
    }

    @Override
    public int updReplygood(Reply reply) {
        Logger logger = Logger.getLogger(ReplyServiceImpl.class);
        logger.info("尝试更新评论id为" + reply.getReply_id() + "的赞数...");
        return replyMapper.updReplygood(reply);
    }

    @Override
    public int updReplyBad(Reply reply) {
        Logger logger = Logger.getLogger(ReplyServiceImpl.class);
        logger.info("尝试更新评论id为" + reply.getReply_id() + "的踩数..");
        return replyMapper.updReplyBad(reply);
    }

    @Override
    public int updreplyIsDeleted(int reply_id) {
        return replyMapper.updreplyIsDeleted(reply_id);
    }

    @Override
    public int updreplyIsNotDeleted(int reply_id) {
        return replyMapper.updreplyIsNotDeleted(reply_id);
    }
}
