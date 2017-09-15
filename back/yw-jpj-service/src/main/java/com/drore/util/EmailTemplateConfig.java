package com.drore.util;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明:旅行社邮件模板 <br/>
 * 项目名称: guided-assistant-service <br/>
 * 创建日期: 2016年8月26日 上午10:49:35 <br/>
 * 作者: wdz
 */
public class EmailTemplateConfig {
	 

	/**
	 * 系统用户 密码重置邮件主题
	 */
	private static String sysRePassSubject;

	/**
	 * 系统用户 密码重置邮件内容
	 */
	private static String sysRePassContent;

	/**
	 * 系统用户 新增用户密码邮件主题
	 */
	private static String sysNewPassSubject;
	/**
	 * 系统用户 新增用户密码邮件内容
	 */
	private static String sysNewPassContent;
	public static String getSysRePassSubject() {
		return sysRePassSubject;
	}
	public static void setSysRePassSubject(String sysRePassSubject) {
		EmailTemplateConfig.sysRePassSubject = sysRePassSubject;
	}
	public static String getSysRePassContent() {
		return sysRePassContent;
	}
	public static void setSysRePassContent(String sysRePassContent) {
		EmailTemplateConfig.sysRePassContent = sysRePassContent;
	}
	public static String getSysNewPassSubject() {
		return sysNewPassSubject;
	}
	public static void setSysNewPassSubject(String sysNewPassSubject) {
		EmailTemplateConfig.sysNewPassSubject = sysNewPassSubject;
	}
	public static String getSysNewPassContent() {
		return sysNewPassContent;
	}
	public static void setSysNewPassContent(String sysNewPassContent) {
		EmailTemplateConfig.sysNewPassContent = sysNewPassContent;
	}
	
	
	
 
    

}
