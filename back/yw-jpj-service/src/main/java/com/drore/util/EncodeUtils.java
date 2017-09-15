/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 */
package com.drore.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2015<br/>
 * 说明:生成盐值码 <br/>
 * 项目名称: cloud-uc <br/>
 * 创建日期: 2016年6月30日 下午4:44:24 <br/>
 * 作者: wdz
 */
public class EncodeUtils
{

	private static final String DEFAULT_URL_ENCODING = "UTF-8";

	/**
	 * Hex编码.
	 */
	public static String hexEncode(byte[] input)
	{
		return Hex.encodeHexString(input);
	}

	/**
	 * Hex解码.
	 */
	public static byte[] hexDecode(String input)
	{
		try
		{
			return Hex.decodeHex(input.toCharArray());
		}
		catch (DecoderException e)
		{
			throw new IllegalStateException("Hex Decoder exception", e);
		}
	}

	/**
	 * Base64编码.
	 */
	public static String base64Encode(byte[] input)
	{
		return new String(Base64.encodeBase64(input));
	}

	/**
	 * Base64编码, URL安全(将Base64中的URL非法字符如+,/=转为其他字符, 见RFC3548).
	 */
	public static String base64UrlSafeEncode(byte[] input)
	{
		return Base64.encodeBase64URLSafeString(input);
	}

	/**
	 * Base64解码.
	 */
	public static byte[] base64Decode(String input)
	{
		return Base64.decodeBase64(input);
	}

	/**
	 * URL 编码, Encode默认为UTF-8.
	 */
	public static String urlEncode(String input)
	{
		try
		{
			return URLEncoder.encode(input, DEFAULT_URL_ENCODING);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new IllegalArgumentException(
					"Unsupported Encoding Exception", e);
		}
	}

	/**
	 * URL 解码, Encode默认为UTF-8.
	 */
	public static String urlDecode(String input)
	{
		try
		{
			return URLDecoder.decode(input, DEFAULT_URL_ENCODING);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new IllegalArgumentException(
					"Unsupported Encoding Exception", e);
		}
	}

	/**
	 * Xml 转码.
	 */
	public static String xmlEscape(String xml)
	{
		return StringEscapeUtils.escapeXml(xml);
	}

	/**
	 * Xml 解码.
	 */
	public static String xmlUnescape(String xmlEscaped)
	{
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}
}
