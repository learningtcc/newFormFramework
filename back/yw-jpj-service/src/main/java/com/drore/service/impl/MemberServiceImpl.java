package com.drore.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.model.MemberInfo;
import com.drore.service.MemberService;
import com.drore.util.PageUtil;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by wangl on 2017/9/1 0001.
 */
@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    private CloudQueryRunner run;

    public RestMessage getUserList(Integer page_size,Integer current_page,JsonObject params){
        RestMessage restMessage = new RestMessage();
        //动态拼接sql语句
        StringBuffer sql = new StringBuffer("select * from member_info where is_deleted='N'");
        if (params.has("nick_name")){
            if(!params.get("nick_name").getAsString().equals("")&&params.get("nick_name").getAsString()!=null) {
                sql.append(" and nick_name like '%" + params.get("nick_name").getAsString() + "%'");
            }
        }
        if (params.has("city")){
            if(!params.get("city").getAsString().equals("")&&params.get("city").getAsString()!=null) {
                sql.append(" and city like '%" + params.get("city").getAsString() + "%'");
            }
        }
        if (params.has("startTime")){
            if(params.get("startTime").getAsString()!=null&&!params.get("startTime").getAsString().equals("")) {
                sql.append(" and subscribe_time >= '"+params.get("startTime").getAsString()+" 00:00:00'");
            }
        }
        if (params.has("endTime")){
            if(params.get("endTime").getAsString()!=null&&!params.get("endTime").getAsString().equals("")) {
                sql.append(" and subscribe_time <= '"+params.get("endTime").getAsString()+" 23:59:59'");
            }
        }
        sql.append(" order by subscribe_time desc");
        Pagination<MemberInfo> pagination = run.sql(MemberInfo.class, sql.toString(), current_page, page_size);
        PageUtil pageUtil = new PageUtil(pagination);
        restMessage.setData(pageUtil);
        return restMessage;
    }

    public RestMessage getMemberAddress(String memberId){
        RestMessage restMessage = new RestMessage();
        Pagination<Map> pagination = run.queryListByExample("member_address", ImmutableMap.of("member_id", memberId));
        PageUtil pageUtil = new PageUtil(pagination);
        restMessage.setData(pageUtil);
        return restMessage;
    }

    @Override
    public RestMessage getMember(String memberId) {
        RestMessage restMessage = new RestMessage();
        MemberInfo memberInfo = run.queryOne(MemberInfo.class, "member_info", memberId);
        restMessage.setData(memberInfo);
        return restMessage;
    }
}
