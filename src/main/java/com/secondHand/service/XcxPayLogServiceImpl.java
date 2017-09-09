package com.secondHand.service;

import com.secondHand.dao.StoreDao;
import com.secondHand.dao.StoreGoodsDao;
import com.secondHand.dao.XcxPayLogDao;
import com.secondHand.dao.XcxUserDao;
import com.secondHand.entity.Store;
import com.secondHand.entity.StoreGoods;
import com.secondHand.entity.XcxPayLog;
import com.secondHand.entity.XcxUser;
import com.tool.TemplateConfig;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XcxPayLogServiceImpl
  implements XcxPayLogService
{

  @Autowired
  private XcxPayLogDao xcxPayLogDao;

  @Autowired
  private XcxUserDao xcxUserDao;

  @Autowired
  private StoreGoodsDao storeGoodsDao;

  @Autowired
  private StoreDao storeDao;
  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

  public XcxPayLog get(String id)
  {
    return this.xcxPayLogDao.get(id);
  }

  public List<XcxPayLog> getByOpenId(String openId)
  {
    List<XcxPayLog> list = this.xcxPayLogDao.getByOpenId(openId);
    if ((list != null) && (list.size() > 0)) {
      for (XcxPayLog xcxPayLog : list) {
        xcxPayLog.setCreateTime(sdf.format(xcxPayLog.getCreateDate()));

        StoreGoods storeGoods = this.storeGoodsDao.get(xcxPayLog.getGoodsId());
        if (storeGoods != null) {
          xcxPayLog.setGoodsName(storeGoods.getName());
          xcxPayLog.setDescription(storeGoods.getDescription());
          xcxPayLog.setPrice(storeGoods.getPrice());
          xcxPayLog.setYunFee(storeGoods.getYunFee());
          xcxPayLog.setMoney(String.valueOf(Float.valueOf(storeGoods.getPrice()).floatValue() + 
            Float.valueOf(storeGoods.getYunFee()).floatValue()));
        }

        XcxUser xcxUser = this.xcxUserDao.getByOpenId(xcxPayLog.getOpenId());
        if (xcxUser != null) {
          xcxPayLog.setUserName(xcxUser.getUserName());
          xcxPayLog.setUserMobile(xcxUser.getMobile());
          xcxPayLog.setArea(xcxUser.getArea());
        }
      }
      return list;
    }
    return null;
  }

  public XcxPayLog getPayInfo(String id)
  {
    XcxPayLog xcxPayLog = this.xcxPayLogDao.get(id);
    if (xcxPayLog != null) {
      xcxPayLog.setCreateTime(sdf.format(xcxPayLog.getCreateDate()));

      StoreGoods storeGoods = this.storeGoodsDao.get(xcxPayLog.getGoodsId());
      if (storeGoods != null) {
        xcxPayLog.setDescription(storeGoods.getDescription());
        xcxPayLog.setGoodsName(storeGoods.getName());
        xcxPayLog.setPrice(storeGoods.getPrice());
        xcxPayLog.setYunFee(storeGoods.getYunFee());
        xcxPayLog.setMoney(String.valueOf(Float.valueOf(storeGoods.getPrice()).floatValue() + 
          Float.valueOf(storeGoods.getYunFee()).floatValue()));
        String url = TemplateConfig.getValue("IP") + 
          storeGoods.getViewPicUrl().split("secondHand_back-1.0-SNAPSHOT")[1];
        url = url.replaceAll("\\\\", "/");
        xcxPayLog.setViewUrl(url.split(",")[0]);
      }

      XcxUser xcxUser = this.xcxUserDao.getByOpenId(xcxPayLog.getOpenId());
      if (xcxUser != null) {
        xcxPayLog.setUserName(xcxUser.getUserName());
        xcxPayLog.setUserMobile(xcxUser.getMobile());
        xcxPayLog.setArea(xcxUser.getArea());
      }

      Store store = this.storeDao.get();
      xcxPayLog.setStoreMobile(store.getMobile());
    }
    return xcxPayLog;
  }

  public void add(XcxPayLog xcxPayLog)
  {
    xcxPayLog.setStatus("0");
    xcxPayLog.setCreateDate(new Date());
    xcxPayLog.setDelFlag("0");
    this.xcxPayLogDao.add(xcxPayLog);
  }

  public void update(XcxPayLog xcxPayLog)
  {
    xcxPayLog.setUpdateDate(new Date());
    this.xcxPayLogDao.update(xcxPayLog);
  }

  public XcxPayLog getByOrderNum(String orderNum)
  {
    return this.xcxPayLogDao.getByOrderNum(orderNum);
  }
}