package com.lf.service;


import com.lf.pojo.Post;
import com.lf.pojo.User;
import org.apache.ibatis.annotations.Update;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface UserService {

    /**
     * 用户登录
     *
     * @param user 传入含有用户名和密码的user对象
     * @return
     */
    User login(User user);

    /**
     * 查询用户
     *
     * @param userId
     * @return
     */
    User getUserById(int userId);

    /**
     * 更新用户信息
     *
     * @param
     * @return
     */
    int updateUserpwd(String user_password, int user_id);

    /**
     * 添加用户
     *
     * @param user 用户对象
     * @return
     */
    String addUser(User user);

    /**
     * 获得所有用户信息
     *
     * @return List<User>
     */
    List<User> getAllUser();

    /**
     * 修改用户状态
     *
     * @param user_id     用户id
     * @param user_status 用户新状态
     * @return
     */
    String modifyUserStatus(int user_id, int user_status);

    /**
     * 根据用户名获取用户信息
     *
     * @param user_name 用户名
     * @return 用户对象
     */
    User getUserByUserName(String user_name);

    /**
     * 修改用户最近登录时间
     *
     * @param user 用户对象
     * @return 0成功，-1用户不存在，-2更新失败
     */
    int modifyUserLastLoginTime(User user);

    /**
     * 模糊查询用户（ID/用户名/昵称）
     *
     * @param userKeyword 关键词
     * @return
     */
    List<User> searchUserFuzzy(String userKeyword);

    /**
     * 修改用户昵称
     *
     * @param user 用户对象
     * @return
     */
    int modifyUserNickName(User user);


    /**
     * 找回密码修改
     *
     * @param
     * @return
     */
    @Transactional(readOnly = false)
    int updaptpwd(String user_name, String user_password);

    /**
     * 上传用户头像
     *
     * @param uploadImg
     * @return
     */
    String uploadUserImg(@RequestParam("file") MultipartFile uploadImg);

    /**
     * 更新用户头像
     *
     * @param user
     * @return
     */
    int updUserImg(User user);


}
