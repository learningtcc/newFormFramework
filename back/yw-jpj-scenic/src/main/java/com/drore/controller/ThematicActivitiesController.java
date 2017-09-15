package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.model.ThemeActivities;
import com.drore.service.ThemeActivitiesService;
import com.drore.util.JSONObjResult;
import com.drore.util.PageUtil;
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
 * Created by zhangh on 2017/9/1 0001.
 */
@Api("主题活动-张豪")
@RestController
@Controller
@RequestMapping("/thematicActivities/")
public class ThematicActivitiesController {

    private Logger log = LoggerFactory.getLogger(ThematicActivitiesController.class);

    @Autowired
    ThemeActivitiesService themeActivitiesService;

    @ApiOperation(value = "查询主题活动列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "pageSize",name = "pageSize",required = false,dataType = "Integer"),
            @ApiImplicitParam(value = "pageNo",name = "pageNo",required = false,dataType = "Integer"),
            @ApiImplicitParam(name = "title",value = "标题",required = false,dataType = "String"),
            @ApiImplicitParam(name = "type",value = "活动类型",required = false,dataType = "String"),
            @ApiImplicitParam(name = "isRelease",value = "是否发布",required = false,dataType = "String"),
            @ApiImplicitParam(name = "startTime",value = "活动开始时间",required = false,dataType = "String"),
            @ApiImplicitParam(name = "endTime",value = "活动结束时间",required = false,dataType = "String")})
    @PostMapping("queryList")
    @ResponseBody
    public JSONObject queryList(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                @Valid ThemeActivities themeActivities) {
        log.info("查询主题活动列表");
        try {
            PageUtil pageUtil = themeActivitiesService.getThemeActivities(themeActivities,pageSize,pageNo);
            return JSONObjResult.toJSONObj(pageUtil,true, "查询成功");
        }catch (Exception e) {
            log.error("查询用户列表异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }
    }

    @ApiOperation(value = "保存主题活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title",value = "标题",required = true,dataType = "String"),
            @ApiImplicitParam(name = "type",value = "活动类型（节假日活动：Holidays，风俗文化：Culture，社区治理：Community）",required = true,dataType = "String"),
            @ApiImplicitParam(name = "typeName",value = "活动类型名称(中文)",required = true,dataType = "String"),
            @ApiImplicitParam(name = "address",value = "活动地址",required = true,dataType = "String"),
            @ApiImplicitParam(name = "themePic",value = "主图",required = true,dataType = "String"),
            @ApiImplicitParam(name = "startTime",value = "活动开始时间",required = true,dataType = "String"),
            @ApiImplicitParam(name = "endTime",value = "活动结束时间",required = true,dataType = "String"),
            @ApiImplicitParam(name = "explains",value = "活动详情",required = true,dataType = "String"),
            @ApiImplicitParam(name = "isRelease",value = "是否发布",required = true,dataType = "String")})
    @PostMapping("save")
    @ResponseBody
    public JSONObject save(@Valid ThemeActivities themeActivities) {
        log.info("保存主题活动");
        //themeActivities.validate();
        RestMessage restMessage = null;
        try {
            restMessage = themeActivitiesService.saveThemeActivitie(themeActivities);
            if(restMessage.isSuccess()){
                restMessage.setMessage("保存成功");
            }
            return JSONObjResult.toJSONObj(restMessage);
        } catch (Exception e) {
            log.error("保存信息 异常" + e);
            return JSONObjResult.toJSONObj("保存异常，请联系管理员");
        }
    }

    @ApiOperation(value = "删除主题活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id",required = true,dataType = "String")}
    )
    @PostMapping("deleteById")
    @ResponseBody
    public JSONObject delete(@RequestParam String id) {
        log.info("删除主题活动");
        RestMessage restMessage = null;
        try {
            if (StringUtils.isEmpty(id)) {
                return JSONObjResult.toJSONObj("主键不能为空");
            }
            restMessage = themeActivitiesService.deleteThemeActivity(id);
            if(restMessage.isSuccess()){
                restMessage.setMessage("删除成功");
            }
            return JSONObjResult.toJSONObj(restMessage);
        }catch (Exception e) {
            log.error("异常信息" + e);
            return JSONObjResult.toJSONObj("删除异常");
        }
    }

    @ApiOperation(value = "修改主题活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id",required = true,dataType = "String"),
            @ApiImplicitParam(name = "title",value = "标题",required = true,dataType = "String"),
            @ApiImplicitParam(name = "type",value = "活动类型",required = true,dataType = "String"),
            @ApiImplicitParam(name = "typeName",value = "活动类型名称(中文)",required = true,dataType = "String"),
            @ApiImplicitParam(name = "address",value = "活动地址",required = true,dataType = "String"),
            @ApiImplicitParam(name = "themePic",value = "主图",required = true,dataType = "String"),
            @ApiImplicitParam(name = "isRelease",value = "是否发布",required = true,dataType = "String"),
            @ApiImplicitParam(name = "startTime",value = "活动开始时间",required = true,dataType = "String"),
            @ApiImplicitParam(name = "endTime",value = "活动结束时间",required = true,dataType = "String"),
            @ApiImplicitParam(name = "explains",value = "活动详情",required = true,dataType = "String"),
            @ApiImplicitParam(name = "isRelease",value = "是否发布",required = true,dataType = "String"),
            @ApiImplicitParam(name = "isTop",value = "是否置顶",required = false,dataType = "String")}
    )
    @PostMapping("update")
    @ResponseBody
    public JSONObject update(@Valid ThemeActivities themeActivities) {
        log.info("修改主题活动");
        RestMessage restMessage = null;
        try {
            if (StringUtils.isEmpty(themeActivities.getId())) {
                return JSONObjResult.toJSONObj("主键不能为空");
            }
            restMessage = themeActivitiesService.updateThemeActivity(themeActivities);
            if(restMessage.isSuccess()){
                restMessage.setMessage("修改成功");
            }
            return JSONObjResult.toJSONObj(restMessage);
        } catch (Exception e) {
            log.error("修改信息 异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }
    }

    @ApiOperation(value = "查询主题活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id",required = true,dataType = "String")}
    )
    @PostMapping("queryById")
    @ResponseBody
    public JSONObject queryById(@RequestParam String id) {
        log.info("查询主题活动 id=" + id);
        RestMessage restMessage = null;
        try {
            restMessage = themeActivitiesService.queryThemeActivity(id);
            if(restMessage.isSuccess()){
                restMessage.setMessage("查询成功");
            }
            return JSONObjResult.toJSONObj(restMessage);
        }catch (Exception e) {
            log.error("查询用户信息异常" + e);
            return JSONObjResult.toJSONObj("查询失败");
        }
    }

    @ApiOperation(value = "启用禁用",notes = "启用禁用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id",dataType = "int",required = true),
            @ApiImplicitParam(name = "status",value = "启用（status=Y），禁用（status=N）",dataType = "string",required = true)
    })
    @PostMapping("setIsUsing")
    public JSONObject setUsing(String id, String status){
        RestMessage restMessage = themeActivitiesService.setIsUsing(id,status);
        return JSONObjResult.toJSONObj(restMessage);
    }

    @ApiOperation(value = "置顶热门(只有一个)",notes = "置顶热门(只有一个)")
    @ApiImplicitParam(name = "id",value = "id",dataType = "String",required = true)
    @PostMapping("setTop")
    public JSONObject setTop(String id){
        RestMessage restMessage = themeActivitiesService.setTop(id);
        return JSONObjResult.toJSONObj(restMessage);
    }
}
