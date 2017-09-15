package com.drore.util;

import com.drore.cloud.sdk.common.resp.RestMessage;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明: 自定义的返回结果 <br/>
 * 项目名称: guided-assistant-service <br/>
 * 创建日期: 2016年8月10日 下午2:15:06 <br/>
 * 作者: wdz
 */
public class RestMessageModel extends RestMessage {

	private static final long serialVersionUID = -6425094769354205188L;
 
	@SuppressWarnings("deprecation")
	public RestMessageModel(){
		super();
		super.setSuccess(false);
		super.setErrorMessage("正确");
	}
	@SuppressWarnings("deprecation")
	public RestMessageModel(RestMessage restMessage){
		super.setData(restMessage.getData());
		super.setErrCode(restMessage.getErrCode());
		super.setErrMsg(restMessage.getErrMsg());
		super.setErrorMessage(restMessage.getErrorMessage());
		super.setId(restMessage.getId());
		super.setMessage(restMessage.getMessage());
		super.setSuccess(restMessage.isSuccess());
	}
	
	
}
