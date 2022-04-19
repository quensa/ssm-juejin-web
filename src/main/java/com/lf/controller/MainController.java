package com.lf.controller;

import com.lf.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.lf.service.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MainController {
    @Resource
    private UserService userService;

    @Resource
    private ForumService forumService;

    @Resource
    private TabService tabService;

    @Resource
    private PostService postService;

    @Resource
    private ReplyService replyService;


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;


    @RequestMapping("tophoto.do")
    public ModelAndView tophoto() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("photo");
        return mv;
    }

    /**
     * 跳转到主页面，同时读取数据库显示文章
     *
     * @return
     */
    @RequestMapping("toMainPage.do")
    public ModelAndView toMainPage() {
        ModelAndView mv = new ModelAndView();
        List<Post> myPostList = postService.getMainPageposts();
        request.setAttribute("posts", myPostList);
        mv.setViewName("main");
        return mv;
    }


    /**
     * 跳转到登录页面
     *
     * @return
     */
    @RequestMapping("toLoginPage.do")
    public ModelAndView toLoginPage() {
        ModelAndView mv = new ModelAndView();
        String postIdStr = null;
        if (request.getParameter("postId") != null) {
            postIdStr = request.getParameter("postId");
        }
        if (postIdStr == null || postIdStr.equals("null")) {
            mv.setViewName("login");
        } else {
            mv.setViewName("login");
            mv.addObject("postId", postIdStr);
        }
        return mv;
    }

    /**
     * 跳转到注册页面
     *
     * @return
     */
    @RequestMapping("toSignUpPage.do")
    public String toSignUpPage() {

        return "signUp";
    }

    /**
     * 跳转到发文章页面
     *
     * @return
     */
    @RequestMapping("toPublishTipPage.do")
    public ModelAndView toPublishTipPage() {
        ModelAndView mv = new ModelAndView();
        // 发文前检查用户登录
        User user = (User) session.getAttribute("USER");
        if (user == null) {
            request.setAttribute("myInfo", "发文章请先登录！");
            mv.setViewName("publishTip");
            return mv;
        }
        // 先获取所有版块
        List<Forum> forumList = forumService.getAllForum();
        if (forumList != null) {
            request.setAttribute("forums", forumList);
        }

        // 获取所有分类
        List<Tab> tabList = tabService.getAllTab();
        if (tabList != null) {
            request.setAttribute("tabs", tabList);
        }

        mv.setViewName("publishTip");
        return mv;
    }

    /**
     * 跳转到用户管理（管理员）页面，会先从数据库读取用户数据
     *
     * @return
     */
    @RequestMapping("toUserManagePage.do")
    public ModelAndView toUserManagePage() {
        ModelAndView mv = new ModelAndView();
        List<User> userList = userService.getAllUser();
        request.setAttribute("users", userList);
        mv.setViewName("userManage");
        return mv;
    }

    /**
     * 跳转到版块管理页面
     *
     * @return
     */
    @RequestMapping("toForumManagePage.do")
    public ModelAndView toForumManagePage() {
        ModelAndView mv = new ModelAndView();
        List<Forum> forumList = forumService.selForumAllMain();
        request.setAttribute("forums", forumList);
        mv.setViewName("forumManage");
        return mv;
    }

    /**
     * 跳转到评论管理页面
     *
     * @return
     */
    @RequestMapping("toReplyManagePage.do")
    public ModelAndView toReplyManagePage() {
        ModelAndView mv = new ModelAndView();
        List<Reply> replies = replyService.selReplyAll();
        request.setAttribute("replies", replies);
        mv.setViewName("repliesManage");
        return mv;
    }
}
