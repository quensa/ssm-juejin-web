package com.lf.mapper;

import com.lf.pojo.Bad;
import com.lf.pojo.Collect;
import com.lf.pojo.Focus;
import com.lf.pojo.Good;
import org.apache.ibatis.annotations.*;

import java.util.Map;

public interface UtilMapper {

    /**
     * 查询关注表信息
     *
     * @param focus
     * @return
     */
    @Select("select * from  focus where uid = #{uid} and buid = #{buid}")
    Focus queryFocus(Focus focus);

    /**
     * 插入关注信息
     *
     * @param focus
     * @return
     */
    @Insert("insert into focus (uid,buid) value (#{uid},#{buid})")
    int insertFocus(Focus focus);

    /**
     * 删除关注信息
     *
     * @param focus
     * @return
     */
    @Delete("delete from focus where uid = #{uid} and buid = #{buid}")
    int deleteFocus(Focus focus);

    /**
     * 查询点赞表
     *
     * @param good
     * @return
     */
    @Select("select * from  good where uid = #{uid} and arId = #{arid}")
    Good queryGood(Good good);


    /**
     * 插入一条点赞信息
     *
     * @param good
     * @return
     */
    @Insert("insert into good (uid,arid) value (#{uid},#{arid})")
    int insertGood(Good good);


    /**
     * 删除一条点赞信息
     *
     * @param good
     * @return
     */
    @Delete("delete from good where uid = #{uid} and arid = #{arid}")
    int deleteGood(Good good);

    /**
     * 根据文章的id查询点赞数
     *
     * @param arid
     * @return
     */
    @Select("SELECT COUNT(good.uId) FROM good WHERE arid = #{arid}")
    int goodCount(Integer arid);

    /**
     * 查询点踩表
     *
     * @param bad
     * @return
     */
    @Select("select * from  bad where uid = #{uid} and arId = #{arid}")
    Bad querybad(Bad bad);


    /**
     * 插入一条点踩信息
     *
     * @param bad
     * @return
     */
    @Insert("insert into bad (uid,arid) value (#{uid},#{arid})")
    int insertBad(Bad bad);

    /**
     * 删除一条点赞信息
     *
     * @param bad
     * @return
     */
    @Delete("delete from bad where uid = #{uid} and arid = #{arid}")
    int deleteBad(Bad bad);

    /**
     * 根据文章的id查询点踩数
     *
     * @param arid
     * @return
     */
    @Select("SELECT COUNT(bad.uId) FROM bad WHERE arid = #{arid}")
    int badCount(Integer arid);

    /**
     * 查询收藏表
     *
     * @param collect {
     *                uid: 用户id
     *                aid: 文章id
     *                }
     * @return 一条记录
     */
    @Select("select * from  collect where uid = #{uid} and aid = #{aid}")
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
    @Insert("insert into collect (uid,aid) value (#{uid},#{aid})")
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
    @Delete("delete from collect where uid = #{uid} and aid = #{aid}")
    int deleteCollect(Collect collect);


}
