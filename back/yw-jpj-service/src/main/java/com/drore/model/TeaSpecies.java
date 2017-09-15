package com.drore.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.drore.cloud.sdk.domain.SystemModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:   茶种类信息  <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/9/1 11:55  <br/>
 * 作者:    wdz
 */
public class TeaSpecies extends SystemModel {
   public static  String table ="tea_species";

    /**
     * 名称
     */
    private  String name;

    @NotNull(message = "主图不能为空")
    @Length(min = 1,max = 200)
    @SerializedName("theme_pic")
    private String themePic;

    @NotNull(message = "最低价不能为空")
    @DecimalMin(value = "0")
    @DecimalMax(value = "100000000")
    @SerializedName("price_low")
    private Integer priceLow;


    @NotNull(message = "最低价不能为空")
    @DecimalMin(value = "0")
    @DecimalMax(value = "100000000")

    @SerializedName("price_high")
    private Integer priceHigh;

    @NotNull(message = "类型不能为空")
    @Length(min = 1,max = 40)
    @SerializedName("type_id")
    private String typeId;

    /**
     * 类别中文名称
     */
    @JSONField(serialize=false)
    private String typeIdName;


    @NotNull(message = "单位不能为空")
    @Length(min = 1,max = 40)
    @SerializedName("price_unit")
    private String priceUnit;
    /**
     *图集 ，临时字段
     */
    @Expose(serialize=false)
    private List<String> pics;

    public String getThemePic() {
        return themePic;
    }

    public void setThemePic(String themePic) {
        this.themePic = themePic;
    }

    public Integer getPriceLow() {
        return priceLow;
    }

    public void setPriceLow(Integer priceLow) {
        this.priceLow = priceLow;
    }

    public Integer getPriceHigh() {
        return priceHigh;
    }

    public void setPriceHigh(Integer priceHigh) {
        this.priceHigh = priceHigh;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getTypeIdName() {
        return typeIdName;
    }

    public void setTypeIdName(String typeIdName) {
        this.typeIdName = typeIdName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
