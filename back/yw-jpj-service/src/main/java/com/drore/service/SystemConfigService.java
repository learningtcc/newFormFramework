package com.drore.service;


import com.drore.domain.SystemConfig;
import com.drore.util.RestMessageModel;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明: 系统配置服务 接口<br/>
 * 项目名称: drore-base <br/>
 * 创建日期: 2016年12月8日 上午10:47:47 <br/>
 * 作者: wdz
 */
public interface SystemConfigService {

	/**
	 * 新增系统配置
	 * 
	 * @param data
	 * @return
	 */
	public RestMessageModel addConfig(SystemConfig data);
	
	
	/**
	 * 删除系统配置
	 * 
	 * @param keyword
	 * @return
	 */
	public RestMessageModel removeConfigByKeyword(String keyword);
	/**
	 * 获取系统配置
	 * 
	 * @param code
	 * @return 返回null没有该配置
	 */
	public SystemConfig getSysConfigByKeyword(String code);

	/**
	 * 保存系统配置
	 * 
	 * @param keyword
	 * @param value 
	 * @return 
	 */
	public RestMessageModel setSysConfigByKeyword(String keyword, String value);

	/**
	 * 根据Code查询
	 *
	 * @param code
	 * @return
	 */
	public SystemConfig findByCode(String code);

	/**
	 * 更新
	 * @param code
	 * @param value
	 * @return
	 */
	public RestMessageModel update(String code, String value);


	public <T> T getSysConfigByKeyword(String code, Class<T> clazz);

}
