package com.drore.cloud.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.service.OrderService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wangl on 2017/9/7 0007.
 */
@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private CloudQueryRunner run;

    @Override
    public RestMessage getOrderList(String condition,Integer page_size,Integer current_page){
        RestMessage restMessage = new RestMessage();
        Pagination<Map> pagination;
        String user_id = "6b5e8fcc07c54e33918f434aff5d3376";
        if ("NoPay".equals(condition)){
            pagination = run.queryListByExample("order_info", ImmutableMap.of("member_id", user_id,"order_status",condition),current_page,page_size);
        }else if ("HasPay".equals(condition)){
            pagination = run.queryListByExample("order_info", ImmutableMap.of("member_id", user_id,"order_status",condition),current_page,page_size);
        }else if ("NoReceive".equals(condition)){
            pagination = run.queryListByExample("order_info", ImmutableMap.of("member_id", user_id,"order_status",condition),current_page,page_size);
        }else {
            pagination = run.queryListByExample("order_info", ImmutableMap.of("member_id", user_id),current_page,page_size);
        }
        List<Map> data = pagination.getData();
        for (int i = 0; i < data.size(); i++) {
            String id = String.valueOf(data.get(i).get("id"));
            Pagination<Map> order_detail_list = run.queryListByExample("order_detail", ImmutableMap.of("order_id", id));
            for (int j = 0; j < order_detail_list.getData().size(); j++) {
                pagination.getData().get(i).put("order_detail",order_detail_list.getData());
            }
        }
        restMessage.setData(pagination);
        return restMessage;
    }

    @Override
    public RestMessage getOrderDetail(String orderId) {
        RestMessage restMessage = new RestMessage();
        Map<String, Object> order_info = run.queryOne("order_info", orderId);
        Pagination<Map> order_detail = run.queryListByExample("order_detail", ImmutableMap.of("order_id", orderId));

        Map<String, Object> map = new HashMap<>();
        map.put("order_info",order_info);
        map.put("order_detail",order_detail);

        restMessage.setData(map);
        return restMessage;
    }

    @Override
    public RestMessage hasCancel(String orderId){
        RestMessage restMessage = new RestMessage();
        Map<String, Object> map = run.queryOne("order_info", orderId);
        String order_status = String.valueOf(map.get("order_status"));
        if ("NoPay".equals(order_status)){
            restMessage = run.update("order_info", orderId, ImmutableMap.of("order_status", "HasCancel"));
            if (restMessage.isSuccess()){
                restMessage.setMessage("取消成功");
            }else {
                restMessage.setMessage("取消失败");
            }
        }else {
            restMessage.setMessage("异常，不是未支付订单");
            restMessage.setSuccess(false);
        }
        return restMessage;
    }

    @Override
    public RestMessage receive(final String orderId){
        RestMessage restMessage = new RestMessage();
        Map<String, Object> map = run.queryOne("order_info", orderId);
        String order_status = String.valueOf(map.get("order_status"));
        if ("NoReceive".equals(order_status)){
            restMessage = run.update("order_info", orderId, ImmutableMap.of("order_status", "Received"));
            if (restMessage.isSuccess()){
                restMessage.setMessage("收货成功");
                //7天定时更新订单状态为已完成
                long delay = 1000*60*60*24*7; //7天
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        run.update("order_info",orderId,ImmutableMap.of("order_status","Finished"));
                    }
                },delay);
            }else {
                restMessage.setMessage("收货失败");
            }
        }else {
            restMessage.setMessage("异常，不是待收货订单");
            restMessage.setSuccess(false);
        }
        return restMessage;
    }
}
