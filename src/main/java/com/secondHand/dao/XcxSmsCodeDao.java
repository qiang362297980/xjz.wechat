package com.secondHand.dao;

import com.secondHand.entity.XcxSmsCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by XCA on 2017/5/27.
 */
@Repository
public interface XcxSmsCodeDao {

    XcxSmsCode get(@Param(value = "id")String id);

    XcxSmsCode check(@Param(value = "mobile")String mobile,
                     @Param(value = "code")String code);

    void add(XcxSmsCode xcxSmsCode);

    void update(XcxSmsCode xcxSmsCode);
}
