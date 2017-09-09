package com.secondHand.entity;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/9.
 */
public class XcxFollow {

    private String id;
    private String uOpenId;
    private String aOpenId;
    private Date createDate;
    private Date updateDate;
    private String delFlag;

    private String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getuOpenId() {
        return uOpenId;
    }

    public void setuOpenId(String uOpenId) {
        this.uOpenId = uOpenId;
    }

    public String getaOpenId() {
        return aOpenId;
    }

    public void setaOpenId(String aOpenId) {
        this.aOpenId = aOpenId;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
