package com.drore.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.enums.CommonEnum;
import com.drore.exception.CustomException;
import com.drore.model.StreetCultureInfo;
import com.drore.service.ClicksInfoService;
import com.drore.service.ImageInfoService;
import com.drore.service.StreetCultureService;
import com.drore.util.PageUtil;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/1 0001.
 */
@Service
public class StreetCultureServiceImpl implements StreetCultureService {

    @Autowired
    private CloudQueryRunner run;
    @Autowired
    ClicksInfoService clicksInfoService;
    @Autowired
    private ImageInfoService imageInfoService;

    public RestMessage cultureList(Integer page_size, Integer current_page, JsonObject params) {
        RestMessage restMessage = new RestMessage();
        //动态拼接sql语句
        StringBuffer sql = new StringBuffer("select * from street_culture_info where is_deleted='N'");
        if (params.has("title")) {
            if (!params.get("title").getAsString().equals("") && params.get("title").getAsString() != null) {
                sql.append(" and title like '%" + params.get("title").getAsString() + "%'");
            }
        }
        if (params.has("is_release")) {
            if (!params.get("is_release").getAsString().equals("") && params.get("is_release").getAsString() != null) {
                sql.append(" and is_release='" + params.get("is_release").getAsString() + "'");
            }
        }
        if (params.has("startTime")) {
            if (params.get("startTime").getAsString() != null && !params.get("startTime").getAsString().equals("")) {
                sql.append(" and announcer_time >= '" + params.get("startTime").getAsString() + " 00:00:00'");
            }
        }
        if (params.has("endTime")) {
            if (params.get("endTime").getAsString() != null && !params.get("endTime").getAsString().equals("")) {
                sql.append(" and announcer_time <= '" + params.get("endTime").getAsString() + " 23:59:59'");
            }
        }
        sql.append(" order by create_time desc");

        Pagination<StreetCultureInfo> pagination = run.sql(StreetCultureInfo.class, sql.toString(), current_page, page_size);

        PageUtil pageUtil = new PageUtil(pagination);

        restMessage.setData(pageUtil);
        return restMessage;
    }

    public RestMessage addCulture(StreetCultureInfo streetCultureInfo) {
        RestMessage restMessage;
        String id = streetCultureInfo.getId();
        String isRelease = streetCultureInfo.getIsRelease();
        if ("Y".equals(isRelease)){
            streetCultureInfo.setAnnouncerTime(new Date());
        }
        if (id != null && !"".equals(id)) {
            restMessage = run.update("street_culture_info", id, streetCultureInfo);
            if (restMessage.isSuccess()) {
                restMessage.setMessage("更新成功");
            }
        } else {
            streetCultureInfo.setIsHot("N");
            streetCultureInfo.setIsUsing("Y");
            streetCultureInfo.setClicks(0);
            restMessage = run.insert("street_culture_info", streetCultureInfo);
            if (restMessage.isSuccess()) {
                restMessage.setMessage("添加成功");
            }
        }
        return restMessage;
    }

    public RestMessage setTop(String id) {
        RestMessage restMessage;
        Pagination<Map> pagination = run.queryListByExample("street_culture_info", 1, Integer.MAX_VALUE);
        List<Map> data = pagination.getData();
        for (int i = 0; i < data.size(); i++) {
            String id1 = String.valueOf(data.get(i).get("id"));
            run.update("street_culture_info", id1, ImmutableMap.of("is_hot", "N"));
        }
        restMessage = run.update("street_culture_info", id, ImmutableMap.of("is_hot", "Y", "top_time", new Date()));
        if (restMessage.isSuccess()) {
            restMessage.setMessage("置顶成功");
        }
        return restMessage;
    }


    public RestMessage delCulture(String id) {
        RestMessage street_culture_info = run.delete("street_culture_info", id);
        if (street_culture_info.isSuccess()) {
            street_culture_info.setMessage("删除成功");
        }
        return street_culture_info;
    }


    public RestMessage setIsUsing(String id, String status) {
        RestMessage restMessage = run.update("street_culture_info", id, ImmutableMap.of("is_using", status));
        return restMessage;
    }

    public RestMessage detail(String id){
        RestMessage restMessage = new RestMessage();
        StreetCultureInfo streetCultureInfo = run.queryOne(StreetCultureInfo.class, StreetCultureInfo.table, id);
        List<String> pics = imageInfoService.findPics(StreetCultureInfo.table, streetCultureInfo.getId());
        streetCultureInfo.setPics(pics);
        restMessage.setData(streetCultureInfo);
        return restMessage;
    }


    public StreetCultureInfo queryDetailApiById(String id, String name, String memberId, String memberName) {
        StreetCultureInfo streetCulture = run.queryOne(StreetCultureInfo.class, StreetCultureInfo.table, id);
        if (streetCulture == null) {
            throw new CustomException("未发现查找对象!");
        }


        streetCulture.setClicks(streetCulture.getClicks() + 1);
        RestMessage restMessage = run.update(StreetCultureInfo.table, id, streetCulture);
        if (!restMessage.isSuccess()) {
            throw new CustomException(restMessage.getMessage());
        }
        restMessage = clicksInfoService.save(id, StreetCultureInfo.table, name, CommonEnum.ClickSourceEnum.WEIXIN.getCode(), memberId, memberName);
        if (!restMessage.isSuccess()) {
            throw new CustomException("保存点击率信息报错" + restMessage.getMessage());
        }

        return streetCulture;
    }

}
