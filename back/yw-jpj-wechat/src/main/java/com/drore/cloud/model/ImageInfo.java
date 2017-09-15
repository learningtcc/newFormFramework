package com.drore.cloud.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.google.gson.annotations.SerializedName;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明: 公共图集部分    <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/9/1 10:48  <br/>
 * 作者:    wdz
 */
public class ImageInfo extends SystemModel {
    private static final long serialVersionUID = 1L;
    public static String table ="image_info";

    @SerializedName("pic_url")
    private String picUrl;
    @SerializedName("table_name")
    private String tableName;
    @SerializedName("table_pk")
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
