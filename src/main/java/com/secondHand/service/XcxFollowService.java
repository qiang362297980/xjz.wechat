package com.secondHand.service;

import java.util.List;

import com.secondHand.entity.XcxFollow;
import com.secondHand.entity.XcxUser;
import com.tool.JsonResult;

/**
 * Created by Administrator on 2017/6/9.
 */
public interface XcxFollowService {

    XcxFollow get(String id);

    List<XcxFollow> getByYou(String uOpenId);

    void add(XcxFollow xcxFollow);

    JsonResult<Object> doFollow(String concernOpenId, String currentOpenId,String type);

    List<XcxUser> myFollowMan(String openId);

}
