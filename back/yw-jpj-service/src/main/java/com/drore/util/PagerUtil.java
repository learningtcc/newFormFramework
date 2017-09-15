package com.drore.util;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class PagerUtil
{

	private Integer pageNo = 1;

	private Integer pageSize = 20;
	private List<?> root;

	@JSONField(serialize = false)
	private boolean success = true;
	/**
	 * 总页数
	 */
	private Integer total;
	
	/**
	 * 总数
	 */
	private Integer count;

	private String uplink;
	/**
	 * 错误提示
	 */
	@JSONField(serialize = false)
	private String errorMessage;
	
	
	public PagerUtil()
	{
	}

	public PagerUtil(int pageSize, int pageNo)
	{
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}
	public PagerUtil(int pageSize)
	{
		this.pageNo = 1;
		this.pageSize = pageSize;
	}
	public Integer getPageNo()
	{
		return pageNo;
	}

	public void setPageNo(Integer pageNo)
	{
		this.pageNo = pageNo;
	}

	public Integer getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(Integer pageSize)
	{
		this.pageSize = pageSize;
	}

	public String getUplink()
	{
		return uplink;
	}

	public void setUplink(String uplink)
	{
		this.uplink = uplink;
	}

	public Integer getTotal()
	{
		return total;
	}

	public void setTotal(Integer total)
	{
		this.total = total;
	}

	public List<?> getRoot()
	{
		return root;
	}

	public void setRoot(List<?> root)
	{
		this.root = root;
	}

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
