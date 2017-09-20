package com.drore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.common.util.DateUtil;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.enums.CommonEnum;
import com.drore.exception.CustomException;
import com.drore.model.MessageInfo;
import com.drore.model.OrderInfo;
import com.drore.service.MessageService;
import com.drore.service.OrderService;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangl on 2017/9/7 0007.
 */
@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private CloudQueryRunner run;

    @Autowired
    private MessageService messageService;

    @Override
    public Pagination queryByPage(Pagination pagination, OrderInfo orderInfo) {
        StringBuffer sql = new StringBuffer("select * from "+ OrderInfo.table);
        sql.append(" where is_deleted='"+ CommonEnum.YesOrNo.NO.getCode()+"'");
        //店铺名称
        if(StringUtils.isNotEmpty(orderInfo.getOrderNo())){
            sql.append(" and order_no like '%"+orderInfo.getOrderNo()+"%'");
        }
        //是否发布
        if(StringUtils.isNotEmpty(orderInfo.getOrderStatus())){
            sql.append(" and order_status ='"+orderInfo.getOrderStatus()+"'");
        }
        return  run.sql(OrderInfo.class,sql.toString(),pagination.getCurrent_page(),pagination.getPage_size());
    }

    @Override
    public OrderInfo queryDetailById(String id) {
        OrderInfo orderInfo = run.queryOne(OrderInfo.class, OrderInfo.table, id);
        if(orderInfo==null){
            throw  new CustomException("未发现查找对象!");
        }
        return  orderInfo;
    }

    @Override
    public RestMessage send(OrderInfo orderInfo){
        RestMessage restMessage = run.update("order_info", orderInfo.getId(),
                ImmutableMap.of("logistics_company", orderInfo.getLogisticsCompany(), "tracking_num",
                        orderInfo.getTrackingNum(),"order_status",CommonEnum.OrderStatusEnum.NO_RECEIVE.getValue()));
        if (restMessage.isSuccess()){
            OrderInfo succOrderInfo = run.queryOne(OrderInfo.class, OrderInfo.table, restMessage.getId());
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setTitle("系统消息");
            messageInfo.setIntroduction("您有新的订单消息，请点击查看");
            messageInfo.setContent("亲爱的用户，您有一个订单号为"+succOrderInfo.getOrderNo()+"的订单商家已发货，请注意查收");
            messageInfo.setTime(DateUtil.format(new Date(),"yyyy-MM-dd"));
            messageInfo.setIsRead("N");
            messageInfo.setRecipient(succOrderInfo.getMemberId());
            messageService.saveMessage(messageInfo);
            restMessage.setMessage("发货成功");
        }else {
            restMessage.setMessage("发货失败");
        }
        return restMessage;
    }

    @Override
    public RestMessage receivables(OrderInfo orderInfo){
        RestMessage restMessage = run.update("order_info", orderInfo.getId(),
                ImmutableMap.of("tracking_num", orderInfo.getTrackingNum(), "pay_time", orderInfo.getPayTime(),
                        "order_status",CommonEnum.OrderStatusEnum.HasPay.getValue()));
        if (restMessage.isSuccess()){
            if(restMessage.isSuccess()){
                OrderInfo succOrderInfo = run.queryOne(OrderInfo.class, OrderInfo.table, restMessage.getId());
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setTitle("系统消息");
                messageInfo.setIntroduction("您有新的订单消息，请点击查看");
                messageInfo.setContent("亲爱的用户，您有一个订单号为"+succOrderInfo.getOrderNo()+"的订单商家已确认收款，等待发货");
                messageInfo.setTime(DateUtil.format(new Date(),"yyyy-MM-dd"));
                messageInfo.setIsRead("N");
                messageInfo.setRecipient(succOrderInfo.getMemberId());
                messageService.saveMessage(messageInfo);
            }
            restMessage.setMessage("收款成功");
        }else {
            restMessage.setMessage("收款失败");
        }
        return restMessage;
    }

    @Override
    public List<JSONObject> getOrderStatusList(){
        CommonEnum.OrderStatusEnum [] orderStatuses = CommonEnum.OrderStatusEnum.values();
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (CommonEnum.OrderStatusEnum ord : orderStatuses) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", ord.getValue());
            jsonObject.put("name", ord.getName());
            list.add(jsonObject);
        }
        return  list;
    }
}
