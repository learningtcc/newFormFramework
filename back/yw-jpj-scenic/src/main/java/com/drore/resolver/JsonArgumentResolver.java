package com.drore.resolver;

import com.drore.cloud.sdk.common.annotation.JsonArgument;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: SpringBoot <br/>
 * 创建日期:  2017/4/18 18:17  <br/>
 * 作者:    wdz
 */
public class JsonArgumentResolver  implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonArgument.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request=webRequest.getNativeRequest(HttpServletRequest.class);
        JsonObject jo=new JsonObject();
        Enumeration<String> parameters=request.getParameterNames();
        while (parameters.hasMoreElements()) {
            String key=(String)parameters.nextElement();
            String[] value=request.getParameterValues(key);
            String joinValue= StringUtils.join(value);
            jo.addProperty(key, joinValue);
			/*if (StringUtils.isNotBlank(joinValue)) {

			}*/
        }
        LogbackLogger.info("JsonRequestBody--->");
        LogbackLogger.info(jo.toString());
        return jo;
    }
}
