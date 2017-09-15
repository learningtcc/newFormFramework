/*
 * Copyright (C) 2017 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */

package com.drore.cloud.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

/***
 * 地址
 * @since:hy-api 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2017/03/23 14:23
 */
public class AddressInfo extends SystemModel implements Comparable<AddressInfo>{
    @ApiModelProperty("收货地址")
    @SerializedName("receipt_add")
    private String receiptAdd;
    @ApiModelProperty("详细地址")
    private String address;
    @ApiModelProperty("收货人")
    private String receiver;
    @ApiModelProperty("联系方式")
    private String phone;
    @ApiModelProperty("是否默认地址")
    @SerializedName("is_default")
    private String isDefault;
    @SerializedName("member_id")
    private String memberId;

    public AddressInfo() {
    }

    public AddressInfo(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getReceiptAdd() {return receiptAdd;}

    public String getAddress() {return address;}

    public String getReceiver() {return receiver;}

    public String getPhone() {return phone;}

    public String getIsDefault() {return isDefault;}

    public String getMemberId() {return memberId;}

    public void setReceiptAdd(String receiptAdd) {this.receiptAdd = receiptAdd;}

    public void setAddress(String address) {this.address = address;}

    public void setReceiver(String receiver) {this.receiver = receiver;}

    public void setPhone(String phone) {this.phone = phone;}

    public void setIsDefault(String isDefault) {this.isDefault = isDefault;}

    public void setMemberId(String memberId) {this.memberId = memberId;}


    @Override
    public int compareTo(AddressInfo o) {
        int i = 0;
        if("Y".equals(this.getIsDefault())){
            return i;
        }else{
            i = o.getCreateTime().compareTo(this.getCreateTime());
        }
        return i;
    }
}
