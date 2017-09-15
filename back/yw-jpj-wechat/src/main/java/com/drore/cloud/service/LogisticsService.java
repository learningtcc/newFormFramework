package com.drore.cloud.service;

import com.drore.cloud.sdk.common.resp.RestMessage;

/**
 * Created by wangl on 2017/9/12 0012.
 */
public interface LogisticsService {

    RestMessage detail(String orderId) throws Exception;
}
