package com.drore.cloud.service;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;

import java.util.Map;

/**
 * Created by 仁杰 on 2017/9/7
 */
public interface CommodityService {


    /**
     * 商品详情
     * @param commodityId 商品id
     * @return
     */
    RestMessage commodityDetail(String commodityId);


    /**
     * 商品评价列表
     * @param commodity_id
     * @param current_page
     * @param page_size
     * @return
     */
    Pagination<Map> evaluateList(String commodity_id, Integer current_page, Integer page_size);

}
