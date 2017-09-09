package com.secondHand.service;

import com.secondHand.entity.StoreGoods;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;

/**
 * Created by XCA on 2017/5/23.
 */
public interface StoreGoodsService {

    StoreGoods get(String id);

    List<StoreGoods> getGoods(StoreGoods storeGoods,int pageNum) throws Exception;

    void add(StoreGoods storeGoods);

    void update(StoreGoods storeGoods);

    StoreGoods goodsLike(String key);

    String pushOrder(String goodsId, String openId) throws IllegalAccessException, UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException;

    String signOrder(String repayId) throws IllegalAccessException;
}
