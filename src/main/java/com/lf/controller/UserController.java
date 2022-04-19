package com.lf.controller;


import com.lf.pojo.Post;
import com.lf.pojo.User;
import com.lf.service.PostService;
import com.lf.service.UserService;
import com.lf.utils.MD5Utils;
import com.lf.utils.SendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private PostService postService;


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    /**
     * 验证用户是否勾选记住按钮单选框
     *
     * @param rememberMe 单选框的值
     * @param user       当前登陆用户对象
     * @param request    请求对象
     * @param response   响应对象-
     * @throws
     */
    private void isMemory(boolean rememberMe, String pwd, User user, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        if (rememberMe) {
            Cookie username = new Cookie("username", user.getUser_name());
            username.setPath("/");
            Cookie password = new Cookie("password", pwd);
            password.setPath("/");

            //设置账号和密码最长保存时间 单位是秒 1天=60*60*24 *需要保存的填数 保存7填为=(60*60*24)*7
            username.setMaxAge((60 * 60 * 24) * 7);
            password.setMaxAge((60 * 60 * 24) * 7);

            // 将cookie返回给客户端
            response.addCookie(username);
            response.addCookie(password);
        } else {
            // 遍历cookie中的username和password 如果用户没有选择记住密码 则删除客户端的密码
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    if (cookies[i].getName().equals("username") || cookies[i].getName().equals("password")) {
                        System.out.println("删除掉的cookie----》" + cookies[i].getName());
                        cookies[i].setMaxAge(0);
                        cookies[i].setPath("/");
                        response.addCookie(cookies[i]);
                    }
                }
            }

        }

    }


    /**
     * 用户登录控制
     * 增加登录后更新最近登录时间的逻辑
     *
     * @Param user 用户对象（需要用户名和密码）
     */
    @RequestMapping("userLogin.do")
    public ModelAndView userLogin(User user, String rememberMe, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        String resultStr = null;
        User tmpUser = userService.getUserByUserName(user.getUser_name());
        if (tmpUser == null) { // 先检查用户是否存在
            resultStr = new String("登录失败：用户不存在！");
            mv.setViewName("login");
        } else { // 用户存在时继续判断密码
            Boolean pwdCheck = MD5Utils.encodeByMD5(user.getUser_password()).equals(tmpUser.getUser_password());
            if (pwdCheck) { // 密码正确
                /**
                 * 更新最近登录时间
                 */
                Date tmpDate = new Date(); // 获取当前时间
                tmpUser.setUser_lastLoginTime(tmpDate); // 将登录时间放入对象
                // 执行更新
                userService.modifyUserLastLoginTime(tmpUser);
                // 判断是否能正常登录1
                if (tmpUser.getUser_status() != 1) { // 非禁用状态
                    // 登录成功，将用户对象添加到session
                    HttpSession session = request.getSession();
                    session.setAttribute("USER", tmpUser);
                    // 处理是否从查看文章页面登录的
                    String tipIdStr = null;
                    if (request.getParameter("postId") != null) {
                        // 记录传过来的文章id
                        tipIdStr = request.getParameter("postId");
                    }
                    if (tipIdStr == null || tipIdStr.equals("null")) {
                        mv.setViewName("redirect:toMainPage.do");
                    } else {
                        // 如果用户是在文章详情中登录的，返回对应的文章
                        mv.setViewName("redirect:showpost.do?postId=" + tipIdStr);
                    }
                } else { // user_status == 1 表示被禁用，不能登录
                    resultStr = new String("登录失败：用户已被禁用！");
                    mv.setViewName("login");
                }
            } else { // 密码不正确
                resultStr = new String("登录失败！密码不正确！");
                mv.setViewName("login");
            }
        }
        try {
            isMemory(rememberMe != null, user.getUser_password(), user, request, response);
        } catch (UnsupportedEncodingException e) {
            System.out.println("设置字符集出错=>request.setCharacterEncoding()");
            e.printStackTrace();
        }
        request.setAttribute("myInfo", resultStr);
        return mv;
    }

    /**
     * 用户登出控制
     *
     * @param session
     * @return
     */
    @RequestMapping("userSignOut.do")
    public ModelAndView userSignOut(HttpSession session) {
        ModelAndView mv = new ModelAndView();
        // 移除session中的用户对象
        session.removeAttribute("USER");
        mv.setViewName("redirect:/");
        return mv;
    }

    /**
     * 用户信息页面
     */
    @RequestMapping("getUserInfo.do")
    public ModelAndView getUserInfo(int userId) {
        ModelAndView mv = new ModelAndView();
        User user = userService.getUserById(userId);
        if (user != null) {
            request.setAttribute("userObject", user);
            mv.setViewName("userInfo");
        } else {
            request.setAttribute("myInfo", "查询用户信息失败！");
            mv.setViewName("main");
        }
        return mv;
    }

    /**
     * 修改用户密码控制
     *
     * @return
     */
    @RequestMapping("updateUserpwd.do")
    public ModelAndView updateUserInfo(String oldPassword, String user_password) {
        ModelAndView mv = new ModelAndView();
        // 获取原session
        User oldUserInfo = (User) session.getAttribute("USER");
        if (oldUserInfo != null && oldUserInfo.getUser_password().equals(oldPassword)) {
            if (userService.updateUserpwd(user_password, oldUserInfo.getUser_id()) > 0) {
                // 新旧密码不同，退出登录
                request.setAttribute("myInfo", "由于您修改了密码，请重新登录！");
                session.removeAttribute("USER");
                mv.setViewName("login");
            } else {
                request.setAttribute("myInfo", "修改用户密码失败！");
                mv.setViewName("userInfo");
            }
        } else {
            request.setAttribute("myInfo", "用户原密码错误！");
            mv.setViewName("userInfo");
        }
        return mv;
    }


    /**
     * 用户注册控制
     *
     * @param user
     * @return
     */
    @RequestMapping("userSignUp.do")
    public ModelAndView userSignUp(User user) {
        ModelAndView mv = new ModelAndView();
        if (user != null) {
            user.setUser_password(MD5Utils.encodeByMD5(user.getUser_password()));
            String resultStr = userService.addUser(user);
            request.setAttribute("myInfo", resultStr);
            mv.setViewName("signUp");
        }
        return mv;
    }

    /**
     * 禁用用户控制
     *
     * @param userId
     * @return
     */
    @RequestMapping("disableUser.do")
    public ModelAndView disableUser(int userId) {
        ModelAndView mv = new ModelAndView();
        String resultStr = userService.modifyUserStatus(userId, 1);
        request.setAttribute("myInfo", resultStr);
        request.setAttribute("users", this.getUpdateUserData());
        mv.setViewName("userManage");
        return mv;
    }

    /**
     * 启用用户控制
     *
     * @param userId
     * @return
     */
    @RequestMapping("enableUser.do")
    public ModelAndView enableUser(int userId) {
        ModelAndView mv = new ModelAndView();
        String resultStr = userService.modifyUserStatus(userId, 0);
        request.setAttribute("myInfo", resultStr);
        request.setAttribute("users", this.getUpdateUserData());
        mv.setViewName("userManage");
        return mv;
    }

    /**
     * 锁定用户控制
     *
     * @param userId
     * @return
     */
    @RequestMapping("lockUser.do")
    public ModelAndView lockUser(int userId) {
        ModelAndView mv = new ModelAndView();
        String resultStr = userService.modifyUserStatus(userId, 2);
        request.setAttribute("myInfo", resultStr);
        request.setAttribute("users", this.getUpdateUserData());
        mv.setViewName("userManage");
        return mv;
    }

    /**
     * 获取更新后的用户数据
     *
     * @return List<User>
     */
    private List<User> getUpdateUserData() {
        List<User> userList = userService.getAllUser();
        return userList;
    }

    /**
     * 跳转到修改用户信息页面
     *
     * @return
     */
    @RequestMapping("toUpdateUserInfoPage.do")
    public ModelAndView toUpdateUserInfoPage(int userId) {
        ModelAndView mv = new ModelAndView();
        User user = userService.getUserById(userId);
        if (user != null) {
            request.setAttribute("userObject", user);
            mv.setViewName("update_userInfo");
        } else {
            request.setAttribute("myInfo", "修改前获取用户信息失败！");
            mv.setViewName("getUserInfo.do?userId=" + userId);
        }
        return mv;
    }

    /**
     * 模糊查询用户 ajax
     */
    @ResponseBody
    @RequestMapping("searchUserFuzzyByKeywordForAjax.do")
    public Object searchUserFuzzyByKeyword(HttpServletResponse response) {
        response.setContentType("application/json; charset=UTF-8");
        String keyword = request.getParameter("userKeyword");
        List<User> userList = userService.searchUserFuzzy(keyword);
        return userList;
    }

    /**
     * 【用户管理界面】模糊查询用户
     * 2020-09-20 新增
     */
    @RequestMapping("searchUsersFuzzy.do")
    public ModelAndView searchUsersFuzzy() {
        ModelAndView mv = new ModelAndView();
        List<User> userList = null;
        // 处理参数
        String userKeyword = request.getParameter("userKeyword"); // 获取输入的关键词
        System.out.println(userKeyword);
        // 判断关键词是否为空
        if (userKeyword.equals("") || userKeyword.isEmpty()) {
            // 为空时返回所有用户
            userList = userService.getAllUser();
        } else {
            // 不为空时模糊查询
            userList = userService.searchUserFuzzy(userKeyword); // 调用服务层 执行查询
        }
        request.setAttribute("users", userList);
        mv.setViewName("userManage");
        return mv;
    }

    /**
     * 跳转到修改昵称页面
     *
     * @return
     */
    @RequestMapping("toModifyNickNamePage.do")
    public ModelAndView toModifyNickNamePage(int userId) {
        ModelAndView mv = new ModelAndView();
        User user = userService.getUserById(userId);
        if (user != null) {
            request.setAttribute("userObject", user);
            // 跳转页面
            mv.setViewName("user_modify_nickName");
        } else {
            request.setAttribute("myInfo", "获取用户信息失败！");
            mv.setViewName("getUserInfo.do?userId=" + userId);
        }
        return mv;
    }

    /**
     * 修改用户昵称
     *
     * @param user 用户对象
     * @return
     */
    @RequestMapping("modifyUserNickName.do")
    public ModelAndView modifyUserNickName(User user) {
        ModelAndView mv = new ModelAndView();
        // 传入对象判空
        if (user != null) {
            String tempUserIdStr = user.getUser_id() + "";
            // 基础数据判空
            if (!"".equals(tempUserIdStr) && user.getUser_nick() != null) {
                // 执行更新
                int result = userService.modifyUserNickName(user);
                if (result < 1) { // 失败
                    request.setAttribute("myInfo", "修改用户信息失败！请联系管理员");
                    mv.setViewName("userInfo");
                } else { // 成功
                    // 获取新user信息
                    User newUserInfo = userService.getUserById(user.getUser_id());
                    // 刷新session
                    session.setAttribute("USER", newUserInfo);
                    // 刷新request
                    request.setAttribute("userObject", newUserInfo);
                    request.setAttribute("myInfo", "修改昵称成功！");
                    // 跳转用户信息页面
                    mv.setViewName("userInfo");
                }
            }
        }
        return mv;
    }

    @RequestMapping("forget")
    public String forget() {
        return "forget";
    }

    @RequestMapping("send")
    @ResponseBody
    public String register2(HttpSession session, String user_name, Model model, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        //用户注册成功之后，给用户发一封邮件
        //使用线程来专门发送邮件，防止出现耗时，和网站注册人数较多的情况
        resp.addHeader("Content-Type", "application/json;charset=UTF-8");
        System.out.println("send");
        User user = userService.getUserByUserName(user_name);
        if (user != null) {
            SendMail send = new SendMail(user);
            //启动线程，执行run方法发送邮件
            send.start();
            //这里写run()的话，得等邮件发送完了才跳转，用户体验贼差！！

            System.out.println("线程结束");
            //注册用户
            return "已发送";
        } else
            return "用户不存在";
    }


    @RequestMapping("check")
    public String check(String user_password, String user_name, Model model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        System.out.println();
        User user = userService.getUserByUserName(user_name);
        if (user != null && SendMail.nonce_str.equals(user_password)) {
            model.addAttribute("username", user_name);
            return "getback";
        } else {
            return "forget";
        }
    }

    @RequestMapping("updatepwd")
    public String updatepwd(String user_name, String user_password) throws Exception {
        int user = userService.updaptpwd(user_password, user_name);
        return "login";
    }

    @RequestMapping("uploadImg")
    public String upload(@RequestParam("file") MultipartFile uploadImg) {
        User user = (User) session.getAttribute("USER");
        String filename = userService.uploadUserImg(uploadImg);
        System.out.println(filename);
        user.setUser_img(filename);
        userService.updUserImg(user);
        return "redirect:toMainPage.do";
    }

    @RequestMapping("changeImg")
    public String changeImg() {
        return "photo";
    }

    @RequestMapping("MyCollect")
    public String MyCollect() {
        User user = (User) session.getAttribute("USER");
        List<Post> posts = postService.selpostIdByCollectId(user.getUser_id());
        request.setAttribute("posts", posts);
        return "main";
    }
}
