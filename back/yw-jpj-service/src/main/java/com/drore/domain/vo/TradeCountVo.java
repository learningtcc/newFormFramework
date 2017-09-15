package com.drore.domain.vo;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:   交易信息统一返回结果  <br/>
 * 项目名称: yw-jpj-service <br/>
 * 创建日期:  2017/9/6 15:39  <br/>
 * 作者:    wdz
 */
public class TradeCountVo {
    /**
     * 交易金额
     */
    private double money;
    /**
     * 交易笔数
     */
    private  int count;
    /**
     * 交易商家
     */
    private  String storeName;
    /**
     * 交易日期
     */
    private  String day;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
