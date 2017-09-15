package com.drore.cloud.model;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.math.BigDecimal;

/***
 * 浙江卓锐科技股份有限公司 版权所有@Copyright 2016
 * 说明 购物车
 * 项目名称
 * @since:cloud-ims 1.0
 * @author <a href="mailto:baoec@drore.com">baoec@drore.com </a> 
 * 2017/09/05 17:14
 */

public class ShoppingCart implements Serializable {
    @ApiModelProperty("商店id")
    private String store_id;
    @ApiModelProperty("商店名称")
    private String store_name;
    @ApiModelProperty("商品id")
    private String commodity_id;
    @ApiModelProperty("商品名称")
    private String commodity_name;
    @Range(min=1,message = "商品数量必须大于0")
    @ApiModelProperty("商品数量")
    private Integer num;
    @Range(message = "商品单价:应该要大于0吧")
    @ApiModelProperty("商品单价")
    private BigDecimal price;

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(String commodity_id) {
        this.commodity_id = commodity_id;
    }

    public String getCommodity_name() {
        return commodity_name;
    }

    public void setCommodity_name(String commodity_name) {
        this.commodity_name = commodity_name;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
