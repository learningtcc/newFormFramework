package com.drore.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明: 订单信息    <br/>
 * 项目名称: yw-jpj-service <br/>
 * 创建日期:  2017/9/4 16:01  <br/>
 * 作者:    wdz
 */
public class OrderInfo extends SystemModel{
    public  static String table="order_info";

    /**
     * 支付时间
     */
    @SerializedName("pay_time")
    private String payTime;




    /**
     * 下单时间
     */
    @SerializedName("order_time")
    private Date orderTime;
    /**
     * 收货地址
     */
    @SerializedName("receipt_add")
    private String receiptAdd;

    /**
     *总价
     */
    @SerializedName("total")
    private BigDecimal total;
    /**
     * 会员主键
     */
    @SerializedName("member_id")
    private String memberId;
    /**
     * 运单编号
     */
    @SerializedName("tracking_num")
    private String trackingNum;
    /**
     * 优惠券折扣率1
     */
    @SerializedName("offer_voucher_full")
    private double offerVoucherFull;
    /**
     * 商家名称
     */
    @SerializedName("store_name")
    private String storeName;
    /**
     * 物流公司
     */
    @SerializedName("logistics_company")
    private String logisticsCompany;
    /**
     * 优惠券名称
     */
    @SerializedName("offer_voucher_name")
    private String offerVoucherName;
    /**
     * 联系方式
     */
    @SerializedName("receipt_phone")
    private String receiptPhone;
    /**
     * 备注，描述
     */
    @SerializedName("description")
    private String description;
    /**
     * 运费
     */
    @SerializedName("fee")
    private double fee;
    /**
     *优惠券 折扣率2
     */
    @SerializedName("offer_voucher_less")
    private int offerVoucherLess;
    /**
     * 优惠券主键
     */
    @SerializedName("offer_voucher_id")
    private String offerVoucherId;
    /**
     * 收货人
     */
    @SerializedName("receipt_people")
    private String receiptPeople;
    /**
     * 优惠券类型
     */
    @SerializedName("offer_voucher_type")
    private String offerVoucherType;
    @SerializedName("receipt_way")
    /**
     * 收货方式，自提还是快递
     */
    private String receiptWay;


    /**
     * 订单状态
     */
    @SerializedName("order_status")
    private String orderStatus;
    /**
     * 商铺主键
     */
    @SerializedName("store_id")
    private String storeId;
    /**
     * 订单编号
     */
    @SerializedName("order_no")
    private String orderNo;

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getReceiptAdd() {
        return receiptAdd;
    }

    public void setReceiptAdd(String receiptAdd) {
        this.receiptAdd = receiptAdd;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getTrackingNum() {
        return trackingNum;
    }

    public void setTrackingNum(String trackingNum) {
        this.trackingNum = trackingNum;
    }

    public double getOfferVoucherFull() {
        return offerVoucherFull;
    }

    public void setOfferVoucherFull(double offerVoucherFull) {
        this.offerVoucherFull = offerVoucherFull;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getOfferVoucherName() {
        return offerVoucherName;
    }

    public void setOfferVoucherName(String offerVoucherName) {
        this.offerVoucherName = offerVoucherName;
    }

    public String getReceiptPhone() {
        return receiptPhone;
    }

    public void setReceiptPhone(String receiptPhone) {
        this.receiptPhone = receiptPhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getOfferVoucherLess() {
        return offerVoucherLess;
    }

    public void setOfferVoucherLess(int offerVoucherLess) {
        this.offerVoucherLess = offerVoucherLess;
    }

    public String getOfferVoucherId() {
        return offerVoucherId;
    }

    public void setOfferVoucherId(String offerVoucherId) {
        this.offerVoucherId = offerVoucherId;
    }

    public String getReceiptPeople() {
        return receiptPeople;
    }

    public void setReceiptPeople(String receiptPeople) {
        this.receiptPeople = receiptPeople;
    }

    public String getOfferVoucherType() {
        return offerVoucherType;
    }

    public void setOfferVoucherType(String offerVoucherType) {
        this.offerVoucherType = offerVoucherType;
    }

    public String getReceiptWay() {
        return receiptWay;
    }

    public void setReceiptWay(String receiptWay) {
        this.receiptWay = receiptWay;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
