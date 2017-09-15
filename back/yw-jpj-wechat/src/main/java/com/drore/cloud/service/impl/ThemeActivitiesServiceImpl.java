package com.drore.cloud.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.service.ThemeActivitiesService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangl on 2017/9/4 0004.
 */
@Service
public class ThemeActivitiesServiceImpl implements ThemeActivitiesService{

    @Autowired
    private CloudQueryRunner run;

    public RestMessage list(Integer page_size, Integer current_page, String type, String keyword) {
        RestMessage restMessage = new RestMessage();

        StringBuffer sql = new StringBuffer("select * from theme_activities where is_deleted='N' and is_using='Y' and is_release='Y'");
        if (type != null && !"".equals(type)){
            sql.append(" and type="+type);
        }
        if (keyword != null && !"".equals(keyword)){
            sql.append(" and title like '%"+keyword+"%'");
        }
        Pagination<Map> theme_activities = run.sql(sql.toString(), current_page, page_size);

        Map newest_theme_activities = run.queryFirstByRName("theme_activities", ImmutableMap.of("is_using","Y","is_release","Y","is_newest", "Y"));

        Map<String, Object> map = new HashMap<>();
        map.put("newest_theme_activities",newest_theme_activities);
        map.put("theme_activities",theme_activities);

        restMessage.setData(map);
        return restMessage;
    }

    public RestMessage detail(String id){
        RestMessage restMessage = new RestMessage();

        Map<String, Object> theme_activities = run.queryOne("theme_activities", id);
        Pagination<Map> image_info = run.queryListByExample("image_info", ImmutableMap.of("table_pk", id, "table_name", "theme_activities"));

        Map<String, Object> map = new HashMap<>();
        map.put("theme_activities",theme_activities);
        map.put("image_info",image_info);

        restMessage.setData(map);
        return restMessage;
    }
}
