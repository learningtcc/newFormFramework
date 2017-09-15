package com.drore.cloud.service;

import com.drore.cloud.model.ShoppingCart;
import com.drore.cloud.model.ShoppingStore;

import java.util.Map;

/***
 * 浙江卓锐科技股份有限公司 版权所有@Copyright 2016
 * 说明
 * 项目名称
 * @since:cloud-ims 1.0
 * @author <a href="mailto:baoec@drore.com">baoec@drore.com </a> 
 * 2017/09/06 9:47
 */

public interface ShoppingCartService {
    /**
     * 添加商品进购物车
     * */
    public Map<String,ShoppingStore> SaveGoods(ShoppingCart cart);
    /**
     * 根据商品id增加数量
     * */
    public Map<String,ShoppingStore> addGoods(ShoppingCart cart);
    /**
     * 根据商品id减少数量
     * */
    public Map<String,ShoppingStore> subtractGoods(ShoppingCart cart);
    /**
     * 根据商品id设置购物车中某个商品的数量
     * **/
    public Map<String,ShoppingStore> setGoods(ShoppingCart cart);
    /**
     * 根据商品id清空购物车
     * **/
    public Map<String,ShoppingStore> removeGoodsByCommodityId(ShoppingCart cart);
    /**
     * 根据商家id清空购物车
     * **/
    public Map<String,ShoppingStore> removeGoodsByStoreId(ShoppingCart cart);
    /**
     * 清空整个购物车
     * **/
    public boolean removeAll();
    /**
     * 检查购物车中是否已经存在该商品
     * **/
    public boolean checkGood(ShoppingCart cart);
    /**
     * 检查购物车中是否已经存在该商店
     * **/
    public boolean checkStore(ShoppingCart cart);
    /**
     * 购物车列表
     * **/
    public Map<String,ShoppingStore> list();
}
