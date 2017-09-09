package com.secondHand.service;

import com.secondHand.dao.XcxCollectDao;
import com.secondHand.dao.XcxGoodsDao;
import com.secondHand.entity.XcxCollect;
import com.secondHand.entity.XcxGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by XCA on 2017/5/24.
 */
@Service
public class XcxCollectServiceImpl implements XcxCollectService {

    @Autowired
    private XcxCollectDao xcxCollectDao;

    @Autowired
    private XcxGoodsDao xcxGoodsDao;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public XcxCollect get(String id) {
        return xcxCollectDao.get(id);
    }

    @Override
    public XcxCollect getCollect(String openId, String goodsId) {
        List<XcxCollect> list = xcxCollectDao.getByOpenId(openId, goodsId);
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    public List<XcxGoods> getByOpenId(String openId, String goodsId) {
        List<XcxCollect> list = xcxCollectDao.getByOpenId(openId,goodsId);
        List<String> goods = new ArrayList<>();
        for (XcxCollect xcxCollect : list) {
            goods.add(xcxCollect.getGoodsId());
        }
        List<XcxGoods> goodsList = new ArrayList<>();
        for (int i=0; i<goods.size(); i++) {
            XcxGoods xcxGoods = xcxGoodsDao.get(goods.get(i));
            if (xcxGoods != null) {
                xcxGoods.setCreateTime(sdf.format(xcxGoods.getCreateDate()));
                String[] urls = xcxGoods.getUrl().split(",");
                List<String> url = new ArrayList<>();
                url = Arrays.asList(urls);
                xcxGoods.setUrls(url);
                goodsList.add(xcxGoods);
            }
        }
        return goodsList;
    }

    public void add(XcxCollect xcxCollect) {
        xcxCollect.setCreateDate(new Date());
        xcxCollect.setDelFlag("0");
        xcxCollectDao.add(xcxCollect);
    }

    public void update(XcxCollect xcxCollect) {
        xcxCollect.setUpdateDate(new Date());
        xcxCollectDao.update(xcxCollect);
    }
}
