package com.secondHand.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.secondHand.entity.XcxPayLog;

/**
 * Created by Administrator on 2017/6/4.
 */
public interface XcxPayLogDao {

    XcxPayLog get(@Param(value = "id")String id);

    XcxPayLog getByOrderNum(@Param(value = "orderNum")String orderNum);

    List<XcxPayLog> getByOpenId(@Param(value = "openId")String openId);

    void add(XcxPayLog xcxPayLog);

    void update(XcxPayLog xcxPayLog);

}
