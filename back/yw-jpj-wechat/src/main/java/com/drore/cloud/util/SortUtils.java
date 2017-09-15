package com.drore.cloud.util;

import com.drore.cloud.sdk.domain.util.RequestExample;

import java.util.HashMap;

/**
 * 排序工具类
 * @author 仁杰
 *
 */
public class SortUtils {

	
	/**
	 * 通常数据表排序
	 * @param sortName	排序字段
	 * @param sort		排序规则
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	public static RequestExample sort(String sortName, String sort, Integer pageSize, Integer currentPage){
		//设置默认分页
		if(pageSize == null || "".equals(pageSize))		pageSize = 10;
		if(currentPage == null || "".equals(currentPage))	currentPage = 1;
			
		RequestExample req = new RequestExample(pageSize, currentPage);
		HashMap<String, Object> param = new HashMap<String,Object>();
		param.put(sortName, sort);
		req.setSort(param);
		return req;
	}
	
}
