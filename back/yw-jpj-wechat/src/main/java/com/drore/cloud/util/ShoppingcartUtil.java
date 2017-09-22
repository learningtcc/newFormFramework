package com.drore.cloud.util;

import com.beust.jcommander.internal.Maps;
import com.drore.cloud.model.ShoppingCart;
import com.drore.cloud.model.ShoppingGoods;
import com.drore.cloud.model.ShoppingStore;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/***
 * 浙江卓锐科技股份有限公司 版权所有@Copyright 2016
 * 说明
 * 项目名称
 * @since:cloud-ims 1.0
 * @author <a href="mailto:baoec@drore.com">baoec@drore.com </a> 
 * 2017/09/05 19:48
 */

public class ShoppingcartUtil {

    /**
     * 根据前端传入对象 获得全新的一个商品对象
     * **/
    public static ShoppingGoods getNewgood(ShoppingCart cart){
        ShoppingGoods newgood = new ShoppingGoods();
        newgood.setNum(cart.getNum());
        newgood.setPrice(cart.getPrice());
        newgood.setCommodity_id(cart.getCommodity_id());
        newgood.setCommodity_name(cart.getCommodity_name());
        newgood.setCommodity_image(cart.getCommodity_image());
        return newgood;
    }
    /**
     * 根据前端传入对象 获得全新的一个商家对象
     * **/
    public static ShoppingStore getNewStore(ShoppingCart cart){
        ShoppingStore store = new ShoppingStore();
        store.setStore_id(cart.getStore_id());
        store.setStore_name(cart.getStore_name());
        Map<String,ShoppingGoods> goodmap= Maps.newHashMap();
        ShoppingGoods goods = new ShoppingGoods();
        goods.setCommodity_id(cart.getCommodity_id());
        goods.setCommodity_name(cart.getCommodity_name());
        goods.setCommodity_image(cart.getCommodity_image());
        goods.setNum(cart.getNum());
        goods.setPrice(cart.getPrice());
        goodmap.put(cart.getCommodity_id(),goods);
        store.setGoodmap(goodmap);
        return store;
    }
    /**
     * 根据前端传入对象 获得全新的一个购物车
     * **/
    public  static Map<String,ShoppingStore> getNewStoreMap(ShoppingCart cart){
        Map<String,ShoppingStore> storemap= Maps.newHashMap();
        ShoppingStore store = new ShoppingStore();
        store.setStore_id(cart.getStore_id());
        store.setStore_name(cart.getStore_name());
        Map<String,ShoppingGoods> goodmap=Maps.newHashMap();
        ShoppingGoods goods = new ShoppingGoods();
        goods.setCommodity_id(cart.getCommodity_id());
        goods.setCommodity_name(cart.getCommodity_name());
        goods.setCommodity_image(cart.getCommodity_image());
        goods.setNum(cart.getNum());
        goods.setPrice(cart.getPrice());
        goodmap.put(cart.getCommodity_id(),goods);
        store.setGoodmap(goodmap);
        storemap.put(cart.getStore_id(),store);
        return storemap;
    }
}
