package com.drore.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.google.gson.annotations.SerializedName;

/**
 * 商品表
 * Created by wangl on 2017/9/1 0001.
 */
public class CommodityEvaluation extends SystemModel{

    public static String table = "commodity_evaluation";

    private String interactiveContentFk;

    @SerializedName("order_no")
    private String orderNo;

    @SerializedName("order_id")
    private String orderId;

    @SerializedName("commodity_id")
    private String commodityId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("commodity_name")
    private String commodityName;

    private String points;

    private String content;

    @SerializedName("user_image")
    private String userImage;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("store_id")
    private String storeId;

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

    public String getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(String evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    public String getInteractiveContentFk() {
        return interactiveContentFk;
    }

    public void setInteractiveContentFk(String interactiveContentFk) {
        this.interactiveContentFk = interactiveContentFk;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }
}
