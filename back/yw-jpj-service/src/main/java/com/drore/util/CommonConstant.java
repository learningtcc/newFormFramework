package com.drore.util;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明:公共常量 <br/>
 * 项目名称: guided-assistant-service <br/>
 * 创建日期: 2016年8月15日 下午1:16:35 <br/>
 * 作者: wdz
 */
public class CommonConstant {
	/**
	 * 顶级
	 */
	public static String ROOT = "root";
	/**
	 * 密码加密
	 */
	public static final int SALT_SIZE = 8;
	/**
	 * 密码加密
	 */
	public static int HASH_INTERATIONS = 1024;

	/**
	 * 启用
	 */
	public static String ISENABLE = "Y";
	/**
	 * 禁用
	 */
	public static String ISNOTENABLE = "N";

	/**
	 * 邮件服务器配置项
	 */
	public static final String SYSCONFIG_EMAILCONFIGKEY = "email_config";

	/**
	 * 邮箱找回密码有效时间
	 */
	public static final String SYSCONFIG_LOOK_PASSWORD_TIME = "look_password_time";
	
	
	/**
	 * 客户端导出报表列名称
	 */
	public static String[] CLIENTREPORTCOLUMNS = new String[]{"线路名称","发团人数","成人","儿童","幼儿"};
	
	/**
	 * 客户端导出报表文件名称
	 */
	public static String CLIENTREPORTFILENAME = "发团统计报表.xls";
	/**
	 * 客户端导出报表文件页签显示
	 */
	public static String CLIENTREPORTSHEETNAME = "发团统计报表";
	
	/**
	 * 导游类型
	 */
	public static String GUIDTYPECODE = "guidType";
	
	/**
	 * 报表默认开始时间
	 */
	public static String REPORTSTARTTIME = "1900-01-01";
	
	
	/**
	 * 资源文件xml 名称
	 */
	public static final String RESOURCE_XML="resource.xml";
	/**
	 * 资源详情xml文件
	 */
	public static final String RESOURCE_DETAIL_XML="resource_detail.xml";

}
