package com.secondHand.service;

import com.secondHand.entity.XcxGoodsAssess;
import com.tool.AssessInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by XCA on 2017/5/23.
 */
public interface XcxAssessService {

    XcxGoodsAssess get(String id);

    List<XcxGoodsAssess> getList(String goodsId);

    List<String> getParentType(String goodsId);

    List<XcxGoodsAssess> getByParentId(String goodsId, String parentId);

    List<XcxGoodsAssess> getListByOpenId(String openId);

    void add(XcxGoodsAssess assess);

    void update(XcxGoodsAssess assess);

    XcxGoodsAssess addGoodsAssess(String goodsId, String mobile, String context, String pOpenId);

    List<List<XcxGoodsAssess>> groupAssessList(String goodsId);

    List<AssessInfo> getMyNews(String openId,Integer pageNum) throws Exception;
}
