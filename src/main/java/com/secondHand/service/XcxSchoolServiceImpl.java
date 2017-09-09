package com.secondHand.service;

import com.secondHand.dao.XcxSchoolDao;
import com.secondHand.entity.XcxSchool;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XcxSchoolServiceImpl
  implements XcxSchoolService
{

  @Autowired
  private XcxSchoolDao xcxSchoolDao;

  public XcxSchool get(String id)
  {
    return this.xcxSchoolDao.get(id);
  }

  public List<XcxSchool> list()
  {
    return this.xcxSchoolDao.list();
  }

  public List<String> school()
  {
    List<XcxSchool> list = this.xcxSchoolDao.list();
    List<String> school = new ArrayList<String>();
    for (XcxSchool xcxSchool : list) {
      school.add(xcxSchool.getSchool());
    }
    return school;
  }

  public void update(XcxSchool xcxSchool)
  {
    xcxSchool.setUpdateDate(new Date());
    this.xcxSchoolDao.update(xcxSchool);
  }
}