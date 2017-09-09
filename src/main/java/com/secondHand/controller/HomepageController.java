package com.secondHand.controller;

import com.secondHand.entity.XcxCollect;
import com.secondHand.entity.XcxGoods;
import com.secondHand.entity.XcxGoodsAssess;
import com.secondHand.entity.XcxPic;
import com.secondHand.entity.XcxUser;
import com.secondHand.service.XcxAssessService;
import com.secondHand.service.XcxCollectService;
import com.secondHand.service.XcxFollowService;
import com.secondHand.service.XcxGoodsService;
import com.secondHand.service.XcxPicService;
import com.secondHand.service.XcxSmsCodeService;
import com.secondHand.service.XcxUserService;
import com.tool.FileUploadServlet;
import com.tool.JsonResult;
import com.tool.weixin.UserInfoData;
import com.tool.weixin.WXLoginService;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping({"/homepage"})
public class HomepageController
{
  private Logger log = Logger.getLogger(HomepageController.class);

  @Autowired
  private XcxUserService xcxUserService;

  @Autowired
  private XcxGoodsService xcxGoodsService;

  @Autowired
  private XcxAssessService assessService;

  @Autowired
  private XcxCollectService xcxCollectService;

  @Autowired
  private XcxSmsCodeService xcxSmsCodeService;

  @Autowired
  private XcxPicService xcxPicService;

  @Autowired
  private XcxFollowService xcxFollowService;

