package com.drore.util.captcah;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明:创建验证码异常类 <br/>
 * 项目名称: guided-assistant-client <br/>
 * 创建日期: 2016年8月29日 上午11:02:42 <br/>
 * 作者: wdz
 */
public class CaptchaException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public CaptchaException() {

		super();

	}

	public CaptchaException(String message, Throwable cause) {

		super(message, cause);

	}

	public CaptchaException(String message) {

		super(message);

	}

	public CaptchaException(Throwable cause) {

		super(cause);

	}

}	