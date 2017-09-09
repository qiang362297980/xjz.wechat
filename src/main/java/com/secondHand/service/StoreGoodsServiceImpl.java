package com.secondHand.service;

import com.alibaba.fastjson.JSONObject;
import com.secondHand.dao.StoreGoodsDao;
import com.secondHand.dao.XcxPayLogDao;
import com.secondHand.entity.StoreGoods;
import com.secondHand.entity.XcxPayLog;
import com.thoughtworks.xstream.XStream;
import com.tool.ListUtil;
import com.tool.TemplateConfig;
import com.tool.weixin.entity.OrderInfo;
import com.tool.weixin.entity.OrderReturnInfo;
import com.tool.weixin.entity.SignInfo;
import com.tool.weixin.tool.Configure;
import com.tool.weixin.tool.HttpRequest;
import com.tool.weixin.tool.RandomStringGenerator;
import com.tool.weixin.tool.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by XCA on 2017/5/23.
 */
@Service
public class StoreGoodsServiceImpl implements StoreGoodsService {

    @Autowired
    private StoreGoodsDao storeGoodsDao;

    @Autowired
    private XcxPayLogDao xcxPayLogDao;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static int pageSize = Integer.valueOf(TemplateConfig.getValue("pageSize"));

    public StoreGoods get(String id) {
        StoreGoods storeGoods = storeGoodsDao.get(id);
        if (storeGoods == null) {
        	return null;
        }
        
        storeGoods.setCreateTime(sdf.format(storeGoods.getCreateDate()));
            
        if(storeGoods.getPicUrl().indexOf(",") < 0){
			return storeGoods;
		}	
            
        String[] pics = storeGoods.getPicUrl().split(",");
        
        List<String> picList = new ArrayList<String>();
        for (String pic:pics) {
        	if(pic == null || pic.length() <= 0){
        		continue;
        	}
        	
        	picList.add(pic.replaceAll("\\\\", "/"));
        }
        storeGoods.setPic(picList);
        return storeGoods;
    }

    public List<StoreGoods> getGoods(StoreGoods storeGoods,int pageNum) throws Exception {
        List<StoreGoods> list = storeGoodsDao.getGoods(storeGoods);
        
        if (list == null) {
        	return null;
        }
        	
    	for (StoreGoods goods : list) {
    		
    		if(goods.getViewPicUrl().indexOf(",") < 0){
    			continue;
    		}	
    		  
             goods.setViewPic(goods.getViewPicUrl().split(",")[0].replaceAll("\\\\", "/"));
		}
              
       
        return list;
    }

    public void add(StoreGoods storeGoods) {
        storeGoods.setCreateDate(new Date());
        storeGoods.setDelFlag("0");
        storeGoodsDao.add(storeGoods);
    }

    public void update(StoreGoods storeGoods) {
        storeGoodsDao.update(storeGoods);
    }

    public StoreGoods goodsLike(String key) {
        StoreGoods storeGoods = new StoreGoods();
        if (!"".equals(key) && key != null) {
            storeGoods.setName(key);
            storeGoods.setAddress(key);
            storeGoods.setDegree(key);
            storeGoods.setDescription(key);
        }
        return storeGoods;
    }

    @Override
    public String pushOrder(String goodsId, String openId) throws IllegalAccessException, UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String prepay_id = "";
        StoreGoods goods = storeGoodsDao.get(goodsId);
        if (goods != null) {
            OrderInfo order = new OrderInfo();
            order.setAppid(TemplateConfig.getValue("APPID"));
            order.setMch_id(TemplateConfig.getValue("MCHID"));
            order.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
            String goodsName = goods.getName();
            order.setBody(goodsName);
            Date date = new Date();
            String time = String.valueOf(date.getTime());
            order.setOut_trade_no(time);
            int price = (int) (Float.valueOf(goods.getPrice())*100);
            int yunFee = (int) (Float.valueOf(goods.getYunFee())*100);
            order.setTotal_fee(price+yunFee);
            order.setSpbill_create_ip(TemplateConfig.getValue("spbillCreateIp"));
            order.setNotify_url(TemplateConfig.getValue("notifyUrl"));
            order.setTrade_type("JSAPI");
            order.setOpenid(openId);
            order.setSign_type("MD5");

            //生成本地订单
            XcxPayLog xcxPayLog = new XcxPayLog();
            xcxPayLog.setOpenId(openId);
            xcxPayLog.setOrderNum(time);
            xcxPayLog.setGoodsId(goodsId);
            xcxPayLog.setGoodsName(goods.getName());
            xcxPayLog.setMoney(String.valueOf(price+yunFee));
            xcxPayLog.setPayType("1");
            xcxPayLog.setStatus("0");
            xcxPayLog.setCreateDate(new Date());
            xcxPayLog.setDelFlag("0");
            xcxPayLogDao.add(xcxPayLog);

            //生成签名
            String sign = Signature.getSign(order);
            order.setSign(sign);
            String result = HttpRequest.sendPost("https://api.mch.weixin.qq.com/pay/unifiedorder", order);
            System.out.println(result);
            XStream xStream = new XStream();
            xStream.alias("xml", OrderReturnInfo.class);

            OrderReturnInfo returnInfo = (OrderReturnInfo)xStream.fromXML(result);
            prepay_id = returnInfo.getPrepay_id();
        }
        return prepay_id;
    }

    @Override
    public String signOrder(String repayId) throws IllegalAccessException {
        SignInfo signInfo = new SignInfo();
        signInfo.setAppId(Configure.getAppID());
        long time = System.currentTimeMillis()/1000;
        signInfo.setTimeStamp(String.valueOf(time));
        signInfo.setNonceStr(RandomStringGenerator.getRandomStringByLength(32));
        signInfo.setRepay_id("prepay_id="+repayId);
        signInfo.setSignType("MD5");
        //生成签名
        String sign = Signature.getSign(signInfo);

        JSONObject json = new JSONObject();
        json.put("timeStamp", signInfo.getTimeStamp());
        json.put("nonceStr", signInfo.getNonceStr());
        json.put("package", signInfo.getRepay_id());
        json.put("signType", signInfo.getSignType());
        json.put("paySign", sign);
        return json.toJSONString();
    }
}
