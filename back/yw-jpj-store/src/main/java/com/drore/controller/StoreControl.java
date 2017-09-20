package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.model.StoreInfo;
import com.drore.service.StoreService;
import com.drore.util.JSONObjResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangl on 2017/9/4 0004.
 */
@Api(value = "商家管理 wdz 可以使用")
@RestController
@RequestMapping("/store")
public class StoreControl {

    @Autowired
    private StoreService storeService;

    @ApiOperation(value = "更新商家信息",notes = "更新商家信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address",value = "联系地址 1-100",dataType = "string",required = true),
            @ApiImplicitParam(name = "name",value = "商家名称 1-50",dataType = "string",required = true),
            @ApiImplicitParam(name = "isRelease",value = "是否发布 Y|N",dataType = "string",required = true),
            @ApiImplicitParam(name = "contactPerson",value = "联系人不能为空，1-50",dataType = "string",required = true),
            @ApiImplicitParam(name = "contactPhone",value = "联系电话不能为空，1-50",dataType = "string",required = true),
            @ApiImplicitParam(name = "dimension",value = "维度，0-10",dataType = "string" ),
            @ApiImplicitParam(name = "longitude",value = "经度，0-10",dataType = "string" ),
            @ApiImplicitParam(name = "payee",value = "收款人，0-50",dataType = "string" ),
            @ApiImplicitParam(name = "bankCard",value = "银行卡号，0-50",dataType = "string" ),
            @ApiImplicitParam(name = "bankAddress",value = "银行卡开户地址，0-100",dataType = "string" ),
            @ApiImplicitParam(name = "alipayUrl",value = "支付宝图片，0-200",dataType = "string" ),
            @ApiImplicitParam(name = "weixinUrl",value = "微信图片，0-200",dataType = "string" ),
            @ApiImplicitParam(name = "wxUrl",value = "微信图片(微信/公众号)，0-200",dataType = "string" ),
            @ApiImplicitParam(name = "businessLicenseUrl",value = "营业执照，0-200",dataType = "string" ),
            @ApiImplicitParam(name = "pics",value = "图集，多个逗号隔开",dataType = "string" )

    })
    @PostMapping("/update")
    public JSONObject update(@Validated  StoreInfo storeInfo){
        String logMsg = "更新详情";
        LogbackLogger.info(logMsg);
        if(StringUtils.isEmpty(storeInfo.getId())){
            return  JSONObjResult.toJSONObj("主键不能为空");
        }
        RestMessage restMessage = storeService.update(storeInfo);
        return JSONObjResult.toJSONObj(restMessage);
    }



    @ApiOperation(value = "获取详情", notes = "获取详情")
    @ApiImplicitParams({
    })
    @GetMapping(value = "queryDetail")
    public JSONObject queryDetail() {
        String logMsg = "获取详情";
        LogbackLogger.info(logMsg);
        StoreInfo storeInfo =  storeService.queryDetailByStore();
        return JSONObjResult.toJSONObj(storeInfo, true, "查询成功");
    }




}
