package com.secondHand.service;

import com.secondHand.entity.XcxUser;
import com.tool.JsonResult;
import com.tool.UserInfo;

/**
 * Created by XCA on 2017/5/22.
 */
public interface XcxUserService {


    XcxUser get(String id);

    XcxUser getByMobile(String mobile);

    XcxUser getByOpenId(String openId);

    JsonResult<XcxUser> isFollow(String openId,String uOpenId,Integer pageNum);

    void add(XcxUser xcxUser);

    void update(XcxUser xcxUser);

    UserInfo getUserInfo(String openId);

//    XcxUser getUserPage(String openId);

}
