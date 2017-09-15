package com.drore.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * 浙江卓锐科技股份有限公司 版权所有 Copyright 2016<br/>
 * 说明: 启动的时候加载zabbix 授权码<br/>
 * 项目名称: cloud-dataCenter-manage <br/>
 * 创建日期: 2016年11月30日 下午3:51:45 <br/>
 * 作者: lj
 */
@Service
public class StartupInit implements InitializingBean {
	private static Logger log = LoggerFactory.getLogger(StartupInit.class);

	@Autowired
	RedisService redisService;


	 
	static boolean firstInit = false;

	public void afterPropertiesSet() throws Exception {
		
		
		if (firstInit) {
			log.info("has been start");
		} else {

			System.out.println("---------启动执行方法---刷新缓存----------------");
			redisService.set("wdz","测试数据");
			System.out.printf(redisService.get("wdz"));
			redisService.del("wdz");

//
//			Set<String> set = redisService.keys(DefaultProperties.redisCode + "*");
//			Iterator<String> it = set.iterator();
//			while (it.hasNext()) {
//				String keyStr = it.next();
//				System.out.println("启动删除缓存名称==" + keyStr);
//				redisService.delInit(keyStr);
//			}
			
		}
	}

}
