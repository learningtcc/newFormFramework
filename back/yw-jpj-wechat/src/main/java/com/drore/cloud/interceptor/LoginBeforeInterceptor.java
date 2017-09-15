/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */
package com.drore.cloud.interceptor;

import com.drore.cloud.constant.LocalConstant;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.annotation.Login;
import com.drore.cloud.sdk.common.exception.UserNotLoginException;
import com.drore.cloud.sdk.common.util.GsonUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.net.URLDecoder;


/***
 * @since:cloud-ims 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2016/9/20 12:22
 */
public class LoginBeforeInterceptor implements HandlerInterceptor {
    @Autowired
    private CloudQueryRunner run;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag=true;
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Login login= AnnotationUtils.findAnnotation(method, Login.class);
        if (login!=null) {
            //need login
            HttpSession session=request.getSession();
            //获取login注解
            if (session.getAttribute(LocalConstant.SESSION_CURRENT_USER)==null) {
                Cookie[] cookies=request.getCookies();
                System.out.println("cookies:"+cookies);
                String value="";
                if (cookies!=null&&cookies.length>0){
                    for (Cookie cookie:cookies){
                        System.out.println("Name:"+cookie.getName());
                        System.out.println("Value:"+cookie.getValue());
                        if (StringUtils.equalsIgnoreCase("MemberInfo",cookie.getName())){
                            value=cookie.getValue();
                            break;
                        }
                    }
                }
                if (StringUtils.isNotBlank(value)){
                    ApplicationContext ac2 = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
                    JsonElement je=new JsonParser().parse(URLDecoder.decode(value,"UTF-8"));
                    System.out.println("value====="+je.toString());
                    if (je!=null&&!je.isJsonNull()&&je.isJsonObject()){
                        //获取账号
                        String account= GsonUtil.toStringValue(je.getAsJsonObject().get("account_no"));
                        String nonstr=GsonUtil.toStringValue(je.getAsJsonObject().get("nonstr"));
//                        String password= DES.decrypt(nonstr, getSystemConfig().getDesKey());
//                        LogbackLogger.info("account:"+account+",nonstr:"+nonstr+",password;"+password);
//                        MemberService memberService = ac2.getBean("memberService", MemberService.class);
//
//                        memberService.login(account,password);
                    }
                    //CloudQueryRunner run = ac2.getBean(CloudQueryRunner.class);
                    //MemberInfo member_info = run.queryOne(MemberInfo.class,"member_info", value);
                    //ThreadLocalHolder.getSession().setAttribute(LocalConstant.SESSION_CURRENT_USER, member_info);
                }else{
                    flag=false;
                    throw new UserNotLoginException("用户未登陆");
                }
            }
        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
