package com.drore.cloud.service.impl;

import com.alibaba.fastjson.JSON;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.service.LogisticsService;
import com.drore.cloud.util.KdniaoTrackQueryAPI;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangl on 2017/9/12 0012.
 */
@Service
public class LogisticsServiceImpl implements LogisticsService{

    @Autowired
    private CloudQueryRunner run;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public RestMessage detail(String orderId) throws Exception{
        RestMessage restMessage = new RestMessage();
        //暂时写死
        String open_id = "o3rH8wri5cCaROOUQ8f5HM_XVI8k";

        ValueOperations<String, Object> data = redisTemplate.opsForValue();
        String key = "logistics_info:"+open_id;

        //redis查询该用户物流信息。没有，就去请求快递鸟接口获取物流信息,保存到redis
        // ，设置过期时间2小时。有就直接返回（快递鸟接口有次数限制，所有要做本地缓存）
        if (data.get(key) == null){
            Map<String, Object> order_info = run.queryOne("order_info", orderId);

            //获取运单编号，物流公司
            String tracking_num = String.valueOf(order_info.get("tracking_num"));
            String logistics_company = String.valueOf(order_info.get("logistics_company"));
            Map logistics_company_info = run.queryFirstByRName("logistics_company_info", ImmutableMap.of("name", logistics_company));

            //获取物流公司对应编码
            String code = String.valueOf(logistics_company_info.get("code"));

            //快递鸟查询物流信息
            KdniaoTrackQueryAPI kdniaoTrackQueryAPI = new KdniaoTrackQueryAPI();
            String result = kdniaoTrackQueryAPI.getOrderTracesByJson(code ,tracking_num);
            Map logistics_info = JSON.parseObject(result, Map.class);
            //加入物流公司
            logistics_info.put("logistics_company",logistics_company);
            //fastjson序列化
            result = JSON.toJSONString(logistics_info);
            //保存快递信息到redis
            data.set(key, result);
            //设置过期时间2小时
            redisTemplate.expire(key,2, TimeUnit.HOURS);
            //redisTemplate.expire(key,60, TimeUnit.SECONDS);
        }
        String logistics_info_str = String.valueOf(data.get(key));

        //fastjson反序列化
        Map logistics_info = JSON.parseObject(logistics_info_str, Map.class);
        restMessage.setData(logistics_info);

        return restMessage;
    }
}
