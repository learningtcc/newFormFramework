package com.drore.domain.vo;

/**
 * 说明: 物品租赁统计
 * 项目名称: taihu-rent
 * 创建时间: 2017/5/22 9:54
 * 作者: xiangwb
 */

public class GoodsLeaseStatisticalVo {
    private String name;
    private int count;
    private int amount;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
