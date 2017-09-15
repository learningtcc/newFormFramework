package com.drore.domain.store;

import com.alibaba.fastjson.annotation.JSONField;
import com.drore.domain.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/8/31 10:26  <br/>
 * 作者:    wdz
 */
public class StoreUser extends BaseEntity {
    /**
     * 序列化
     */
    private static final long serialVersionUID = 1646822429681006434L;


    public static String table = "store_user_info";
    /**
     * 密码盐长度
     */
    public static final int SALT_SIZE = 10;
    /**
     * 初始默认密码长度
     */
    public static final int PWD_LENGTH = 8;
    /**
     * hash
     */
    public static int HASH_INTERATIONS = 1024;

    /**
     * 商家主键
     **/
    @JSONField(name = "store_id")
    private String storeId;
    /**
     * 用户名
     */
    @JSONField(name = "user_name")
    private String userName;
    /**
     * 昵称
     */
    @JSONField(name = "nick_name")
    private String nickName;

    /**
     * 姓名
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 盐值
     */
    @JSONField(name = "salt")
    private String salt;
    /**
     * 密码
     */
    @JSONField(name = "password")
    private String password;
    /**
     * 是否锁定
     */
    @JSONField(name = "is_lock")
    private String isLock;
    /**
     * 最后登录时间
     */
    @JSONField(name = "last_login_time")
    private Date lastLoginTime;
    /**
     * 最后登录IP
     */
    @JSONField(name = "last_login_ip")
    private String lastLoginIp;

    /**
     * 是否首次登录
     */
    @JSONField(name = "is_first_login")
    private String isFirstLogin;
    /**
     * 激活状态
     */
    @JSONField(name = "is_activated")
    private String isActivated;

    /**
     * 激活时间
     */
    @JSONField(name = "activated_time")
    private Date activatedTime;
    /**
     * 邮箱
     */
    @JSONField(name = "mail")
    private String mail;

    /**
     * 性别
     */
    @JSONField(name = "sex")
    private int sex;
    /**
     * 是否管理员
     */
    @JSONField(name = "is_admin")
    private String isAdmin;

    /**
     * 登录错误次数
     */
    @JSONField(name = "error_count")
    private int errorCount;
    /**
     * 密码失效时间
     */
    @JSONField(name = "password_invalid_time")
    private Date passwordInvalidTime;
    /**
     * 是否重置密码
     */
    @JSONField(name = "is_reset_password")
    private String isResetPassword;


    @JSONField(serialize=false)
    List<StoreAuthority> menuArray;
    @JSONField(serialize=false)
    List<StoreAuthority> otherArray;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

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

    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(String isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public String getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(String isActivated) {
        this.isActivated = isActivated;
    }

    public Date getActivatedTime() {
        return activatedTime;
    }

    public void setActivatedTime(Date activatedTime) {
        this.activatedTime = activatedTime;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public Date getPasswordInvalidTime() {
        return passwordInvalidTime;
    }

    public void setPasswordInvalidTime(Date passwordInvalidTime) {
        this.passwordInvalidTime = passwordInvalidTime;
    }

    public String getIsResetPassword() {
        return isResetPassword;
    }

    public void setIsResetPassword(String isResetPassword) {
        this.isResetPassword = isResetPassword;
    }

    public List<StoreAuthority> getMenuArray() {
        return menuArray;
    }

    public void setMenuArray(List<StoreAuthority> menuArray) {
        this.menuArray = menuArray;
    }

    public List<StoreAuthority> getOtherArray() {
        return otherArray;
    }

    public void setOtherArray(List<StoreAuthority> otherArray) {
        this.otherArray = otherArray;
    }
}
