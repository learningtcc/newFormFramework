package com.drore.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2015<br/>
 * 说明: 获取登录ip工具类 <br/>
 * 项目名称: cloud-uc <br/>
 * 创建日期: 2016年7月12日 上午10:44:10 <br/>
 * 作者: wdz
 */
public class IpUtil
{
	public static String getIpAddr(HttpServletRequest request)
	{
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress))
		{
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress))
		{
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress))
		{
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1")
					|| ipAddress.equals("0:0:0:0:0:0:0:1"))
			{
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try
				{
					inet = InetAddress.getLocalHost();
				}
				catch (UnknownHostException e)
				{
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15)
		{ // "***.***.***.***".length() = 15
			if (ipAddress.indexOf(",") > 0)
			{
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

}
