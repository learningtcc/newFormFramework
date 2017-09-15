package com.drore.domain.vo;

import java.math.BigDecimal;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:  购物车模型   <br/>
 * 项目名称: yw-jpj-service <br/>
 * 创建日期:  2017/9/1 18:16  <br/>
 * 作者:    wdz
 */
public class ShoppingCartVo {
    /**
     * 店铺主键
     */
    private String storeId;

    /**
     * 店铺名称
     */
    private String storeName;


    /**
     * 商品主键
     */
    private String commodityId;

    /**
     * 商品名称
     */
    private String commodityName;
    /**
     * 商品数量
     */
    private int count;
    /**
     * 商品价格
     */
    private BigDecimal price;


    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
