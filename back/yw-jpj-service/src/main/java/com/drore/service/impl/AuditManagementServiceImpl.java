package com.drore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.common.util.DateUtil;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.enums.CommonEnum;
import com.drore.exception.CustomException;
import com.drore.model.*;
import com.drore.service.AuditManagementService;
import com.drore.service.ImageInfoService;
import com.drore.service.MessageService;
import com.drore.util.PageUtil;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangh on 2017/9/4 0001.
 */

@Service
public class AuditManagementServiceImpl implements AuditManagementService {

    @Autowired
    private CloudQueryRunner run;

    @Autowired
    private ImageInfoService imageInfoService;

    @Autowired
    private MessageService messageService;


    @Override
    public InteractiveEvaluation queryDetailById(String id) {
        InteractiveEvaluation interactiveEvaluation = run.queryOne(InteractiveEvaluation.class, InteractiveEvaluation.table, id);
        if(interactiveEvaluation==null){
            throw  new CustomException("未发现查找对象!");
        }
        interactiveEvaluation.setPicList(imageInfoService.findPics(InteractiveEvaluation.table,id));
        return  interactiveEvaluation;
    }

    @Override
    public RestMessage updateInteractiveContentAudit(InteractiveContent interactiveContent) {
        //审核人...
        String auditor = "当前用户主键";
        String audit_name = "当前用户昵称";
        //审核时间
        String auditTime = DateUtil.format("yyyy-MM-dd HH:mm:ss");
        String auditExplain = interactiveContent.getAuditExplain();
        String auditStatus = interactiveContent.getAuditStatus();
        ImmutableMap<String, String> immutableMap = ImmutableMap.of("audit_explain", auditExplain, "audit_status",
                auditStatus, "audit_time", auditTime,"auditor",auditor,"audit_name",audit_name);

        RestMessage restMessage = run.update(InteractiveContent.table,interactiveContent.getId(),immutableMap);

        if(restMessage.isSuccess()){
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setTitle("系统消息");
            messageInfo.setIntroduction("您有新的互动审核消息，请点击查看");
            messageInfo.setContent("亲爱的用户，您于"+interactiveContent.getSubmitTime()+"提交审核的互动未通过审核，请查看");
            if("Audited".equals(interactiveContent.getAuditStatus())){
                messageInfo.setContent("亲爱的用户，您于"+interactiveContent.getSubmitTime()+"提交审核的互动已经通过审核，请查看");
            }
            messageInfo.setTime(DateUtil.format(new Date(),"yyyy-MM-dd"));
            messageInfo.setIsRead("N");
            messageService.saveMessage(messageInfo);
        }

        return restMessage;
    }

    @Override
    public RestMessage updateInteractiveEvaluationAudit(InteractiveEvaluation interactiveEvaluation) {
        //审核人...
        String auditor = "当前用户主键";
        String audit_name = "当前用户昵称";

        //审核时间
        String auditTime = DateUtil.format("yyyy-MM-dd HH:mm:ss");
        String auditExplain = interactiveEvaluation.getAuditExplain();
        String auditStatus = interactiveEvaluation.getAuditStatus();
        ImmutableMap<String, String> immutableMap =  ImmutableMap.of("audit_explain",auditExplain,"audit_status",
                auditStatus,"audit_time",auditTime,"auditor",auditor,"audit_name",audit_name);

        return run.update(InteractiveEvaluation.table,interactiveEvaluation.getId(),immutableMap);
    }

    @Override
    public RestMessage updateCommodityEvaluationAudit(CommodityEvaluation commodityEvaluation) {
        //审核人...
        String auditor = "当前用户主键";
        String audit_name = "当前用户昵称";
        //审核时间
        String auditTime = DateUtil.format("yyyy-MM-dd HH:mm:ss");
        String auditExplain = commodityEvaluation.getAuditExplain();
        String auditStatus = commodityEvaluation.getAuditStatus();
        ImmutableMap<String, String> immutableMap = ImmutableMap.of("audit_explain",auditExplain,"audit_status",
                auditStatus,"audit_time",auditTime,"auditor",auditor,"audit_name",audit_name);

        return run.update(CommodityEvaluation.table,commodityEvaluation.getId(),immutableMap);
    }

