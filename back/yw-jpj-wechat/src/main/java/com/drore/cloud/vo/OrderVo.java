package com.drore.cloud.vo;

import com.google.gson.JsonArray;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/***
 * 浙江卓锐科技股份有限公司 版权所有@Copyright 2016
 * 说明
 *  提交订单
 * @since:cloud-ims 1.0
 * @author <a href="mailto:baoec@drore.com">baoec@drore.com </a> 
 * 2017/09/07 14:49
 */

public class OrderVo {
    @ApiModelProperty("收货方式:自提offline 快递 express")
    private String receipt_way;
    @ApiModelProperty("会员地址信息id,收货方式为自提 可不传")
    private String member_address_id;
    @NotNull(message = "商店id不能为空")
    @ApiModelProperty("商店id")
    private String store_id;
//    @NotNull(message = "商品信息不能为空")
//    @ApiModelProperty("商品信息")
//    private JsonArray commodity_info;
    @NotNull(message = "商品id不能为空")
    @ApiModelProperty("商品id")
    private String commodity_id;
    @ApiModelProperty("购买数量")
    @Range(min=1,message = "商品数量必须大于0")
    private Integer buy_num;
    @ApiModelProperty("优惠券id,可为空")
    private String offer_voucher_id;

    public String getReceipt_way() {
        return receipt_way;
    }

    public void setReceipt_way(String receipt_way) {
        this.receipt_way = receipt_way;
    }

    public String getMember_address_id() {
        return member_address_id;
    }

    public void setMember_address_id(String member_address_id) {
        this.member_address_id = member_address_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public Integer getBuy_num() {
        return buy_num;
    }

    public String getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(String commodity_id) {
        this.commodity_id = commodity_id;
    }

    public void setBuy_num(Integer buy_num) {
        this.buy_num = buy_num;
    }

//    public JsonArray getCommodity_info() {
//        return commodity_info;
//    }
//
//    public void setCommodity_info(JsonArray commodity_info) {
//        this.commodity_info = commodity_info;
//    }

    public String getOffer_voucher_id() {
        return offer_voucher_id;
    }

    public void setOffer_voucher_id(String offer_voucher_id) {
        this.offer_voucher_id = offer_voucher_id;
    }
}
