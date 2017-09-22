package com.drore.cloud.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.service.StreetCultureService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangl on 2017/9/4 0004.
 */
@Service
public class StreetCultureServiceImpl implements StreetCultureService{

    @Autowired
    private CloudQueryRunner run;

    public RestMessage list(Integer page_size, Integer current_page){
        RestMessage restMessage = new RestMessage();
        Pagination<Map> advertising_info = run.queryListByExample("advertising_info", ImmutableMap.of("table_name", "街区文化","is_deleted","N"));
        Pagination<Map> street_culture_info = run.queryListByExample("street_culture_info", ImmutableMap.of("is_deleted","N","is_using","Y","is_release","Y"),current_page, page_size);
        Map hot_street_culture_info = run.queryFirstByRName("street_culture_info", ImmutableMap.of("is_deleted","N","is_using","Y","is_release","Y","is_hot", "Y"));


        Map<String, Object> map = new HashMap<>();
        map.put("advertising_info",advertising_info);
        map.put("street_culture_info",street_culture_info);
        map.put("hot_street_culture_info",hot_street_culture_info);

        restMessage.setData(map);

        return restMessage;
    }

    @Override
    public RestMessage addClicks(String id) {
        Map<String, Object> map = run.queryOne("street_culture_info", id, "clicks");
        Double clicks = Double.valueOf(map.get("clicks").toString());
        RestMessage restMessage = run.update("street_culture_info", id, ImmutableMap.of("clicks", clicks + 1));
        return restMessage;
    }
}
