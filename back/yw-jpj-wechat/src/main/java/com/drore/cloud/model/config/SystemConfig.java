/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */
package com.drore.cloud.model.config;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/***
 * 系统配置参数类
 * @since:cloud-ims 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2016/9/4 10:52
 */
public class SystemConfig {

    /**
     * 系统名称
     */
    @SerializedName("system_title")
    private String systemTitle;
    /***
     * 系统code，这里code指uc系统中的子系统code
     */
    @SerializedName("system_code")
    private String systemCode;
    /***
     * 系统简介
     */
    @SerializedName("systemDescription")
    private String systemDescription;
    /***
     * 系统应用appid
     */
    private String appid;
    /***
     * 系统应用appsecret
     */
    private String appsecret;
    /***
     * 云数据中心url地址
     */
    @SerializedName("cloud_url")
    private String cloudUrl="y.drore.com";

    @SerializedName("cloud_port")
    private Integer cloudPort=80;
    /***
     * 是否启动当前系统，0 禁用 1.启用
     */
    private Boolean enable;
    /***
     * DES加解密key值
     */
    @SerializedName("des_key")
    private String desKey="a#xf$#234df$*x";

    /**
     * 通用接口，排除resource资源查询，
     * 系统、配置等表资源信息不能通过通用接口查询给出
     * 集合，例如["order_info","member_info"]
     */
    @SerializedName("exclude_api_resources")
    private List<String> excludeApiResources=new ArrayList<String>();

    public List<String> getExcludeApiResources() {
        return excludeApiResources;
    }

    public void setExcludeApiResources(List<String> excludeApiResources) {
        this.excludeApiResources = excludeApiResources;
    }

    /***
     * 微信配置信息
     */
    @SerializedName("wxConfig")
    private WxConfig wxConfig=new WxConfig();

    /***
     * macro依赖外部jar
     */
    private List<String> dependencies=new ArrayList<String>();

    /***
     * 业务处理地址url
     */
    @SerializedName("business_url")
    private String businessUrl;
    /***
     * 业务签名,用于服务端验证
     */
    @SerializedName("business_sign")
    private String businessSign;

    public String getBusinessUrl() {
        return businessUrl;
    }

    public void setBusinessUrl(String businessUrl) {
        this.businessUrl = businessUrl;
    }

    public String getBusinessSign() {
        return businessSign;
    }

    public void setBusinessSign(String businessSign) {
        this.businessSign = businessSign;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    public String getDesKey() {
        return desKey;
    }

    public void setDesKey(String desKey) {
        this.desKey = desKey;
    }


    public WxConfig getWxConfig() {
        return wxConfig;
    }

    public void setWxConfig(WxConfig wxConfig) {
        this.wxConfig = wxConfig;
    }

    public String getSystemTitle() {
        return systemTitle;
    }

    public void setSystemTitle(String systemTitle) {
        this.systemTitle = systemTitle;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getSystemDescription() {
        return systemDescription;
    }

    public void setSystemDescription(String systemDescription) {
        this.systemDescription = systemDescription;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }


    public String getCloudUrl() {
        return cloudUrl;
    }

    public void setCloudUrl(String cloudUrl) {
        this.cloudUrl = cloudUrl;
    }

    public Integer getCloudPort() {
        return cloudPort;
    }

    public void setCloudPort(Integer cloudPort) {
        this.cloudPort = cloudPort;
    }
}
