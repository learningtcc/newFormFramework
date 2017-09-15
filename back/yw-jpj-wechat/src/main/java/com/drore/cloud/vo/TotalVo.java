package com.drore.cloud.vo;

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
    @NotNull(message = "商品id不能为空")
    @ApiModelProperty("商品id")
    private String commodity_id;
    @ApiModelProperty("购买数量")
    @Range(min=1,message = "商品数量必须大于0")
    private Integer buy_num;
    @NotNull(message = "优惠券id不能为空")
    @ApiModelProperty("优惠券id")
    private String offer_voucher_id;

    public String getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(String commodity_id) {
        this.commodity_id = commodity_id;
    }

    public Integer getBuy_num() {
        return buy_num;
    }

    public void setBuy_num(Integer buy_num) {
        this.buy_num = buy_num;
    }

    public String getOffer_voucher_id() {
        return offer_voucher_id;
    }

    public void setOffer_voucher_id(String offer_voucher_id) {
        this.offer_voucher_id = offer_voucher_id;
    }
}
