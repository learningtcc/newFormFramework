package com.drore.domain.sys;

import com.alibaba.fastjson.annotation.JSONField;
import com.drore.domain.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明:权限表 <br/>
 * 项目名称: drore-tenant-entity <br/>
 * 创建日期: 2016年9月27日 下午1:11:11 <br/>
 * 作者: wdz
 */
public class SysAuthority extends BaseEntity implements Serializable
{
	private static final long serialVersionUID = 1L;
	public static String table = "sys_authority";

	private String id;
	@JSONField(name = "sort")
	private int sort;
	@JSONField(name = "synch_status")
	private String synchStatus;
	private String creator;
	private String modifier;
	@JSONField(name = "is_deleted")
	private String isDeleted;
	@JSONField(name = "create_time")
	private Date createTime;
	@JSONField(name = "modified_time")
	private Date modifiedTime;
	private String datafrom;

	/**
	 * 名称
	 */
	private String name;
	/**
	 * 编号
	 */
	private String code;
	/**
	 * 类型
	 */
	private int type;
	/**
	 * 是否启用
	 */
	@JSONField(name = "is_enable")
	private String isEnable;
	/**
	 * url地址
	 */
	private String url;

	/**
	 * 父ID
	 */
	@JSONField(name = "parent_id")
	private String parentId;

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
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

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public String getIsEnable()
	{
		return isEnable;
	}

	public void setIsEnable(String isEnable)
	{
		this.isEnable = isEnable;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int getSort() {
		return sort;
	}

	@Override
	public void setSort(int sort) {
		this.sort = sort;
	}

	@Override
	public String getSynchStatus() {
		return synchStatus;
	}

	@Override
	public void setSynchStatus(String synchStatus) {
		this.synchStatus = synchStatus;
	}

	@Override
	public String getCreator() {
		return creator;
	}

	@Override
	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Override
	public String getModifier() {
		return modifier;
	}

	@Override
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	@Override
	public String getIsDeleted() {
		return isDeleted;
	}

	@Override
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public Date getCreateTime() {
		return createTime;
	}

	@Override
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public Date getModifiedTime() {
		return modifiedTime;
	}

	@Override
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	@Override
	public String getDatafrom() {
		return datafrom;
	}

	@Override
	public void setDatafrom(String datafrom) {
		this.datafrom = datafrom;
	}
}
