package com.drore.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.google.gson.annotations.SerializedName;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明: 公用广告信息表    <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/9/1 11:25  <br/>
 * 作者:    wdz
 */
public class AdvertisingInfo extends SystemModel  {
    public  static  String table="advertising_info";

    @NotNull(message = "栏目不能为空")
    @Length(min = 1,max = 50)
    @SerializedName("table_name")
    private String tableName;

    @Length(max = 200)
    @SerializedName("link_url")
    private String linkUrl;

    @NotNull(message = "图片不能为空")
    @Length(min = 1,max = 200)
    @SerializedName("image_url")
    private String imageUrl;


    @NotNull(message = "名称不能为空")
    @Length(min = 1,max = 100)
    @SerializedName("name")
    private String name;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
