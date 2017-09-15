package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.annotation.JsonArgument;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.model.StreetCultureInfo;
import com.drore.service.StreetCultureService;
import com.drore.util.JSONObjResult;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by wangl on 2017/9/1 0001.
 */
@Api(value = "街区文化活动-王璐")
@RestController
@RequestMapping("/cms/street")
public class StreetCultureController {

    @Autowired
    private StreetCultureService streetCultureService;


    @ApiOperation(value = "街区文化列表",notes = "街区文化列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title",value = "标题",dataType = "int",required = true),
            @ApiImplicitParam(name = "is_release",value = "发布状态",dataType = "int",required = true),
            @ApiImplicitParam(name = "startTime",value = "开始时间",dataType = "int",required = true),
            @ApiImplicitParam(name = "endTime",value = "结束时间",dataType = "int",required = true),
            @ApiImplicitParam(name = "page_size",value = "每页条数",dataType = "int",required = true),
            @ApiImplicitParam(name = "current_page",value = "当前页",dataType = "int",required = true)

    })
    @GetMapping("/cultureList")
    public JSONObject cultureList(@RequestParam(value = "page_size", defaultValue = "10") Integer page_size,
                                   @RequestParam(value = "current_page", defaultValue = "1") Integer current_page,
                                   @JsonArgument JsonObject params){
        RestMessage restMessage = streetCultureService.cultureList(page_size, current_page, params);
        return JSONObjResult.toJSONObj(restMessage);
    }

    @ApiOperation(value = "添加或修改街区文化活动",notes = "添加或修改街区文化活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id(新增不传，修改传)",dataType = "int",required = false),
            @ApiImplicitParam(name = "title",value = "标题",dataType = "int",required = true),
            @ApiImplicitParam(name = "imageUrl",value = "图片url",dataType = "int",required = true),
            @ApiImplicitParam(name = "videoUrl",value = "视频url",dataType = "int",required = true),
            @ApiImplicitParam(name = "description",value = "详情",dataType = "int",required = true),
            @ApiImplicitParam(name = "isRelease",value = "是否发布(Y/N)",dataType = "int",required = true),
    })
    @PostMapping("/addCulture")
    public JSONObject addCulture(@Valid StreetCultureInfo streetCultureInfo){
        RestMessage restMessage = streetCultureService.addCulture(streetCultureInfo);
        return JSONObjResult.toJSONObj(restMessage);
    }

    @ApiOperation(value = "置顶热门(只有一个)",notes = "置顶热门(只有一个)")
    @ApiImplicitParam(name = "id",value = "id",dataType = "int",required = true)
    @PostMapping("/setTop")
    public JSONObject setTop(String id){
        RestMessage restMessage = streetCultureService.setTop(id);
        return JSONObjResult.toJSONObj(restMessage);
    }

    @ApiOperation(value = "删除",notes = "删除")
    @ApiImplicitParam(name = "id",value = "id",dataType = "int",required = true)
    @PostMapping("/delCulture")
    public JSONObject delCulture(String id){
        RestMessage restMessage = streetCultureService.delCulture(id);
        return JSONObjResult.toJSONObj(restMessage);
    }

    @ApiOperation(value = "启用禁用",notes = "启用禁用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id",dataType = "int",required = true),
            @ApiImplicitParam(name = "status",value = "启用（status=Y），禁用（status=N）",dataType = "string",required = true)
    })
    @PostMapping("/setIsUsing")
    public JSONObject setUsing(String id,String status){
        RestMessage restMessage = streetCultureService.setIsUsing(id,status);
        return JSONObjResult.toJSONObj(restMessage);
    }

    @ApiOperation(value = "详情",notes = "详情")
    @ApiImplicitParam(name = "id",value = "id",dataType = "int",required = true)
    @GetMapping("/detail")
    public JSONObject detail(String id){
        RestMessage restMessage = streetCultureService.detail(id);
        return JSONObjResult.toJSONObj(restMessage);
    }


}
