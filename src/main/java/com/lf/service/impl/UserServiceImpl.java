package com.lf.service.impl;


import com.lf.mapper.UserMapper;
import com.lf.pojo.Post;
import com.lf.pojo.User;
import com.lf.service.UserService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    // 日志 2020-09-25 优化 不用每个方法都声明Logger
    private Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Override
    public User login(User user) {
        Logger logger = Logger.getLogger(UserServiceImpl.class);
        logger.info(user.getUser_name() + "尝试登录");
        return userMapper.selUser(user);
    }

    @Override
    public User getUserById(int userId) {
        Logger logger = Logger.getLogger(UserServiceImpl.class);
        User user = null;
        logger.info("尝试查询ID为" + userId + "的用户");
        user = userMapper.selUserByUserId(userId);
        return user;
    }

    @Override
    public int updateUserpwd(String user_password, int user_id) {
        Logger logger = Logger.getLogger(UserServiceImpl.class);
        logger.info("尝试修改ID为" + user_id + "的用户密码");
        int result = userMapper.updUserpwd(user_password, user_id);
        return result;
    }

    @Override
    public String addUser(User user) {
        Logger logger = Logger.getLogger(UserServiceImpl.class);
        // 先检查要注册的用户是否存在
        logger.info("尝试检查用户名为" + user.getUser_name() + "是否存在...");
        User tmpUser = userMapper.selUser(user);
        if (tmpUser != null) {
            return "注册失败！用户已存在！";
        }
        String resultStr = null;
        switch (user.getUser_type()) {
            case 1:
                logger.info("尝试添加新管理员，新用户名：" + user.getUser_name());
                if (userMapper.insUser(user) > 0) {
                    resultStr = new String("注册管理员成功！");
                } else {
                    resultStr = new String("注册管理员失败！");
                }
                break;
            case 2:
                logger.info("尝试添加新用户，新用户名：" + user.getUser_name());
                if (userMapper.insUser(user) > 0) {
                    resultStr = new String("注册成功！");
                } else {
                    resultStr = new String("注册失败！");
                }
                break;
            default:
                break;
        }
        return resultStr;
    }

    @Override
    public List<User> getAllUser() {
        Logger logger = Logger.getLogger(UserServiceImpl.class);
        logger.info("尝试获取所有的用户信息...");
        List<User> userList = userMapper.selUserAll();
        if (userList != null) {
            return userList;
        }
        return null;
    }

    /**
     * 修改用户状态
     *
     * @param user_id     用户id
     * @param user_status 用户新状态：0正常，1禁用，2锁定
     * @return
     */
    @Override
    public String modifyUserStatus(int user_id, int user_status) {
        Logger logger = Logger.getLogger(UserServiceImpl.class);
        // 先检查用户是否存在
        logger.info("尝试检查id为" + user_id + "的用户是否存在...");
        if (userMapper.selUserByUserId(user_id) == null) {
            return "错误：用户不存在";
        }
        String resultStr = null; // 保存结果
        switch (user_status) {
            case 0: // 正常
                logger.info("尝试启用id为" + user_id + "的用户...");
                if (userMapper.updUserStatus(user_id, user_status) > 0) {
                    resultStr = new String("启用成功！");
                } else {
                    resultStr = new String("启用失败！");
                }
                break;
            case 1: // 禁用
                logger.info("尝试禁用id为" + user_id + "的用户...");
                if (userMapper.updUserStatus(user_id, user_status) > 0) {
                    resultStr = new String("禁用成功！");
                } else {
                    resultStr = new String("禁用失败！");
                }
                break;
            case 2:
                logger.info("尝试锁定id为" + user_id + "的用户...");
                if (userMapper.updUserStatus(user_id, user_status) > 0) {
                    resultStr = new String("锁定成功！");
                } else {
                    resultStr = new String("锁定失败！");
                }
                break;
        }
        return resultStr;
    }

    @Override
    public User getUserByUserName(String user_name) {
        Logger logger = Logger.getLogger(UserServiceImpl.class);
        logger.info("尝试查询用户名为" + user_name + "的用户信息...");
        return userMapper.selUserByUserName(user_name);
    }

    /**
     * 更新用户最近登录时间
     *
     * @param user 用户对象
     * @return 0成功，-1用户不存在，-2更新失败
     */
    @Override
    public int modifyUserLastLoginTime(User user) {
        Logger logger = Logger.getLogger(UserServiceImpl.class);
        // 先检查用户是否存在
        logger.info("检查用户是否存在，id：" + user.getUser_id());
        if (userMapper.selUserByUserId(user.getUser_id()) == null) {
            return -1;
        }
        logger.info("更新用户最近登录时间，用户id【" + user.getUser_id() + "】最近登录时间：" + user.getUser_lastLoginTime());
        if (userMapper.updUserLastLoginTime(user) <= 0) {
            return -2;
        } else {
            return 0;
        }
    }

    /**
     * 模糊查询用户
     */
    @Override
    public List<User> searchUserFuzzy(String userKeyword) {
        Logger logger = Logger.getLogger(UserServiceImpl.class);
        logger.info("尝试根据关键词搜索用户");
        List<User> userList = userMapper.selUserFuzzy(userKeyword);
        if (userList != null) {
            return userList;
        }
        return null;
    }

    /**
     * 修改用户昵称
     */
    @Override
    public int modifyUserNickName(User user) {
        logger.info("尝试修改用户昵称");
        int result = userMapper.updUserNickName(user);
        return result;
    }

    @Transactional(readOnly = false)
    @Override
    public int updaptpwd(String user_name, String user_password) {
        logger.info("找回用户密码");
        int result = userMapper.updaptpwd(user_name, user_password);
        return result;
    }

    @Override
    public String uploadUserImg(MultipartFile uploadImg) {
        //原始文件名称
        String originImgName = uploadImg.getOriginalFilename();
        System.out.println(originImgName);
        //后缀名
        String suffixName = originImgName.substring(originImgName.lastIndexOf("."));
        System.out.println(suffixName);
        // 利用uuid随机生成新的文件名称
        String newImgName = UUID.randomUUID().toString() + suffixName;
        System.out.println(newImgName);
        //文件存放路径。现在存在本地，之后可以存在数据库中  路径的最后加两个\\ 如果不加，字符串拼接会拼到父目录里面去
        String filePath = "/upload/";
        File file = new File(filePath + newImgName);
        String filename = file.toString();
        System.out.println("文件地址" + filename);

        String uploadOir = "D:/develop/java/idea正版/deepcode/out/artifacts/deepcode_war_exploded/upload";

        //如果不存在该目录就创建文件夹
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            //保存上传的文件
            uploadImg.transferTo(new File(uploadOir, newImgName));
        } catch (Exception e) {
            e.printStackTrace();
            return "error";   //抓到异常就上传失败
        }
        //没有异常就上传成功
        return filename;
    }

    @Override
    public int updUserImg(User user) {
        return userMapper.updUserImg(user);
    }


}
