package com.drore.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2016<br/>
 * 说明:基础实体类 <br/>
 * 项目名称: drore-base <br/>
 * 创建日期: 2016年11月2日 下午3:15:40 <br/>
 * 作者: xwb
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    @JSONField(name = "sort")
    private int sort;
    @JSONField(name = "synch_status")
    private String synchStatus;
    private String creator;
    private String modifier;
    @JSONField(name = "is_deleted")
    private String isDeleted;
    @JSONField(name = "create_time",format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(name = "modified_time",format = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;
    private String datafrom;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getSynchStatus() {
        return synchStatus;
    }

    public void setSynchStatus(String synchStatus) {
        this.synchStatus = synchStatus;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getDatafrom() {
        return datafrom;
    }

    public void setDatafrom(String datafrom) {
        this.datafrom = datafrom;
    }
}
