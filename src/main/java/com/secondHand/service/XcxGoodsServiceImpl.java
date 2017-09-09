package com.secondHand.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.secondHand.dao.XcxGoodsDao;
import com.secondHand.dao.XcxUserDao;
import com.secondHand.entity.XcxGoods;
import com.secondHand.entity.XcxUser;
import com.tool.ListUtil;

/**
 * Created by XCA on 2017/5/22.
 */
@Service
public class XcxGoodsServiceImpl implements XcxGoodsService{

    @Autowired
    private XcxGoodsDao xcxGoodsDao;

    @Autowired
    private XcxUserDao xcxUserDao;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    public XcxGoods get(String id) {
        XcxGoods xcxGoods = xcxGoodsDao.get(id);
        if (xcxGoods != null) {
            int lookNum = xcxGoods.getLookNum();
            ++lookNum;
            xcxGoods.setLookNum(lookNum);
            xcxGoodsDao.update(xcxGoods);
            
            String[] urls = xcxGoods.getUrl().split(",");
            List<String> list = new ArrayList<>();
            list = Arrays.asList(urls);
            xcxGoods.setUrls(list);
            XcxUser xcxUser = xcxUserDao.getByOpenId(xcxGoods.getOpenId());
            if (xcxUser != null) {
                xcxGoods.setHeadImg(xcxUser.getHeadUrl());
            }
        }
        return xcxGoods;
    }

    public List<XcxGoods> getGoods(XcxGoods xcxGoods,int pageNum) throws Exception {
    	
    	if(xcxGoods == null){
    		return null;
    	}
    	
    	xcxGoods.setStartPage(pageNum);
    		
        List<XcxGoods> goodsList = xcxGoodsDao.getGoods(xcxGoods);
        
        XcxUser xcxUser = null;
        
        for (XcxGoods goods : goodsList) {
        	
        	xcxUser = xcxUserDao.getByOpenId(goods.getOpenId());
            
            if (xcxUser != null) {
                goods.setHeadImg(xcxUser.getHeadUrl());
            }
        	
    		if(StringUtils.isEmpty(goods.getUrl())){
    			continue;
    		}
    		
    		if(goods.getUrl().indexOf(",") < 0){
    			continue;
    		}
    		
            goods.setUrls(Arrays.asList(goods.getUrl().split(",")));
            
         }
        return goodsList;
    }

    @Override
    public List<XcxGoods> getMyGoods(String openId,Integer pageNum) throws Exception {
        List<XcxGoods> list = xcxGoodsDao.getMyGoods(openId);
        List<XcxGoods> goodsList = ListUtil.page(pageNum,5,list);
        if (goodsList != null ) {
            for (XcxGoods goods : goodsList) {
                String[] urls = goods.getUrl().split(",");
                List<String> urlList = new ArrayList<>();
                urlList = Arrays.asList(urls);
                goods.setUrls(urlList);
                System.out.print("");
                XcxUser xcxUser = xcxUserDao.getByOpenId(goods.getOpenId());
                if (xcxUser != null) {
                    goods.setHeadImg(xcxUser.getHeadUrl());
                }
            }
        }
        return goodsList;
    }

    public void add(XcxGoods xcxGoods) {
        xcxGoods.setLookNum(0);
        xcxGoods.setZanNum(0);
        xcxGoods.setAssessNum(0);
        xcxGoods.setStatus("审核中");
        xcxGoods.setDelFlag("0");
        if ("转让".equals(xcxGoods.getType())) {
            xcxGoods.setType("0");
        } else if ("求购".equals(xcxGoods.getType())) {
            xcxGoods.setType("1");
        }
        xcxGoodsDao.add(xcxGoods);
    }

    public void update(XcxGoods xcxGoods) {
        xcxGoodsDao.update(xcxGoods);
    }

    public XcxGoods goodsLike(String key) {
        XcxGoods xcxGoods = new XcxGoods();
        xcxGoods.setMobile(key);
        xcxGoods.setUserName(key);
        xcxGoods.setGoodsName(key);
        xcxGoods.setSchool(key);
        xcxGoods.setDescription(key);
        xcxGoods.setDegree(key);
        return xcxGoods;
    }

    public boolean addZanNum(String id) {
        XcxGoods xcxGoods = xcxGoodsDao.get(id);
        if (xcxGoods != null) {
            //赞商品
            int gNum = xcxGoods.getZanNum();
            ++gNum;
            xcxGoods.setZanNum(gNum);
            xcxGoodsDao.update(xcxGoods);
            return true;
        }
        return false;
    }

    @Override
    public boolean addAssessNum(String id) {
        XcxGoods xcxGoods = xcxGoodsDao.get(id);
        if (xcxGoods != null) {
            int aNum = xcxGoods.getAssessNum();
            ++aNum;
            xcxGoods.setAssessNum(aNum);
            xcxGoodsDao.update(xcxGoods);
            return true;
        }
        return false;
    }

    public XcxGoods doUploadGoods(String openId,String mobile, String goodsName, String school, String description,
                                  String price, String oldPrice, String degree, String type,String[] pics) {
      
        	XcxUser xcxUser = xcxUserDao.getByOpenId(openId);
            if (xcxUser == null) {
            	return null;
            }
            
            XcxGoods xcxGoods = new XcxGoods();
            xcxGoods.setOpenId(xcxUser.getOpenId());
            xcxGoods.setUserName(xcxUser.getUserName());
            xcxGoods.setMobile(mobile);
            xcxGoods.setGoodsName(goodsName);
            xcxGoods.setSchool(school);
            xcxGoods.setDescription(description);
            xcxGoods.setPrice(price);
            xcxGoods.setOldPrice(oldPrice);
            xcxGoods.setDegree(degree);
            xcxGoods.setType(type);
            String goodsUrl = "";
            
            for (String pic:pics) {
            	if(StringUtils.isEmpty(pic)){
            		continue;
            	}
                goodsUrl+=pic+",";
            }
            xcxGoods.setUrl(goodsUrl);
        return xcxGoods;
    }

	@Override
	public Integer getCountGoodsSumWithOpenId(String openId) {
		return xcxGoodsDao.getCountGoodsSumWithOpenId(openId);
	}

}
