package com.lf.service.impl;

import com.lf.mapper.ForumMapper;
import com.lf.mapper.PostMapper;
import com.lf.mapper.TabMapper;
import com.lf.mapper.UtilMapper;
import com.lf.pojo.Bad;
import com.lf.pojo.Collect;
import com.lf.pojo.Focus;
import com.lf.pojo.Good;
import com.lf.service.UtilService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UtilServiceImpl implements UtilService {


    @Resource
    private PostMapper postMapper;

    @Resource
    private UtilMapper utilMapper;

    @Override
    public Focus queryFocus(Focus focus) {
        return utilMapper.queryFocus(focus);
    }

    @Override
    public int insertFocus(Focus focus) {
        return utilMapper.insertFocus(focus);
    }

    @Override
    public int deleteFocus(Focus focus) {
        return utilMapper.deleteFocus(focus);
    }

    @Override
    public Good queryGood(Good good) {
        return utilMapper.queryGood(good);
    }

    @Override
    public int insertGood(Good good) {
        return utilMapper.insertGood(good);
    }

    @Override
    public int deleteGood(Good good) {
        return utilMapper.deleteGood(good);
    }

    @Override
    public int goodCount(Integer arid) {
        return utilMapper.goodCount(arid);
    }

    @Override
    public Collect queryCollect(Collect collect) {
        return utilMapper.queryCollect(collect);
    }

    @Override
    public int insertCollect(Collect collect) {
        return utilMapper.insertCollect(collect);
    }

    @Override
    public int deleteCollect(Collect collect) {
        return utilMapper.deleteCollect(collect);
    }

    @Override
    public Bad querybad(Bad bad) {
        return utilMapper.querybad(bad);
    }

    @Override
    public int insertBad(Bad bad) {
        return utilMapper.insertBad(bad);
    }

    @Override
    public int deleteBad(Bad bad) {
        return utilMapper.deleteBad(bad);
    }

    @Override
    public int badCount(Integer arid) {
        return utilMapper.badCount(arid);
    }
}
