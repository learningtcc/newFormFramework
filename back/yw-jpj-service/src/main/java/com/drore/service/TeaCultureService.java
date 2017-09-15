package com.drore.service;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.model.TeaCultureInfo;

/**
 * Created by wangl on 2017/9/1 0001.
 */
public interface TeaCultureService {

    RestMessage addTeaCulture(TeaCultureInfo teaCultureInfo);

    RestMessage setIsUsing(String id,String status);

    Pagination queryByPage(Pagination pagination, TeaCultureInfo teaCultureInfo);

    TeaCultureInfo queryDetailById(String id);

}