    @Override
    public PageUtil getInteractiveContentAuditList(String auditStatus, String startTime, String endTime,Integer page_size, Integer current_page) {
        //动态拼接sql语句
        StringBuffer sql = new StringBuffer("select * from interactive_content where is_deleted='N'");
        if(StringUtils.isNotEmpty(auditStatus)){
            sql.append(" and audit_status = '"+auditStatus+"'");
        }
        if(StringUtils.isNotEmpty(startTime)){
            sql.append(" and audit_time >= '" + startTime +" 00:00:00'");
        }
        if(StringUtils.isNotEmpty(endTime)){
            sql.append(" and audit_time <= '" + endTime +" 23:59:59'");
        }
        sql.append(" order by create_time desc");
        Pagination<InteractiveContent> contentPagination = run.sql(InteractiveContent.class, sql.toString(), current_page, page_size);
        PageUtil pageUtil = new PageUtil(contentPagination);
        return pageUtil;
    }

    @Override
    public PageUtil getInteractiveEvaluationAuditList(String auditStatus, String startTime, String endTime, Integer page_size, Integer current_page) {
        //动态拼接sql语句
        StringBuffer sql = new StringBuffer("select * from interactive_content_evaluation where is_deleted='N'");
        if(StringUtils.isNotEmpty(auditStatus)){
            sql.append(" and audit_status = '"+auditStatus+"'");
        }
        if(StringUtils.isNotEmpty(startTime)){
            sql.append(" and audit_time >= '" + startTime + " 00:00:00'");
        }
        if(StringUtils.isNotEmpty(endTime)){
            sql.append(" and audit_time <= '" + endTime + " 23:59:59'");
        }
        sql.append(" order by create_time desc");
        Pagination<InteractiveEvaluation> evaluationPagination = run.sql(InteractiveEvaluation.class, sql.toString(), current_page, page_size);
        for (int i = 0; i < evaluationPagination.getData().size(); i++) {
            String interactiveContentFk = evaluationPagination.getData().get(i).getInteractiveContentFk();
            InteractiveContent interactiveContent = run.queryOne(InteractiveContent.class, InteractiveContent.table, interactiveContentFk);
            String interactionTitle = interactiveContent.getInteractionTitle();
            evaluationPagination.getData().get(i).setInteractionTitle(interactionTitle);
        }
        PageUtil pageUtil = new PageUtil(evaluationPagination);
        return pageUtil;
    }

    @Override
    public PageUtil getCommodityEvaluationAuditList(String auditStatus, String startTime, String endTime, Integer page_size, Integer current_page) {
        //动态拼接sql语句
        StringBuffer sql = new StringBuffer("select * from commodity_evaluation where is_deleted='N'");
        if(StringUtils.isNotEmpty(auditStatus)){
            sql.append(" and audit_status = '"+auditStatus + "'");
        }
        if(StringUtils.isNotEmpty(startTime)){
            sql.append(" and audit_time >= '" + startTime + "'");
        }
        if(StringUtils.isNotEmpty(endTime)){
            sql.append(" and audit_time <= '" + endTime + "'");
        }
        sql.append(" order by create_time desc");
        Pagination<CommodityEvaluation> commodityEvaluationPagination = run.sql(CommodityEvaluation.class, sql.toString(), current_page, page_size);
        PageUtil pageUtil = new PageUtil(commodityEvaluationPagination);
        return pageUtil;
    }

    @Override
    public List<JSONObject> getAuditStatusList() {
        CommonEnum.AuditStatus [] auditStatuses = CommonEnum.AuditStatus.values();
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (CommonEnum.AuditStatus ord : auditStatuses) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", ord.getValue());
            jsonObject.put("name", ord.getName());
            list.add(jsonObject);
        }
        return  list;
    }

}