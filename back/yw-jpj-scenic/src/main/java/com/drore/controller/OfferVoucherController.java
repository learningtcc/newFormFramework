package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.model.InteractiveContent;
import com.drore.model.InteractiveTheme;
import com.drore.model.OfferVoucher;
import com.drore.service.InteractiveService;
import com.drore.service.OfferVoucherService;
import com.drore.util.JSONObjResult;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.validation.Valid;

/**
 * Created by zhangh on 2017/09/04 0001.
 */
@Api(value = "优惠活动-张豪")
@RestController
@Controller
@RequestMapping("/offerVoucher/")
public class OfferVoucherController {

    private Logger log = LoggerFactory.getLogger(OfferVoucherController.class);

    @Autowired
    private OfferVoucherService offerVoucherService;

    @ApiOperation(value = "查询优惠活动列表")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "storeName",value = "商家名称", required = false, dataType = "String"),
            @ApiImplicitParam( name = "type",value = "优惠类型(打折：discount|满减：full_cut|满折:full_discount)", required = true, dataType = "String"),
            @ApiImplicitParam( name = "offerStatus",value = "优惠状态(未开始：NotBegin|进行中：InHand|已结束：HasEnd)", required = true, dataType = "String"),
            @ApiImplicitParam( name = "startTime",value = "开始时间", required = false, dataType = "String"),
            @ApiImplicitParam( name = "endTime",value = "结束时间", required = false, dataType = "String"),
            @ApiImplicitParam(value = "page_size", name = "page_size", required = false, dataType = "Integer"),
            @ApiImplicitParam(value = "current_page", name = "current_page", required = false, dataType = "Integer")
    })
    @PostMapping("getOfferVoucherList")
    @ResponseBody
    public JSONObject getOfferVoucherList(@Valid OfferVoucher offerVoucher, @RequestParam(value = "page_size", defaultValue = "10") Integer page_size,
                                          @RequestParam(value = "current_page", defaultValue = "1") Integer current_page){
        log.info("查询优惠活动列表");
        try {
            PageUtil pageUtil = offerVoucherService.getOfferVoucherList(offerVoucher,page_size,current_page);
            return  JSONObjResult.toJSONObj(pageUtil, true, "查询成功");
        }catch (Exception e) {
            log.error("查询优惠活动列表异常" + e);
            return JSONObjResult.toJSONObj("查询优惠活动列表异常，请联系管理员");
        }
    }

    @ApiOperation(value = "查询优惠活动")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "id",value = "主键", required = true, dataType = "String")
    })
    @PostMapping("getOfferVoucher")
    @ResponseBody
    public JSONObject getOfferVoucher(String id){
        log.info("查询优惠活动 id=" + id);
        RestMessage restMessage = null;
        try {
            if (StringUtils.isEmpty(id)) {
                return JSONObjResult.toJSONObj("主键不能为空");
            }
            restMessage = offerVoucherService.queryOfferVoucher(id);
            if(restMessage.isSuccess()){
                restMessage.setMessage("查询成功");
            }
            return JSONObjResult.toJSONObj(restMessage);
        }catch (Exception e) {
            log.error("查询优惠活动" + e);
            return JSONObjResult.toJSONObj("查询失败");
        }
    }

    @ApiOperation(value = "获取优惠类型枚举")
    @GetMapping(value = "getCouponType")
    public JSONObject getCouponType() {
        String logMsg = "获取订单状态列表";
        LogbackLogger.info(logMsg);
        RestMessageModel model = new RestMessageModel();
        model.setSuccess(true);
        model.setData(offerVoucherService.getCouponType());
        return JSONObjResult.toJSONObj(model);
    }

    @ApiOperation(value = "获取优惠状态枚举")
    @GetMapping(value = "getCouponState")
    public JSONObject getCouponState() {
        String logMsg = "获取优惠状态枚举";
        LogbackLogger.info(logMsg);
        RestMessageModel model = new RestMessageModel();
        model.setSuccess(true);
        model.setData(offerVoucherService.getCouponState());
        return JSONObjResult.toJSONObj(model);
    }
}
