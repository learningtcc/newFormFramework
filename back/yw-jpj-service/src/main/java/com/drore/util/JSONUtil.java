package com.drore.util;

import com.google.gson.Gson;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.beanutils.PropertyUtilsBean;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明: json格式转换<br/>
 * 项目名称: guided-assistant-service <br/>
 * 创建日期: 2016年8月16日 下午8:06:01 <br/>
 * 作者: wdz
 */
public class JSONUtil
{

	/**
	 * 实体对象不序列化转换成jsonobject
	 * @param <T>
	 * 
	 * @param obj
	 * @return
	 */
	public static Object beanToMap(Object obj)
	{
		if (obj == null)
			return null;
		if (obj instanceof ArrayList)
		{
			ArrayList<?> list = (ArrayList<?>) obj;
			ArrayList<JSONObject> result = new ArrayList<JSONObject>();
			for (Object o : list)
			{
				JSONObject javaObject = (JSONObject)beanToMap(o);
				result.add(javaObject);
			}
			return result;
		}
//		else if(obj instanceof PagerUtil)
//		{
//			((PagerUtil) obj).setRoot((List<?>) beanToMap(((PagerUtil) obj).getRoot()));
//			
//			return obj;
//			
//		}
		else
		{

			Map<String, Object> params = new HashMap<String, Object>(0);
			try
			{
				PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
				PropertyDescriptor[] descriptors = propertyUtilsBean
						.getPropertyDescriptors(obj);
				for (int i = 0; i < descriptors.length; i++)
				{
					String name = descriptors[i].getName();
					if (!"class".equals(name))
					{
						params.put(name, propertyUtilsBean.getNestedProperty(
								obj, name));
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			JSONObject to = new JSONObject(params);
			return to;
		}
	}
	
	/**
	 * 序列化成数据中心的数据格式
	 * @param o
	 * @return
	 */
	public static Object toDataCenterJSONObject(Object o)
	{
		Gson gson = new Gson();
		
		return JSONObject.parse(gson.toJson(o));
	}
}
