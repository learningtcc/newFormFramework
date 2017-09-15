package com.drore.domain.vo;

/**
 * 说明: 账单明细
 * 项目名称: taihu-rent
 * 创建时间: 2017/5/22 11:14
 * 作者: xiangwb
 */

public class BillingDetailsVo {
    /**
     * 物品名称
     */
    private String name;
    /**
     * 原价
     */
    private double price;
    /**
     * 会员租赁系数
     */
    private int vipLeaseCount;
    /**
     * 非会员租赁次数
     */
    private int leaseCount;
    /**
     * 总押金
     */
    private double allDeposit;
    /**
     * 赔损数量
     */
    private int damageCount;
    /**
     * 赔损价格
     */
    private double damageFee;

    /**
     * 协商赔偿数量
     */
    private int indemnifyCount;
    /**
     * 协商赔偿费用
     */
    private double indemnifyFee;
    /**
     * 租赁收入
     */
    private double leaseIncome;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVipLeaseCount() {
        return vipLeaseCount;
    }

    public void setVipLeaseCount(int vipLeaseCount) {
        this.vipLeaseCount = vipLeaseCount;
    }

    public int getLeaseCount() {
        return leaseCount;
    }

    public void setLeaseCount(int leaseCount) {
        this.leaseCount = leaseCount;
    }

    public double getAllDeposit() {
        return allDeposit;
    }

    public void setAllDeposit(double allDeposit) {
        this.allDeposit = allDeposit;
    }

    public int getDamageCount() {
        return damageCount;
    }

    public void setDamageCount(int damageCount) {
        this.damageCount = damageCount;
    }

    public double getDamageFee() {
        return damageFee;
    }

    public void setDamageFee(double damageFee) {
        this.damageFee = damageFee;
    }

    public double getLeaseIncome() {
        return leaseIncome;
    }

    public void setLeaseIncome(double leaseIncome) {
        this.leaseIncome = leaseIncome;
    }

    public int getIndemnifyCount() {
        return indemnifyCount;
    }

    public void setIndemnifyCount(int indemnifyCount) {
        this.indemnifyCount = indemnifyCount;
    }

    public double getIndemnifyFee() {
        return indemnifyFee;
    }

    public void setIndemnifyFee(double indemnifyFee) {
        this.indemnifyFee = indemnifyFee;
    }
}
