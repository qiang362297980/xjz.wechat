package com.secondHand.service;

import com.secondHand.entity.XcxPayLog;

import java.util.List;

/**
 * Created by Administrator on 2017/6/4.
 */
public interface XcxPayLogService {

    XcxPayLog get(String id);

    List<XcxPayLog> getByOpenId(String openId);

    XcxPayLog getByOrderNum(String orderNum);

    XcxPayLog getPayInfo(String id);

    void add(XcxPayLog xcxPayLog);

    void update(XcxPayLog xcxPayLog);

}
