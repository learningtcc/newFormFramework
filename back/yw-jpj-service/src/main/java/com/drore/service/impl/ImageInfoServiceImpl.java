package com.drore.service.impl;

import com.drore.domain.ImageInfo;
import com.drore.exception.CustomException;
import com.drore.service.ImageInfoService;
import com.drore.util.RestMessageModel;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:  公共图集服务层实现   <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/9/1 10:57  <br/>
 * 作者:    wdz
 */
@Service
public class ImageInfoServiceImpl extends  BaseServiceImpl implements ImageInfoService {
    private Logger log = LoggerFactory.getLogger(ImageInfoServiceImpl.class);
    @Override
    public RestMessageModel saveBatch(String tableName, String pid, List<String> pics) {
        if(CollectionUtils.isEmpty(pics)){
             return  new RestMessageModel();
        }
        Map<String, Object> params = new HashMap<String,Object>();
        params.put("table_name",tableName);
        params.put("table_pk",pid);
        RestMessageModel model =  super.deleteByCriterion(ImageInfo.table,params);
        if(! model.isSuccess()){
            log.warn(model.getErrorMessage());
            throw  new CustomException("图集删除失败!"+model.getErrorMessage());
        }
        List<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
        ImageInfo imageInfo = null;
        for(String pic:pics){
            imageInfo = new ImageInfo();
            imageInfo.setTableName(tableName);
            imageInfo.setTablePk(pid);
            imageInfo.setPicUrl(pic);
            imageInfos.add(imageInfo);
        }
        return   saveObjectBatch(ImageInfo.table,imageInfos);
    }

    @Override
    public List<String> findPics(String tableName, String pid) {
        Map<String, Object> params = new HashMap<String,Object>();
        params.put("table_name",tableName);
        params.put("table_pk",pid);
        List<ImageInfo> list = super.queryList(ImageInfo.table,params,ImageInfo.class);
        if(CollectionUtils.isEmpty(list)){
            return  null;
        }
        List<String> imageInfos = new ArrayList<>();
        for(ImageInfo imageInfo :list){
            imageInfos.add(imageInfo.getPicUrl());
        }
        return  imageInfos;
    }
}
