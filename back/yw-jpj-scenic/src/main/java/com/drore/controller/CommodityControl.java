package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.model.CommodityInfo;
import com.drore.service.CommodityService;
import com.drore.util.JSONObjResult;
import com.drore.util.LoginSysUserUtil;
import com.drore.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明: 商品信息控制器   此处只能查询，操作上下架，不能有其它操作  <br/>
 * 项目名称: yw-jpj-service <br/>
 * 创建日期:  2017/9/4 13:31  <br/>
 * 作者:    wdz
 */
@Api("商品信息 商品管理 wdz 已完成")
@RestController
@RequestMapping("/commodity/")
public class CommodityControl {
    @Autowired
    CommodityService commodityService;

    @ApiOperation(value = "查询详情", notes = "根据主键查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "string")
    })
    @GetMapping("queryById")
    public JSONObject queryById(@RequestParam String id) {
        String logMsg = "查询信息";
        LogbackLogger.info(logMsg + id);
        if (StringUtils.isEmpty(id)) {
            return JSONObjResult.toJSONObj("主键不能为空!");
        }
        CommodityInfo commodityInfo = commodityService.queryDetailById(id);
        return JSONObjResult.toJSONObj(commodityInfo,true,null);
    }


    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int"),
            @ApiImplicitParam(name = "name", value = "名称",  dataType = "String"),
            @ApiImplicitParam(name = "typeId", value = "分类主键，下拉框",   dataType = "String")
    })
    @PostMapping(value = "queryPage")
    public JSONObject queryPage(@RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "10") int pageSize, CommodityInfo commodityInfo) {
        String logMsg = "分页查询";
        LogbackLogger.info(logMsg);
        Pagination pagination = new Pagination(pageSize, pageNo);
          pagination = commodityService.queryByPage(pagination, commodityInfo);
        return JSONObjResult.toJSONObj(new PageUtil(pagination), true, "查询成功");
    }

    @ApiOperation(value = "上下架", notes = "上下架")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品主键", required = true, dataType = "String"),
    })
    @PostMapping(value = "onOffShelves")
    public JSONObject onOffShelves(@RequestParam String id) {
        String logMsg = "上下架";
        LogbackLogger.info(logMsg);
        if(StringUtils.isEmpty(id)){
            return JSONObjResult.toJSONObj("主键不能为空!");
        }
        RestMessage  restMessage =  commodityService.onOffShelves(id, LoginSysUserUtil.getUserId());
        return JSONObjResult.toJSONObj(restMessage);
    }




}
