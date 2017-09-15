package com.drore.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明:扩展默认的用户认证的bean为 <br/>
 * 项目名称: guided-assistant-client <br/>
 * 创建日期: 2016年8月29日 上午11:03:56 <br/>
 * 作者: wdz
 */
public class UsernamePasswordCaptchaToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 1L;

	private String captcha;

	/**
	 * uc系统获取的token，
	 */
	private String ucToken;

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public UsernamePasswordCaptchaToken() {
		super();

	}

	public UsernamePasswordCaptchaToken(String username, char[] password,
										boolean rememberMe, String host, String captcha, String ucToken) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;
		this.ucToken = ucToken;
	}
	public UsernamePasswordCaptchaToken(String username, char[] password,
										boolean rememberMe, String host, String captcha) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;
	}

	public String getUcToken() {
		return ucToken;
	}

	public void setUcToken(String ucToken) {
		this.ucToken = ucToken;
	}
}