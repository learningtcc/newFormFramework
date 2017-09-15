package com.drore.domain.store;

import com.alibaba.fastjson.annotation.JSONField;
import com.drore.domain.BaseEntity;

import java.util.Date;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/8/31 10:37  <br/>
 * 作者:    wdz
 */
public class StoreUserLoginInOut extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public static String table="store_user_login_in_out";

    /**
     * ip
     */
    private String ip	;

    /**
     * 记录时间
     */
    @JSONField(name="record_date")
    private Date recordDate;

    /**
     * 用户主键
     */
    @JSONField(name="user_id")
    private String userId;
    /**
     * 用户名 临时字段
     */
    @JSONField(serialize=false)
    private String userIdName;
    /**
     * 开始时间 临时字段
     */
    @JSONField(serialize=false)
    private String startDate;
    /**
     * 结束时间 临时字段
     */
    @JSONField(serialize=false)
    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getUserIdName() {
        return userIdName;
    }

    public void setUserIdName(String userIdName) {
        this.userIdName = userIdName;
    }

    /**
     * 登录登出
     */
    @JSONField(name="inout_type")
    private int inoutType;
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public Date getRecordDate() {
        return recordDate;
    }
    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public int getInoutType() {
        return inoutType;
    }
    public void setInoutType(int inoutType) {
        this.inoutType = inoutType;
    }

}
