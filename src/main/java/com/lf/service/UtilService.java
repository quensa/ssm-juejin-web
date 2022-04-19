package com.lf.service;

import com.lf.pojo.Bad;
import com.lf.pojo.Collect;
import com.lf.pojo.Focus;
import com.lf.pojo.Good;


public interface UtilService {


    /**
     * 查询关注表信息
     *
     * @param focus
     * @return
     */
    Focus queryFocus(Focus focus);

    /**
     * 插入关注信息
     *
     * @param focus
     * @return
     */
    int insertFocus(Focus focus);

    /**
     * 删除关注信息
     *
     * @param focus
     * @return
     */
    int deleteFocus(Focus focus);

    /**
     * 查询点赞表
     *
     * @param good
     * @return
     */
    Good queryGood(Good good);

    /**
     * 插入一条点赞信息
     *
     * @param good
     * @return
     */
    int insertGood(Good good);

    /**
     * 删除一条点赞信息
     *
     * @param good
     * @return
     */
    int deleteGood(Good good);

    /**
     * 根据文章的id查询点赞数
     *
     * @param arid
     * @return
     */
    int goodCount(Integer arid);

    /**
     * 查询收藏表
     *
     * @param collect {
     *                uid: 用户id
     *                aid: 文章id
     *                }
     * @return 一条记录
     */
    Collect queryCollect(Collect collect);

    /**
     * 插入一条收藏信息
     *
     * @param collect {
     *                uid: 用户id
     *                aid: 文章id
     *                }
     * @return 插入行数
     */
    int insertCollect(Collect collect);

    /**
     * 删除一条收藏信息
     *
     * @param collect {
     *                uid：用户id
     *                aid：文章id
     *                }
     * @return 删除行数
     */
    int deleteCollect(Collect collect);


    /**
     * 查询点踩表
     *
     * @param bad
     * @return
     */
    Bad querybad(Bad bad);


    /**
     * 插入一条点踩信息
     *
     * @param bad
     * @return
     */
    int insertBad(Bad bad);

    /**
     * 删除一条点赞信息
     *
     * @param bad
     * @return
     */
    int deleteBad(Bad bad);

    /**
     * 根据文章的id查询点踩数
     *
     * @param arid
     * @return
     */
    int badCount(Integer arid);


}
