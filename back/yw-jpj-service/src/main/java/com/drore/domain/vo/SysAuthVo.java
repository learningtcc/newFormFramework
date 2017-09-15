package com.drore.domain.vo;

import com.drore.domain.sys.SysAuthority;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;


/**
 * 
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2016<br/>
 * 说明:权限树vo <br/>
 * 项目名称: guided-assistant-manage <br/>
 * 创建日期: 2016年8月16日 下午7:53:29 <br/>
 * 作者: lj
 */
public class SysAuthVo extends SysAuthority
{
	/**
	 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
	 * 说明: <br/>
	 * 项目名称: drore-tenant-manage <br/>
	 * 创建日期: 2016年9月27日 下午1:30:07 <br/>
	 * 作者: wdz
	 */
	private static final long serialVersionUID = 524028812401017834L;

	public SysAuthVo()
	{
	}
	
	public SysAuthVo(SysAuthority orig)
	{
		try
		{
			BeanUtils.copyProperties(this, orig);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private List<SysAuthVo> children;

	public List<SysAuthVo> getChildren()
	{
		return children;
	}

	public void setChildren(List<SysAuthVo> children)
	{
		this.children = children;
	}
	
}
