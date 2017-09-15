package com.drore.cloud.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.service.OfferVoucherService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangl on 2017/9/4 0004.
 */
@Service
public class OfferVoucherServiceImpl implements OfferVoucherService{

    @Autowired
    private CloudQueryRunner run;

    @Override
    public RestMessage list(Integer page_size, Integer current_page) {
        RestMessage restMessage = new RestMessage();
        Pagination<Map> offer_voucher = run.queryListByExample("offer_voucher",
                ImmutableMap.of("is_deleted", "N","is_enable","Y"),current_page,page_size);
        restMessage.setData(offer_voucher);
        return restMessage;
    }

    @Override
    public RestMessage detail(String storeId) {

        String user_id = "6b5e8fcc07c54e33918f434aff5d3376";

        RestMessage restMessage = new RestMessage();
        Map store_info = run.queryFirstByRName("store_info", ImmutableMap.of("id", storeId));
        Pagination<Map> offer_voucher = run.queryListByExample("offer_voucher", ImmutableMap.of("store_id", storeId,
                "is_deleted","N","is_enable","Y"),1,Integer.MAX_VALUE);
        Pagination<Map> commodity_info = run.queryListByExample("commodity_info", ImmutableMap.of("store_id", storeId,
                "is_deleted","N","is_shelves","Y"),1,2);
        Pagination<Map> image_info = run.queryListByExample("image_info", ImmutableMap.of("table_pk", storeId, "table_name", "store_info"));
        Map user_collection = run.queryFirstByRName("user_collection", ImmutableMap.of("user_id", user_id, "collection_type", "Store", "commodity_id", storeId));
        Map<String, Object> map = new HashMap<>();
        if (user_collection != null && !user_collection.isEmpty()){
            map.put("is_collection","Y");
        }else {
            map.put("is_collection","N");
        }
        map.put("store_info",store_info);
        map.put("offer_voucher",offer_voucher);
        map.put("commodity_info",commodity_info);
        map.put("image_info",image_info);

        restMessage.setData(map);
        return restMessage;
    }
}
