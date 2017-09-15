package com.drore.cloud.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/***
 * 浙江卓锐科技股份有限公司 版权所有@Copyright 2016
 * 说明
 * 项目名称
 * @since:cloud-ims 1.0
 * @author <a href="mailto:baoec@drore.com">baoec@drore.com </a> 
 * 2017/09/05 19:04
 */

public class ShoppingStore implements Serializable {
    @ApiModelProperty(name="商店id",dataType="String",required = true)
    private String store_id;
    @ApiModelProperty(name="商店名称",dataType="String",required = true)
    private String store_name;
    private Map<String,ShoppingGoods> goodmap;

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

    public Map<String, ShoppingGoods> getGoodmap() {
        return goodmap;
    }

    public void setGoodmap(Map<String, ShoppingGoods> goodmap) {
        this.goodmap = goodmap;
    }
}
