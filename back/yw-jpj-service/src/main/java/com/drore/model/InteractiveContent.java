/*
* Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
* All rights reserved.
* Official Web Site: http://www.drore.com.
* Developer Web Site: http://open.drore.com.
*/
package com.drore.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.drore.cloud.sdk.exception.IllegalRequestParametersException;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 互动内容表
 * Created by zhangh on 2017/9/4 0001.
 */

public class InteractiveContent extends SystemModel {

    public static String table="interactive_content";

    @SerializedName("interactiv_theme_fk")
    private String interactivThemeFk;

    @SerializedName("Interaction_title")
    private String interactionTitle;

    @SerializedName("comment_num")
    private Integer commentNum;

    @SerializedName("interactive_content")
    private String interactiveContent;

    @SerializedName("audit_status")
    private String auditStatus;

    @SerializedName("audit_explain")
    private String auditExplain;

    @SerializedName("audit_time")
    private String auditTime;

    private String auditor;

    private String publisher;

    @SerializedName("publisher_nickname")
    private String publisherNickname;

    private List<String> picList;

    @SerializedName("publisher_time")
    private String publisherTime;

    @SerializedName("audit_name")
    private String auditName;

    @SerializedName("submit_time")
    private String submitTime;

    @SerializedName("interactiv_theme_name")
    private String interactivThemeName;

    public String getInteractivThemeFk() {
        return interactivThemeFk;
    }

    public void setInteractivThemeFk(String interactivThemeFk) {
        this.interactivThemeFk = interactivThemeFk;
    }

    public String getInteractionTitle() {
        return interactionTitle;
    }

    public void setInteractionTitle(String interactionTitle) {
        this.interactionTitle = interactionTitle;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public String getInteractiveContent() {
        return interactiveContent;
    }

    public void setInteractiveContent(String interactiveContent) {
        this.interactiveContent = interactiveContent;
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
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisherNickname() {
        return publisherNickname;
    }

    public void setPublisherNickname(String publisherNickname) {
        this.publisherNickname = publisherNickname;
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }

    public String getPublisherTime() {
        return publisherTime;
    }

    public void setPublisherTime(String publisherTime) {
        this.publisherTime = publisherTime;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getInteractivThemeName() {
        return interactivThemeName;
    }

    public void setInteractivThemeName(String interactivThemeName) {
        this.interactivThemeName = interactivThemeName;
    }
}
