package com.drore.cloud.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.google.gson.annotations.SerializedName;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:  订单详情信息表   <br/>
 * 项目名称: yw-jpj-service <br/>
 * 创建日期:  2017/9/4 11:08  <br/>
 * 作者:    wdz
 */
public class OrderDetail extends SystemModel {
   public  static String table="order_detail";

    @SerializedName("commodity_price")
    private double commodityPrice;
    @SerializedName("commodity_amout")
    private double commodityAmout;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("commodity_name")
    private String commodityName;
    @SerializedName("commodity_id")
    private String commodityId;
    @SerializedName("commodity_pic")
    private String commodityPic;

    public String getCommodityPic() {
        return commodityPic;
    }

    public void setCommodityPic(String commodityPic) {
        this.commodityPic = commodityPic;
    }

    public double getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(double commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public double getCommodityAmout() {
        return commodityAmout;
    }

    public void setCommodityAmout(double commodityAmout) {
        this.commodityAmout = commodityAmout;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }
}
