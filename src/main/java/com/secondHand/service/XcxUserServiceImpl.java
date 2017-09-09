package com.secondHand.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.secondHand.dao.XcxCollectDao;
import com.secondHand.dao.XcxFollowDao;
import com.secondHand.dao.XcxGoodsDao;
import com.secondHand.dao.XcxUserDao;
import com.secondHand.entity.XcxCollect;
import com.secondHand.entity.XcxFollow;
import com.secondHand.entity.XcxGoods;
import com.secondHand.entity.XcxUser;
import com.tool.JsonResult;
import com.tool.UserInfo;
import com.tool.XJZConstant;

/**
 * Created by XCA on 2017/5/22.
 */
@Service
public class XcxUserServiceImpl implements XcxUserService {

    @Autowired
    private XcxUserDao xcxUserDao;

    @Autowired
    private XcxCollectDao xcxCollectDao;

    @Autowired
    private XcxGoodsDao xcxGoodsDao;

    @Autowired
    private XcxFollowDao xcxFollowDao;

    public XcxUser get(String id) {
        return xcxUserDao.get(id);
    }

    public XcxUser getByMobile(String mobile) {
        return xcxUserDao.getByMobile(mobile);
    }

    public XcxUser getByOpenId(String openId) {
        return xcxUserDao.getByOpenId(openId);
    }

    @Override
    public JsonResult<XcxUser> isFollow(String concernOpenId, String currentOpenId,Integer pageNum) {
    	
    	JsonResult<XcxUser> jsonResult = new JsonResult<XcxUser>();
    	
    	if(StringUtils.isEmpty(concernOpenId)){
			 jsonResult.setCode(JsonResult.FAILD);
			 jsonResult.setMessage("被关注人的OPENID不能空;");
			 return jsonResult;
   	     }
   	 
		 if(StringUtils.isEmpty(currentOpenId)){
	   		 jsonResult.setCode(JsonResult.FAILD);
	   		 jsonResult.setMessage("当前用户OPENID不能空");
	   		 return jsonResult;
		 }
    	
		 XcxUser xcxUser = xcxUserDao.getByOpenId(concernOpenId);
        
        if(xcxUser == null){
        	jsonResult.setCode(JsonResult.FAILD);
            jsonResult.setMessage("用户不存在");
            return jsonResult;
        }
        
        XcxFollow xcxFollow = xcxFollowDao.getFollowLog(currentOpenId,concernOpenId);
        
        if (xcxFollow == null || XJZConstant.DELFLAG_TYPE_YES.equals(xcxFollow.getDelFlag())) {
        	xcxUser.setIsFollow(XJZConstant.USER_FOLLOW_TYPE_CANCEL_FOLLOW); 
        }else{
        	xcxUser.setIsFollow(XJZConstant.USER_FOLLOW_TYPE_FOLLOW);
        }
        
        XcxGoods xcxGoods = new XcxGoods();
        xcxGoods.setOpenId(concernOpenId);
        xcxGoods.setStartPage(pageNum);
        xcxGoods.setPageSize(5);
        
        List<XcxGoods> goodsList = xcxGoodsDao.getGoods(xcxGoods);
        
        for (XcxGoods goods : goodsList) {
        	
        	goods.setHeadImg(xcxUser.getHeadUrl());
        	
    		if(StringUtils.isEmpty(goods.getUrl())){
    			continue;
    		}
    		
    		if(goods.getUrl().indexOf(",") < 0){
    			continue;
    		}
    		
            goods.setUrls(Arrays.asList(goods.getUrl().split(",")));
            
         }
        
        xcxUser.setList(goodsList);
        xcxUser.setGoodsNum(xcxGoodsDao.getCountGoodsSumWithOpenId(concernOpenId));
        jsonResult.setCode(JsonResult.SUCCESS);
        jsonResult.setMessage("获取个人主页成功");
        jsonResult.setData(xcxUser);
        return jsonResult;
    }

    public void add(XcxUser xcxUser) {
        xcxUser.setCreateDate(new Date());
        xcxUser.setLookNewsDate(new Date());
        xcxUser.setStatus("0");
        xcxUser.setDelFlag("0");
        xcxUserDao.add(xcxUser);
    }

    public void update(XcxUser xcxUser) {
        xcxUser.setUpdateDate(new Date());
        xcxUserDao.update(xcxUser);
    }

    @Override
    public UserInfo getUserInfo(String openId) {
        UserInfo userInfo = new UserInfo();
        XcxUser xcxUser = xcxUserDao.getByOpenId(openId);
        if (xcxUser != null) {
            userInfo.setName(xcxUser.getUserName());
            userInfo.setOpenId(openId);
            userInfo.setFollowNum(xcxUser.getFollowNum());
            userInfo.setLookNum(xcxUser.getLookNum());
            userInfo.setIsNews(xcxUser.getIsNews());
            //我的货
            XcxGoods xcxGoods = new XcxGoods();
            xcxGoods.setOpenId(openId);
            xcxGoods.setPageSize(0);
            List<XcxGoods> goodsList = xcxGoodsDao.getGoods(xcxGoods);
            if (goodsList != null) {
                userInfo.setMyGoodsNum(goodsList.size());
            }
            //我的收藏
            List<XcxCollect> collectList = xcxCollectDao.getByOpenId(openId,"");
            if (collectList != null) {
                userInfo.setMyCollectNum(collectList.size());
            }
//            //我关注
//            List<XcxFollow> followList = xcxFollowDao.getByYou(openId);
//            if (followList != null) {
//                userInfo.setMyLookNum(followList.size());
//            }
        } else {
            return null;
        }
        return userInfo;
    }


//    @Override
//    public XcxUser getUserPage(String openId) {
//        XcxUser xcxUser = xcxUserDao.getByOpenId(openId);
//        if (xcxUser != null) {
//            XcxGoods xcxGoods = new XcxGoods();
//            xcxGoods.setOpenId(openId);
//            List<XcxGoods> list = xcxGoodsDao.getGoods(xcxGoods);
//            xcxUser.setList(list);
//        } else {
//            return null;
//        }
//        return xcxUser;
//    }
}
