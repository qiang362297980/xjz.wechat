package com.secondHand.controller;

import com.secondHand.entity.*;
import com.secondHand.service.*;
import com.tool.AssessInfo;
import com.tool.JsonResult;
import com.tool.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * 我家控制层
 */
@Controller
@RequestMapping(value = "/myHome")
public class MyHomeController {

    @Autowired
    private XcxGoodsService xcxGoodsService;

    @Autowired
    private XcxCollectService xcxCollectService;

    @Autowired
    private XcxAdviceService xcxAdviceService;

    @Autowired
    private XcxUserService xcxUserService;

    @Autowired
    private XcxPayLogService xcxPayLogService;

    @Autowired
    private XcxFollowService xcxFollowService;

    @Autowired
    private XcxAssessService xcxAssessService;

    /**
     * 信息
     */
    @RequestMapping(value = "/userInfo",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getUserInfo(String openId){
        JsonResult<UserInfo> jsonResult = new JsonResult<UserInfo>();
        UserInfo userInfo = xcxUserService.getUserInfo(openId);
        if (userInfo != null) {
            jsonResult.setCode(0);
            jsonResult.setMessage("获取信息成功");
            jsonResult.setData(userInfo);
        } else {
            jsonResult.setCode(9);
            jsonResult.setMessage("用户不存在");
        }
        return jsonResult;
    }

    /**
     * 个人主页
     */
    @RequestMapping(value = "/userPage",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<XcxUser> userPage(String openId,String uOpenId,Integer pageNum) throws Exception {
        return xcxUserService.isFollow(openId,uOpenId,pageNum);
       
    }

    /**
     * 我发布的
     */
    @RequestMapping(value = "/myGoods",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getMyGoods(String openId,Integer pageNum) throws Exception {
        JsonResult<List> jsonResult = new JsonResult<List>();
        XcxUser xcxUser = xcxUserService.getByOpenId(openId);
        if (xcxUser != null) {
            XcxGoods xcxGoods = new XcxGoods();
            xcxGoods.setOpenId(openId);
            List<XcxGoods> list = xcxGoodsService.getMyGoods(openId,pageNum);
            if (list != null) {
                jsonResult.setCode(0);
                jsonResult.setMessage("获取成功");
                jsonResult.setData(list);
            } else {
                jsonResult.setCode(1);
                jsonResult.setMessage("暂无发布");
            }
        } else {
            jsonResult.setCode(9);
            jsonResult.setMessage("用户不存在");
        }
        return jsonResult;
    }

    /**
     * 状态变化
     */
    @RequestMapping(value = "/delMyGoods",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult delGoods(String goodsId,String status){
        JsonResult jsonResult = new JsonResult();
        XcxGoods xcxGoods = xcxGoodsService.get(goodsId);
        if (xcxGoods != null) {
            xcxGoods.setStatus(status);
            xcxGoodsService.update(xcxGoods);
            jsonResult.setCode(0);
            jsonResult.setMessage("变更商品成功");
        } else {
            jsonResult.setCode(1);
            jsonResult.setMessage("查询商品失败");
        }
        return jsonResult;
    }

    /**
     * 我收藏的
     */
    @RequestMapping(value = "/myCollect",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getMyCollect(String openId){
        JsonResult<List> jsonResult = new JsonResult<List>();
        XcxUser xcxUser = xcxUserService.getByOpenId(openId);
        if (xcxUser != null) {
            List<XcxGoods> xcxCollects = xcxCollectService.getByOpenId(openId,"");
            if (xcxCollects.size() > 0) {
                jsonResult.setCode(0);
                jsonResult.setMessage("获取成功");
                jsonResult.setData(xcxCollects);
            } else {
                jsonResult.setCode(1);
                jsonResult.setMessage("暂无收藏");
            }
        } else {
            jsonResult.setCode(9);
            jsonResult.setMessage("用户不存在");
        }
        return jsonResult;
    }

    /**
     * 取消收藏
     */
    @RequestMapping(value = "/delCollect")
    @ResponseBody
    public JsonResult delCollect(String openId,String goodsId){
        JsonResult jsonResult = new JsonResult();
        XcxCollect collect = xcxCollectService.getCollect(openId, goodsId);
        if (collect != null) {
            collect.setDelFlag("1");
            xcxCollectService.update(collect);
            jsonResult.setCode(0);
            jsonResult.setMessage("取消收藏成功");
        } else {
            jsonResult.setCode(1);
            jsonResult.setMessage("查询收藏失败");
        }
        return jsonResult;
    }

    /**
     * 我关注的
     */
    @RequestMapping(value = "/myFollow")
    @ResponseBody
    public JsonResult myFollow(String openId){
        JsonResult<List> jsonResult = new JsonResult<List>();
        XcxUser xcxUser = xcxUserService.getByOpenId(openId);
        if (xcxUser != null) {
            List<XcxUser> users = xcxFollowService.myFollowMan(openId);
            if (users != null && users.size() > 0) {
                jsonResult.setCode(0);
                jsonResult.setMessage("获取关注列表成功");
                jsonResult.setData(users);
            } else {
                jsonResult.setCode(1);
                jsonResult.setMessage("暂无关注");
            }
        } else {
            jsonResult.setCode(9);
            jsonResult.setMessage("用户不存在");
        }
        return jsonResult;
    }

    /**
     * 反馈
     */
    @RequestMapping(value = "/addAdvice",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addAdvice(String openId,String context){
        JsonResult jsonResult = new JsonResult();
        XcxUser xcxUser = xcxUserService.getByOpenId(openId);
        if (xcxUser != null) {
            XcxAdvice xcxAdvice = new XcxAdvice();
            xcxAdvice.setOpenId(openId);
            xcxAdvice.setContext(context);
            xcxAdviceService.add(xcxAdvice);
            jsonResult.setCode(0);
            jsonResult.setMessage("反馈成功");
        } else {
            jsonResult.setCode(9);
            jsonResult.setMessage("用户不存在");
        }
        return jsonResult;
    }

    /**
     * 修改地址
     */
    @RequestMapping(value = "/updateArea",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult updateArea(String area,String openId){
        JsonResult jsonResult = new JsonResult();
        XcxUser xcxUser = xcxUserService.getByOpenId(openId);
        if (xcxUser != null) {
            xcxUser.setArea(area);
            xcxUserService.update(xcxUser);
            jsonResult.setCode(0);
            jsonResult.setMessage("修改成功");
        } else {
            jsonResult.setCode(1);
            jsonResult.setMessage("查询用户失败");
        }
        return jsonResult;
    }

    /**
     * 交易记录
     */
    @RequestMapping(value = "/payLog")
    @ResponseBody
    public JsonResult myPayLog(String openId){
        JsonResult<List> jsonResult = new JsonResult<List>();
        List<XcxPayLog> list = xcxPayLogService.getByOpenId(openId);
        if (list != null) {
            jsonResult.setCode(0);
            jsonResult.setMessage("获取成功");
            jsonResult.setData(list);
        } else {
            jsonResult.setCode(1);
            jsonResult.setMessage("暂无交易记录");
        }
        return jsonResult;
    }

    /**
     * 订单详情
     */
    @RequestMapping(value = "/payInfo")
    @ResponseBody
    public JsonResult payInfo(String id){
        JsonResult<XcxPayLog> jsonResult = new JsonResult<XcxPayLog>();
        XcxPayLog xcxPayLog = xcxPayLogService.getPayInfo(id);
        if (xcxPayLog != null) {
            jsonResult.setCode(0);
            jsonResult.setMessage("查询成功");
            jsonResult.setData(xcxPayLog);
        } else {
            jsonResult.setCode(1);
            jsonResult.setMessage("查询失败");
        }
        return jsonResult;
    }

    /**
     * 我的消息
     */
    @RequestMapping(value = "/myNews")
    @ResponseBody
    public JsonResult myNews(String openId,Integer pageNum) throws Exception {
       JsonResult<List> jsonResult = new JsonResult<List>();
        XcxUser xcxUser = xcxUserService.getByOpenId(openId);
        if (xcxUser != null) {
                List<AssessInfo> assessInfo = xcxAssessService.getMyNews(openId,pageNum);
                if (assessInfo != null) {
                    jsonResult.setCode(0);
                    jsonResult.setMessage("获取消息成功");
                    jsonResult.setData(assessInfo);
                } else {
                    jsonResult.setCode(1);
                    jsonResult.setMessage("暂无消息或已拉到底部");
                }
                xcxUser.setIsNews("0");
                xcxUserService.update(xcxUser);
        }
        return jsonResult;
    }

    /**
     * 修改商品
     */
    @RequestMapping(value = "/updateGoods")
    @ResponseBody
    public JsonResult upGoods(String goodsId){
        JsonResult<XcxGoods> jsonResult = new JsonResult<XcxGoods>();
        XcxGoods xcxGoods = xcxGoodsService.get(goodsId);
        if (xcxGoods != null) {
            jsonResult.setCode(0);
            jsonResult.setMessage("获取商品成功");
            jsonResult.setData(xcxGoods);
        } else {
            jsonResult.setCode(1);
            jsonResult.setMessage("获取商品失败");
        }
        return jsonResult;
    }
}
