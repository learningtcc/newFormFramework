package com.drore.cloud.controller;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.service.ThemeActivitiesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangl on 2017/9/4 0004.
 */
@Api(value = "主题活动-王璐")
@RestController
@RequestMapping("/wechat/themeActivities")
public class ThemeActivitiesController {

    @Autowired
    private ThemeActivitiesService themeActivitiesService;

    @ApiOperation(value = "主题活动列表",notes = "主题活动列表")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_size",value = "每页条数",dataType = "int",required = true),
            @ApiImplicitParam(name = "current_page",value = "当前页",dataType = "int",required = true),
            @ApiImplicitParam(name = "type",value = "类型",dataType = "string",required = true),
            @ApiImplicitParam(name = "keyword",value = "关键字",dataType = "string",required = true)
    })
    public RestMessage list(@RequestParam(value = "page_size", defaultValue = "10") Integer page_size,
                            @RequestParam(value = "current_page", defaultValue = "1") Integer current_page,
                            String type,String keyword){
        RestMessage restMessage = themeActivitiesService.list(page_size, current_page,type,keyword);
        return restMessage;
    }

    @ApiOperation(value = "主题活动详情",notes = "主题活动详情")
    @GetMapping("/detail")
    @ApiImplicitParam(name = "id",value = "id",dataType = "string",required = true)
    public RestMessage detail(String id){
        RestMessage restMessage = new RestMessage();
        if (StringUtils.isEmpty(id)){
            restMessage.setSuccess(false);
            restMessage.setMessage("id不能为空");
            return restMessage;
        }
        restMessage = themeActivitiesService.detail(id);
        return restMessage;
    }
}
