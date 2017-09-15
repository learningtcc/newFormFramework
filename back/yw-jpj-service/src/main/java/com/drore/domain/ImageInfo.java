package com.drore.domain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明: 公共图集部分    <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/9/1 10:48  <br/>
 * 作者:    wdz
 */
public class ImageInfo extends  BaseEntity {
    private static final long serialVersionUID = 1L;
    public static String table ="image_info";
    /**
     * pic_url : 1221
     * table_name : 1212
     * table_pk : 1212
     */

    @JSONField(name ="pic_url")
    private String picUrl;
    @JSONField(name ="table_name")
    private String tableName;
    @JSONField(name ="table_pk")
    private String tablePk;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTablePk() {
        return tablePk;
    }

    public void setTablePk(String tablePk) {
        this.tablePk = tablePk;
    }
}
