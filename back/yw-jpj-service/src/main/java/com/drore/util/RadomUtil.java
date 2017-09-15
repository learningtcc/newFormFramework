package com.drore.util;

import java.util.Random;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2015<br/>
 * 说明: 生成随机码<br/>
 * 项目名称: cloud-uc <br/>
 * 创建日期: 2016年4月6日 下午5:19:53 <br/>
 * 作者: wdz
 */
public class RadomUtil
{

	public static String getRandomString(int length)
	{
		if (length < 1)
			return "";
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; i++)
		{

			int number = random.nextInt(62);

			sb.append(str.charAt(number));
		}
		return sb.toString();
	}

	public static void main(String[] args)
	{
		while (true)
			System.out.println(getRandomString(8));
	}

}
