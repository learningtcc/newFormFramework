package com.drore.domain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2016<br/>
 * 说明: 系统配置表<br/>
 * 项目名称: drore-base <br/>
 * 创建日期: 2016年11月2日 下午3:15:14 <br/>
 * 作者: xwb
 */
public class SystemConfig extends BaseEntity {
	 
	private static final long serialVersionUID = 1L;

	public static String table = "system_config";

	/**
	 * 配置项的key
	 */
	private String code;
	/**
	 * 配置项的值
	 */
	private String value;
	/**
	 * 是否启用
	 */
	@JSONField(name = "is_enable")
	private String isEnable;
	/**
	 * 配置项的描述（名称）
	 */
	private String name;

 

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
