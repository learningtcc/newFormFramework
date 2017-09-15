/*
 * Copyright (C) 2017 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */

package com.drore.task;

import com.drore.service.OfferVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/***
 * 任务机制
 * @since:hy-api 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2017/03/15 9:39
 */
@Component
@Configuration
@EnableScheduling
@ConfigurationProperties(prefix = "com.drore.cloud.jpj.task",locations = "classpath:application.properties")
@EnableAsync
public class ScheduledHyTasks {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private OfferVoucherService offerVoucherService;

    /***
     * 处理过期订单
     */
    @Async
    @Scheduled(cron = "0 0/60 * * * * ")
    public void expireOrders(){
        offerVoucherService.judgeOfferVoucherState();
    }

}
