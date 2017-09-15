/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */

package com.drore.cloud.configuration;


import com.drore.cloud.interceptor.LoginBeforeInterceptor;
import com.drore.cloud.sdk.common.filter.GlobalRequestMappingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;
import java.util.List;

/***
 * 程序配置
 * @since:cloud-ims 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2016/11/15 15:10
 */
@Configuration
public class WebApplicationConfig extends WebMvcConfigurerAdapter{

    /***
     * 添加拦截器
     * @param
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录判断
        registry.addInterceptor(new LoginBeforeInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

//
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        argumentResolvers.add(new JsonArgumentResolver());
//        super.addArgumentResolvers(argumentResolvers);
//    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
    }

//    @Bean
//    public ViewResolver viewResolver(){
//        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//        templateResolver.setTemplateMode("HTML5");
//        templateResolver.setPrefix("/templates/");
//        templateResolver.setSuffix(".html");
//        SpringTemplateEngine engine = new SpringTemplateEngine();
//        engine.setTemplateResolver(templateResolver);
//        //engine.addDialect(new LayoutDialect());
//        engine.addDialect(new LayoutDialect(new GroupingStrategy()));
//        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
//        return viewResolver;
//    }
//    @Bean
//    public LayoutDialect layoutDialect() {
//        return new LayoutDialect();
//    }

    /**
     * encoding编码问题
     * @return
     */
    @Bean
    public Filter characterEncodingFilter(){
        CharacterEncodingFilter characterEncodingFilter=new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

    @Bean
    public Filter globalRequestMappingFilter(){
        GlobalRequestMappingFilter globalRequestMappingFilter=new GlobalRequestMappingFilter();
        return globalRequestMappingFilter;
    }
}
