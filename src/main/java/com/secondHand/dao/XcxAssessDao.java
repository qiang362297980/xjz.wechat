package com.secondHand.dao;

import com.secondHand.entity.XcxGoodsAssess;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by XCA on 2017/5/23.
 */
@Repository
public interface XcxAssessDao {

    XcxGoodsAssess get(@Param(value = "id")String id);

    List<XcxGoodsAssess> getList(@Param(value = "goodsId")String goodsId);

    List<String> getParentType(@Param(value = "goodsId")String goodsId);

    List<XcxGoodsAssess> getByParentId(@Param(value = "goodsId")String goodsId,
                                   @Param(value = "parentId")String parentId);

    List<XcxGoodsAssess> getListByOpenId(@Param(value = "openId")String openId);

    void add(XcxGoodsAssess assess);

    void update(XcxGoodsAssess assess);

    List<XcxGoodsAssess> getMyNews(@Param(value = "pOpenId")String pOpenId);
}
