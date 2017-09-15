package com.drore.service;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.model.AdvertisingInfo;

import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:  公用广告信息服务层接口   <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/9/1 11:34  <br/>
 * 作者:    wdz
 */
public interface AdvertisingInfoService {
    /**
     * 新增广告信息
     * @param advertisingInfo
     * @return
     */
    public RestMessage save(AdvertisingInfo advertisingInfo);

    /**
     * 编辑
     * @param advertisingInfo
     * @return
     */
    public RestMessage update(AdvertisingInfo advertisingInfo);

    /**
     * 根据主键查找 原生的
     * @param id
     * @return
     */
    public AdvertisingInfo queryById(String id);

    /**
     * 根据表名查找广告信息
     * @param tableName
     * @return
     */
    public List<AdvertisingInfo> findList(String tableName);


    public RestMessage removeById(String id);


    Pagination queryByPage(Pagination pagination, AdvertisingInfo advertisingInfo);

    List<JSONObject> queryTableName();
}
