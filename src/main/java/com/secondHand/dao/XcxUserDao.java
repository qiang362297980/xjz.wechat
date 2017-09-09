package com.secondHand.dao;

import com.secondHand.entity.XcxUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public abstract interface XcxUserDao
{
  public abstract XcxUser get(@Param("id") String paramString);

  public abstract XcxUser getByMobile(@Param("mobile") String paramString);

  public abstract XcxUser getByOpenId(@Param("openId") String paramString);

  public abstract void add(XcxUser paramXcxUser);

  public abstract void update(XcxUser paramXcxUser);

  public abstract void increaseFollowNumWithOpenId(String paramString);

  public abstract void reduceFollowNumWithOpenId(String paramString);
}