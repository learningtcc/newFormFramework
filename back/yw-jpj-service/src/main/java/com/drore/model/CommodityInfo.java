package com.drore.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * 商品表
 * Created by wangl on 2017/9/1 0001.
 */
public class CommodityInfo extends SystemModel{
    public static String table = "commodity_info";
    /**
     * 商品价格
     */
    private double price;
    /**
     * 详情
     */
    private String details;
    /**
     * 商品规格
     */
    @NotBlank
    private String standard;
    /**
     * 商品名称
     */
    @NotBlank
    @Length(min=1, max=100)
    private String name;
    /**
     * 类别
     */
    @NotBlank
    @Length(min=1, max=40)
    @SerializedName("type_id")
    private String typeId;
    /**
     * 临时字段 中文显示
     */
    @Expose(serialize = false)
    private String typeIdName;
    /**
     * 上下架人 景区或者商家
     */
    @SerializedName("shelves_id")
    private String shelvesId;

    @SerializedName("shelves_time")
    private Date shelvesTime;
    /**
     * 是否上架
     */
    @Pattern(regexp="^[Y|N]$",message = "必须为Y或者N")
    @SerializedName("is_shelves")
    private String isShelves;

    /**
     * 价格单位
     */
    @NotBlank
    @Length(min=1, max=40)
    @SerializedName("price_id")
    private String priceId;
    /**
     * 临时字段，价格单位中文显示
     */
    @Expose(serialize = false)
    private String priceIdName;
    /**
     * 是否特色商品
     */
    @Pattern(regexp="^[Y|N]$",message = "必须为Y或者N")
    @SerializedName("is_features")
    private String isFeatures;
    /**
     * 种类
     */
    @NotBlank
    @Length(min=1, max=40)
    @SerializedName("species_id")
    private String speciesId;

    /**
     * 临时字段，种类名称
     */
    @Expose(serialize = false)
    private String speciesIdName;

    @SerializedName("store_id")
    private String storeId;

    /**
     * 商店名称
     */
    @Expose(serialize = false)
    private String storeIdName;
    /**
     * 点击量
     */
    private int clicks;


    /**
     *图集 ，临时字段
     */
    @Expose(serialize=false)
    private List<String> pics;


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getShelvesId() {
        return shelvesId;
    }

    public void setShelvesId(String shelvesId) {
        this.shelvesId = shelvesId;
    }

    public Date getShelvesTime() {
        return shelvesTime;
    }

    public void setShelvesTime(Date shelvesTime) {
        this.shelvesTime = shelvesTime;
    }

    public String getIsShelves() {
        return isShelves;
    }

    public void setIsShelves(String isShelves) {
        this.isShelves = isShelves;
    }


    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public String getIsFeatures() {
        return isFeatures;
    }

    public void setIsFeatures(String isFeatures) {
        this.isFeatures = isFeatures;
    }

    public String getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(String speciesId) {
        this.speciesId = speciesId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public String getTypeIdName() {
        return typeIdName;
    }

    public void setTypeIdName(String typeIdName) {
        this.typeIdName = typeIdName;
    }

    public String getSpeciesIdName() {
        return speciesIdName;
    }

    public void setSpeciesIdName(String speciesIdName) {
        this.speciesIdName = speciesIdName;
    }

    public String getStoreIdName() {
        return storeIdName;
    }

    public void setStoreIdName(String storeIdName) {
        this.storeIdName = storeIdName;
    }

    public String getPriceIdName() {
        return priceIdName;
    }

    public void setPriceIdName(String priceIdName) {
        this.priceIdName = priceIdName;
    }
}
