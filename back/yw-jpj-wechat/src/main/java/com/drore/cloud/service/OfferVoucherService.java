package com.drore.cloud.service;

import com.drore.cloud.sdk.common.resp.RestMessage;

/**
 * Created by wangl on 2017/9/4 0004.
 */
public interface OfferVoucherService {

    RestMessage list(Integer page_size,Integer current_page);

    RestMessage detail(String storeId);
}
