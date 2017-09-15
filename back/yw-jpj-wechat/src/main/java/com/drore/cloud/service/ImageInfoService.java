package com.drore.cloud.service;


import com.drore.cloud.sdk.common.resp.RestMessage;

import java.util.List;

/**
 * Created by zhangh on 2017/9/12 0001.
 */

public interface ImageInfoService {

    /**
     * 公共图集部分,图集插入，实行全删全插
     * @param tableName 对应的表名
     * @param pid  对应的数据的主键
     * @param pics  图片地址集合
     * @return
     */
    public RestMessage saveBatch(String tableName, String pid, List<String> pics);


    /**
     * 查询图集
     * @param tableName   对应的表名
     * @param pid   对应的数据的主键
     * @return
     */
    public List<String> findPics(String tableName, String pid);


}
