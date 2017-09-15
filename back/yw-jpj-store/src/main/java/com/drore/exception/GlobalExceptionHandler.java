/******************************************************
* 浙江卓锐科技股份有限公司 版权所有 © Copyright 2015
*
* 项目名称: cloud-cms-webapp
* 创建日期: 2015年12月10日 上午10:38:32
* 作者:    肖玉明   xiaoymin@foxmail.com
*******************************************************/
package com.drore.exception;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.exception.ObjectValidateErrorException;
import com.drore.cloud.sdk.common.exception.UserNotLoginException;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.common.util.RootLogger;
import com.drore.cloud.sdk.exception.AuthenticationErrorException;
import com.drore.cloud.sdk.exception.IllegalRequestParametersException;
import com.drore.cloud.sdk.exception.UcException;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.kernel.pay.exception.PayException;
import com.drore.util.JSONObjResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/***
 * GlobalExceptionHandler
 * @since:cloud-cms-webapp 1.0 
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	/***
	 * 用户未登陆
	 */
	private final int USER_NOT_LOGIN_ERR_CODE=302;

	/***
	 * 服务器参数异常
	 * @param request
	 * @param response
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value=AuthenticationErrorException.class)
	@ResponseStatus(value=HttpStatus.OK)
	@ResponseBody
	public JSONObject handlerAuthenticationErrorException(HttpServletRequest request, HttpServletResponse response, Exception ex){
		RestMessage r=new RestMessage();
		r.setSuccess(false);
		r.setMessage(ex.getMessage());
		r.setErrorMessage(ex.getMessage());
		return JSONObjResult.toJSONObj(r);
	}

	/***
	 * 服务器参数异常
	 * @param request
	 * @param response
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value=IllegalArgumentException.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public JSONObject handlerServerException(HttpServletRequest request, HttpServletResponse response, Exception ex){
		RestMessage r=new RestMessage();
		r.setSuccess(false);
		r.setMessage(ex.getMessage());
		r.setErrorMessage(ex.getMessage());
		return JSONObjResult.toJSONObj(r);
	}

	/***
	 * 服务器参数异常
	 * @param request
	 * @param response
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value=IllegalRequestParametersException.class)
	@ResponseBody
	public JSONObject handlerRequestParametersException(HttpServletRequest request, HttpServletResponse response, Exception ex){
		RestMessage r=new RestMessage();
		try{
			LogbackLogger.info("requestError..");
			r.setSuccess(false);
			r.setMessage(ex.getMessage());
			r.setErrorMessage(ex.getMessage());
			response.setStatus(HttpStatus.OK.value());
		}catch (Exception e){
		}
		  return JSONObjResult.toJSONObj(r);
	}

	/***
	 * 服务器参数异常
	 * @param request
	 * @param response
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value=ApiException.class)
	@ResponseBody
	public JSONObject handlerApiException(HttpServletRequest request, HttpServletResponse response, Exception ex){
		RestMessage r=new RestMessage();
		try{

			r.setSuccess(false);
			r.setMessage(ex.getMessage());
			r.setErrorMessage(ex.getMessage());
			response.setStatus(HttpStatus.OK.value());
		}catch (Exception e){
		}
		return JSONObjResult.toJSONObj(r);
	}

	/**
	 * 自定义业务异常
	 * @param request
	 * @param response
	 * @param ex
     * @return
     */
	@ExceptionHandler(value=CustomException.class)
	@ResponseBody
	public JSONObject handlerCustomException(HttpServletRequest request, HttpServletResponse response, Exception ex){
		RestMessage r=new RestMessage();
		try{

			r.setSuccess(false);
			r.setMessage(ex.getMessage());
			r.setErrorMessage(ex.getMessage());
			response.setStatus(HttpStatus.OK.value());
		}catch (Exception e){
		}
		return JSONObjResult.toJSONObj(r);
	}

	@ExceptionHandler(value=PayException.class)
	@ResponseBody
	public JSONObject handlerPayException(HttpServletRequest request, HttpServletResponse response, Exception ex){
		RestMessage r=new RestMessage();
		try{
			r.setSuccess(false);
			r.setMessage("支付异常:"+ex.getMessage());
			r.setErrorMessage(ex.getMessage());
			response.setStatus(HttpStatus.OK.value());
		}catch (Exception e){
		}
		return JSONObjResult.toJSONObj(r);
	}

	@ExceptionHandler(value=UcException.class)
	@ResponseBody
	public JSONObject handlerUcException(HttpServletRequest request, HttpServletResponse response, Exception ex){
		RestMessage r=new RestMessage();
		try{

			r.setSuccess(false);
			r.setMessage("用户中心异常:"+ex.getMessage());
			r.setErrorMessage(ex.getMessage());
			response.setStatus(HttpStatus.OK.value());
		}catch (Exception e){
		}
		return JSONObjResult.toJSONObj(r);
	}

	@ExceptionHandler(value=ObjectValidateErrorException.class)
	@ResponseBody
	public JSONObject handlerObjectValidateErrorException(HttpServletRequest request, HttpServletResponse response, Exception ex){
		RestMessage r=new RestMessage();
		try{

			r.setSuccess(false);
			r.setMessage(ex.getMessage());
			r.setErrorMessage("数据中心错误!请检查是否是删除了最后一页最后一条数据!"+ex.getMessage());
			response.setStatus(HttpStatus.OK.value());
		}catch (Exception e){
		}
		return JSONObjResult.toJSONObj(r);
	}




	@ExceptionHandler(value=UserNotLoginException.class)
	@ResponseBody
	public JSONObject handlerUserNotLoginException(HttpServletRequest request, HttpServletResponse response, Exception ex){
		//用户未登陆
		//判断请求头,是否ajax请求
		String header=request.getHeader("X-Requested-With");
		RestMessage r=new RestMessage();
		try {
			RootLogger.info("Request Header:"+header);
			if (StringUtils.isNotBlank(header)||StringUtils.equalsIgnoreCase(header, "XMLHttpRequest")) {
				//ajax
				response.setStatus(USER_NOT_LOGIN_ERR_CODE);
				r.setSuccess(false);
				r.setMessage("用户未登录");
				r.setErrorMessage(ex.getMessage());
			}else{
				response.sendRedirect("/html/login.html");
			}
		} catch (Exception e) {
			RootLogger.error(e);
		}
		return JSONObjResult.toJSONObj(r);
		//return "login";
	}
	@ExceptionHandler(value=BindException.class)
	@ResponseStatus(value=HttpStatus.OK)
	@ResponseBody
	public JSONObject handlerBindException(HttpServletRequest request, HttpServletResponse response, BindException ex){
		RestMessage r=new RestMessage();
		try{
			List<ObjectError> list = ex.getAllErrors();
			StringBuffer stringBuffer = new StringBuffer();
			for(ObjectError objectError :list){
				if(stringBuffer.length()>0)stringBuffer.append(" && ");
				stringBuffer.append(objectError.getDefaultMessage());
			}
			r.setSuccess(false);
			r.setMessage(stringBuffer.toString());
			r.setErrorMessage(stringBuffer.toString());
		}catch (Exception e){
		}
		return JSONObjResult.toJSONObj(r);
	}



}

