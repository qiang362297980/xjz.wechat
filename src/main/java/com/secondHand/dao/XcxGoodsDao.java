package com.secondHand.dao;

import com.secondHand.entity.XcxGoods;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by XCA on 2017/5/22.
 */
@Repository
public interface XcxGoodsDao {

    XcxGoods get(@Param(value = "id")String id);

    List<XcxGoods> getGoods(XcxGoods xcxGoods);

    void add(XcxGoods xcxGoods);

    void update(XcxGoods xcxGoods);

    int goodsNum(@Param(value = "openId")String openId);

    List<XcxGoods> getMyGoods(@Param(value = "openId")String openId);
    
    Integer getCountGoodsSumWithOpenId(String openId);
}
