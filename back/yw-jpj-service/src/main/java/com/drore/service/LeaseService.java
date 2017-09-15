package com.drore.service;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.model.LeaseInfo;

/**
 * Created by wangl on 2017/9/1 0001.
 */
public interface LeaseService {

    RestMessage addOrUpdate(LeaseInfo leaseInfo);

    RestMessage setTop(String id);

    Pagination leasingList(Pagination pagination,LeaseInfo leaseInfo);

    RestMessage list(LeaseInfo leaseInfo, Integer pageSize, Integer pageNo);

    RestMessage setIsUsing(String id,String status);

    RestMessage detail(String id);

    RestMessage setIsPublish(String id,String status);
}
