package com.drore.cloud.advice;

import com.drore.cloud.exception.MacroApiException;
import com.drore.cloud.sdk.common.exception.UserNotLoginException;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.common.util.RootLogger;
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
 * @author <a href="mailto:baoec@drore.com"></a>
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	/***
	 * 用户未登陆
	 */
	private final int USER_NOT_LOGIN_ERR_CODE=302;
	
	@ExceptionHandler(value=UserNotLoginException.class)
	@ResponseBody
	public RestMessage handlerUserNotLoginException(HttpServletRequest request,HttpServletResponse response, Exception ex){
		//用户未登陆
		//判断请求头,是否ajax请求
		String header=request.getHeader("X-Requested-With");
		RestMessage r=new RestMessage();
		try {
			RootLogger.info("Request Header:"+header);
				//ajax
				response.setStatus(USER_NOT_LOGIN_ERR_CODE);
				r.setSuccess(false);
				r.setMessage("用户未登录");
		} catch (Exception e) {
			RootLogger.error(e);
		}
		return r;
	}
	/***
	 * 服务器参数异常
	 * @param request
	 * @param response
	 * @param ex
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(value=MacroApiException.class)
	public RestMessage handlerApiException(HttpServletRequest request, HttpServletResponse response, Exception ex){
		RestMessage r=new RestMessage();
		try{
			r.setSuccess(false);
			r.setMessage(ex.getLocalizedMessage());
			response.setStatus(HttpStatus.OK.value());
		}catch (Exception e){

		}
		return r;
	}

	@ExceptionHandler(value=BindException.class)
	@ResponseStatus(value=HttpStatus.OK)
	@ResponseBody
	public RestMessage handlerBindException(BindException ex){
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
		}catch (Exception e){
		}
		return r;
	}
}

