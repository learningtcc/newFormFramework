package com.drore.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:  hibernat校验   <br/>
 * 项目名称: taihu-rent-manage <br/>
 * 创建日期:  2017/5/27 18:46  <br/>
 * 作者:    wdz
 */
@Configuration
@EnableAutoConfiguration(exclude={WebMvcAutoConfiguration.class})
public class FactoryConfig {
    final static Logger logger= LoggerFactory.getLogger(FactoryConfig.class);

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(){
        return new MethodValidationPostProcessor();
    }

}