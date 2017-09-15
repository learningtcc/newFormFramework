package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.exception.CustomException;
import com.drore.model.TeaCultureInfo;
import com.drore.service.TeaCultureService;
import com.drore.util.JSONObjResult;
import com.drore.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by wangl on 2017/9/1 0001.
 */
@Api(value = "茶文化-王璐")
@RestController
@RequestMapping("/cms/teaCulture")
public class TeaCultureController {

    @Autowired
    private TeaCultureService teaCultureService;

    @ApiOperation(value = "增加或修改茶文化",notes = "增加或修改茶文化")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id(新增不传，修改传)",dataType = "int",required = false),
            @ApiImplicitParam(name = "title",value = "主题",dataType = "string",required = true),
            @ApiImplicitParam(name = "serial",value = "排序",dataType = "string",required = true),
            @ApiImplicitParam(name = "introduction",value = "简介",dataType = "string",required = true),
            @ApiImplicitParam(name = "description",value = "详情",dataType = "string",required = true)
    })
    @PostMapping("/addTeaCulture")
    public JSONObject addTeaCulture(@Valid TeaCultureInfo teaCultureInfo){
        RestMessage restMessage = teaCultureService.addTeaCulture(teaCultureInfo);
        return JSONObjResult.toJSONObj(restMessage);
    }

    @ApiOperation(value = "启用禁用",notes = "启用禁用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id",dataType = "int",required = true),
            @ApiImplicitParam(name = "status",value = "启用（status=Y），禁用（status=N）",dataType = "string",required = true)
    })
    @PostMapping("/setIsUsing")
    public JSONObject setUsing(String id, String status){
        RestMessage restMessage = teaCultureService.setIsUsing(id,status);
        return JSONObjResult.toJSONObj(restMessage);
    }

    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int"),
            @ApiImplicitParam(name = "title", value = "主题",  dataType = "String")
    })
    @PostMapping("/queryPage")
    public JSONObject queryPage(@RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "10") int pageSize, TeaCultureInfo teaCultureInfo){
        String logMsg = "分页查询";
        LogbackLogger.info(logMsg);
        Pagination pagination = new Pagination(pageSize, pageNo);
        pagination = teaCultureService.queryByPage(pagination, teaCultureInfo);
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
        TeaCultureInfo teaCultureInfo =  teaCultureService.queryDetailById(id);
        return JSONObjResult.toJSONObj(teaCultureInfo, true, "查询成功");
    }
}
