/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */

package com.drore.config;


import com.drore.cloud.sdk.common.filter.GlobalRequestMappingFilter;
import com.drore.resolver.JsonArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
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
public class WebApplicationConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/website/build/static/");
        registry.addResourceHandler("/*.js").addResourceLocations("classpath:/website/build/");
        registry.addResourceHandler("/*.ttf").addResourceLocations("classpath:/website/build/");
        registry.addResourceHandler("/*.woff2").addResourceLocations("classpath:/website/build/");
        registry.addResourceHandler("/*.woff").addResourceLocations("classpath:/website/build/");
        super.addResourceHandlers(registry);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController( "/" ).setViewName( "forward:/index.html" );
        registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
        super.addViewControllers( registry );
    }



    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new JsonArgumentResolver());
        super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
    }

    /**
     * encoding编码问题
     *
     * @return
     */
    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

    @Bean
    public Filter globalRequestMappingFilter() {
        GlobalRequestMappingFilter globalRequestMappingFilter = new GlobalRequestMappingFilter();
        return globalRequestMappingFilter;
    }
}
