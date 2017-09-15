package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.model.AdvertisingInfo;
import com.drore.service.AdvertisingInfoService;
import com.drore.util.JSONObjResult;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:  广告信息控制器   <br/>
 * 项目名称: yw-jpj-service <br/>
 * 创建日期:  2017/9/1 17:07  <br/>
 * 作者:    wdz
 */
@Api("广告信息")
@RestController
@RequestMapping("/advertising/")
public class AdvertisingInfoControl {

    @Autowired
    AdvertisingInfoService advertisingInfoService;

    /**
     * 已测试
     * @return
     */
    @ApiOperation(value = "获取广告类型", notes = "获取广告类型")
    @GetMapping(value = "findTypeList")
    public JSONObject findTypeList() {
        String logMsg = "获取订单状态列表";
        LogbackLogger.info(logMsg);


        RestMessageModel model = new RestMessageModel();
        model.setSuccess(true);
        model.setData(advertisingInfoService.queryTableName());
        return JSONObjResult.toJSONObj(model);
    }

    /**
     * 查询详情 已测试
     *
     * @param id
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
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
        AdvertisingInfo advertisingInfo = advertisingInfoService.queryById(id);
        return JSONObjResult.toJSONObj(advertisingInfo,true,null);
    }

    /**
     * 已测试
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "根据主键删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "string")
    })
    @GetMapping("deleteById")
    public JSONObject deleteById(@RequestParam String id) {
        String logMsg = "删除信息";
        LogbackLogger.info(logMsg + id);
        if (StringUtils.isEmpty(id)) {
            return JSONObjResult.toJSONObj("主键不能为空!");
        }
        RestMessage rm = advertisingInfoService.removeById(id);
        return JSONObjResult.toJSONObj(rm);

    }


    /**
     * 保存 已测试
     *
     * @param advertisingInfo
     * @return
     */
    @ApiOperation(value = "新增", notes = "保存信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableName", value = "栏目不能为空 下拉框选择，从方法中获取", required = true, dataType = "string"),
            @ApiImplicitParam(name = "linkUrl", value = "链接地址，长度0-200", required = false, dataType = "string"),
            @ApiImplicitParam(name = "imageUrl", value = "图片地址 长度0-200", required = true, dataType = "string"),
            @ApiImplicitParam(name = "name", value = "名称 长度1-100", required = true, dataType = "string")
    })
    @PostMapping("save")
    public JSONObject save(@Valid AdvertisingInfo advertisingInfo) {
        String logMsg = "保存信息";
        LogbackLogger.info(logMsg);
        RestMessage rm = advertisingInfoService.save(advertisingInfo);
        return JSONObjResult.toJSONObj(rm);
    }


    /**
     * 修改 已测试
     *
     * @param advertisingInfo
     * @return
     */
    @ApiOperation(value = "修改", notes = "修改信息")
    @PostMapping("modify")
    public JSONObject modify(@Valid AdvertisingInfo advertisingInfo) {
        String logMsg = "修改信息";
        LogbackLogger.info(logMsg);

        if (StringUtils.isEmpty(advertisingInfo.getId())) {
            return JSONObjResult.toJSONObj("主键不能为空");
        }

        RestMessage rm = advertisingInfoService.update(advertisingInfo);
        return JSONObjResult.toJSONObj(rm);
    }


    /**
     *  已测试
     * @param pageNo
     * @param pageSize
     * @param advertisingInfo
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int")
    })
    @PostMapping(value = "queryPage")
    public JSONObject queryPage(@RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "10") int pageSize, AdvertisingInfo advertisingInfo) {
        String logMsg = "分页查询";
        LogbackLogger.info(logMsg);
        Pagination pagination  = new Pagination(pageSize, pageNo);
        pagination = advertisingInfoService.queryByPage(pagination, advertisingInfo);
        return JSONObjResult.toJSONObj(new PageUtil(pagination), true, "查询成功");
    }

}
