package com.drore.cloud.controller;

import com.drore.cloud.model.InteractiveContent;
import com.drore.cloud.model.InteractiveEvaluation;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.service.InteractiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by zhangh on 2017/9/12 0001.
 */
@Api(value = "互动-张豪")
@RestController
@Controller
@RequestMapping("/wechat/interactive/")
public class InteractiveController {

    private Logger log = LoggerFactory.getLogger(InteractiveController.class);

    @Autowired
    private InteractiveService interactiveService;

    @ApiOperation(value = "保存互动内容")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "interactivThemeFk",value = "主题主键", required = true, dataType = "String"),
            @ApiImplicitParam( name = "interactivThemeName",value = "主题名称", required = true, dataType = "String"),
            @ApiImplicitParam( name = "interactionTitle",value = "互动标题", required = true, dataType = "String"),
            @ApiImplicitParam( name = "interactiveContent",value = "互动内容", required = true, dataType = "String"),
            @ApiImplicitParam( name = "picList",value = "图集", required = true, dataType = "List<String>")
    })
    @PostMapping("saveInteractiveContent")
    @ResponseBody
    public RestMessage saveInteractiveContent(@Valid InteractiveContent interactiveContent){

        return interactiveService.saveInteractiveContent(interactiveContent);
    }

    @ApiOperation(value = "保存互动评价")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "interactiveContentFk",value = "内容主键", required = true, dataType = "String"),
            @ApiImplicitParam( name = "evaluationContent",value = "评价内容", required = true, dataType = "String")
    })
    @PostMapping("saveInteractiveEvaluation")
    @ResponseBody
    public RestMessage saveInteractiveEvaluation(@Valid InteractiveEvaluation interactiveEvaluation){

        return interactiveService.saveInteractiveEvaluation(interactiveEvaluation) ;
    }

    @ApiOperation(value = "查询互动主题列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "page_size", name = "page_size", required = false, dataType = "Integer"),
            @ApiImplicitParam(value = "current_page", name = "current_page", required = false, dataType = "Integer")
    })
    @PostMapping("getInteractiveThemeList")
    @ResponseBody
    public RestMessage getInteractiveThemeList(@RequestParam(value = "page_size", defaultValue = "10") Integer page_size,
                                               @RequestParam(value = "current_page", defaultValue = "1") Integer current_page){

        return interactiveService.getInteractiveThemeList(page_size,current_page);
    }

    @ApiOperation(value = "查询互动内容列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "interactivThemeFk", value = "互动主题主键", required = true, dataType = "String"),
            @ApiImplicitParam(name = "page_size", value = "page_size", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "current_page", value = "current_page", required = false, dataType = "Integer")
    })
    @PostMapping("getInteractiveContentList")
    @ResponseBody
    public RestMessage getInteractiveContentList(@RequestParam(value = "interactivThemeFk") String interactivThemeFk, @RequestParam(value = "page_size", defaultValue = "10") Integer page_size,
                                                @RequestParam(value = "current_page", defaultValue = "1") Integer current_page){

            return interactiveService.getInteractiveContentList(interactivThemeFk,page_size,current_page);
    }

    @ApiOperation(value = "查询互动内容详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "互动内容主键", required = true, dataType = "String"),
            @ApiImplicitParam(name = "page_size", value = "page_size(互动评价分页)", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "current_page", value = "current_page(互动评价分页)", required = false, dataType = "Integer")
    })
    @PostMapping("getInteractiveContent")
    @ResponseBody
    public RestMessage getInteractiveContent(@RequestParam(value = "id") String id, @RequestParam(value = "page_size", defaultValue = "10") Integer page_size,
                                             @RequestParam(value = "current_page", defaultValue = "1") Integer current_page){

            return interactiveService.queryInteractiveContent(id,page_size,current_page);
    }

    @ApiOperation(value = "删除互动内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "互动内容主键", required = true, dataType = "String")
    })
    @PostMapping("deleteInteractiveContent")
    @ResponseBody
    public RestMessage deleteInteractiveContent(@RequestParam(value = "id") String id){
        if(StringUtils.isEmpty(id)){
            RestMessage restMessage = new RestMessage();
            restMessage.setMessage("没有主键");
            return restMessage;
        }
        return interactiveService.deleteInteractiveContent(id);
    }
}
