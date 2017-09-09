package com.secondHand.dao;

import com.secondHand.entity.XcxAdvice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by XCA on 2017/5/26.
 */
@Repository
public interface XcxAdviceDao {

    XcxAdvice get(@Param(value = "id")String id);

    void add(XcxAdvice xcxAdvice);

    void update(XcxAdvice xcxAdvice);
}
