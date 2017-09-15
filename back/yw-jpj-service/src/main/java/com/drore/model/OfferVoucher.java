/*
* Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
* All rights reserved.
* Official Web Site: http://www.drore.com.
* Developer Web Site: http://open.drore.com.
*/
package com.drore.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.google.gson.annotations.SerializedName;

/**
 * 优惠活动表
 * Created by zhangh on 2017/9/4 0001.
 */

public class OfferVoucher extends SystemModel {

    public static String table="offer_voucher";

    @SerializedName("store_id")
    private String storeId;

    @SerializedName("offer_name")
    private String offerName;

    private String type;

    private Double full;

    private Double less;

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("end_time")
    private String endTime;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("is_enable")
    private String isEnable;

    @SerializedName("enable_time")
    private String enableTime;

    @SerializedName("enable_user_id")
    private String enableUserId;

    @SerializedName("store_name")
    private String storeName;

    @SerializedName("offer_status")
    private String offerStatus;

    @SerializedName("surplus_time")
    private String surplusTime;

    @SerializedName("publish_time")
    private String publishTime;

    private Double discount;

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getFull() {
        return full;
    }

    public void setFull(Double full) {
        this.full = full;
    }

    public Double getLess() {
        return less;
    }

    public void setLess(Double less) {
        this.less = less;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getEnableTime() {
        return enableTime;
    }

    public void setEnableTime(String enableTime) {
        this.enableTime = enableTime;
    }

    public String getEnableUserId() {
        return enableUserId;
    }

    public void setEnableUserId(String enableUserId) {
        this.enableUserId = enableUserId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }

    public String getSurplusTime() {
        return surplusTime;
    }

    public void setSurplusTime(String surplusTime) {
        this.surplusTime = surplusTime;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
