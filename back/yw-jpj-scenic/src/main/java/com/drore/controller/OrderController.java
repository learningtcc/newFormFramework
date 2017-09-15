package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.exception.CustomException;
import com.drore.model.OrderInfo;
import com.drore.service.OrderService;
import com.drore.util.JSONObjResult;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by wangl on 2017/9/7 0007.
 */
@Api(value = "订单-王璐")
@RestController
@Controller
@RequestMapping("/order/")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int"),
            @ApiImplicitParam(name = "orderNo", value = "订单编号",  dataType = "String"),
            @ApiImplicitParam(name = "orderStatus", value = "订单状态",  dataType = "String")
    })
    @GetMapping(value = "queryPage")
    public JSONObject queryPage(@RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "10") int pageSize,@Valid OrderInfo orderInfo) {
        String logMsg = "分页查询";
        LogbackLogger.info(logMsg);
        Pagination pagination = new Pagination(pageSize, pageNo);
        pagination = orderService.queryByPage(pagination, orderInfo);
        return JSONObjResult.toJSONObj(new PageUtil(pagination), true, "查询成功");
    }

    @ApiOperation(value = "详情", notes = "详情")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int")
    @PostMapping(value = "queryById")
    public JSONObject queryById(@RequestParam String id) {
        String logMsg = "根据主键查询详情";
        LogbackLogger.info(logMsg+id);
        if(StringUtils.isEmpty(id)){
            throw  new CustomException("主键不能为空!");
        }
        OrderInfo orderInfo = orderService.queryDetailById(id);
        return JSONObjResult.toJSONObj(orderInfo, true, "查询成功");
    }

    @ApiOperation(value = "获取订单状态枚举")
    @PostMapping(value = "getOrderStatusList")
    public JSONObject getOrderStatusList() {
        String logMsg = "获取订单状态枚举";
        LogbackLogger.info(logMsg);
        RestMessageModel model = new RestMessageModel();
        model.setSuccess(true);
        model.setData(orderService.getOrderStatusList());
        return JSONObjResult.toJSONObj(model);
    }
}
