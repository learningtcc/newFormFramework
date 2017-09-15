/*
* Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
* All rights reserved.
* Official Web Site: http://www.drore.com.
* Developer Web Site: http://open.drore.com.
*/
package com.drore.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 互动评价表
 * Created by zhangh on 2017/09/07 0001.
 */

public class InteractiveEvaluation extends SystemModel {

    public static String table="interactive_content_evaluation";

    @SerializedName("interactive_content_fk")
    private String interactiveContentFk;

    @SerializedName("evaluation_content")
    private String evaluationContent;

    private String reviewer;

    @SerializedName("reviewer_nickname")
    private String reviewerNickname;

    @SerializedName("audit_status")
    private String auditStatus;

    @SerializedName("audit_explain")
    private String auditExplain;

    @SerializedName("audit_time")
    private String auditTime;

    private String Auditor;

    @SerializedName("audit_name")
    private String auditName;

    @SerializedName("evaluation_time")
    private String evaluationTime;

    /**
     *图集 ，临时字段
     */
    @Expose(serialize=false)
    private List<String> picList;

    /**
     *互动标题
     */
    @Expose(serialize=false)
    private String InteractionTitle;

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }

    public String getInteractionTitle() {
        return InteractionTitle;
    }

    public void setInteractionTitle(String interactionTitle) {
        InteractionTitle = interactionTitle;
    }

    public String getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(String evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public String getInteractiveContentFk() {
        return interactiveContentFk;
    }

    public void setInteractiveContentFk(String interactiveContentFk) {
        this.interactiveContentFk = interactiveContentFk;
    }

    public String getEvaluationContent() {
        return evaluationContent;
    }

    public void setEvaluationContent(String evaluationContent) {
        this.evaluationContent = evaluationContent;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getReviewerNickname() {
        return reviewerNickname;
    }

    public void setReviewerNickname(String reviewerNickname) {
        this.reviewerNickname = reviewerNickname;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditExplain() {
        return auditExplain;
    }

    public void setAuditExplain(String auditExplain) {
        this.auditExplain = auditExplain;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditor() {
        return Auditor;
    }

    public void setAuditor(String auditor) {
        Auditor = auditor;
    }
}
