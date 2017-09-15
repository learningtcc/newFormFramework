/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */
package com.drore.cloud.model.config;

import com.google.gson.annotations.SerializedName;

/***
 * 微信配置文件类
 * @since:cloud-ims 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2016/9/19 11:29
 */
public class WxConfig {
    /***
     * 微信公众号appid
     */
    private String appid;

    /***
     * 微信公众号appsecret
     */
    private String appsecret;

    /***
     * 重定向跳转地址,一般是授权获取用户信息地址
     */
    @SerializedName("redirect_url")
    private String redirectUrl;
    /***
     * 微信异步支付回调
     */
    @SerializedName("notify_url")
    private String notifyUrl;

    public String getNotifyUrl() {return notifyUrl;}

    public void setNotifyUrl(String notifyUrl) {this.notifyUrl = notifyUrl;}

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

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
