package com.drore.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.domain.DataDictionary;
import com.drore.enums.CommonEnum;
import com.drore.exception.CustomException;
import com.drore.model.TeaSpecies;
import com.drore.service.CommodityService;
import com.drore.service.DataDictionaryService;
import com.drore.service.ImageInfoService;
import com.drore.service.TeaSpeciesService;
import com.drore.util.LoginSysUserUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:   茶种类信息服务层实现  <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/9/1 13:13  <br/>
 * 作者:    wdz
 */
@Service
public class TeaSpeciesServiceImpl   implements TeaSpeciesService {
    @Autowired
      CloudQueryRunner run;

    @Autowired
    ImageInfoService imageInfoService;
    @Autowired
    CommodityService commodityService;
    @Autowired
    DataDictionaryService dataDictionaryService;

    @Override
    public RestMessage save(TeaSpecies teaSpecies) {
        List<String> list = teaSpecies.getPics();
        teaSpecies.setPics(null);
        teaSpecies.setCreator(LoginSysUserUtil.getUserId());
        RestMessage model = run.insert(TeaSpecies.table,teaSpecies);
        if (!model.isSuccess()) {
            return model;
        }
        String id = model.getId();
        if (CollectionUtils.isNotEmpty(list)) {//保存图集
            model = imageInfoService.saveBatch(TeaSpecies.table, model.getId(), list);
            if (!model.isSuccess()) {
                throw new CustomException("保存图集失败");
            }
        }
        model.setId(id);
        return model;
    }

    @Override
    public RestMessage update(TeaSpecies teaSpecies) {
        TeaSpecies old =queryById(teaSpecies.getId());
        if (old == null) {
            throw new CustomException("未发现对象!");
        }
        old.setTypeId(teaSpecies.getTypeId());
        old.setPriceHigh(teaSpecies.getPriceHigh());
        old.setPriceLow(teaSpecies.getPriceLow());
        old.setPriceUnit(teaSpecies.getPriceUnit());
        old.setModifier(LoginSysUserUtil.getUserId());


        RestMessage model = run.update(TeaSpecies.table,teaSpecies.getId(),old);
        if (!model.isSuccess()) {
            return model;
        }
        String id = model.getId();
        List<String> list = teaSpecies.getPics();
        if (CollectionUtils.isNotEmpty(list)) {//保存图集
            model = imageInfoService.saveBatch(TeaSpecies.table, model.getId(), list);
            if (!model.isSuccess()) {
                throw new CustomException("保存图集失败");
            }
        }
        model.setId(id);
        return model;

    }

    @Override
    public TeaSpecies queryDetailById(String id) {
        TeaSpecies teaSpecies = queryById(id);
        if (teaSpecies != null) {
            //查找下类别，查找下图集
            if (StringUtils.isNotEmpty(teaSpecies.getTypeId())) {
                DataDictionary dataDictionary = dataDictionaryService.findById(teaSpecies.getTypeId());
                if (dataDictionary != null) {
                    teaSpecies.setTypeIdName(dataDictionary.getName());
                }
            }
            //获取下图集
            teaSpecies.setPics(imageInfoService.findPics(TeaSpecies.table,id));
        }
        return  teaSpecies;
    }

    @Override
    public TeaSpecies queryById(String id) {
        return  run.queryOne(TeaSpecies.class,TeaSpecies.table,id);
    }

    @Override
    public List<TeaSpecies> queryListByTypeId(String typeId) {
        Map<String,Object> term = new HashMap<String,Object>();
        if(StringUtils.isNotEmpty(typeId)){
            term.put("type_id",typeId);
        }
        Pagination pagination =   run.queryListByExample(TeaSpecies.class,TeaSpecies.table,term,null,null,null,1,10000);
        return  pagination.getData();
    }

    @Override
    public RestMessage removeById(String id) {
        if(commodityService.exitShelves(id)){
            throw  new CustomException("相关商品已经引用此种类，不能删除!");
        }
        //在删除
        return run.delete(TeaSpecies.table,id);
    }


    @Override
    public Pagination queryByPage(Pagination pagination, TeaSpecies teaSpecies) {
        StringBuffer sql = new StringBuffer("select * from " + TeaSpecies.table  );
        sql.append(" where is_deleted='" + CommonEnum.YesOrNo.NO.getCode() + "'");
        sql.append(" order by create_time desc ");
        pagination = run.sql(TeaSpecies.class,sql.toString(),pagination.getCurrent_page(),pagination.getPage_size());
        return pagination;
    }
}
