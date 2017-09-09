package com.secondHand.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.secondHand.dao.XcxFollowDao;
import com.secondHand.dao.XcxGoodsDao;
import com.secondHand.dao.XcxUserDao;
import com.secondHand.entity.XcxFollow;
import com.secondHand.entity.XcxUser;
import com.tool.JsonResult;
import com.tool.XJZConstant;

/**
 * Created by Administrator on 2017/6/9.
 */
@Service
public class XcxFollowServiceImpl implements XcxFollowService {

    @Autowired
    private XcxFollowDao xcxFollowDao;

    @Autowired
    private XcxUserDao xcxUserDao;

    @Autowired
    private XcxGoodsDao xcxGoodsDao;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public XcxFollow get(String id) {
        return xcxFollowDao.get(id);
    }

    @Override
    public List<XcxFollow> getByYou(String uOpenId) {
        List<XcxFollow> list = xcxFollowDao.getByYou(uOpenId);
        for (XcxFollow follow : list) {
            follow.setCreateTime(sdf.format(follow.getCreateDate()));
        }
        return list;
    }

    @Override
    public void add(XcxFollow xcxFollow) {
        xcxFollow.setCreateDate(new Date());
        xcxFollow.setDelFlag("0");
        xcxFollowDao.add(xcxFollow);
    }

    /**
     * concernOpenId  受关注用户OPENID
     * currentOpenId  当前用户ID
     * 
     * 
     */
    @Override
    public JsonResult<Object> doFollow(String concernOpenId,String currentOpenId,  String type) {
    	JsonResult<Object> jsonResult = new JsonResult<Object>();
    	
    	 if(StringUtils.isEmpty(type)){
    		 jsonResult.setCode(JsonResult.FAILD);
    		 jsonResult.setMessage("取消关注类型不能空");
         	return jsonResult;
         }
    	 
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
    	 
    	 if(concernOpenId.equals(currentOpenId)){
    		 jsonResult.setCode(JsonResult.FAILD);
    		 jsonResult.setMessage("自己不能关注自己");
    		 return jsonResult;
    	 }
    	 
        XcxUser concernUser = xcxUserDao.getByOpenId(concernOpenId);
        
        if(concernUser == null){
        	 jsonResult.setCode(JsonResult.FAILD);
    		 jsonResult.setMessage("被关注用户不存在;concernOpenId:"+concernOpenId);
    		 return jsonResult;
        }
        
        XcxUser currentUser = xcxUserDao.getByOpenId(currentOpenId);
        
        if(currentUser == null){
        	 jsonResult.setCode(JsonResult.FAILD);
    		 jsonResult.setMessage("当前用户用户不存在;currentOpenId:"+currentOpenId);
    		 return jsonResult;
        }
        
        //查询用户已发布并通过商品数量； 优化：增加用户数据表：增加以下字段总发布数量，已审核通过商品数量，在售商品数量，已售出商品数量，已下架商品数量
        concernUser.setGoodsNum(xcxGoodsDao.getCountGoodsSumWithOpenId(concernOpenId));
        
        XcxFollow xcxFollow = xcxFollowDao.getFollowLog(currentOpenId,concernOpenId);
        
        if(xcxFollow == null){
		   xcxFollow = new XcxFollow();
		   xcxFollow.setuOpenId(currentOpenId);
		   xcxFollow.setaOpenId(concernOpenId);
		   xcxFollowDao.add(xcxFollow);
		   xcxUserDao.increaseFollowNumWithOpenId(concernOpenId);
		   concernUser.setIsFollow(XJZConstant.USER_FOLLOW_TYPE_FOLLOW);
		   concernUser.setFollowNum(concernUser.getFollowNum()+1);
		   jsonResult.setCode(JsonResult.SUCCESS);
		   jsonResult.setData(concernUser);
		   jsonResult.setMessage("SUCCESS");
	       return jsonResult;
        }
        
        
        //未关注则进行关注操作
        if( XJZConstant.DELFLAG_TYPE_YES.equals(xcxFollow.getDelFlag())){
        	xcxUserDao.increaseFollowNumWithOpenId(concernOpenId);
        	xcxFollowDao.increaseFollowWithOpenId(currentOpenId,concernOpenId);
        	concernUser.setIsFollow(XJZConstant.USER_FOLLOW_TYPE_FOLLOW);
        	concernUser.setFollowNum(concernUser.getFollowNum()+1);
        }else{
        	 xcxFollowDao.reduceFollowWithOpenId(currentOpenId,concernOpenId);
             xcxUserDao.reduceFollowNumWithOpenId(concernOpenId);
             concernUser.setIsFollow(XJZConstant.USER_FOLLOW_TYPE_CANCEL_FOLLOW);
             concernUser.setFollowNum(concernUser.getFollowNum()-1);
        }
        
		   jsonResult.setCode(JsonResult.SUCCESS);
		   jsonResult.setData(concernUser);
		   jsonResult.setMessage("SUCCESS");
		   return jsonResult;
    }

    @Override
    public List<XcxUser> myFollowMan(String openId) {
        List<XcxFollow> follows = xcxFollowDao.getByYou(openId);
        List<XcxUser> users = new ArrayList<>();
        if (follows != null) {
            for (XcxFollow follow : follows) {
                XcxUser xcxUser = xcxUserDao.getByOpenId(follow.getaOpenId());
                if (xcxUser != null) {
                    int goodsNum = xcxGoodsDao.goodsNum(xcxUser.getOpenId());
                    xcxUser.setGoodsNum(goodsNum);
                    users.add(xcxUser);
                }
            }
        }
        return users;
    }
    
}
