package com.secondHand.service;

import com.secondHand.entity.XcxPic;

import java.util.List;

/**
 * Created by XCA on 2017/5/31.
 */
public interface XcxPicService {

    XcxPic get(String id);

    List<XcxPic> getList(XcxPic xcxPic);
}
