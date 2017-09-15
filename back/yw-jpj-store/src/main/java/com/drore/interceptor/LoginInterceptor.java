package com.drore.interceptor;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:  此项目未使用到   <br/>
 * 项目名称: SpringBoot <br/>
 * 创建日期:  2017/4/18 18:15  <br/>
 * 作者:    wdz
 */
@Deprecated
public class LoginInterceptor implements HandlerInterceptor {
    static Logger logger=Logger.getLogger(LoginInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        boolean flag=true;
        HttpSession session=request.getSession();
//        if (session.getAttribute(LocalConstant.SESSION_CURRENT_USER)==null) {
//            flag=false;
//            Cookie ck = new Cookie("accountNo", "");
//            ck.setMaxAge(7 * 24 * 60 * 60);
//            ck.setPath(request.getContextPath() + "/");
//            response.addCookie(ck);
//            throw new UserNotLoginException("登录超时");
//        }

        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        logger.debug("postHandle......");

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        logger.debug("afterCompletion...");

    }
}
