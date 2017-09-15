package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.exception.CustomException;
import com.drore.model.CommodityEvaluation;
import com.drore.service.CommodityEvaluationService;
import com.drore.util.JSONObjResult;
import com.drore.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangl on 2017/9/8 0008.
 */
@Api(value = "评价管理-王璐")
@RestController
@RequestMapping("/commodityEvaluation/")
public class CommodityEvaluationController {

    @Autowired
    private CommodityEvaluationService commodityEvaluationService;

    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int"),
            @ApiImplicitParam(name = "orderNo", value = "订单编号", dataType = "String"),
            @ApiImplicitParam(name = "auditStatus", value = "审核状态", dataType = "String")
    })
    @PostMapping(value = "queryPage")
    public JSONObject queryPage(@RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "10") int pageSize, CommodityEvaluation commodityEvaluation) {
        String logMsg = "分页查询";
        LogbackLogger.info(logMsg);
        Pagination pagination = new Pagination(pageSize, pageNo);
        pagination = commodityEvaluationService.queryByPage(pagination, commodityEvaluation);
        return JSONObjResult.toJSONObj(new PageUtil(pagination), true, "查询成功");
    }

    @ApiOperation(value = "根据主键查询详情", notes = "根据主键查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int")
    })
    @PostMapping(value = "queryById")
    public JSONObject queryById(@RequestParam String id) {
        String logMsg = "根据主键查询详情";
        LogbackLogger.info(logMsg+id);
        if(StringUtils.isEmpty(id)){
            throw  new CustomException("主键不能为空!");
        }
        CommodityEvaluation commodityEvaluation =  commodityEvaluationService.queryDetailById(id);
        return JSONObjResult.toJSONObj(commodityEvaluation, true, "查询成功");
    }
}
