package com.lf.mapper;

import com.lf.pojo.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface MessageMapper {

    /**
     * 增加信件
     */
    @Insert("insert into message(user_id,buser_id,message_content,message_type)" +
            "VALUES(#{user_id},#{buser_id},#{message_content},#{message_type})")
    int insertMessage(Message message);

    /**
     * 查询自己的来信 返回信件集合
     */
    @Select("select * from message where buser_id = #{buser_id} and message_type!=2")
    List<Message> selMessage(int buser_id);

    /**
     * 更新信件的状态
     */
    @Update("update message set message_type = #{message_type} where message_id=#{message_id}")
    int updMessageStatus(@Param("message_type") int message_type, @Param("message_id") int message_id);
}
