package com.drore.config;

import com.drore.util.captcah.CaptchaServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:    统一的servlet配置 <br/>
 * 项目名称: spring-boot-base <br/>
 * 创建日期:  2017/4/19 9:12  <br/>
 * 作者:    wdz
 */
 @Configuration
public class ServletConfig {
    @Bean
    public CaptchaServlet testServlet(){
        return new CaptchaServlet();
    }

    @Bean
    public ServletRegistrationBean captchaServletRegistrationBean(CaptchaServlet captchaServlet){
        ServletRegistrationBean registration = new ServletRegistrationBean(captchaServlet);
        registration.setEnabled(true);
        registration.addUrlMappings("/servlet/captchaCode");
        return registration;
    }


}
