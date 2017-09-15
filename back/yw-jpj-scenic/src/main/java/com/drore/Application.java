package com.drore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;

import java.util.Date;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: SpringBoot <br/>
 * 创建日期:  2017/3/2 14:02  <br/>
 * 作者:    wdz
 */
@SpringBootApplication
public class Application   implements EmbeddedServletContainerCustomizer {



    public static void main(String[] args) {
        System.out.println("Application.main==========================执行了"+new Date());

        SpringApplication.run(Application.class, args);
    }


    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(8084);
    }




}
