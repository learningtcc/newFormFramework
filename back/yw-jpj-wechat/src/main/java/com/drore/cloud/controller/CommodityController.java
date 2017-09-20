package com.drore.cloud.controller;

import com.drore.cloud.sdk.common.annotation.Login;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.service.CommodityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by 仁杰 on 2017/9/7
 */
@Api(value = "商品模块——何仁杰")
@RestController
@RequestMapping("/wechat/shop")
public class CommodityController {

    @Autowired
    private CommodityService commodityService;


    @ApiOperation(value = "商品详情——何仁杰",notes = "返回参数data:{商品信息:commodity_info;商铺信息:store_info}")
    @ApiImplicitParam(name = "id",value = "商品主键id",dataType = "string",required = true)
    @PostMapping("/detail")
    public RestMessage CommodityDetail(@RequestParam("id") String commodityId){
        return commodityService.commodityDetail(commodityId);
    }

    //商品的评价列表
    @ApiOperation(value = "商品的评价列表——何仁杰")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commodity_id",value = "店铺/商品id",dataType = "string",required = true),
            @ApiImplicitParam(name = "current_page",value = "当前页",dataType = "Integer",required = true),
            @ApiImplicitParam(name = "page_size",value = "每页大小",dataType = "Integer",required = true)
    })
    @PostMapping("/evaluateList")
    public Pagination<Map> evaluateList(@RequestParam("commodity_id") String commodity_id,
                                        @RequestParam(value = "current_page",defaultValue = "1") Integer current_page,
                                        @RequestParam(value = "page_size",defaultValue = "5") Integer page_size){
        return commodityService.evaluateList(commodity_id,current_page,page_size);
    }



}
