package com.drore.service;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.model.*;
import com.drore.util.PageUtil;

import java.util.List;

/**
 * Created by zhangh on 2017/09/07 0001.
 */
public interface AuditManagementService {


    /**
     * 互动内容评价详情
     */
    InteractiveEvaluation queryDetailById(String id);

    /**
     * 修改互动内容审核状态
     * @param interactiveContent
     * @return
     */
    public RestMessage updateInteractiveContentAudit(InteractiveContent interactiveContent);

    /**
     * 修改互动评价审核状态
     * @param interactiveEvaluation
     * @return
     */
    public RestMessage updateInteractiveEvaluationAudit(InteractiveEvaluation interactiveEvaluation);

    /**
     * 修改商品评价审核状态
     * @param commodityEvaluation
     * @return
     */
    public RestMessage updateCommodityEvaluationAudit(CommodityEvaluation commodityEvaluation);

    /**
     * 获取互动内容审核状态列表
     * @param auditStatus
     * @param startTime
     * @param endTime
     * @return
     */
    public PageUtil getInteractiveContentAuditList(String auditStatus,String startTime,String endTime,Integer page_size, Integer current_page);

    /**
     * 获取互动评价审核状态列表
     * @param auditStatus
     * @param startTime
     * @param endTime
     * @return
     */
    public PageUtil getInteractiveEvaluationAuditList(String auditStatus,String startTime,String endTime,Integer page_size, Integer current_page);

    /**
     * 获取商品评价审核状态列表
     * @param auditStatus
     * @param startTime
     * @param endTime
     * @return
     */
    public PageUtil getCommodityEvaluationAuditList(String auditStatus,String startTime,String endTime,Integer page_size, Integer current_page);

    /**
     * 获取审核状态枚举
     * @return
     */
    public List<JSONObject> getAuditStatusList();

}
