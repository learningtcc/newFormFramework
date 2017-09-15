package com.drore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.enums.CommonEnum;
import com.drore.exception.CustomException;
import com.drore.model.AdvertisingInfo;
import com.drore.service.AdvertisingInfoService;
import com.drore.util.LoginSysUserUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:  公用广告信息服务层实现   <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/9/1 11:36  <br/>
 * 作者:    wdz
 */
@Service
public class AdvertisingInfoServiceImpl   implements AdvertisingInfoService {
    @Autowired
    CloudQueryRunner run;


    @Override
    public RestMessage removeById(String id) {
        return run.delete(AdvertisingInfo.table,id);
    }

    @Override
    public RestMessage update(AdvertisingInfo advertisingInfo) {
        AdvertisingInfo old = queryById(advertisingInfo.getId());
        if(old==null){throw  new CustomException("对象为空!");}
        old.setName(advertisingInfo.getName());
        old.setTableName(advertisingInfo.getTableName());
        old.setImageUrl(advertisingInfo.getImageUrl());
        old.setLinkUrl(advertisingInfo.getImageUrl());
        old.setModifier(LoginSysUserUtil.getUserId());
        return  run.update(AdvertisingInfo.table,advertisingInfo.getId(),old);
    }

    @Override
    public AdvertisingInfo queryById(String id) {
        return run.queryOne(AdvertisingInfo.class,AdvertisingInfo.table,id);
    }

    @Override
    public RestMessage save(AdvertisingInfo advertisingInfo) {
        advertisingInfo.setCreator(LoginSysUserUtil.getUserId());
        return  run.insert(AdvertisingInfo.table,advertisingInfo);
    }

    @Override
    public List<AdvertisingInfo> findList(String tableName) {
        Map<String,Object> term = new HashMap<String,Object>();
        term.put("table_name",tableName);
        Pagination pagination =   run.queryListByExample(AdvertisingInfo.class,AdvertisingInfo.table,term,null,null,null,1,10000);
        return  pagination.getData();
    }

    @Override
    public Pagination queryByPage(Pagination pagination, AdvertisingInfo advertisingInfo) {
        StringBuffer sql = new StringBuffer("select * from " + AdvertisingInfo.table  );
        sql.append(" where is_deleted='" + CommonEnum.YesOrNo.NO.getCode() + "'");
        sql.append(" order by create_time desc ");
        pagination = run.sql(AdvertisingInfo.class,sql.toString(),pagination.getCurrent_page(),pagination.getPage_size());
        if(CollectionUtils.isNotEmpty(pagination.getData())){
            List<AdvertisingInfo>  advertisingInfos =  pagination.getData();
            CommonEnum.AdvertisingTypeEnum [] orderStatusEnums = CommonEnum.AdvertisingTypeEnum.values();
            for(AdvertisingInfo temp:advertisingInfos){
                for (CommonEnum.AdvertisingTypeEnum ord : orderStatusEnums) {
                    if(ord.getTable().equalsIgnoreCase(temp.getTableName())){
                        temp.setTableName(ord.getName());
                    }
                }

            }
            pagination.setData(advertisingInfos);
        }

        return pagination;
    }

    @Override
    public List<JSONObject> queryTableName() {
        CommonEnum.AdvertisingTypeEnum [] orderStatusEnums = CommonEnum.AdvertisingTypeEnum.values();
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (CommonEnum.AdvertisingTypeEnum ord : orderStatusEnums) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", ord.getTable());
            jsonObject.put("name", ord.getName());
            list.add(jsonObject);
        }
        return  list;
    }
}
