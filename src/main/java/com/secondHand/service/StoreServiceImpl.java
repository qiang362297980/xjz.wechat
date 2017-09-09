package com.secondHand.service;

import com.secondHand.dao.StoreDao;
import com.secondHand.entity.Store;
import com.tool.TemplateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by XCA on 2017/6/2.
 */
@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreDao storeDao;

    @Override
    public Store get() {
        return storeDao.get();
    }
    
    @Override
    public void add(Store store) {
        store.setCreateDate(new Date());
        store.setDelFlag("0");
        storeDao.add(store);
    }

    @Override
    public void update(Store store) {
        store.setUpdateDate(new Date());
        storeDao.update(store);
    }

}
