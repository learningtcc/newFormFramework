package com.drore.domain.store;

import com.alibaba.fastjson.annotation.JSONField;
import com.drore.domain.BaseEntity;

import java.io.Serializable;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/8/31 10:38  <br/>
 * 作者:    wdz
 */
public class StoreRole extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static String table="store_role";
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色编码
     */
    private String code;
    /**
     * 是否启用
     */
    @JSONField(name="is_enable")
    private String isEnable;
    /**
     * 描述
     */
    private String remark;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }
    public void setCode(String code)
    {
        this.code = code;
    }
    public String getIsEnable() {
        return isEnable;
    }
    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }



}
