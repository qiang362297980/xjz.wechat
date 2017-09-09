package com.secondHand.service;

import com.secondHand.dao.XcxAssessDao;
import com.secondHand.dao.XcxGoodsDao;
import com.secondHand.dao.XcxUserDao;
import com.secondHand.entity.XcxGoods;
import com.secondHand.entity.XcxGoodsAssess;
import com.secondHand.entity.XcxUser;
import com.tool.AssessInfo;
import com.tool.ListUtil;
import com.tool.TemplateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by XCA on 2017/5/23.
 */
@Service
public class XcxAssessServiceImpl implements XcxAssessService {

    @Autowired
    private XcxAssessDao xcxAssessDao;

    @Autowired
    private XcxUserDao xcxUserDao;

    @Autowired
    private XcxGoodsDao xcxGoodsDao;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static int pageSize = Integer.valueOf(TemplateConfig.getValue("pageSize"));

    public XcxGoodsAssess get(String id) {
        return xcxAssessDao.get(id);
    }

    public List<XcxGoodsAssess> getList(String goodsId) {
        List<XcxGoodsAssess> list = xcxAssessDao.getList(goodsId);
        for (XcxGoodsAssess assess : list) {
            XcxUser xcxUser = xcxUserDao.getByOpenId(assess.getOpenId());
            if (xcxUser != null) {
                assess.setHeadImg(xcxUser.getHeadUrl());
                assess.setName(xcxUser.getUserName());
            }
            assess.setCreateTime(sdf.format(assess.getCreateDate()));
            XcxUser pUser = xcxUserDao.getByOpenId(assess.getpOpenId());
            if (pUser != null) {
                assess.setParentName("@"+pUser.getUserName());
            } else {
                assess.setParentName("");
            }
        }
        return list;
    }

    public List<String> getParentType(String goodsId) {
        return xcxAssessDao.getParentType(goodsId);
    }

    public List<XcxGoodsAssess> getByParentId(String goodsId, String parentId) {
        return xcxAssessDao.getByParentId(goodsId, parentId);
    }

    public List<XcxGoodsAssess> getListByOpenId(String openId) {
        return xcxAssessDao.getListByOpenId(openId);
    }

    public void add(XcxGoodsAssess assess) {
        assess.setCreateDate(new Date());
        assess.setDelFlag("0");
        xcxAssessDao.add(assess);
        //改变回复对象消息状态
        XcxUser theOne = xcxUserDao.getByOpenId(assess.getpOpenId());
        if (theOne != null) {
            theOne.setIsNews("1");
            xcxUserDao.update(theOne);
        }
    }

    public void update(XcxGoodsAssess assess) {
        assess.setUpdateDate(new Date());
        xcxAssessDao.update(assess);
    }

    public XcxGoodsAssess addGoodsAssess(String goodsId, String openId, String context, String pOpenId) {
        XcxGoodsAssess assess = new XcxGoodsAssess();
        assess.setGoodsId(goodsId);
        assess.setOpenId(openId);
        assess.setContext(context);
//        assess.setParentId(parentId);
        assess.setpOpenId(pOpenId);
        return assess;
    }

    @Override
    public List<List<XcxGoodsAssess>> groupAssessList(String goodsId) {
        List<XcxGoodsAssess> list = xcxAssessDao.getList(goodsId);
        for (XcxGoodsAssess assess : list) {
            assess.setpOpenId(assess.getOpenId());
//            if (assess.getParentId() == null || "".equals(assess.getParentId())) {
//                assess.setParentId(assess.getId());
//
                xcxAssessDao.update(assess);
//            }
        }
        List<List<XcxGoodsAssess>> lists = new ArrayList<>();
        List<String> parentIds = xcxAssessDao.getParentType(goodsId);
        for (int i=0; i<parentIds.size(); i++) {
            List<XcxGoodsAssess> sonList = xcxAssessDao.getByParentId(goodsId, parentIds.get(i));
            if (sonList != null && sonList.size() > 0) {
                for (XcxGoodsAssess assess : sonList) {
                    assess.setCreateTime(sdf.format(assess.getCreateDate()));
                    XcxUser pUser = xcxUserDao.getByOpenId(assess.getpOpenId());
                    if (pUser != null) {
                        assess.setParentName(pUser.getUserName());
                    }
                }
                lists.add(sonList);
            }
        }
        return lists;
    }

    @Override
    public List<AssessInfo> getMyNews(String openId,Integer pageNum) throws Exception {
        List<XcxGoodsAssess> list = xcxAssessDao.getMyNews(openId);
        List<XcxGoodsAssess> assessList = ListUtil.page(pageNum,pageSize,list);
        List<AssessInfo> infoList = new ArrayList<>();
        if (assessList != null) {
            for (XcxGoodsAssess assess : assessList) {
                AssessInfo assessInfo = new AssessInfo();
                assessInfo.setUserName(assess.getName());
                assessInfo.setContent(assess.getContext());
                assessInfo.setGoodsId(assess.getGoodsId());
                assessInfo.setCreateTime(sdf.format(assess.getCreateDate()));
                //物
                XcxGoods xcxGoods = xcxGoodsDao.get(assess.getGoodsId());
                if (xcxGoods != null) {
                    assessInfo.setGoodsName(xcxGoods.getGoodsName());
                    assessInfo.setGoodsContent(xcxGoods.getDescription());
                    assessInfo.setGoodsPic(xcxGoods.getUrl().split(",")[0]);
                }
                //人
                XcxUser xcxUser = xcxUserDao.getByOpenId(assess.getOpenId());
                if (xcxUser != null) {
                    assessInfo.setOpenId(xcxUser.getOpenId());
                    assessInfo.setHeadImg(xcxUser.getHeadUrl());
                }
                infoList.add(assessInfo);
            }
        }
        return infoList;
    }
}
