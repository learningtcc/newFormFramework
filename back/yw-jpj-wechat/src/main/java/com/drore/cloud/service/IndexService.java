package com.drore.cloud.service;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;

import java.util.Map;

/**
 * Created by 仁杰 on 2017/9/5
 */
public interface IndexService {

    /**
     * 商城首页
     * @return
     */
    RestMessage index();


    /**
     * 查询出售客户指定茶的商铺列表
     * @param id    茶种类主键id
     * @param current_page
     * @param page_size
     * @return
     */
    RestMessage online(String id,Integer current_page,Integer page_size);
}
