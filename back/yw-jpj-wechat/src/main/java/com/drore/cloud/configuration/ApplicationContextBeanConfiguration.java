/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */

package com.drore.cloud.configuration;

import com.drore.cloud.model.config.SystemConfig;
import com.drore.cloud.sdk.basic.CloudBasicConnection;
import com.drore.cloud.sdk.basic.CloudBasicDataSource;
import com.drore.cloud.sdk.basic.CloudPoolingConnectionManager;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.cloud.util.PackageConfigUtils;
import com.drore.cloud.wx.client.CloudWeixinService;
import com.drore.cloud.wx.model.WeixinToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/***
 * spring所有bean注入口
 * @since:cloud-ims 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2016/11/15 18:51
 */
@Configuration
public class ApplicationContextBeanConfiguration {

    /***
     * 注入云数据中心查询类
     * @return
     */
    @Bean(value = "run")
    public CloudQueryRunner createCloudQueryRunner(){
        //创建连接
        LogbackLogger.info("create cloudBasicConnection...");
        CloudBasicConnection cloudBasicConnection=new CloudBasicConnection();
        SystemConfig config= PackageConfigUtils.getSystemConfig();
        cloudBasicConnection.setUrl(config.getCloudUrl());
        cloudBasicConnection.setPort(config.getCloudPort());
        cloudBasicConnection.setUserName(config.getAppid());
        cloudBasicConnection.setPassword(config.getAppsecret());
        //创建连接池管理类
        LogbackLogger.info("create cloudPoolingConnectionManager...");
        CloudPoolingConnectionManager cloudPoolingConnectionManager=new CloudPoolingConnectionManager();
        cloudPoolingConnectionManager.setConnection(cloudBasicConnection);
        //创建数据源
        LogbackLogger.info("create cloudBasicDataSource...");
        CloudBasicDataSource cloudBasicDataSource=new CloudBasicDataSource();
        cloudBasicDataSource.setCloudPoolingConnectionManager(cloudPoolingConnectionManager);
        //创建查询runner
        LogbackLogger.info("create cloudQueryRunner...");
        CloudQueryRunner runner=new CloudQueryRunner();
        runner.setDataSource(cloudBasicDataSource);
        return runner;
    }


    @Bean(value = "wxService")
    public CloudWeixinService createwxService(){
        SystemConfig systemConfig = PackageConfigUtils.getSystemConfig();
        WeixinToken weixinToken = new WeixinToken(systemConfig.getWxConfig().getAppid(),systemConfig.getWxConfig().getAppsecret());
        CloudWeixinService wxService = new CloudWeixinService(weixinToken);
        return wxService;
    }

}
