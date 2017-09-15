package com.drore.cloud.controller;

import com.drore.cloud.model.CommodityEvaluation;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.service.CommodityEvaluationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by wangl on 2017/9/8 0008.
 */
@Api(value = "评价管理-王璐")
@RestController
@RequestMapping("/wechat/commodityEvaluation/")
public class CommodityEvaluationController {

    @Autowired
    private CommodityEvaluationService commodityEvaluationService;

    @ApiOperation(value = "提交评价",notes = "提交评价")
    @PostMapping("save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId",value = "订单ID",dataType = "int",required = true),
            @ApiImplicitParam(name = "content",value = "内容",dataType = "int",required = true),
            @ApiImplicitParam(name = "points",value = "评分",dataType = "int",required = true),
            @ApiImplicitParam(name = "picList",value = "图集",dataType = "int",required = true)
    })
    public RestMessage save(@Valid CommodityEvaluation commodityEvaluation){
        RestMessage restMessage = new RestMessage();
        if (StringUtils.isEmpty(commodityEvaluation.getOrderId())){
            restMessage.setSuccess(false);
            restMessage.setMessage("orderId不能为空");
            return restMessage;
        }
        restMessage = commodityEvaluationService.save(commodityEvaluation);
        return restMessage;
    }


}
