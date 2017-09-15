package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.exception.CustomException;
import com.drore.model.StoreInfo;
import com.drore.service.StoreService;
import com.drore.util.JSONObjResult;
import com.drore.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 浙江卓锐科技股份有限公司 版权所有 ? Copyright 2017<br/>
 * 说明: 商家管理 <br/>
 * 项目名称:  <br/>
 * 创建日期: 2017/9/5 13:23<br/>
 * 作者: wdz
 */

@Api(value = "商家管理 wdz 可以使用")
@RestController
@RequestMapping("/store")
public class StoreControl {

    @Autowired
    private StoreService storeService;

    @ApiOperation(value = "添加商家",notes = "添加商家")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address",value = "联系地址 1-100",dataType = "string",required = true),
            @ApiImplicitParam(name = "name",value = "商家名称 1-50",dataType = "string",required = true),
            @ApiImplicitParam(name = "isRelease",value = "是否发布 Y|N",dataType = "string",required = true),
            @ApiImplicitParam(name = "businessLicenseUrl",value = "营业执照 0,200",dataType = "string"),
            @ApiImplicitParam(name = "contactPerson",value = "联系人不能为空，1-50",dataType = "string",required = true),
            @ApiImplicitParam(name = "contactPhone",value = "联系电话不能为空，1-50",dataType = "string",required = true)
    })
    @PostMapping("/add")
    public JSONObject add(@Validated  StoreInfo storeInfo){
        String logMsg = "新增信息";
        LogbackLogger.info(logMsg );
        RestMessage restMessage = storeService.save(storeInfo);
        return JSONObjResult.toJSONObj(restMessage);
    }


    @ApiOperation(value = "修改商家",notes = "修改商家")
    @PostMapping("/update")
    public JSONObject update(@Validated  StoreInfo storeInfo){
        String logMsg = "新增信息";
        LogbackLogger.info(logMsg );
        if(StringUtils.isEmpty(storeInfo.getId())){
            throw  new CustomException("主键不能为空!");
        }
        RestMessage restMessage = storeService.updateScenic(storeInfo);
        return JSONObjResult.toJSONObj(restMessage);
    }



    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int"),
            @ApiImplicitParam(name = "name", value = "名称",  dataType = "String"),
            @ApiImplicitParam(name = "isRelease", value = "是否发布 Y|N",  dataType = "String")
    })
    @PostMapping(value = "queryPage")
    public JSONObject queryPage(@RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "10") int pageSize, StoreInfo storeInfo) {
        String logMsg = "分页查询";
        LogbackLogger.info(logMsg);
        Pagination pagination = new Pagination(pageSize, pageNo);
        pagination = storeService.queryByPage(pagination, storeInfo);
        return JSONObjResult.toJSONObj(new PageUtil(pagination), true, "查询成功");
    }


    @ApiOperation(value = "根据主键查询详情", notes = "根据主键查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int")
    })
    @PostMapping(value = "queryById")
    public JSONObject queryById(@RequestParam String id) {
        String logMsg = "根据主键查询详情";
        LogbackLogger.info(logMsg+id);
        if(StringUtils.isEmpty(id)){
            throw  new CustomException("主键不能为空!");
        }
       StoreInfo storeInfo =  storeService.queryDetailById(id);
        return JSONObjResult.toJSONObj(storeInfo, true, "查询成功");
    }

    @ApiOperation(value = "置顶成热门", notes = "置顶成热门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int")
    })
    @PostMapping(value = "updateHot")
    public JSONObject updateHot(@RequestParam String id) {
        String logMsg = "置顶成热门";
        LogbackLogger.info(logMsg+id);
        if(StringUtils.isEmpty(id)){
            throw  new CustomException("主键不能为空!");
        }
        RestMessage  restMessage =  storeService.updateHot(id);
        return JSONObjResult.toJSONObj(restMessage);
    }







}
