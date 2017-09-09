package com.secondHand.service;

import com.secondHand.entity.XcxAdvice;

/**
 * Created by XCA on 2017/5/26.
 */
public interface XcxAdviceService {

    XcxAdvice get(String id);

    void add(XcxAdvice xcxAdvice);

    void update(XcxAdvice xcxAdvice);

}
