package com.secondHand.controller;

import com.secondHand.entity.Store;
import com.secondHand.entity.StoreGoods;
import com.secondHand.entity.XcxPayLog;
import com.secondHand.entity.XcxSchool;
import com.secondHand.service.StoreGoodsService;
import com.secondHand.service.StoreService;
import com.secondHand.service.XcxPayLogService;
import com.secondHand.service.XcxSchoolService;
import com.thoughtworks.xstream.XStream;
import com.tool.JsonResult;
import com.tool.weixin.WXPay.PayResult;
import com.tool.weixin.entity.OrderReturnInfo;
import com.tool.weixin.entity.ReturnInfo;
import com.tool.weixin.tool.StreamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;

/**
 * 商店控制层
 */
@Controller
@RequestMapping(value = "/store")
public class StoreController {

    @Autowired
    private StoreGoodsService storeGoodsService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private XcxSchoolService xcxSchoolService;

    @Autowired
    private XcxPayLogService xcxPayLogService;

    /**
     * 商店
     */
    @RequestMapping(value = "/getStore",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getStore(){
        JsonResult<Store> jsonResult = new JsonResult<Store>();
        Store store = storeService.get();
        if (store != null) {
            jsonResult.setCode(0);
            jsonResult.setMessage("商店信息");
            jsonResult.setData(store);
        } else {
            jsonResult.setCode(1);
            jsonResult.setMessage("获取商店失败");
        }
        return jsonResult;
    }

    /**
     * 商品列表
     */
    @RequestMapping(value = "/goodsList",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getGoodsList(Integer pageNum) throws Exception {
        JsonResult<List> jsonResult = new JsonResult<List>();
        List<StoreGoods> list = storeGoodsService.getGoods(new StoreGoods(),pageNum);
        if (list.size() == 0) {
            jsonResult.setCode(1);
            jsonResult.setMessage("暂无商品");
        } else {
            jsonResult.setMessage("获取成功");
            jsonResult.setCode(0);
            jsonResult.setData(list);
        }
        return jsonResult;
    }

    /**
     * 关键字查询
     */
    @RequestMapping(value = "/getList" ,method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getStoreGoods(String keyWord,Integer pageNum) throws Exception {
        JsonResult<List> jsonResult = new JsonResult<List>();
        String key = doKeyWord(keyWord);
        StoreGoods storeGoods = storeGoodsService.goodsLike(key);
        List<StoreGoods> goodsList = storeGoodsService.getGoods(storeGoods,pageNum);
        if (goodsList.size() > 0) {
            jsonResult.setCode(0);
            jsonResult.setMessage("获取成功");
            jsonResult.setData(goodsList);
        } else {
            jsonResult.setCode(1);
            jsonResult.setMessage("获取失败");
        }
        return jsonResult;
    }

    @RequestMapping(value = "/view",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult viewGoods(String id){
        JsonResult<StoreGoods> jsonResult = new JsonResult<StoreGoods>();
        StoreGoods storeGoods = storeGoodsService.get(id);
        if (storeGoods != null) {
            jsonResult.setCode(0);
            jsonResult.setMessage("获取成功");
            jsonResult.setData(storeGoods);
        } else {
            jsonResult.setMessage("获取失败");
            jsonResult.setCode(1);
        }
        return jsonResult;
    }


    /**
     * 学校
     */
    @RequestMapping(value = "/getSchool",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getSchool(){
        JsonResult<List> jsonResult = new JsonResult<>();
        List<XcxSchool> list = xcxSchoolService.list();
        if (list != null) {
            jsonResult.setCode(0);
            jsonResult.setMessage("获取学校成功");
            jsonResult.setData(list);
        } else {
            jsonResult.setCode(1);
            jsonResult.setMessage("暂无学校");
        }
        return jsonResult;
    }

    /**
     * 学校名称
     */
    @RequestMapping(value = "/getSchoolName",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getSchoolName(){
        JsonResult<List> jsonResult = new JsonResult<>();
        List<String> school = xcxSchoolService.school();
        if (school != null) {
            jsonResult.setCode(0);
            jsonResult.setMessage("获取成功");
            jsonResult.setData(school);
        } else {
            jsonResult.setCode(1);
            jsonResult.setMessage("获取失败");
        }
        return jsonResult;
    }

    /**
     * 统一下单
     */
    @RequestMapping(value = "/pushOrder")
    @ResponseBody
    public JsonResult pushOrder(String goodsId,String openId) throws IllegalAccessException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, IOException {
        JsonResult<String> jsonResult = new JsonResult<String>();
        String prepayId = storeGoodsService.pushOrder(goodsId, openId);
        if (prepayId != null && !"".equals(prepayId)) {
            jsonResult.setCode(0);
            jsonResult.setMessage("下单成功");
            jsonResult.setData(prepayId);
        } else {
            jsonResult.setCode(1);
            jsonResult.setMessage("下单失败");
        }
        return jsonResult;
    }

    /**
     * 再次签名
     */
    @RequestMapping(value = "/signOrder")
    @ResponseBody
    public JsonResult signOrder(String prepayId) throws IllegalAccessException {
        JsonResult<String> jsonResult = new JsonResult<String>();
        String jsonSignString = storeGoodsService.signOrder(prepayId);
        if (jsonSignString != null && !"".equals(jsonSignString)) {
            jsonResult.setCode(0);
            jsonResult.setMessage("再次签名成功");
            jsonResult.setData(jsonSignString);
        } else {
            jsonResult.setCode(1);
            jsonResult.setMessage("再次签名失败");
        }
        return jsonResult;
    }

    /**
     * 订单完成 验证与生成
     */
    @RequestMapping(value = "/addOrder")
    public void addOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String reqParams = StreamUtil.read(request.getInputStream());
        XStream xStream = new XStream();
        xStream.alias("xml", ReturnInfo.class);
        ReturnInfo returnInfo = (ReturnInfo)xStream.fromXML(reqParams);
        String orderNum = returnInfo.getOut_trade_no();
        XcxPayLog xcxPayLog = xcxPayLogService.getByOrderNum(orderNum);
        if (xcxPayLog != null) {
            xcxPayLog.setStatus("1");
            xcxPayLogService.update(xcxPayLog);
            StringBuffer sb = new StringBuffer("<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>");
            response.getWriter().append(sb.toString());
            System.out.println("回调显示成功");
        }
    }

    /**
     * 变量
     */
    public String doKeyWord(String keyword){
        int length = keyword.length();
        char[] keyArr = new char[length << 1];
        for (int i=0,j=0;i<length;++i,j=i<<1) {
            keyArr[j] = keyword.charAt(i);
            keyArr[1+j] = '%';
        }
        keyword = "%"+new String(keyArr);
        return keyword;
    }
}
