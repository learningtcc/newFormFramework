package com.drore.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.enums.CommonEnum;
import com.drore.exception.CustomException;
import com.drore.model.TeaCultureInfo;
import com.drore.service.TeaCultureService;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wangl on 2017/9/1 0001.
 */
@Service
public class TeaCultureServiceImpl implements TeaCultureService{

    @Autowired
    private CloudQueryRunner run;

    public RestMessage addTeaCulture(TeaCultureInfo teaCultureInfo){
        RestMessage restMessage;
        String id = teaCultureInfo.getId();
        if (id != null && !"".equals(id)){
            restMessage = run.update("tea_culture", id, teaCultureInfo);
            if (restMessage.isSuccess()){
                restMessage.setMessage("更新成功");
            }
        }else {
            teaCultureInfo.setIsUsing("Y");
            restMessage = run.insert("tea_culture", teaCultureInfo);
            if (restMessage.isSuccess()){
                restMessage.setMessage("添加成功");
            }
        }

        return restMessage;
    }


    public RestMessage setIsUsing(String id,String status){
        RestMessage restMessage = run.update("tea_culture", id, ImmutableMap.of("is_using", status));
        return restMessage;
    }


    @Override
    public Pagination queryByPage(Pagination pagination, TeaCultureInfo teaCultureInfo) {
        StringBuffer sql = new StringBuffer("select * from "+TeaCultureInfo.table);
        sql.append(" where is_deleted='"+ CommonEnum.YesOrNo.NO.getCode()+"'");
        //店铺名称
        if(StringUtils.isNotEmpty(teaCultureInfo.getTitle())){
            sql.append(" and title like '%"+teaCultureInfo.getTitle()+"%'");
        }
        sql.append(" order by serial asc");
        System.out.println(sql.toString());
        return  run.sql(TeaCultureInfo.class,sql.toString(),pagination.getCurrent_page(),pagination.getPage_size());
    }

    @Override
    public TeaCultureInfo queryDetailById(String id) {
        TeaCultureInfo teaCultureInfo = run.queryOne(TeaCultureInfo.class, TeaCultureInfo.table, id);
        if(teaCultureInfo==null){
            throw  new CustomException("未发现查找对象!");
        }
        return  teaCultureInfo;
    }
}
