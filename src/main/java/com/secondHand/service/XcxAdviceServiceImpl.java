package com.secondHand.service;

import com.secondHand.dao.XcxAdviceDao;
import com.secondHand.entity.XcxAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by XCA on 2017/5/26.
 */
@Service
public class XcxAdviceServiceImpl implements XcxAdviceService {

    @Autowired
    private XcxAdviceDao xcxAdviceDao;

    public XcxAdvice get(String id) {
        return xcxAdviceDao.get(id);
    }

    public void add(XcxAdvice xcxAdvice) {
        xcxAdvice.setCreateDate(new Date());
        xcxAdvice.setDelFlag("0");
        xcxAdviceDao.add(xcxAdvice);
    }

    public void update(XcxAdvice xcxAdvice) {
        xcxAdvice.setUpdateDate(new Date());
        xcxAdviceDao.update(xcxAdvice);
    }
}
