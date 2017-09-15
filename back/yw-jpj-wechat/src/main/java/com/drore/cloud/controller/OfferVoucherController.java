package com.drore.cloud.controller;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.service.OfferVoucherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangl on 2017/9/4 0004.
 */
@Api(value = "店铺优惠-王璐")
@RestController
@RequestMapping("/wechat/offerVoucher")
public class OfferVoucherController {

    @Autowired
    private OfferVoucherService offerVoucherService;

    @ApiOperation(value = "店铺优惠列表",notes = "店铺优惠列表")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_size",value = "每页条数",dataType = "int",required = true),
            @ApiImplicitParam(name = "current_page",value = "当前页",dataType = "int",required = true)
    })
    public RestMessage list(@RequestParam(value = "page_size", defaultValue = "10") Integer page_size,
                            @RequestParam(value = "current_page", defaultValue = "1") Integer current_page){
        RestMessage restMessage = offerVoucherService.list(page_size, current_page);
        return restMessage;
    }

    @ApiOperation(value = "店铺优惠详情",notes = "店铺优惠详情")
    @GetMapping("/detail")
    @ApiImplicitParam(name = "storeId",value = "店铺主键",dataType = "int",required = true)
    public RestMessage detail(String storeId){
        RestMessage restMessage = new RestMessage();
        if (StringUtils.isEmpty(storeId)){
            restMessage.setSuccess(false);
            restMessage.setMessage("storeId不能为空");
            return restMessage;
        }
        restMessage = offerVoucherService.detail(storeId);
        return restMessage;
    }
}
