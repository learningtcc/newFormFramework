package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.model.InteractiveContent;
import com.drore.model.InteractiveTheme;
import com.drore.model.ThemeActivities;
import com.drore.service.InteractiveService;
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
import java.util.List;

/**
 * Created by zhangh on 2017/9/1 0001.
 */
@Api(value = "互动-张豪")
@RestController
@Controller
@RequestMapping("/interactive/")
public class InteractiveController {

    private Logger log = LoggerFactory.getLogger(ThematicActivitiesController.class);

    @Autowired
    private InteractiveService interactiveService;

    @ApiOperation(value = "新增互动主题")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "themeName",value = "主题名称", required = true, dataType = "String"),
            @ApiImplicitParam( name = "themePic",value = "主题图片", required = true, dataType = "String"),
            @ApiImplicitParam( name = "number",value = "序号", required = true, dataType = "Integer")
    })
    @PostMapping("addInteractiveTheme")
    @ResponseBody
    public JSONObject addInteractiveTheme(@Valid InteractiveTheme interactiveTheme){
        log.info("新增互动主题");
        RestMessage restMessage = null;
        try {
            PageUtil interactiveThemeList = interactiveService.getInteractiveThemeList(Integer.MAX_VALUE, 1);
            List<InteractiveTheme> root = (List<InteractiveTheme>) interactiveThemeList.getRoot();
            for(InteractiveTheme theme : root){
                if(StringUtils.equals(theme.getNumber(),interactiveTheme.getNumber())) {
                    return JSONObjResult.toJSONObj("当前序号已存在！");
                }
            }
            restMessage = interactiveService.saveInteractiveTheme(interactiveTheme);
            if(restMessage.isSuccess()){
                restMessage.setMessage("保存成功");
            }
            return JSONObjResult.toJSONObj(restMessage);
        } catch (Exception e) {
            log.error("保存信息 异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }
    }

    @ApiOperation(value = "修改互动主题")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "id",value = "主题主键", required = true, dataType = "String"),
            @ApiImplicitParam( name = "themeName",value = "主题名称", required = false, dataType = "String"),
            @ApiImplicitParam( name = "themePic",value = "主题图片", required = false, dataType = "String"),
            @ApiImplicitParam( name = "number",value = "序号", required = false, dataType = "Integer")
    })
    @PostMapping("updateInteractiveTheme")
    @ResponseBody
    public JSONObject updateInteractiveTheme(@Valid InteractiveTheme interactiveTheme){
        log.info("修改互动主题");
        RestMessage restMessage = null;
        try {
            restMessage = interactiveService.updateInteractiveTheme(interactiveTheme);
            if(restMessage.isSuccess()){
                restMessage.setMessage("修改成功");
            }
            return JSONObjResult.toJSONObj(restMessage);
        } catch (Exception e) {
            log.error("修改信息 异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }
    }

    @ApiOperation(value = "删除互动主题")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "id",value = "互动主题主键", required = true, dataType = "String")
    })
    @PostMapping("deleteInteractiveTheme")
    @ResponseBody
    public JSONObject deleteInteractiveTheme(String id){
        log.info("查询互动主题 id=" + id);
        RestMessage restMessage = null;
        try {
            if (StringUtils.isEmpty(id)) {
                return JSONObjResult.toJSONObj("主键不能为空");
            }
            restMessage = interactiveService.deleteInteractiveTheme(id);
            if(restMessage.isSuccess()){
                restMessage.setMessage("删除成功");
            }
            return JSONObjResult.toJSONObj(restMessage);
        }catch (Exception e) {
            log.error("删除互动主题" + e);
            return JSONObjResult.toJSONObj("删除失败");
        }
    }

    @ApiOperation(value = "查询互动主题列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "page_size", name = "page_size", required = false, dataType = "Integer"),
            @ApiImplicitParam(value = "current_page", name = "current_page", required = false, dataType = "Integer")
    })
    @PostMapping("getInteractiveThemeList")
    @ResponseBody
    public JSONObject getInteractiveThemeList(@RequestParam(value = "page_size", defaultValue = "10") Integer page_size,
                                               @RequestParam(value = "current_page", defaultValue = "1") Integer current_page){
        log.info("查询互动主题列表");
        try {
            PageUtil pageUtil = interactiveService.getInteractiveThemeList(page_size,current_page);
            return  JSONObjResult.toJSONObj(pageUtil, true, "查询成功");
        }catch (Exception e) {
            log.error("查询互动主题列表异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }
    }

    @ApiOperation(value = "查询互动主题")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "id",value = "互动主题主键", required = true, dataType = "String")
    })
    @PostMapping("getInteractiveTheme")
    @ResponseBody
    public JSONObject getInteractiveTheme(String id){
        log.info("查询互动主题 id=" + id);
        RestMessage restMessage = null;
        try {
            if (StringUtils.isEmpty(id)) {
                return JSONObjResult.toJSONObj("主键不能为空");
            }
            restMessage = interactiveService.queryInteractiveTheme(id);
            if(restMessage.isSuccess()){
                restMessage.setMessage("查询成功");
            }
            return JSONObjResult.toJSONObj(restMessage);
        }catch (Exception e) {
            log.error("查询互动主题" + e);
            return JSONObjResult.toJSONObj("查询失败");
        }
    }

    @ApiOperation(value = "查询互动内容列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "interactionTitle", value = "互动标题", required = false, dataType = "String"),
            @ApiImplicitParam(name = "interactivThemeFk", value = "互动主题主键", required = true, dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String"),
            @ApiImplicitParam(name = "page_size", value = "page_size", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "current_page", value = "current_page", required = false, dataType = "Integer")
    })
    @PostMapping("getInteractiveContentList")
    @ResponseBody
    public JSONObject getInteractiveContentList(@Valid InteractiveContent interactiveContent,@RequestParam(value = "startTime") String startTime,@RequestParam(value = "endTime") String endTime, @RequestParam(value = "page_size", defaultValue = "10") Integer page_size,
                                                @RequestParam(value = "current_page", defaultValue = "1") Integer current_page){
        log.info("查询互动内容列表");
        try {
            PageUtil pageUtil = interactiveService.getInteractiveContentList(interactiveContent,startTime,endTime,page_size,current_page);
            return  JSONObjResult.toJSONObj(pageUtil, true, "查询成功");
        }catch (Exception e) {
            log.error("查询互动内容列表异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }
    }

    @ApiOperation(value = "查询互动内容详情")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "id",value = "互动内容主键", required = true, dataType = "String")
    })
    @PostMapping("getInteractiveContent")
    @ResponseBody
    public JSONObject getInteractiveContent(String id){
        log.info("查询互动内容 id=" + id);
        RestMessage restMessage = null;
        try {
            if (StringUtils.isEmpty(id)) {
                return JSONObjResult.toJSONObj("主键不能为空");
            }
            restMessage = interactiveService.queryInteractiveContent(id);
            if(restMessage.isSuccess()){
                restMessage.setMessage("查询成功");
            }
            return JSONObjResult.toJSONObj(restMessage);
        }catch (Exception e) {
            log.error("查询互动内容" + e);
            return JSONObjResult.toJSONObj("查询失败");
        }
    }

    @ApiOperation(value = "查询互动评价列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contentId", value = "互动内容主键", required = true, dataType = "String"),
            @ApiImplicitParam(name = "page_size", value = "page_size", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "current_page", value = "current_page", required = false, dataType = "Integer")
    })
    @PostMapping("getInteractiveEvaluationList")
    @ResponseBody
    public JSONObject getInteractiveEvaluationList(@RequestParam(value = "contentId") String contentId,
                                                    @RequestParam(value = "page_size", defaultValue = "10") Integer page_size,
                                                    @RequestParam(value = "current_page", defaultValue = "1") Integer current_page){
        log.info("查询互动内容列表");
        try {
            PageUtil pageUtil = interactiveService.getInteractiveEvaluationList(contentId,page_size,current_page);
            return  JSONObjResult.toJSONObj(pageUtil, true, "查询成功");
        }catch (Exception e) {
            log.error("查询互动内容列表异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }
    }
}
