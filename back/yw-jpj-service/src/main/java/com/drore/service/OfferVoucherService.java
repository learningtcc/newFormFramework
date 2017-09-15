package com.drore.service;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.model.OfferVoucher;
import com.drore.util.PageUtil;

import java.util.List;

/**
 * Created by zhangh on 2017/9/4 0001.
 */
public interface OfferVoucherService {

    /**
     *  获取优惠活动列表
     * @param voucher
     * @param page_size
     * @param current_page
     * @return
     */
    public PageUtil getOfferVoucherList(OfferVoucher voucher, Integer page_size, Integer current_page);

    /**
     * 获取优惠活动
     * @param id
     * @return
     */
    public RestMessage queryOfferVoucher(String id);

    /**
     * 判断订单状态
     * @return
     */
    public RestMessage judgeOfferVoucherState();

    /**
     * 获取优惠类型枚举
     * @return
     */
    List<JSONObject> getCouponType();

    /**
     * 获取优惠状态枚举
     * @return
     */
    List<JSONObject> getCouponState();

    /**
     * 保存或修改
     */
    RestMessage addOrUpdate(OfferVoucher offerVoucher);
    /**
     * 结束
     */
    RestMessage over(String id);
}
