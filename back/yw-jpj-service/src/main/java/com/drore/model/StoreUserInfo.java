package com.drore.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.google.gson.annotations.SerializedName;

/**
 * 商家用户信息表
 * Created by wangl on 2017/9/4 0004.
 */
public class StoreUserInfo extends SystemModel {

    public static String table = "store_user_info";

    private String name;

    private String salt;

    private String password;

    private String mail;

    private String sex;

    @SerializedName("store_id")
    private String storeId;

    @SerializedName("password_invalid_time")
    private String passwordInvalidTime;

    @SerializedName("activated_time")
    private String activatedTime;

    @SerializedName("is_lock")
    private String isLock;

    @SerializedName("is_first_login")
    private String isFirstLogin;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("last_login_ip")
    private String lastLoginIp;

    @SerializedName("is_activated")
    private String isActivated;

    @SerializedName("is_admin")
    private String isAdmin;

    @SerializedName("is_reset_password")
    private String isResetPassword;

    @SerializedName("error_count")
    private String errorCount;

    @SerializedName("last_login_time")
    private String lastLoginTime;

    @SerializedName("nick_name")
    private String nickName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getPasswordInvalidTime() {
        return passwordInvalidTime;
    }

    public void setPasswordInvalidTime(String passwordInvalidTime) {
        this.passwordInvalidTime = passwordInvalidTime;
    }

    public String getActivatedTime() {
        return activatedTime;
    }

    public void setActivatedTime(String activatedTime) {
        this.activatedTime = activatedTime;
    }

    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public String getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(String isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(String isActivated) {
        this.isActivated = isActivated;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getIsResetPassword() {
        return isResetPassword;
    }

    public void setIsResetPassword(String isResetPassword) {
        this.isResetPassword = isResetPassword;
    }

    public String getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(String errorCount) {
        this.errorCount = errorCount;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
