package com.drore.util;


import com.drore.enums.CommonEnum;

/**
 * 
 * 浙江卓锐科技股份有限公司 版权所有 Copyright 2016<br/>
 * 说明: 表单校验结果<br/>
 * 项目名称: guided-assistant-service <br/>
 * 创建日期: 2016年7月21日 下午1:06:47 <br/>
 * 作者: lj
 */
public class ValidateResult
{
	/**
	 * 错误信息
	 */
	private String errorMsg;
	/**
	 * 错误码
	 */
	private int errorCode;

	public ValidateResult()
	{
		this.errorCode = CommonEnum.ValidateResultEnum.SUCCESS.getErrorCode();
	}

	public boolean isSuccess()
	{
		return CommonEnum.ValidateResultEnum.SUCCESS.getErrorCode() == this.errorCode;
	}

	public String getErrorMsg()
	{
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg)
	{
		this.errorMsg = errorMsg;
	}

	public int getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(CommonEnum.ValidateResultEnum error)
	{
		this.errorCode = error.getErrorCode();
		this.errorMsg = error.getMsg();
	}

}
