package com.drore.util;

import com.drore.domain.store.StoreUser;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明:商家登录信息 <br/>
 * 项目名称: guided-assistant-service <br/>
 * 创建日期: 2016年8月29日 上午9:04:17 <br/>
 * 作者: wdz
 */
public class LoginStoreUserUtil {

	public static String getUserId() {
		StoreUser sysUser = getStoreUser();
		return sysUser.getId();
	}
	public static String getStoreId() {
		StoreUser sysUser = getStoreUser();
		return sysUser.getStoreId();
	}



	public static StoreUser getStoreUser() {
		StoreUser sysUser = new StoreUser();
		sysUser.setId("test");
		sysUser.setStoreId("develop");
		return  sysUser;


		/*Subject subject = SecurityUtils.getSubject();
		Object object = subject.getPrincipal();
		StoreUser sysUser = null;
		if (object != null) {
			sysUser = (StoreUser) object;
		} else{
			throw new CustomException("无法获取登录用户信息");
		}
		return sysUser;*/
	}
}
