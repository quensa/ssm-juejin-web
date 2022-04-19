package com.lf.utils;

import com.lf.pojo.User;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

//工具类
//多线程实现用户体验！异步处理
public class SendMail extends Thread {

    private String from = "1776411061@qq.com"; //发件人
    private String username = "1776411061@qq.com"; //用户名
    private String password = "jmhegbgjioqobgbe";  //邮箱的授权码
    private String host = "smtp.qq.com"; //发送邮件的服务器地址
    public static String nonce_str;

    public static String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    private User user;

    public SendMail() {
    }

    ;

    public SendMail(User user) {
        this.user = user;
    }

    //重写run方法，在其中发送邮件给指定用户
    @Override
    public void run() {
        try {
            Properties prop = new Properties();
            prop.setProperty("mail.host", "smtp.qq.com"); //QQ邮件服务器
            prop.setProperty("mail.transport.protocol", "smtp"); //邮件发送协议
            prop.setProperty("mail.smtp.auth", "true");  //需要验证用户名、密码

            //关于QQ邮箱，需要设置SSL加密，加上以下代码即可
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.smtp.enable", "true");
            prop.put("mail.smtp.ssl.socketFactory", sf);


            String nonce = DxyzmService.getNonce_str();
            SendMail.nonce_str = nonce;
            String info = "验证码是：" + nonce_str;
            System.out.println(SendMail.nonce_str);


            //1、创建定义整个应用程序所需的环境信息的Session信息
            //下面这个QQ才有！！
            Session session = Session.getDefaultInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("1776411061@qq.com", "jmhegbgjioqobgbe");
                }
            });

            //开启Session的debug模式，可以看到程序发送Email的运行状态
            session.setDebug(true);

            //2、通过Session得到transport对象
            Transport ts = session.getTransport();

            //3、使用邮箱的用户名和授权码  连接邮件服务器
            ts.connect("smtp.qq.com", "1776411061@qq.com", "jmhegbgjioqobgbe");

            //4、创建邮件:写邮件（需要传递Session）
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress("1776411061@qq.com")); //发件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getUser_email())); //收件人
            message.setSubject("验证码");  //邮件主题


            message.setContent(info, "text/html;charset=UTF-8");
            message.saveChanges();

            //5、发送邮件
            ts.sendMessage(message, message.getAllRecipients());

            //关闭
            ts.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(nonce_str);
        }
    }
}

