package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.model.CommodityEvaluation;
import com.drore.model.InteractiveContent;
import com.drore.model.InteractiveEvaluation;
import com.drore.model.MessageInfo;
import com.drore.service.AuditManagementService;
import com.drore.service.MessageService;
import com.drore.util.JSONObjResult;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;
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

/**
 * Created by zhangh on 2017/9/7 0001.
 */
@Api(value = "审核管理-张豪")
@RestController
@Controller
@RequestMapping("/auditManagement/")
public class AuditManagementController {

    private Logger log = LoggerFactory.getLogger(AuditManagementController.class);

    @Autowired
    private AuditManagementService auditManagementService;

    @ApiOperation(value = "查询互动评价详情", notes = "查询互动评价详情")
    @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "string")
    @GetMapping("queryById")
    public JSONObject queryById(String id) {
        String logMsg = "查询信息";
        LogbackLogger.info(logMsg + id);
        if (StringUtils.isEmpty(id)) {
            return JSONObjResult.toJSONObj("主键不能为空!");
        }
       InteractiveEvaluation interactiveEvaluation = auditManagementService.queryDetailById(id);
        return JSONObjResult.toJSONObj(interactiveEvaluation,true,null);
    }


    @ApiOperation(value = "修改互动内容审核状态")
    @PostMapping("updateInteractiveContentAudit")
    @ResponseBody
    public JSONObject updateInteractiveContentAudit(@ModelAttribute InteractiveContent interactiveContent){
            log.info("修改互动内容审核状态");
            RestMessage restMessage = null;
            try {
            restMessage = auditManagementService.updateInteractiveContentAudit(interactiveContent);
            if(restMessage.isSuccess()){
            restMessage.setMessage("修改成功");
            }
            return JSONObjResult.toJSONObj(restMessage);
            } catch (Exception e) {
            log.error("修改互动内容审核状态 异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
            }
    }

    @ApiOperation(value = "修改互动内容评价审核状态")
    @PostMapping("updateInteractiveEvaluationAudit")
    @ResponseBody
    public JSONObject updateInteractiveEvaluationAudit(@ModelAttribute InteractiveEvaluation interactiveEvaluation){
        log.info("修改互动内容评价审核状态");
        RestMessage restMessage = null;
        try {
            restMessage = auditManagementService.updateInteractiveEvaluationAudit(interactiveEvaluation);
            if(restMessage.isSuccess()){
                restMessage.setMessage("修改成功");
            }
            return JSONObjResult.toJSONObj(restMessage);
        } catch (Exception e) {
            log.error("修改互动内容评价审核状态 异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }
    }

    @ApiOperation(value = "修改商品(订单)评价信息审核状态")
    @PostMapping("updateCommodityEvaluationAudit")
    @ResponseBody
    public JSONObject updateCommodityEvaluationAudit(@ModelAttribute CommodityEvaluation commodityEvaluation){
        log.info("修改商品(订单)评价信息审核状态");
        RestMessage restMessage = null;
        try {
            restMessage = auditManagementService.updateCommodityEvaluationAudit(commodityEvaluation);
            if(restMessage.isSuccess()){
                restMessage.setMessage("修改成功");
            }
            return JSONObjResult.toJSONObj(restMessage);
        } catch (Exception e) {
            log.error("修改商品(订单)评价信息审核状态 异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }
    }

    @ApiOperation(value = "查询互动内容审核列表")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "auditStatus",value = "审核状态(待审核：PendingAudit|审核通过：Audited|审核未通过：AuditFail)", required = false, dataType = "String"),
            @ApiImplicitParam( name = "startTime",value = "开始时间", required = false, dataType = "String"),
            @ApiImplicitParam( name = "endTime",value = "结束时间", required = false, dataType = "String"),
            @ApiImplicitParam(value = "page_size", name = "page_size", required = false, dataType = "Integer"),
            @ApiImplicitParam(value = "current_page", name = "current_page", required = false, dataType = "Integer")
    })
    @PostMapping("getInteractiveContentAuditList")
    @ResponseBody
    public JSONObject getInteractiveContentAuditList(
            @RequestParam(value = "auditStatus") String auditStatus,
            @RequestParam(value = "startTime") String startTime,
            @RequestParam(value = "endTime") String endTime,
            @RequestParam(value = "page_size", defaultValue = "10") Integer page_size,
            @RequestParam(value = "current_page", defaultValue = "1") Integer current_page){
        log.info("查询互动内容审核列表");
        try {
            PageUtil pageUtil = auditManagementService.getInteractiveContentAuditList(auditStatus,startTime,endTime,page_size,current_page);
            return  JSONObjResult.toJSONObj(pageUtil, true, "查询成功");
        }catch (Exception e) {
            log.error("查询互动内容审核列表" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }
    }

    @ApiOperation(value = "查询互动内容评价审核列表")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "auditStatus",value = "审核状态(待审核：PendingAudit|审核通过：Audited|审核未通过：AuditFail)", required = false, dataType = "String"),
            @ApiImplicitParam( name = "startTime",value = "开始时间", required = false, dataType = "String"),
            @ApiImplicitParam( name = "endTime",value = "结束时间", required = false, dataType = "String"),
            @ApiImplicitParam(value = "page_size", name = "page_size", required = false, dataType = "Integer"),
            @ApiImplicitParam(value = "current_page", name = "current_page", required = false, dataType = "Integer")
    })
    @PostMapping("getInteractiveEvaluationAuditList")
    @ResponseBody
    public JSONObject getInteractiveEvaluationAuditList(
            @RequestParam(value = "auditStatus") String auditStatus,
            @RequestParam(value = "startTime") String startTime,
            @RequestParam(value = "endTime") String endTime,
            @RequestParam(value = "page_size", defaultValue = "10") Integer page_size,
            @RequestParam(value = "current_page", defaultValue = "1") Integer current_page){
        log.info("查询互动内容评价审核列表");
        try {
            PageUtil pageUtil = auditManagementService.getInteractiveEvaluationAuditList(auditStatus,startTime,endTime,page_size,current_page);
            return  JSONObjResult.toJSONObj(pageUtil, true, "查询成功");
        }catch (Exception e) {
            log.error("查询互动内容评价审核列表" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }
    }

    @ApiOperation(value = "查询商品(订单)评价信息表审核列表")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "auditStatus",value = "审核状态(待审核：PendingAudit|审核通过：Audited|审核未通过：AuditFail)", required = false, dataType = "String"),
            @ApiImplicitParam( name = "startTime",value = "开始时间", required = false, dataType = "String"),
            @ApiImplicitParam( name = "endTime",value = "结束时间", required = false, dataType = "String"),
            @ApiImplicitParam(value = "page_size", name = "page_size", required = false, dataType = "Integer"),
            @ApiImplicitParam(value = "current_page", name = "current_page", required = false, dataType = "Integer")
    })
    @PostMapping("getCommodityEvaluationAuditList")
    @ResponseBody
    public JSONObject getCommodityEvaluationAuditList(
            @RequestParam(value = "auditStatus") String auditStatus,
            @RequestParam(value = "startTime") String startTime,
            @RequestParam(value = "endTime") String endTime,
            @RequestParam(value = "page_size", defaultValue = "10") Integer page_size,
            @RequestParam(value = "current_page", defaultValue = "1") Integer current_page){
        log.info("查询商品(订单)评价信息表审核列表");
        try {
            PageUtil pageUtil = auditManagementService.getCommodityEvaluationAuditList(auditStatus,startTime,endTime,page_size,current_page);
            return  JSONObjResult.toJSONObj(pageUtil, true, "查询成功");
        }catch (Exception e) {
            log.error("查询商品(订单)评价信息表审核列表" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }
    }

    @ApiOperation(value = "获取审核状态枚举")
    @PostMapping(value = "getAuditStatusList")
    public JSONObject getAuditStatusList() {
        String logMsg = "获取审核状态枚举";
        LogbackLogger.info(logMsg);
        RestMessageModel model = new RestMessageModel();
        model.setSuccess(true);
        model.setData(auditManagementService.getAuditStatusList());
        return JSONObjResult.toJSONObj(model);
    }
}
