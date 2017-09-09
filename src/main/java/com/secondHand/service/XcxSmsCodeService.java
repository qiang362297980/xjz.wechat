package com.secondHand.service;

import com.secondHand.entity.XcxSmsCode;

/**
 * Created by XCA on 2017/5/27.
 */
public interface XcxSmsCodeService {

    XcxSmsCode get(String id);

    XcxSmsCode check(String mobile, String code);

    void add(XcxSmsCode xcxSmsCode);

    void update(XcxSmsCode xcxSmsCode);

}
