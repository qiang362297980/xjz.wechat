package com.secondHand.dao;

import com.secondHand.entity.StoreGoods;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by XCA on 2017/5/23.
 */
@Repository
public interface StoreGoodsDao {

    StoreGoods get(@Param(value = "id")String id);

    List<StoreGoods> getGoods(StoreGoods storeGoods);

    void add(StoreGoods storeGoods);

    void update(StoreGoods storeGoods);
}
