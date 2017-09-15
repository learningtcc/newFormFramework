package com.drore.cloud.service;

import com.drore.cloud.sdk.common.resp.RestMessage;

/**
 * Created by wangl on 2017/9/7 0007.
 */
public interface OrderService {

    RestMessage getOrderList(String condition, Integer page_size, Integer current_page);

    RestMessage getOrderDetail(String orderId);

    RestMessage hasCancel(String orderId);

    RestMessage receive(String orderId);
}
