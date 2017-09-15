package com.drore.service;

import com.drore.util.RestMessageModel;

import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:  公共图集服务层接口   <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/9/1 10:53  <br/>
 * 作者:    wdz
 */
public interface ImageInfoService {

    /**
     * 公共图集部分,图集插入，实行全删全插
     * @param tableName 对应的表名
     * @param pid  对应的数据的主键
     * @param pics  图片地址集合
     * @return
     */
    public RestMessageModel saveBatch(String tableName,String pid,List<String> pics);


    /**
     * 查询图集
     * @param tableName   对应的表名
     * @param pid   对应的数据的主键
     * @return
     */
    public List<String> findPics(String tableName,String pid);


}
