package com.secondHand.service;

import com.secondHand.entity.XcxGoods;
import com.secondHand.entity.XcxGoodsAssess;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * Created by XCA on 2017/5/22.
 */
public interface XcxGoodsService {

    XcxGoods get(String id);

    List<XcxGoods> getGoods(XcxGoods xcxGoods,int pageNum) throws Exception;

    List<XcxGoods> getMyGoods(String openId,Integer pageNum) throws Exception;

    void add(XcxGoods xcxGoods);

    void update(XcxGoods xcxGoods);

    XcxGoods goodsLike(String key);

    boolean addZanNum(String id);

    boolean addAssessNum(String id);

    XcxGoods doUploadGoods(String openId,String mobile, String goodsName, String school, String description, String price,
                       String oldPrice, String degree, String type,String[] pics);

	Integer getCountGoodsSumWithOpenId(String openId);
 

}
