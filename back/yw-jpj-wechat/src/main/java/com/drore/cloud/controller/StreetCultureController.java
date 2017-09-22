package com.drore.cloud.controller;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.service.StreetCultureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangl on 2017/9/4 0004.
 */
@Api(value = "街区文化活动-王璐")
@RestController
@RequestMapping("/wechat/streetCulture")
public class StreetCultureController {

    @Autowired
    private StreetCultureService streetCultureService;

    @ApiOperation(value = "街区文化列表",notes = "街区文化列表")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_size",value = "每页条数",dataType = "int",required = true),
            @ApiImplicitParam(name = "current_page",value = "当前页",dataType = "int",required = true)
    })
    public RestMessage list(@RequestParam(value = "page_size", defaultValue = "10") Integer page_size,
                            @RequestParam(value = "current_page", defaultValue = "1") Integer current_page){
        RestMessage restMessage = streetCultureService.list(page_size, current_page);
        return restMessage;
    }

    @ApiOperation(value = "浏览次数",notes = "浏览次数")
    @GetMapping("/addClicks")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "街区文化主键",dataType = "String",required = true)
    })
    public RestMessage addClicks(@RequestParam(value = "id") String id){
        RestMessage restMessage = streetCultureService.addClicks(id);
        return restMessage;
    }
}
