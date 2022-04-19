package com.lf.controller;

import com.lf.pojo.*;
import com.lf.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UtilController {
    @Resource
    private UserService userService;

    @Resource
    private ReplyService replyService;

    @Resource
    private TabService tabService;

    @Resource
    private PostService postService;

    @Resource
    private UtilService utilService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;


    /**
     * 文章点赞接口
     *
     * @return
     */
    @RequestMapping("addFavor")
    @ResponseBody
    public String niceDetail(Integer postId) {
        Good good2 = new Good();
        User user = (User) session.getAttribute("USER");
        good2.setUid(user.getUser_id());
        good2.setArid(postId);
        //查询是否有该用户的点赞记录
        Good good = utilService.queryGood(good2);
        String date = null;
        if (good != null) {
            System.out.println("有该记录");
            //如果找到这条记录，删除该记录，同时文章的点赞数减一
            //删除记录
            int i = utilService.deleteGood(good);
            //根据点赞id找到文章
            Post post1 = postService.getpostBypostId(good.getArid());
            //文章点赞数减一
            post1.setPost_good_count(post1.getPost_good_count() - 1);
            //更新文章点赞数
            System.out.println(post1.getPost_good_count());
            postService.updpostgood(post1);
            date = "取消点赞成功";
        } else {
            //如果没有找到这条记录，则添加这条记录，同时文章数加一；
            //添加记录
            System.out.println("没有记录：");
            utilService.insertGood(good2);
            //文章点赞数加一
            Post post2 = postService.getpostBypostId(postId);
            post2.setPost_good_count(post2.getPost_good_count() + 1);
            //更新文章点赞数
            System.out.println(post2.getPost_good_count());
            postService.updpostgood(post2);
            date = "点赞成功";
        }
        return date;
    }


    /**
     * 收藏接口
     *
     * @return
     */
    @RequestMapping("addCollect")
    @ResponseBody
    public String addCollect(Integer postId) {
        System.out.println("addCollect");
        System.out.println(postId);
        Collect collect = new Collect();
        User user = (User) session.getAttribute("USER");
        collect.setUid(user.getUser_id());
        collect.setAid(postId);
        //查询是否有该用户的点赞记录
        Collect collect1 = utilService.queryCollect(collect);
        String date;
        if (collect1 != null) {
            System.out.println("有该记录");
            //如果找到这条记录，删除该记录，同时文章的收藏数减一
            //删除记录
            utilService.deleteCollect(collect1);
            //根据收藏id找到文章
            Post post1 = postService.getpostBypostId(collect1.getAid());
            //文章收藏数减一
            post1.setPost_collect(post1.getPost_collect() - 1);
            //更新文章收藏数
            postService.updpostcollect(post1);
            date = "取消收藏成功";
        } else {
            System.out.println("无该记录");
            //如果没有找到这条记录，则添加这条记录，同时文章收藏数加一；
            //添加记录
            utilService.insertCollect(collect);
            //文章收藏数加一
            Post post2 = postService.getpostBypostId(postId);
            post2.setPost_collect(post2.getPost_collect() + 1);
            //更新文章收藏数
            System.out.println(post2.getPost_collect());
            postService.updpostcollect(post2);
            date = "收藏成功";
        }
        return date;
    }

    /**
     * 评论点赞接口
     *
     * @return
     */
    @RequestMapping("addReplyFavor")
    @ResponseBody
    public String goodReply(Integer replyid) {
        Good good = new Good();
        User user = (User) session.getAttribute("USER");
        good.setUid(user.getUser_id());
        good.setArid(replyid);
        //查询是否有该用户的点赞记录
        Good goodRecord = utilService.queryGood(good);
        String date = null;
        if (goodRecord != null) {
            System.out.println("有该记录");
            //如果找到这条记录，删除该记录，同时评论的点赞数减一
            //删除记录
            int i = utilService.deleteGood(good);
            //根据评论id找到评论
            Reply reply = replyService.selReplyByreplyId(goodRecord.getArid());
            //评论点赞数减一
            reply.setReply_good_count(reply.getReply_good_count() - 1);
            //更新评论点赞数
            replyService.updReplygood(reply);
            date = "取消点赞成功";
        } else {
            //如果没有找到这条记录，则添加这条记录，同时文章数加一；
            //添加记录
            System.out.println("没有记录：");
            utilService.insertGood(good);
            //评论点赞数加一
            Reply reply = replyService.selReplyByreplyId(replyid);
            reply.setReply_good_count(reply.getReply_good_count() + 1);
            //更新评论点赞数
            replyService.updReplygood(reply);
            date = "点赞成功";
        }
        return date;
    }

    /**
     * 评论点踩接口
     *
     * @return
     */
    @RequestMapping("addReplyBad")
    @ResponseBody
    public String badReply(Integer replyid) {
        Bad bad = new Bad();
        User user = (User) session.getAttribute("USER");
        bad.setUid(user.getUser_id());
        bad.setArid(replyid);
        //查询是否有该用户的点踩记录
        Bad querybad = utilService.querybad(bad);
        String date = null;
        if (querybad != null) {
            System.out.println("有该记录");
            //如果找到这条记录，删除该记录，同时评论的点赞数减一
            //删除记录
            int i = utilService.deleteBad(querybad);
            //根据评论id找到评论
            Reply reply = replyService.selReplyByreplyId(querybad.getArid());
            //评论点赞数减一
            reply.setR_bad_count(reply.getR_bad_count() - 1);
            //更新评论点赞数
            replyService.updReplyBad(reply);
            date = "取消点踩成功";
        } else {
            //如果没有找到这条记录，则添加这条记录，同时文章数加一；
            //添加记录
            System.out.println("没有记录：");
            utilService.insertBad(bad);
            //文章评论数加一
            Reply reply = replyService.selReplyByreplyId(replyid);
            reply.setR_bad_count(reply.getR_bad_count() + 1);
            //更新文章评论数
            replyService.updReplygood(reply);
            date = "点踩成功";
        }
        return date;
    }


    /**
     * 用户关注接口
     *
     * @return
     */
    @RequestMapping("addUserFocus")
    @ResponseBody
    public String addUserFocus(Integer buid) {
        Focus focus = new Focus();
        User user = (User) session.getAttribute("USER");
        focus.setUid(user.getUser_id());
        focus.setBuid(buid);
        //查询是否有该用户的关注记录
        Focus quaryfocus = utilService.queryFocus(focus);
        String date = null;
        if (quaryfocus != null) {
            System.out.println("有该记录");
            //如果找到这条记录，删除该记录
            int i = utilService.deleteFocus(quaryfocus);
            //这里可以根据关注的用户id查到用户进而实现该用户的粉丝量减一（和点赞收藏量类似操作）
            date = "取消关注成功";
        } else {
            System.out.println("没有记录：");
            //如果没有找到这条记录，则添加这条记录
            int i = utilService.insertFocus(focus);
            date = "关注成功";
        }
        return date;
    }
}
