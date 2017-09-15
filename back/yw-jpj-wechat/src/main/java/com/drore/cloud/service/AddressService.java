/*
 * Copyright (C) 2017 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */

package com.drore.cloud.service;

import com.drore.cloud.model.AddressInfo;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;

/***
 *
 * @since:hy-api 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2017/03/23 14:29
 */
public interface AddressService {

    /***
     * 保存活更新地址
     * @param addressInfo
     * @return
     */
    public RestMessage save(AddressInfo addressInfo);


    /***
     * 查找我的地址列表信息
     * @return
     */
    public Pagination<AddressInfo> findMyAddress(int current_page, int page_size);

    /***
     * 删除地址
     * @param addressId
     * @return
     */
    public RestMessage delete(String addressId);

    /**
     * 设置为默认地址
     * @param id    主键id
     * @return
     */
    RestMessage defaultAddress(String id);
}
