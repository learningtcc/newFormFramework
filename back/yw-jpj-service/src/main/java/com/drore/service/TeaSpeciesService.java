package com.drore.service;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.model.TeaSpecies;

import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:  茶种类信息接口   <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/9/1 11:57  <br/>
 * 作者:    wdz
 */
public interface TeaSpeciesService {
    /**
     * 新增
     * @param teaSpecies
     * @return
     */
    RestMessage save(TeaSpecies teaSpecies);

    /**
     * 编辑
     * @param teaSpecies
     * @return
     */
    RestMessage update(TeaSpecies teaSpecies);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    TeaSpecies  queryDetailById(String id);

    /**
     * 根据主键查询 原生返回
     * @param id
     * @return
     */
    TeaSpecies  queryById(String id);


    /**
     * 根据类型查找列表
     * @param typeId
     * @return
     */
    List<TeaSpecies> queryListByTypeId(String typeId);

    /**
     * 根据主键删除，删除的时候要判断下是否被商品引用，如果引用那么不能删除
     * @param id
     * @return
     */
    RestMessage removeById(String id);

    /**
     * 分页查询
     * @param pagination
     * @param teaSpecies
     * @return
     */
    Pagination queryByPage(Pagination pagination, TeaSpecies teaSpecies);
}
