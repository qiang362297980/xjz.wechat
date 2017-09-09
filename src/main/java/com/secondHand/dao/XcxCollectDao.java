package com.secondHand.dao;

import com.secondHand.entity.XcxCollect;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by XCA on 2017/5/24.
 */
@Repository
public interface XcxCollectDao {

    XcxCollect get(@Param(value = "id")String id);

    List<XcxCollect> getByOpenId(@Param(value = "openId")String openId,
                                 @Param(value = "goodsId")String goodsId);

    void add(XcxCollect xcxCollect);

    void update(XcxCollect xcxCollect);

}
