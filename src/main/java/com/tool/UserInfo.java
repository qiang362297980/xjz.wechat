package com.tool;

/**
 * Created by Administrator on 2017/6/10.
 */
public class UserInfo {

    private String name;
    private String openId;
    private int followNum;
    private int lookNum;
    private int myGoodsNum;
    private int myCollectNum;
//    private int myLookNum;
    private String isNews;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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

    public int getMyGoodsNum() {
        return myGoodsNum;
    }

    public void setMyGoodsNum(int myGoodsNum) {
        this.myGoodsNum = myGoodsNum;
    }

    public int getMyCollectNum() {
        return myCollectNum;
    }

    public void setMyCollectNum(int myCollectNum) {
        this.myCollectNum = myCollectNum;
    }

//    public int getMyLookNum() {
//        return myLookNum;
//    }
//
//    public void setMyLookNum(int myLookNum) {
//        this.myLookNum = myLookNum;
//    }

    public String getIsNews() {
        return isNews;
    }

    public void setIsNews(String isNews) {
        this.isNews = isNews;
    }
}
