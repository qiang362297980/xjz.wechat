package com.secondHand.dao;

import com.secondHand.entity.XcxFollow;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */
@Repository
public interface XcxFollowDao {

    XcxFollow get(@Param(value = "id")String id);

    XcxFollow getFollowLog(@Param(value = "uOpenId")String uOpenId,
                           @Param(value = "aOpenId")String aOpenId);

    List<XcxFollow> getByYou(@Param(value = "uOpenId")String uOpenId);

    void add(XcxFollow xcxFollow);

    void update(XcxFollow xcxFollow);
    
    Integer getCountFollowNumWithOpenId(String openId);

	void increaseFollowWithOpenId(@Param(value = "uOpenId")String uOpenId,
            @Param(value = "aOpenId")String aOpenId);

	void reduceFollowWithOpenId(@Param(value = "uOpenId")String uOpenId,
            @Param(value = "aOpenId")String aOpenId);


}
