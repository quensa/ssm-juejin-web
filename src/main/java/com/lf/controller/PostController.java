package com.lf.controller;

import com.lf.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.lf.service.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class PostController {
    @Resource
    private PostService postService;

    @Resource
    private ReplyService replyService;

    @Resource
    private UserService userService;

    @Resource
    private ForumService forumService;

    @Resource
    private TabService tabService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    /**
     * 发文章控制
     *
     * @return
     */
    @RequestMapping(value = "publishNewTip.do", method = RequestMethod.POST)
    public ModelAndView publishNewTip(String IsHide) {
        ModelAndView mv = new ModelAndView();
        Post post = new Post();
        // 发文章用户id
        User user = (User) session.getAttribute("USER");
        post.setUser_id(user.getUser_id());
        // 标题
        String title = request.getParameter("tip_title");
        post.setPost_title(title);
        String content = request.getParameter("tip_content");
        if (!content.equals("")) {
            post.setPost_content(content); // 内容
        } else {
            System.out.println("正文内容为空");
        }
        // 分类
        String tmpTabIdStr = request.getParameter("tab_id");
        if (!tmpTabIdStr.equals("")) {
            int tabId = Integer.valueOf(tmpTabIdStr);
            post.setTab_id(tabId); // 放入文章对象
        }
        if (IsHide != null) {
            post.setPost_isHide(1);
        } else {
            post.setPost_isHide(0);
        }
        // 数据持久化
        String resultStr = postService.addpost(post);
        request.setAttribute("myInfo", resultStr);
        mv.setViewName("publishTip");
        return mv;
    }

    /**
     * 显示文章内容，同时显示回复，在这里增加点击量
     *
     * @param postId 文章id
     * @return
     */
    @RequestMapping("showpost.do")
    public ModelAndView showTip(int postId) {
        ModelAndView mv = new ModelAndView();
        // 获取文章
        Post post = postService.getpostBypostId(postId);
        // 增加点击阅读量
        if (post != null) {
            int oldClick = post.getPost_click();
            String addClickResult = postService.addpostClick(postId);
            if (addClickResult.equals("success")) {
                // 更新文章对象的点击量（不用再读取数据库）
                post.setPost_click(oldClick + 1);
            }
        }
        // 将文章添加到request
        request.setAttribute("post", post);
        // 获取回复
        List<Reply> replyList = replyService.getReplyByTipId(postId);
        // 获取回复对应的用户
        for (int i = 0; i < replyList.size(); i++) {
            User user = userService.getUserById(replyList.get(i).getUser_id());
            replyList.get(i).setUser(user);
        }
        // 将回复添加到request
        request.setAttribute("replies", replyList);
        mv.setViewName("tipContent");
        return mv;
    }


    /**
     * 跳转到管理员修改文章页面
     *
     * @param post_id 文章id
     * @return
     */
    @RequestMapping("toModifyPostPage.do")
    public ModelAndView toModifyPostPage(int post_id) {
        ModelAndView mv = new ModelAndView();
        // 获取文章信息
        Post post = postService.getpostBypostId(post_id);
        // 获取user、forum和tab信息
        User user = userService.getUserById(post.getUser_id());
        Tab tab = tabService.getTabByTabId(post.getTab_id());
        Forum forum = forumService.getForumByForumId(tab.getForum_id());
        // 注入到文章
        post.setUser(user);
        tab.setForum(forum);
        post.setTab(tab);
        // 获取所有版块
        List<Forum> forumList = forumService.getAllForum();
        request.setAttribute("forums", forumList);
        // 获取所有分类
        List<Tab> tabList = tabService.getAllTab();
        request.setAttribute("tabs", tabList);
        request.setAttribute("post", post);
        mv.setViewName("modifyTip");
        return mv;
    }

    /**
     * 修改文章信息
     *
     * @return
     */
    @RequestMapping("modifypost.do")
    public ModelAndView modifyPost(Post post) {
        ModelAndView mv = new ModelAndView();
        // 处理参数
        Date date = new Date();
        post.setPost_modifyTime(date);
        int tabId = Integer.valueOf(request.getParameter("selectedTabId")); // 获取分类
        post.setTab_id(tabId);
        // 开始修改
        String resultStr = postService.modifypost(post);
        request.setAttribute("myInfo", resultStr);
        // 刷新文章数据
        request.setAttribute("posts", this.getUpdatePosts());
        mv.setViewName("tipManage");
        return mv;
    }

    /**
     * 重新获取所有文章数据
     *
     * @return
     */
    private List<Post> getUpdatePosts() {
        List<Post> allpost = postService.getAllpost();
        return allpost;
    }

    /**
     * 跳转到文章管理（管理员）页面，会先从数据库读取文章数据
     *
     * @return
     */
    @RequestMapping("toPostManagePage.do")
    public ModelAndView toPostManagePage() {
        ModelAndView mv = new ModelAndView();
        // 获取文章
        request.setAttribute("posts", this.getUpdatePosts());
        mv.setViewName("tipManage");
        return mv;
    }

    /**
     * 修改文章状态（管理员）
     *
     * @param tipId 文章id
     * @param opr   操作
     * @return
     */
    @RequestMapping("ChangeTipStatus.do")
    public ModelAndView changeTipStatus(int tipId, int opr) {
        ModelAndView mv = new ModelAndView();
        String resultStr = new String();
        switch (opr) {
            case 0:
                StringBuffer strBuff = new StringBuffer();
                if (postService.enablepost(tipId).equals("success")) {
                    strBuff.append("取消删除成功！");
                }
                resultStr = strBuff.toString();
                break;
            case 1:
                if (postService.disablepost(tipId).equals("success")) {
                    resultStr = "删文章成功！";
                } else {
                    resultStr = "删文章失败！";
                }
                break;
            case 2:
                if (postService.enablepost(tipId).equals("success")) {
                    resultStr = "取消删文章成功！";
                } else {
                    resultStr = "取消删文章失败！";
                }
                break;
            case 5:
                if (postService.doToppost(tipId).equals("success")) {
                    resultStr = "置顶成功！";
                } else {
                    resultStr = "置顶失败！";
                }
                break;
            case 6:
                if (postService.disToppost(tipId).equals("success")) {
                    resultStr = "取消置顶成功！";
                } else {
                    resultStr = "取消置顶失败！";
                }
                break;
        }
        request.setAttribute("myInfo", resultStr);
        mv.setViewName("redirect:toPostManagePage.do");
        return mv;
    }


    /**
     * 跳转到发文章人修改文章页面
     *
     * @param tipId 文章id
     * @return
     */
    @RequestMapping("toUserModifyTipPage.do")
    public ModelAndView toUserModifyTipPage(int tipId) {
        ModelAndView mv = new ModelAndView();
        // 获取文章信息
        Post tip = postService.getpostBypostId(tipId);
        // 获取user、forum和tab信息
        User user = userService.getUserById(tip.getUser_id());
        Tab tab = tabService.getTabByTabId(tip.getTab_id());
        Forum forum = forumService.getForumByForumId(tab.getForum_id());
        // 注入到文章对象
        tip.setUser(user); // 用户信息
        tab.setForum(forum); // 版块信息
        tip.setTab(tab); // 分类信息
        // 获取所有版块
        List<Forum> forumList = forumService.getAllForum();
        request.setAttribute("forums", forumList);
        // 获取所有分类
        List<Tab> tabList = tabService.getAllTab();
        request.setAttribute("tabs", tabList);
        request.setAttribute("tip", tip);
        // 跳转页面
        mv.setViewName("userModifyTip");
        return mv;
    }

    /**
     * 【首页搜索】根据关键词搜索文章（标题和内容）
     *
     * @return
     */
    @RequestMapping("searchPostByKeyword.do")
    public ModelAndView searchTipByKeyword() {
        ModelAndView mv = new ModelAndView();
        // 处理参数
        String keyword = request.getParameter("keyword"); // 获取用户输入的关键词
        // 执行搜索
        List<Post> myPostList = postService.searchpostByKeyword(keyword);
        request.setAttribute("posts", myPostList);
        mv.setViewName("main");
        return mv;
    }

    /**
     * 【文章管理界面】模糊查询文章
     */
    @RequestMapping("searchTipsFuzzy.do")
    public ModelAndView searchTipsFuzzy() {
        ModelAndView mv = new ModelAndView();
        // 初始化文章列表
        List<Post> tipList = null;
        // 处理参数
        String tipKeyword = request.getParameter("keyword"); // 获取输入的关键词
        // 判断关键词是否为空
        if (tipKeyword.equals("") || tipKeyword.isEmpty()) {
            // 为空时返回所有数据
            tipList = postService.getAllpost();
        } else {
            // 不为空时模糊查询
            tipList = postService.searchpostFuzzy(tipKeyword); // 调用服务层 执行查询
        }
        request.setAttribute("posts", tipList);
        // 刷新文章管理页面
        mv.setViewName("tipManage");
        return mv;
    }

    /**
     * 根据点击量从高到低推送热榜
     */
    @RequestMapping("getHot.do")
    public ModelAndView getHot() {
        ModelAndView mv = new ModelAndView();
        List<Post> posts = postService.selPostForPost_clickDesc();
        request.setAttribute("posts", posts);
        mv.setViewName("main");
        return mv;
    }

    /**
     * 根据关注用户查询文章
     */
    @RequestMapping("getPush.do")
    public ModelAndView getPush() {
        ModelAndView mv = new ModelAndView();
        User user = (User) session.getAttribute("USER");
        List<Post> posts = postService.selAllpostIdBFocusId(user.getUser_id());
        request.setAttribute("posts", posts);
        mv.setViewName("main");
        return mv;
    }
}
