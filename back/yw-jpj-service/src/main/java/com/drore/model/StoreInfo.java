package com.drore.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * Created by wangl on 2017/9/4 0004.
 */
public class StoreInfo extends SystemModel {
    public  static final  String table="store_info";
    /**
     * 商家类型
     */
    @Length(max = 255)
    private String type;
    /**
     * 运费
     */
    @Length(max = 100)
    private double fee;
    /**
     * 面积
     */
    @Length(max = 100)
    private double area;
    /**
     * 维度
     */
    @Length(max = 10)
    private String dimension;
    /**
     * 经度
     */
    @Length(max = 10)
    private String longitude;
    /**
     * 收款人
     */
    @Length(max = 50)
    private String payee;

    private String description;
    /**
     * 联系地址
     */
    @NotBlank
    @Length(min = 1,max = 100)
    private String address;
    /**
     * 商家名称
     */
    @NotBlank
    @Length(min = 1,max = 100)
    private String name;
    /**
     * 发布人(景区)
     */
    @Length(max = 40)
    @SerializedName("release_id")
    private String releaseId;
    /**
     * 发布时间
     */
    @SerializedName("release_time")
    private Date releaseTime;
    /**
     * 是否发布
     */
    @Pattern(regexp="^[Y|N]$",message = "必须为Y或者N")
    @SerializedName("is_release")
    private String isRelease;

    /**
     * 银行卡号
     */
    @Length(max = 50)
    @SerializedName("bank_card")
    private String bankCard;
    /**
     * 银行卡开户地址
     */
    @Length(max = 100)
    @SerializedName("bank_address")
    private String bankAddress;
    /**
     * 支付宝图片
     */
    @Length(max = 200)
    @SerializedName("alipay_url")
    private String alipayUrl;
    /**
     * 微信图片
     */
    @Length(max = 200)
    @SerializedName("weixin_url")
    private String weixinUrl;
    /**
     * 微信图片(加微信)
     */
    @Length(max = 200)
    @SerializedName("wx_url")
    private String wxUrl;
    /**
     * 是否热门
     */
    @Length(max = 1)
    @SerializedName("is_hot")
    private String isHot;
    /**
     * 客服电话
     */
    @Length(max = 50)
    @SerializedName("service_tel")
    private String serviceTel;
    /**
     * 营业执照
     */
    @Length(max = 200)
    @SerializedName("business_license_url")
    private String businessLicenseUrl;
    /**
     * 联系电话
     */
    @NotEmpty
    @Length(max = 50)
    @SerializedName("contact_phone")
    private String contactPhone;
    /**
     * 联系人
     */
    @NotBlank
    @Length(max = 50)
    @SerializedName("contact_person")
    private String contactPerson;
    /**
     * 点击量
     */
    private  int clicks;


    /**
     *图集 ，临时字段
     */
    @Expose(serialize=false)
    private List<String> pics;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getWxUrl() {
        return wxUrl;
    }

    public void setWxUrl(String wxUrl) {
        this.wxUrl = wxUrl;
    }

    public String getServiceTel() {
        return serviceTel;
    }

    public void setServiceTel(String serviceTel) {
        this.serviceTel = serviceTel;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getIsRelease() {
        return isRelease;
    }

    public void setIsRelease(String isRelease) {
        this.isRelease = isRelease;
    }



    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getAlipayUrl() {
        return alipayUrl;
    }

    public void setAlipayUrl(String alipayUrl) {
        this.alipayUrl = alipayUrl;
    }

    public String getWeixinUrl() {
        return weixinUrl;
    }

    public void setWeixinUrl(String weixinUrl) {
        this.weixinUrl = weixinUrl;
    }

    public String getIsHot() {
        return isHot;
    }

    public void setIsHot(String isHot) {
        this.isHot = isHot;
    }

    public String getBusinessLicenseUrl() {
        return businessLicenseUrl;
    }

    public void setBusinessLicenseUrl(String businessLicenseUrl) {
        this.businessLicenseUrl = businessLicenseUrl;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }
}
