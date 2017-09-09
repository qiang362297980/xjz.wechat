package com.secondHand.service;

import com.secondHand.entity.XcxCollect;
import com.secondHand.entity.XcxGoods;

import java.util.List;

/**
 * Created by XCA on 2017/5/24.
 */
public interface XcxCollectService {

    XcxCollect get(String id);

    XcxCollect getCollect(String openId, String goodsId);

    List<XcxGoods> getByOpenId(String openId, String goodsId);

    void add(XcxCollect xcxCollect);

    void update(XcxCollect xcxCollect);

}
