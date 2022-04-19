package com.lf.controller;

import com.lf.pojo.Forum;
import com.lf.pojo.Message;
import com.lf.pojo.User;
import com.lf.service.MessageService;
import com.lf.service.ReplyService;
import com.lf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.http.HttpResponse;
import java.util.List;

@Controller
public class MessageController {


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @Resource
    private UserService userService;

    @Resource
    private MessageService messageService;

    /**
     * 跳转到提问界面
     */
    @RequestMapping("toAsk.do")
    public ModelAndView toAsk(HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("toAskPage");
        return mv;
    }

    /**
     * 去提问 增加
     */
    @RequestMapping("gotoAsk.do")
    public ModelAndView goAsk(Message message) {
        ModelAndView mv = new ModelAndView();
        User user = (User) session.getAttribute("USER");
        if (user != null) {
            message.setUser_id(user.getUser_id());
            message.setMessage_type(0);
            messageService.insertMessage(message);
            mv.setViewName("redirect:toMainPage.do");
        }
        return mv;
    }

    /**
     * 获取所有问题信息
     *
     * @return List<Forum>
     */
    private List<Message> getAllMessages() {
        User user = (User) session.getAttribute("USER");
        List<Message> messages = messageService.selMessage(user.getUser_id());
        return messages;
    }

    /**
     * 获取自身收到的所有来信
     */
    @RequestMapping("selask.do")
    public ModelAndView selask() {
        ModelAndView mv = new ModelAndView();
        String resultStr = null;
        User user = (User) session.getAttribute("USER");
        List<Message> messages = messageService.selMessage(user.getUser_id());
        if (messages.isEmpty()) {
            resultStr = "没有收到来信";
        }
        mv.addObject("messages", messages);
        request.setAttribute("myInfo", resultStr);
        mv.setViewName("answer");
        return mv;
    }

    /**
     * 改变来信状态
     */
    @RequestMapping("ChangeMessageStatus.do")
    public ModelAndView ChangeMessageStatus(int message_type, int message_id) {
        ModelAndView mv = new ModelAndView();
        messageService.updMessageStatus(message_type, message_id);
        mv.addObject("messages", this.getAllMessages());
        mv.setViewName("answer");
        return mv;
    }
}
