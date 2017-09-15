package com.drore.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.google.gson.annotations.SerializedName;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:   点击信息表  <br/>
 * 项目名称: yw-jpj-service <br/>
 * 创建日期:  2017/9/6 9:48  <br/>
 * 作者:    wdz
 */
public class ClicksInfo extends SystemModel{
    public  static String table="clicks_info";

    /**
     * 来源
     */
    private  String source;

    /**
     * 会员名称
     */
    @SerializedName("member_name")
    private String memberName;
    /**
     * 点击内容名称
     */
    @SerializedName("name")
    private String name;
    /**
     * 点击对应信息主键
     */
    @SerializedName("table_id")
    private String tableId;
    /**
     * 会员主键
     */
    @SerializedName("member_id")
    private String memberId;
    /**
     * 点击对应表名
     */
    @SerializedName("table_name")
    private String tableName;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
