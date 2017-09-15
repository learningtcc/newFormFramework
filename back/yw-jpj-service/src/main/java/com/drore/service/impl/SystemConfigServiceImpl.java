package com.drore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.drore.domain.SystemConfig;
import com.drore.enums.CommonEnum;
import com.drore.exception.CustomException;
import com.drore.service.SystemConfigService;
import com.drore.util.LoginStoreUserUtil;
import com.drore.util.RestMessageModel;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 浙江卓锐科技股份有限公司 版权所有 Copyright 2016<br/>
 * 说明: 系统配置服务<br/>
 * 项目名称: drore-base <br/>
 * 创建日期: 2016年8月5日 上午11:21:10 <br/>
 * 作者: lj
 */
@Service
public class SystemConfigServiceImpl extends BaseServiceImpl implements
		SystemConfigService {

	/**
	 * 新增系统配置
	 * 
	 * @param data
	 * @return
	 */
	public RestMessageModel addConfig(SystemConfig data) {
		if (null == data) {
			throw new CustomException("配置数据不能为空!");
		}
		// 检查当前系统配置
		SystemConfig sysConfig = getSysConfigByKeyword(data.getCode());
		if (null != sysConfig
				&& CommonEnum.YesOrNo.NO.getCode().equals(
						sysConfig.getIsDeleted())) {
			throw new CustomException(data.getCode() + "已存在");
		}
		data.setIsDeleted("N");
		return  saveObject(data, SystemConfig.table);
	}

	/**
	 * 删除系统配置
	 * 
	 * @param keyword
	 * @return
	 */
	public RestMessageModel removeConfigByKeyword(String keyword) {
		JSONObject result = new JSONObject();
		result.put("success", false);
		if (StringUtils.isEmpty(keyword)) {
			throw new CustomException(  "keyword不能为空!");
		}
		// 检查当前系统配置
		SystemConfig sysConfig = getSysConfigByKeyword(keyword);
		if (null == sysConfig) {
			throw new CustomException(  "配置项不存在!");
		}
		return deleteById(sysConfig.getId(), SystemConfig.table);

	}

	/**
	 * 获取系统配置
	 * 
	 * @param code
	 * @return 返回null没有该配置
	 */
	public SystemConfig getSysConfigByKeyword(String code) {

		Map<String, Object> term = new HashMap<String, Object>();
		term.put("code", code);
		term.put("is_deleted", 'N');
		SystemConfig config = queryOne(SystemConfig.table, term,
				SystemConfig.class);
		return config;

	}

	/**
	 * 保存系统配置
	 * 
	 * @param keyword
	 * @param value
	 * @return
	 */
	public RestMessageModel setSysConfigByKeyword(String keyword, String value) {

		// 获取当前系统登陆的密码强度配置
		SystemConfig sysConfig = getSysConfigByKeyword(keyword);
		if (null == sysConfig) {
			throw new CustomException(  "不存在配置项!");
		}
		String id = sysConfig.getId();
		if (StringUtils.isEmpty(id)) {
			throw new CustomException(  keyword + "配置项主键不能为空!");
		}
		JSONObject update = new JSONObject();
		update.put("value", value);
		return updateObject(id, update, SystemConfig.table);
	}

	@Override
	public SystemConfig findByCode(String code) {
		 Map<String, Object> term = new HashMap<String, Object>();
		term.put("code", code);
		term.put("is_deleted", CommonEnum.YesOrNo.NO.getCode());
		return this.queryOne(SystemConfig.table, term, SystemConfig.class);
	}

	@Override
	public RestMessageModel update(String code, String value) {
		SystemConfig systemConfig = findByCode(code);
		if (systemConfig == null) {
			throw new CustomException("查找不到对象!");
		}
		systemConfig.setValue(value);
		systemConfig.setModifier(LoginStoreUserUtil.getUserId());
		return this.updateObject(systemConfig.getId(),
				JSONObject.toJSON(systemConfig), SystemConfig.table);
	}

	@Override
	public <T> T getSysConfigByKeyword(String code, Class<T> clazz) {
		SystemConfig sysConfigByKeyword = this.getSysConfigByKeyword(code);

		if (null != sysConfigByKeyword) {
			String value = sysConfigByKeyword.getValue();
			if (StringUtils.isNotEmpty(value)) {
				return new Gson().fromJson(value, clazz);
			} else {
				return null;
			}
		}
		return null;
	}


}
