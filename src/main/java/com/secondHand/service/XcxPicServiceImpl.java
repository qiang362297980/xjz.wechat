package com.secondHand.service;

import com.secondHand.dao.XcxPicDao;
import com.secondHand.entity.XcxPic;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XcxPicServiceImpl
  implements XcxPicService
{

  @Autowired
  private XcxPicDao xcxPicDao;

  public XcxPic get(String id)
  {
    return this.xcxPicDao.get(id);
  }

  public List<XcxPic> getList(XcxPic xcxPic)
  {
    return this.xcxPicDao.getList(xcxPic);
  }
}