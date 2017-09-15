package com.drore.cloud.controller;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.service.LogisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangl on 2017/9/12 0012.
 */
@Api(value = "物流-王璐")
@RestController
@RequestMapping("/wechat/Logistics")
public class LogisticsController {

    @Autowired
    private LogisticsService logisticsService;

    @ApiOperation(value = "物流查询",notes = "物流查询")
    @ApiImplicitParam(name = "orderId",value = "订单id",dataType = "string",required = true)
    @GetMapping("/detail")
    public RestMessage detail(String orderId) throws Exception{
        RestMessage restMessage = new RestMessage();
        if (StringUtils.isEmpty(orderId)){
            restMessage.setSuccess(false);
            restMessage.setMessage("orderId不能为空");
            return restMessage;
        }
        restMessage = logisticsService.detail(orderId);
        return restMessage;
    }
}
