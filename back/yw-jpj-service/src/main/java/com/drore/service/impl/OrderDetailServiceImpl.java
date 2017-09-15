package com.drore.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.model.OrderDetail;
import com.drore.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: yw-jpj-service <br/>
 * 创建日期:  2017/9/4 11:12  <br/>
 * 作者:    wdz
 */
@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private CloudQueryRunner run;
    @Override
    public boolean exitCommodityId(String commodityId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("commodity_id",commodityId);
        OrderDetail orderDetail =  run.queryFirstByRName(OrderDetail.class,OrderDetail.table,map);
        return orderDetail==null?false:true;
    }

    @Override
    public List<OrderDetail> queryByOrderId(String orderId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("order_id",orderId);
        Pagination pagination  = run.queryListByExample(OrderDetail.class,OrderDetail.table,map,null,null);
        return  pagination.getData();
    }
}
