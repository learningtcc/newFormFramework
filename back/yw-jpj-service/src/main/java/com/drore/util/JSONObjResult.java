package com.drore.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.drore.cloud.sdk.common.resp.RestMessage;

/**
 * 
 * 浙江卓锐科技股份有限公司 版权所有 Copyright 2016<br/>
 * 说明: 封装对象结果的json结果<br/>
 * 项目名称: guided-assistant-service <br/>
 * 创建日期: 2016年8月16日 下午5:04:05 <br/>
 * 作者: lj
 */
public class JSONObjResult
{
	/**
	 * 是否成功
	 */
	private boolean isSuccess;
	
	/***
	 * 新增、修改主鍵返回id
	 */
	private String id;

	/**
	 * 错误信息
	 */
	private String errorMessage = "未知异常";
	/**
	 * 返回数据
	 */
	private Object data;

	public static JSONObject toJSONObj(Object o, boolean isSuccess,
                                       String errorMessage)
	{
		JSONObjResult jsonObjResult = new JSONObjResult();
		jsonObjResult.setIsSuccess(isSuccess);
		jsonObjResult.setErrorMessage(errorMessage);
		jsonObjResult.setData(JSONUtil.beanToMap(o));

		return JSON.parseObject(JSON.toJSONString(jsonObjResult, SerializerFeature.WriteMapNullValue));

	}

	/**
	 * RestMessageModel 转换
	 * @param o
	 * @return
	 */
	public static JSONObject toJSONObj(RestMessageModel o)
	{
		JSONObjResult jsonObjResult = new JSONObjResult();
		jsonObjResult.setIsSuccess(o.isSuccess());
		jsonObjResult.setErrorMessage(o.getErrorMessage());
		jsonObjResult.setData(o.getData());
		jsonObjResult.setId(o.getId());
		return JSON.parseObject(JSON.toJSONString(jsonObjResult, SerializerFeature.WriteMapNullValue));
	}

	public static JSONObject toJSONObj(RestMessage restMessage)
	{
		RestMessageModel model = new RestMessageModel(restMessage);
		return toJSONObj(model);
	}
	/**
	 * 设置错误信息      （默认isSuccess是false）
	 * @param error
	 * @return
	 */
	public static JSONObject toJSONObj(String error)
	{
		JSONObjResult jsonObjResult = new JSONObjResult();
		jsonObjResult.setIsSuccess(false);
		jsonObjResult.setErrorMessage(error);

		return JSON.parseObject(JSONObject.toJSONString(jsonObjResult, SerializerFeature.WriteMapNullValue));
		 
	}

	public boolean getIsSuccess()
	{
		return isSuccess;
	}

	public void setIsSuccess(boolean isSuccess)
	{
		this.isSuccess = isSuccess;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public Object getData()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
