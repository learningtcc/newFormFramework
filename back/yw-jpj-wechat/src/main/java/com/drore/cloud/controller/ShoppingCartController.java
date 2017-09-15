package com.drore.cloud.controller;
import com.drore.cloud.model.ShoppingCart;
import com.drore.cloud.model.ShoppingStore;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/***
 * 浙江卓锐科技股份有限公司 版权所有@Copyright 2016
 * 说明 购物车 模块(当购物车里面内容实际产生订单后，移除购物车中相应商品)
 * 项目名称
 * @since:cloud-ims 1.0
 * @author <a href="mailto:baoec@drore.com">baoec@drore.com </a> 
 * 2017/09/05 14:53
 */
@Api("购物车")
@RestController
@RequestMapping("/shopping_cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService cartService;
    @ApiOperation(value = "购物车列表",notes = "购物车列表")
    @PostMapping("/list")
    public RestMessage list (){
        RestMessage rm=new RestMessage();
        Map<String, ShoppingStore> stores = cartService.list();
        if(stores!=null){
            rm.setData(stores);
        }
        return rm;
    }

    /**购物车新功能
     * 已经考虑所有情况
     * */
    @ApiOperation(value = "加入购物车",notes = "加入购物车")
    @PostMapping("/put")
    public RestMessage put (@Valid@ModelAttribute ShoppingCart cart){
        RestMessage rm=new RestMessage();
        rm.setData(cartService.SaveGoods(cart));
        return rm;
    }
    @ApiOperation(value = "增加数量",notes = "增加数量")
    @PostMapping("/add")
    public RestMessage add (@Valid@ModelAttribute ShoppingCart cart){
        RestMessage rm=new RestMessage();
        rm.setData(cartService.addGoods(cart));
        return rm;
    }
    @ApiOperation(value = "减少数量",notes = "减少数量")
    @PostMapping("/subtract")
    public RestMessage subtract (@Valid@ModelAttribute ShoppingCart cart){
        RestMessage rm=new RestMessage();
        rm.setData(cartService.subtractGoods(cart));
        return rm;
    }
    @ApiOperation(value = "直接设置数量",notes = "直接设置数量")
    @PostMapping("/set")
    public RestMessage set (@Valid@ModelAttribute ShoppingCart cart){
        RestMessage rm=new RestMessage();
        rm.setData(cartService.setGoods(cart));
        return rm;
    }

    @ApiOperation(value = "清空购物车",notes = "清空购物车")
    @PostMapping("/remove")
    public RestMessage remove(){
        RestMessage rm=new RestMessage();
        if(cartService.removeAll()){
            rm.setMessage("清除数据成功");
        }
        return rm;
    }
}
