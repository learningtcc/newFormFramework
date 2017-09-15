package com.drore.domain.sys;

import com.alibaba.fastjson.annotation.JSONField;
import com.drore.domain.BaseEntity;

/**
 * 
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明:用户角色关联类 <br/>
 * 项目名称: drore-tenant-entity <br/>
 * 创建日期: 2016年9月27日 下午1:10:34 <br/>
 * 作者: wdz
 */
public class SysUserRole extends BaseEntity {
 
	private static final long serialVersionUID = 1L;
	public static String table="sys_user_role";
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
