package com.secondHand.service;

import com.secondHand.entity.XcxSchool;

import java.util.List;

/**
 * Created by Administrator on 2017/6/10.
 */
public interface XcxSchoolService {

    XcxSchool get(String id);

    List<XcxSchool> list();

    List<String> school();

    void update(XcxSchool xcxSchool);

}
