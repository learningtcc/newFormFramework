package com.drore.service;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.model.OrderInfo;

import java.util.List;

/**
 * Created by wangl on 2017/9/7 0007.
 */
public interface OrderService {

    Pagination queryByPage(Pagination pagination, OrderInfo orderInfo);

    OrderInfo queryDetailById(String id);

    RestMessage send(OrderInfo orderInfo);

    RestMessage receivables(OrderInfo orderInfo);

    /**
     * 获取订单状态枚举
     * @return
     */
    List<JSONObject> getOrderStatusList();
}
