package com.drore.exception;




/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明:自定义错误异常 <br/>
 * 项目名称: cloud-dataCenter-service <br/>
 * 创建日期: 2016年11月2日 下午8:43:45 <br/>
 * 作者: wdz
 */
public class CustomException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = 7493711492820795133L;

	public CustomException(Throwable cause) {
	    super(cause);
	}

	public CustomException(String message) {
	    super(message);
	}

	public CustomException(String message, Throwable cause) {
	    super(message , cause);
	}


}
