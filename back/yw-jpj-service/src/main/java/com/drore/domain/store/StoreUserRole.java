package com.drore.domain.store;

import com.alibaba.fastjson.annotation.JSONField;
import com.drore.domain.BaseEntity;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/8/31 10:37  <br/>
 * 作者:    wdz
 */
public class StoreUserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;
    public static String table="store_user_role";
    /**
     * 用户主键
     */
    @JSONField(name="user_id")
    private String userId;
    /**
     * 角色主键
     */
    @JSONField(name="role_id")
    private String roleId;
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getRoleId() {
        return roleId;
    }
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }


}
