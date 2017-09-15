package com.drore.cloud.service.impl;

import com.drore.cloud.model.ImageInfo;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.cloud.service.ImageInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangh on 2017/9/12 0001.
 */

@Service
public class ImageInfoServiceImpl implements ImageInfoService {

    @Autowired
    private CloudQueryRunner run;

    private Logger log = LoggerFactory.getLogger(ImageInfoServiceImpl.class);
    @Override
    public RestMessage saveBatch(String tableName, String pid, List<String> pics) {
        if(CollectionUtils.isEmpty(pics)){
             return new RestMessage();
        }
        Map<String, Object> params = new HashMap<String,Object>();
        params.put("table_name",tableName);
        params.put("table_pk",pid);
        RestMessage model =  run.deleteByCriterion(ImageInfo.table,params);
        if(! model.isSuccess()){
            log.warn(model.getErrorMessage());
            return new RestMessage();
        }
        List<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
        ImageInfo imageInfo = null;
        for(String pic:pics){
            imageInfo = new ImageInfo();
            imageInfo.setTableName(tableName);
            imageInfo.setTablePk(pid);
            imageInfo.setPicUrl(pic);
            imageInfos.add(imageInfo);
            return run.insert(ImageInfo.table,imageInfo);
        }
        return new RestMessage();
    }

    @Override
    public List<String> findPics(String tableName, String pid) {
        RequestExample requestExample=new RequestExample();
        //创建Criteria
        RequestExample.Criteria rc = requestExample.create();
        RequestExample.Param pa = requestExample.createParam();
        pa.addTerm("table_name",tableName);
        pa.addTerm("table_pk",pid);
        pa.addTerm("is_deleted","N");
        rc.getMust().add(pa);
        Pagination<ImageInfo> imageInfoPagination = run.queryListByExample(ImageInfo.class, ImageInfo.table, requestExample);

        List<String> imageInfos = new ArrayList<>();
        for(ImageInfo imageInfo :imageInfoPagination.getData()){
            imageInfos.add(imageInfo.getPicUrl());
        }
        return  imageInfos;
    }
}
