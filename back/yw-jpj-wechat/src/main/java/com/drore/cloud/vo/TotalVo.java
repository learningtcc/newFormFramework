package com.drore.cloud.vo;

import com.google.gson.JsonArray;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/***
 * 浙江卓锐科技股份有限公司 版权所有@Copyright 2016
 * 说明
 *   计算订单总价vo
 * @since:cloud-ims 1.0
 * @author <a href="mailto:baoec@drore.com">baoec@drore.com </a> 
 * 2017/09/07 10:33
 */

public class TotalVo {
//    @NotNull(message = "商品id不能为空")
//    @ApiModelProperty("商品id")
//    private String commodity_id;
//    @ApiModelProperty("购买数量")
//    @Range(min=1,message = "商品数量必须大于0")
//    private Integer buy_num;
    @NotNull(message = "商品信息不能为空")
    @ApiModelProperty("商品信息")
    private String commodity_info;
//    @NotNull(message = "优惠券id不能为空")
    @ApiModelProperty("优惠券id")
    private String offer_voucher_id;

    public String getCommodity_info() {
        return commodity_info;
    }

    public void setCommodity_info(String commodity_info) {
        this.commodity_info = commodity_info;
    }

    public String getOffer_voucher_id() {
        return offer_voucher_id;
    }

    public void setOffer_voucher_id(String offer_voucher_id) {
        this.offer_voucher_id = offer_voucher_id;
    }
}
