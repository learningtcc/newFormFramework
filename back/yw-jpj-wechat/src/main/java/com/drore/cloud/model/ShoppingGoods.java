package com.drore.cloud.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/***
 * 浙江卓锐科技股份有限公司 版权所有@Copyright 2016
 * 说明
 * 项目名称
 * @since:cloud-ims 1.0
 * @author <a href="mailto:baoec@drore.com">baoec@drore.com </a> 
 * 2017/09/05 19:05
 */

public class ShoppingGoods implements Serializable {
    private String commodity_id;
    @ApiModelProperty(name="商品名称",dataType="String",required = true)
    private String commodity_name;
    @ApiModelProperty(name="商品图片",dataType = "String",required = true)
    private String commodity_image;
    @ApiModelProperty(name="商品数量",dataType="int",required = true)
    private Integer num;
    @ApiModelProperty(name="商品单价",dataType="Double",required = true)
    private BigDecimal price;

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

    public String getCommodity_image() {
        return commodity_image;
    }

    public void setCommodity_image(String commodity_image) {
        this.commodity_image = commodity_image;
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
