package com.secondHand.dao;

import com.secondHand.entity.XcxPic;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by XCA on 2017/5/31.
 */
@Repository
public interface XcxPicDao {

    XcxPic get(@Param(value = "id") String id);

    List<XcxPic> getList(XcxPic xcxPic);

}
