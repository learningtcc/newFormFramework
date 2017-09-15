package com.drore.cloud.service.impl;


import com.drore.cloud.model.LeaseInfo;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.service.LeaseService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangl on 2017/9/4 0004.
 */
@Service
public class LeaseServiceImpl implements LeaseService {

    @Autowired
    private CloudQueryRunner run;

    public RestMessage list(Integer page_size, Integer current_page, String condition, String sort){
        RestMessage restMessage = new RestMessage();
        Pagination<Map> advertising_info = run.queryListByExample("advertising_info", ImmutableMap.of("table_name", "lease_info"));

        StringBuffer sql = new StringBuffer("select * from lease_info where is_deleted='N' and is_using='Y' order by "+condition+' '+sort+"");
        Pagination<Map> lease_info = run.sql(sql.toString(), current_page, page_size);

        Map<String, Object> map = new HashMap<>();
        map.put("advertising_info",advertising_info);
        map.put("lease_info",lease_info);

        restMessage.setData(map);

        return restMessage;
    }

    public RestMessage detail(String id){
        RestMessage restMessage = new RestMessage();
        LeaseInfo lease_info = run.queryOne(LeaseInfo.class, "lease_info", id);

        Pagination<Map> advertising_info = run.queryListByExample("advertising_info", ImmutableMap.of("table_name", "lease_info","is_deleted","N"));

        Pagination<Map> commodity_info = run.queryListByExample("commodity_info", ImmutableMap.of("store_id", lease_info.getMerchantPk(),
                "is_deleted","N","is_shelves","Y"),1,2);

        Map<String, Object> map = new HashMap<>();
        map.put("lease_info",lease_info);
        map.put("advertising_info",advertising_info);
        map.put("commodity_info",commodity_info);

        restMessage.setData(map);

        return restMessage;
    }
}