  @RequestMapping(value={"/login"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public JsonResult loginCheck(String code, String avatarUrl, String nickName, String gender) { JsonResult jsonResult = new JsonResult();

    UserInfoData userInfo = WXLoginService.checkLogin(code);
    if (userInfo != null) {
      XcxUser xcxUser = this.xcxUserService.getByOpenId(userInfo.getOpenId());
      if (xcxUser == null) {
        xcxUser = new XcxUser();
        xcxUser.setOpenId(userInfo.getOpenId());
        xcxUser.setUserName(nickName);
        xcxUser.setHeadUrl(avatarUrl);
        xcxUser.setSex(gender);
        this.xcxUserService.add(xcxUser);
        jsonResult.setCode(0);
        jsonResult.setMessage("新用户");
        jsonResult.setData(userInfo.getOpenId());
      } else if ("0".equals(xcxUser.getStatus())) {
        jsonResult.setCode(1);
        jsonResult.setMessage("老用户");
        jsonResult.setData(userInfo.getOpenId());
        xcxUser.setUserName(nickName);
        xcxUser.setHeadUrl(avatarUrl);
        xcxUser.setSex(gender);
        this.xcxUserService.update(xcxUser);
      } else {
        jsonResult.setCode(2);
        jsonResult.setMessage("黑名单用户");
      }
    } else {
      jsonResult.setCode(4);
      jsonResult.setMessage("登录失败");
    }
    return jsonResult;
  }

  @RequestMapping(value={"/homepagePic"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public JsonResult homepagePic()
  {
    JsonResult jsonResult = new JsonResult();
    XcxPic xcxPic = new XcxPic();
    xcxPic.setType("1");
    List list = this.xcxPicService.getList(xcxPic);
    if (list != null) {
      jsonResult.setCode(0);
      jsonResult.setMessage("获取成功");
      jsonResult.setData(list);
    } else {
      jsonResult.setCode(1);
      jsonResult.setMessage("暂无轮播图片");
    }
    return jsonResult;
  }

  @RequestMapping(value={"/goodsList"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public JsonResult goodsList(Integer pageNum) throws Exception
  {
    JsonResult jsonResult = new JsonResult();
    List list = this.xcxGoodsService.getGoods(new XcxGoods(), pageNum.intValue());
    if (list == null) {
      jsonResult.setCode(1);
      jsonResult.setMessage("暂无商品");
    } else {
      jsonResult.setCode(0);
      jsonResult.setMessage("获取成功");
      jsonResult.setData(list);
    }
    return jsonResult;
  }

  @RequestMapping(value={"/addGoods"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public JsonResult addGoods(String goodsId, String openId, String mobile, String goodsName, String school, String description, String price, String oldPrice, String degree, String type, String[] pics)
  {
    JsonResult jsonResult = new JsonResult();

    XcxGoods xcxGoods = 
      this.xcxGoodsService.doUploadGoods(
      openId, mobile, goodsName, school, description, price, 
      oldPrice, degree, type, pics);
    if (xcxGoods == null) {
      jsonResult.setCode(1);
      jsonResult.setMessage("操作失败");
      return jsonResult;
    }

    if (StringUtils.isEmpty(goodsId)) {
      this.xcxGoodsService.add(xcxGoods);
    } else {
      xcxGoods.setId(goodsId);
      this.xcxGoodsService.update(xcxGoods);
    }
    jsonResult.setCode(0);
    jsonResult.setMessage("操作成功");

    return jsonResult;
  }

  @RequestMapping({"/pic"})
  public void pic(MultipartHttpServletRequest request, HttpServletResponse response)
  {
    FileUploadServlet.doPost(request, response);
  }

  @RequestMapping(value={"/search"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public JsonResult searchGoods(String keyword, Integer pageNum) throws Exception
  {
    JsonResult jsonResult = new JsonResult();
    String key = doKeyWord(keyword);
    XcxGoods xcxGoods = this.xcxGoodsService.goodsLike(key);
    List goodsList = this.xcxGoodsService.getGoods(xcxGoods, pageNum.intValue());
    if (goodsList != null) {
      jsonResult.setCode(0);
      jsonResult.setMessage("查询成功");
      jsonResult.setData(goodsList);
    } else {
      jsonResult.setCode(1);
      jsonResult.setMessage("查询无结果");
    }
    return jsonResult;
  }

  @RequestMapping(value={"/look"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public JsonResult lookGoods(String openId, String goodsId)
  {
    JsonResult jsonResult = new JsonResult();

    XcxUser xcxUser = this.xcxUserService.getByOpenId(openId);
    if (xcxUser == null) {
      jsonResult.setCode(9);
      jsonResult.setMessage("用户不存在");
      return jsonResult;
    }

    XcxGoods xcxGoods = this.xcxGoodsService.get(goodsId);

    if (xcxGoods == null) {
      jsonResult.setCode(2);
      jsonResult.setMessage("获取失败");
      return jsonResult;
    }

    List xcxCollect = this.xcxCollectService.getByOpenId(openId, xcxGoods.getId());
    if ((xcxCollect == null) || (xcxCollect.size() == 0)) {
      jsonResult.setCode(1);
      jsonResult.setMessage("获取成功,未收藏");
      jsonResult.setData(xcxGoods);
    }

    jsonResult.setCode(0);
    jsonResult.setMessage("获取成功,已收藏");
    jsonResult.setData(xcxGoods);
    return jsonResult;
  }

  @RequestMapping(value={"/getGoodById"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public JsonResult getGoodById(String openId, String goodsId) {
    JsonResult jsonResult = new JsonResult();

    if (StringUtils.isEmpty(openId)) {
      jsonResult.setCode(9);
      jsonResult.setMessage("用户不存在");
      return jsonResult;
    }

    XcxUser xcxUser = this.xcxUserService.getByOpenId(openId);
    if (xcxUser == null) {
      jsonResult.setCode(9);
      jsonResult.setMessage("用户不存在");
      return jsonResult;
    }

    XcxGoods xcxGoods = this.xcxGoodsService.get(goodsId);

    if (xcxGoods == null) {
      jsonResult.setCode(2);
      jsonResult.setMessage("获取失败");
      return jsonResult;
    }

    if (!openId.equals(xcxGoods.getOpenId())) {
      jsonResult.setCode(2);
      jsonResult.setMessage("获取失败");
      return jsonResult;
    }

    jsonResult.setCode(0);
    jsonResult.setMessage("获取成功");
    jsonResult.setData(xcxGoods);
    return jsonResult;
  }

  @RequestMapping(value={"/follow"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public JsonResult<Object> follow(String concernOpenId, String currentOpenId, String _type)
  {
    return this.xcxFollowService.doFollow(concernOpenId, currentOpenId, _type);
  }

  @RequestMapping(value={"/collect"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public JsonResult collect(String openId, String goodId)
  {
    JsonResult jsonResult = new JsonResult();
    XcxUser xcxUser = this.xcxUserService.getByOpenId(openId);
    if (xcxUser != null) {
      List list = this.xcxCollectService.getByOpenId(openId, goodId);
      if (list.size() == 0) {
        XcxCollect xcxCollect = new XcxCollect();
        xcxCollect.setOpenId(openId);
        xcxCollect.setGoodsId(goodId);
        this.xcxCollectService.add(xcxCollect);
        this.xcxGoodsService.addZanNum(goodId);
        jsonResult.setCode(0);
        jsonResult.setMessage("收藏成功,已赞");
      } else {
        jsonResult.setCode(1);
        jsonResult.setMessage("已收藏");
      }
    } else {
      jsonResult.setCode(9);
      jsonResult.setMessage("用户不存在");
    }
    return jsonResult;
  }

  @RequestMapping(value={"/getAssess"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public JsonResult getAssess(String goodsId)
  {
    JsonResult jsonResult = new JsonResult();
    List list = this.assessService.getList(goodsId);
    if ((list != null) && (list.size() > 0)) {
      jsonResult.setCode(0);
      jsonResult.setMessage("获取评论成功");
      jsonResult.setData(list);
    } else {
      jsonResult.setCode(1);
      jsonResult.setMessage("暂无评论");
      list = new ArrayList();
      jsonResult.setData(list);
    }
    return jsonResult;
  }

  @RequestMapping(value={"/addAssess"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public JsonResult addAssess(String goodsId, String openId, String context, String pOpenId)
  {
    JsonResult jsonResult = new JsonResult();
    XcxUser xcxUser = this.xcxUserService.getByOpenId(openId);
    if (xcxUser != null) {
      XcxGoodsAssess xcxGoodsAssess = new XcxGoodsAssess();
      xcxGoodsAssess.setContext(context);
      xcxGoodsAssess.setGoodsId(goodsId);
      xcxGoodsAssess.setOpenId(openId);
      xcxGoodsAssess.setName(this.xcxUserService.getByOpenId(openId).getUserName());

      xcxGoodsAssess.setpOpenId(pOpenId);

      this.assessService.add(xcxGoodsAssess);
      this.xcxGoodsService.addAssessNum(goodsId);
      jsonResult.setCode(0);
      jsonResult.setMessage("新增评论成功");
    } else {
      jsonResult.setCode(9);
      jsonResult.setMessage("用户不存在");
    }
    return jsonResult;
  }

  public String doKeyWord(String keyword)
  {
    int length = keyword.length();
    char[] keyArr = new char[length << 1];
    int i = 0; for (int j = 0; i < length; j = i << 1) {
      keyArr[j] = keyword.charAt(i);
      keyArr[(1 + j)] = '%';

      i++;
    }

    keyword = "%" + new String(keyArr);
    System.out.print(keyword);
    return keyword;
  }
}