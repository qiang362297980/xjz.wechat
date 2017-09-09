package com.secondHand.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by XCA on 2017/5/22.
 */
public class XcxUser {

    private String id;
    private String mobile;
    private String openId;
    private String userName;
    private String sex;
    private String headUrl;
    private String area;
    private String status; //0正常 1黑名单
    private int followNum;
    private int lookNum;
    private String isNews;
    private Date createDate;
    private Date updateDate;
    private String delFlag;

    private List<XcxGoods> list;
    private int goodsNum;
    private Date lookNewsDate;
    private String isFollow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public int getLookNum() {
        return lookNum;
    }

    public void setLookNum(int lookNum) {
        this.lookNum = lookNum;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIsNews() {
        return isNews;
    }

    public void setIsNews(String isNews) {
        this.isNews = isNews;
    }

    public List<XcxGoods> getList() {
        return list;
    }

    public void setList(List<XcxGoods> list) {
        this.list = list;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Date getLookNewsDate() {
        return lookNewsDate;
    }

    public void setLookNewsDate(Date lookNewsDate) {
        this.lookNewsDate = lookNewsDate;
    }

    public String getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(String isFollow) {
        this.isFollow = isFollow;
    }
}
