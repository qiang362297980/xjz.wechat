package com.secondHand.service;

import com.secondHand.dao.XcxSmsCodeDao;
import com.secondHand.entity.XcxSmsCode;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XcxSmsCodeServiceImpl
  implements XcxSmsCodeService
{

  @Autowired
  private XcxSmsCodeDao xcxSmsCodeDao;

  public XcxSmsCode get(String id)
  {
    return this.xcxSmsCodeDao.get(id);
  }

  public XcxSmsCode check(String mobile, String code)
  {
    return this.xcxSmsCodeDao.check(mobile, code);
  }

  public void add(XcxSmsCode xcxSmsCode)
  {
    xcxSmsCode.setCreateDate(new Date());
    xcxSmsCode.setDelFlag("0");
    this.xcxSmsCodeDao.add(xcxSmsCode);
  }

  public void update(XcxSmsCode xcxSmsCode)
  {
    xcxSmsCode.setUpdateDate(new Date());
    this.xcxSmsCodeDao.update(xcxSmsCode);
  }
}