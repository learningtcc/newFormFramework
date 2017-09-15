package com.drore.cloud.controller;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.service.LeaseService;
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
@Api(value = "旺铺招租-王璐")
@RestController
@RequestMapping("/wechat/Lease")
public class LeaseController {

    @Autowired
    private LeaseService leaseService;

    @ApiOperation(value = "旺铺招租列表",notes = "旺铺招租列表")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_size",value = "每页条数",dataType = "int",required = true),
            @ApiImplicitParam(name = "current_page",value = "当前页",dataType = "int",required = true),
            @ApiImplicitParam(name = "condition",value = "排序条件(price,area)",dataType = "string",required = true),
            @ApiImplicitParam(name = "sort",value = "降序(desc)或升序(asc)",dataType = "string",required = true)
    })
    public RestMessage list(@RequestParam(value = "page_size", defaultValue = "10") Integer page_size,
                            @RequestParam(value = "current_page", defaultValue = "1") Integer current_page,
                            @RequestParam(value = "condition", defaultValue = "create_time") String condition,
                            @RequestParam(value = "sort", defaultValue = "desc") String sort){
        RestMessage restMessage = leaseService.list(page_size, current_page, condition, sort);
        return restMessage;
    }

    @ApiOperation(value = "旺铺招租详情",notes = "旺铺招租详情")
    @ApiImplicitParam(name = "id",value = "主键",dataType = "int",required = true)
    @GetMapping("/detail")
    public RestMessage detail(String id){
        RestMessage restMessage = new RestMessage();
        if (StringUtils.isEmpty(id)){
            restMessage.setSuccess(false);
            restMessage.setMessage("id不能为空");
            return restMessage;
        }
        restMessage = leaseService.detail(id);
        return restMessage;
    }
}
