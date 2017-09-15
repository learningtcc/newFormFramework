package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.model.LeaseInfo;
import com.drore.service.LeaseService;
import com.drore.util.JSONObjResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by wangl on 2017/9/6 0006.
 */
@Api(value = "旺铺招租-王璐")
@RestController
@Controller
@RequestMapping("/lease/")
public class LeaseController {

    @Autowired
    private LeaseService leaseService;

    @ApiOperation(value = "旺铺招租列表")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "title",value = "标题", required = true, dataType = "String"),
            @ApiImplicitParam( name = "storeName",value = "商铺名", required = true, dataType = "String"),
            @ApiImplicitParam( name = "type",value = "商铺类型", required = true, dataType = "String"),
            @ApiImplicitParam( name = "startTime",value = "开始时间", required = true, dataType = "String"),
            @ApiImplicitParam( name = "endTime",value = "结束时间", required = true, dataType = "String"),
            @ApiImplicitParam( name = "pageSize",value = "每页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam( name = "pageNo",value = "当前页", required = true, dataType = "Integer")
    })
    @GetMapping("list")
    @ResponseBody
    public JSONObject list(@Valid LeaseInfo leaseInfo,
                                      @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "current_page", defaultValue = "1") Integer pageNo){
        RestMessage list = leaseService.list(leaseInfo, pageSize, pageNo);
        return JSONObjResult.toJSONObj(list);
    }

    @ApiOperation(value = "置顶热门(只有一个)",notes = "置顶热门(只有一个)")
    @ApiImplicitParam(name = "id",value = "id",dataType = "int",required = true)
    @PostMapping("/setTop")
    public JSONObject  setTop(String id){
        RestMessage restMessage = leaseService.setTop(id);
        return JSONObjResult.toJSONObj(restMessage);
    }

    @ApiOperation(value = "启用禁用",notes = "启用禁用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id",dataType = "int",required = true),
            @ApiImplicitParam(name = "status",value = "启用（status=Y），禁用（status=N）",dataType = "string",required = true)
    })
    @PostMapping("/setIsUsing")
    public JSONObject setUsing(String id,String status){
        RestMessage restMessage = leaseService.setIsUsing(id,status);
        return JSONObjResult.toJSONObj(restMessage);
    }

    @ApiOperation(value = "详情",notes = "详情")
    @ApiImplicitParam(name = "id",value = "id",dataType = "int",required = true)
    @GetMapping("/detail")
    public JSONObject detail(String id){
        RestMessage restMessage = leaseService.detail(id);
        return JSONObjResult.toJSONObj(restMessage);
    }
}
