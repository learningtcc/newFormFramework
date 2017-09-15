package com.drore.cloud.service.impl;

import com.drore.cloud.exception.MacroApiException;
import com.drore.cloud.model.ShoppingCart;
import com.drore.cloud.model.ShoppingGoods;
import com.drore.cloud.model.ShoppingStore;
import com.drore.cloud.service.ShoppingCartService;
import com.drore.cloud.util.ShoppingcartUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Map;

/***
 * 浙江卓锐科技股份有限公司 版权所有@Copyright 2016
 * 说明
 * 项目名称
 * @since:cloud-ims 1.0
 * @author <a href="mailto:baoec@drore.com">baoec@drore.com </a> 
 * 2017/09/06 9:48
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{
    @Autowired
    private RedisTemplate redisTemplate;
    private String openid="admin1234567";
    /**
     * 添加商品进购物车
     * */
    @Override
    public Map<String,ShoppingStore> SaveGoods(ShoppingCart cart) {
        ValueOperations<String,Map> data = redisTemplate.opsForValue();
        Map<String,ShoppingStore> oldmap =data.get(openid+"shoppingcart");
        if(cart.getNum()>0){
            if(oldmap!=null){
                if(checkStore(cart)){
                    ShoppingStore store = oldmap.get(cart.getStore_id());
                    //已经存在该商品
                    if(checkGood(cart)){
                        return addGoods(cart);
                    }else {
                        ShoppingGoods newgood = ShoppingcartUtil.getNewgood(cart);
                        store.getGoodmap().put(cart.getCommodity_id(),newgood);
                    }
                }else {
                    ShoppingStore newStore = ShoppingcartUtil.getNewStore(cart);
                    oldmap.put(cart.getStore_id(),newStore);
                }
            }else {
                oldmap = ShoppingcartUtil.getNewStoreMap(cart);
            }
        }else {
            throw new MacroApiException("添加商品进购物车的时候数量要大于等于1好吗？");
        }



        data.set(openid+"shoppingcart",oldmap);
        return oldmap;
    }
    /**
     * 根据商品id增加数量
     * */
    @Override
    public Map<String,ShoppingStore> addGoods(ShoppingCart cart) {
        ValueOperations<String,Map> data = redisTemplate.opsForValue();
        Map<String,ShoppingStore> oldmap =data.get(openid+"shoppingcart");
        if (checkGood(cart)){
            ShoppingStore store = oldmap.get(cart.getStore_id());
            Map<String, ShoppingGoods> goodmap = store.getGoodmap();
            ShoppingGoods good = goodmap.get(cart.getCommodity_id());
            good.setNum(good.getNum()+cart.getNum());
        }else {
            throw new MacroApiException("购物车里还没有这个商品,告诉我怎么添加数量？");
        }
        data.set(openid+"shoppingcart",oldmap);
        return oldmap;
    }
    /**
     * 根据商品id减少数量
     * */
    @Override
    public Map<String,ShoppingStore> subtractGoods(ShoppingCart cart) {
        ValueOperations<String,Map> data = redisTemplate.opsForValue();
        Map<String,ShoppingStore> oldmap =data.get(openid+"shoppingcart");
        ShoppingStore store = oldmap.get(cart.getStore_id());
        Map<String, ShoppingGoods> goodmap = store.getGoodmap();
        ShoppingGoods good = goodmap.get(cart.getCommodity_id());
        if(good!=null){
            int num = good.getNum() - cart.getNum();
            if(num==0){
                goodmap.remove(cart.getCommodity_id());
                if(goodmap.size()==0){
                    oldmap.remove(cart.getStore_id());
                }
            }else if(num<0){
                throw new MacroApiException("该商品对应的购物车已经清空,不能再减了哥.");
            }else {
                good.setNum(good.getNum()-cart.getNum());
            }
        }else {
            throw new MacroApiException("该商品对应的购物车已经清空,不能再减了哥.");
        }

        data.set(openid+"shoppingcart",oldmap);
        return oldmap;
    }
    /**
     * 根据商品id设置购物车中某个商品的数量
     * **/
    @Override
    public Map<String,ShoppingStore> setGoods(ShoppingCart cart) {
        ValueOperations<String,Map> data = redisTemplate.opsForValue();
        Map<String,ShoppingStore> oldmap =data.get(openid+"shoppingcart");
        if(checkGood(cart)){
            ShoppingStore store = oldmap.get(cart.getStore_id());
            Map<String, ShoppingGoods> goodmap = store.getGoodmap();
            ShoppingGoods good = goodmap.get(cart.getCommodity_id());
            int num =cart.getNum();
            if(num<=0){
                throw new MacroApiException("受不了了，宝贝不能再减少了哦！");
            }else {
                good.setNum(cart.getNum());
            }
        }else {
            throw new MacroApiException("首先购物车要有这个东西，才能设置数量");
        }
        data.set(openid+"shoppingcart",oldmap);
        return oldmap;
    }
    /**
     * 根据商品id清空购物车
     * **/
    @Override
    public Map<String,ShoppingStore> removeGoodsByCommodityId(ShoppingCart cart) {
        ValueOperations<String,Map> data = redisTemplate.opsForValue();
        Map<String,ShoppingStore> oldmap =data.get(openid+"shoppingcart");
        if(checkStore(cart)){
            ShoppingStore store = oldmap.get(cart.getStore_id());
            if(checkGood(cart)){
                oldmap.get(cart.getStore_id()).getGoodmap().remove(cart.getCommodity_id());
            }else {
                throw new MacroApiException("购物车里已经没有这个商品了,怎么删？");
            }
            if(store.getGoodmap().size()==0){
                oldmap.remove(cart.getStore_id());
            }
        }else {
            throw new MacroApiException("购物车里已经没有这个商家了,怎么删？");
        }
        data.set(openid+"shoppingcart",oldmap);
        return oldmap;
    }
    /**
     * 根据商家id清空购物车
     * **/
    @Override
    public Map<String,ShoppingStore> removeGoodsByStoreId(ShoppingCart cart) {
        ValueOperations<String,Map> data = redisTemplate.opsForValue();
        Map<String,ShoppingStore> oldmap =data.get(openid+"shoppingcart");
        if(checkStore(cart)){
            oldmap.remove(cart.getStore_id());
        }else {
            throw new MacroApiException("购物车里已经没有这个商家了,怎么删？");
        }
        data.set(openid+"shoppingcart",oldmap);
        return oldmap;
    }
    /**
     * 清空整个购物车
     * **/
    @Override
    public boolean removeAll() {
        redisTemplate.delete(openid+"shoppingcart");
        return true;
    }
    /**
     * 检查购物车中是否已经存在该商品
     * **/
    @Override
    public boolean checkGood(ShoppingCart cart) {
        ValueOperations<String,Map> value = redisTemplate.opsForValue();
        Map<String,ShoppingStore> oldmap =value.get(openid+"shoppingcart");
        if(oldmap!=null){
            ShoppingStore store = oldmap.get(cart.getStore_id());
            if(store!=null){
                Map<String, ShoppingGoods> goodmap = store.getGoodmap();
                ShoppingGoods goods = goodmap.get(cart.getCommodity_id());
                if(goods!=null){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 检查购物车中是否已经存在该商店
     * **/
    @Override
    public boolean checkStore(ShoppingCart cart) {
        ValueOperations<String,Map> value = redisTemplate.opsForValue();
        Map<String,ShoppingStore> oldmap =value.get(openid+"shoppingcart");
        if(oldmap!=null){
            ShoppingStore store = oldmap.get(cart.getStore_id());
            if(store!=null){
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<String, ShoppingStore> list() {
        ValueOperations<String,Map<String,ShoppingStore>> data = redisTemplate.opsForValue();
        Map<String,ShoppingStore> stores = data.get(openid+"shoppingcart");
        return stores;
    }
}
