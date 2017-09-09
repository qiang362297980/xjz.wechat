package com.secondHand.dao;

import com.secondHand.entity.XcxSchool;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/6/10.
 */
@Repository
public interface XcxSchoolDao {

    XcxSchool get(@Param(value = "id")String id);

    List<XcxSchool> list();

    void update(XcxSchool xcxSchool);

}
