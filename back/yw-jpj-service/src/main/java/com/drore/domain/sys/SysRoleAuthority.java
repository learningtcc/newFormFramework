package com.drore.domain.sys;

import com.alibaba.fastjson.annotation.JSONField;
import com.drore.domain.BaseEntity;

import java.io.Serializable;

/**
 * 
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明: 角色权限 关系<br/>
 * 项目名称: drore-tenant-entity <br/>
 * 创建日期: 2016年9月27日 下午1:10:56 <br/>
 * 作者: wdz
 */
public class SysRoleAuthority extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	public static String table="sys_role_authoritys";
	 /**
     * 角色主键
     */
	@JSONField(name="role_id")
	private String roleId;
    /**
     * 权限主键
     */
    @JSONField(name="authority_id")
	private String authorityId;
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getAuthorityId() {
		return authorityId;
	}
	public void setAuthorityId(String authorityId) {
		this.authorityId = authorityId;
	}
    
    
}
