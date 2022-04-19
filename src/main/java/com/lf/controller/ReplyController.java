package com.lf.controller;

import com.alibaba.fastjson.JSON;
import com.lf.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.lf.service.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 评论控制器
 */
@Controller
public class ReplyController {
    @Resource
    private ReplyService replyService;

    @Resource
    private PostService postService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @Resource
    private UserService userService;

    /**
     * 回文章控制
     *
     * @param reply 回复实体对象
     * @return
     */
    @RequestMapping("publishReply.do")
    public ModelAndView addReply(Reply reply, String IsHide) {
        ModelAndView mv = new ModelAndView();
        String resultStr = null;
        // 处理参数
        User user = (User) session.getAttribute("USER");
        Integer post_id = Integer.valueOf(request.getParameter("post_id"));
        try {
            if (post_id == null || user == null) {
                throw new Exception("user或post_id不存在");
            } else {
                // 用户登录、状态正常才可以发回复
                if (user.getUser_status() == 0) {
                    Post post = new Post(); // 用于对象包装
                    // 将user对象包装到reply
                    reply.setUser(user);
                    post.setPost_id(post_id);
                    // 将post对象包装到reply
                    reply.setPost(post);
                    // 生成回复时间
                    Date date = new Date();
                    reply.setReply_publishTime(date);
                    reply.setReply_modifyTime(date);
                    if (IsHide != null) {
                        reply.setReply_isHide(1);
                    } else {
                        reply.setReply_isHide(0);
                    }
                    // 调用业务层
                    resultStr = replyService.addReply(reply);
                } else {
                    resultStr = new String("回复失败：用户状态不正常！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("myInfo", resultStr);
        mv.setViewName("redirect:showpost.do?postId=" + post_id);
        return mv;
    }

    /**
     * 显示二级评论内容
     *
     * @param reply_id 一级评论id
     * @return
     */
    @RequestMapping("showReplyTwo.do")
    @ResponseBody
    public String showReplyTwo(int reply_id) {
        // 获取二级评论
        List<ReplyTwo> replyTwos = replyService.selReplyByReplyIdTwo(reply_id);
        // 获取二级评论对应的用户
        for (int i = 0; i < replyTwos.size(); i++) {
            User user = userService.getUserById(replyTwos.get(i).getUser_id());
            replyTwos.get(i).setUser(user);
        }
        //获取评论的子评论
        // 将回复添加到request
        String s = JSON.toJSONString(replyTwos);
        return s;
    }

    /**
     * 修改评论状态（管理员）
     *
     * @param opr 操作
     * @return
     */
    @RequestMapping("ChangeReplyStatus.do")
    public ModelAndView changeReplyStatus(int reply_id, int opr) {
        ModelAndView mv = new ModelAndView();
        switch (opr) {
            case 0:
                replyService.updreplyIsNotDeleted(reply_id);
                break;
            case 1:
                replyService.updreplyIsDeleted(reply_id);
                break;
        }
        mv.setViewName("redirect:toReplyManagePage.do");
        return mv;
    }


}
