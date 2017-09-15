package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.model.LeaseInfo;
import com.drore.service.LeaseService;
import com.drore.util.JSONObjResult;
import com.drore.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by wangl on 2017/8/31 0031.
 */
@Api(value = "招租-王璐")
@RestController
@RequestMapping("/cms/lease")
public class LeaseController {

    @Autowired
    private LeaseService leaseService;

    @ApiOperation(value = "添加招租信息",notes = "添加招租信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id",dataType = "string",required = false),
            @ApiImplicitParam(name = "title",value = "标题",dataType = "string",required = true),
            @ApiImplicitParam(name = "type",value = "类型",dataType = "string",required = true),
            @ApiImplicitParam(name = "contact_tel",value = "联系方式",dataType = "string",required = true),
            @ApiImplicitParam(name = "imgUrls",value = "图片url集合(逗号分隔)",dataType = "string",required = true),
            @ApiImplicitParam(name = "area",value = "面积",dataType = "double",required = true),
            @ApiImplicitParam(name = "price",value = "价格",dataType = "double",required = true),
            @ApiImplicitParam(name = "address",value = "地址",dataType = "string",required = true),
            @ApiImplicitParam(name = "latitude",value = "纬度",dataType = "string",required = true),
            @ApiImplicitParam(name = "longitude",value = "经度",dataType = "string",required = true),
            @ApiImplicitParam(name = "describes",value = "描述",dataType = "string",required = true),
            @ApiImplicitParam(name = "pics",value = "图集",dataType = "string",required = true)
    })
    @PostMapping("/addOrUpdate")
    public JSONObject addLeasing(@Valid LeaseInfo leaseInfo){
        RestMessage restMessage = leaseService.addOrUpdate(leaseInfo);
        return JSONObjResult.toJSONObj(restMessage);
    }

    @ApiOperation(value = "招租信息列表",notes = "招租信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title",value = "标题",dataType = "string",required = true),
            @ApiImplicitParam(name = "startTime",value = "开始时间",dataType = "string",required = true),
            @ApiImplicitParam(name = "endTime",value = "结束时间",dataType = "string",required = true)
    })
    @GetMapping("/leasingList")
    public JSONObject  leasingList(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                   @Valid LeaseInfo leaseInfo){
        String logMsg = "分页查询";
        LogbackLogger.info(logMsg);
        Pagination pagination = new Pagination(pageSize, pageNo);
        pagination = leaseService.leasingList(pagination, leaseInfo);
        return JSONObjResult.toJSONObj(new PageUtil(pagination), true, "查询成功");
    }

    @ApiOperation(value = "详情",notes = "详情")
    @ApiImplicitParam(name = "id",value = "id",dataType = "string",required = true)
    @GetMapping("/detail")
    public JSONObject detail(String id){
        RestMessage restMessage = leaseService.detail(id);
        return JSONObjResult.toJSONObj(restMessage);
    }

    @ApiOperation(value = "发布/撤下",notes = "发布/撤下")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id",value = "id",dataType = "string",required = true),
        @ApiImplicitParam(name = "status",value = "Y/N",dataType = "string",required = true)
    })
    @PostMapping("/setIsPublish")
    public JSONObject setIsPublish(String id,String status){
        RestMessage restMessage = leaseService.setIsPublish(id,status);
        return JSONObjResult.toJSONObj(restMessage);
    }
}
