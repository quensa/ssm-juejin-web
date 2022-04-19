package com.lf.service.impl;

import com.lf.mapper.MessageMapper;
import com.lf.pojo.Message;
import com.lf.service.MessageService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Resource
    private MessageMapper messageMapper;

    @Override
    public int insertMessage(Message message) {
        return messageMapper.insertMessage(message);
    }

    @Override
    public List<Message> selMessage(int buser_id) {
        return messageMapper.selMessage(buser_id);
    }

    @Override
    public int updMessageStatus(int message_type, int message_id) {
        return messageMapper.updMessageStatus(message_type, message_id);
    }
}
