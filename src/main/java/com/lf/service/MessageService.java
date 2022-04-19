package com.lf.service;

import com.lf.pojo.Message;

import java.util.List;


public interface MessageService {


    int insertMessage(Message message);


    List<Message> selMessage(int buser_id);

    int updMessageStatus(int message_type, int message_id);
}
