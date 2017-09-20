package com.drore.cloud.service.impl;


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
        Map<String, Object> lease_info = run.queryOne("lease_info", id);

        Pagination<Map> image_info = run.queryListByExample("image_info", ImmutableMap.of("table_name", "lease_info","table_pk", id,"is_deleted","N"));

        Pagination<Map> more_lease_info = run.queryListByExample("lease_info", ImmutableMap.of("is_deleted","N","is_release","Y"),1,2);

        Map<String, Object> map = new HashMap<>();
        map.put("lease_info",lease_info);
        map.put("image_info",image_info);
        map.put("more_lease_info",more_lease_info);

        restMessage.setData(map);

        return restMessage;
    }
}
