package com.drore.cloud.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by wangl on 2017/9/1 0001.
 */
public class LeaseInfo extends SystemModel {

    public static String table = "lease_info";

    private String publisher;

    private String auditor;

    private String describes;

    private String latitude;

    private String longitude;

    private String address;

    private double price;

    private double area;

    private String contacts;

    private String type;

    private String title;

    @SerializedName("theme_pic")
    private String themePic;

    @SerializedName("release_time")
    private Date releaseTime;

    @SerializedName("is_release")
    private String isRelease;

    @SerializedName("audit_status")
    private String auditStatus;

    @SerializedName("audit_time")
    private Date auditTime;

    @SerializedName("is_discuss")
    private String isDiscuss;

    @SerializedName("contact_tel")
    private String contactTel;

    @SerializedName("merchant_pk")
    private String merchantPk;

    @SerializedName("is_top")
    private String isTop;

    @SerializedName("is_using")
    private String isUsing;

    public String getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(String isUsing) {
        this.isUsing = isUsing;
    }

    public String getThemePic() {
        return themePic;
    }

    public void setThemePic(String themePic) {
        this.themePic = themePic;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getIsRelease() {
        return isRelease;
    }

    public void setIsRelease(String isRelease) {
        this.isRelease = isRelease;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getIsDiscuss() {
        return isDiscuss;
    }

    public void setIsDiscuss(String isDiscuss) {
        this.isDiscuss = isDiscuss;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getMerchantPk() {
        return merchantPk;
    }

    public void setMerchantPk(String merchantPk) {
        this.merchantPk = merchantPk;
    }
}
