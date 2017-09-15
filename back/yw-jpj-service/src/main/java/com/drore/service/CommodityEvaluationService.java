package com.drore.service;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.model.CommodityEvaluation;

/**
 * Created by wangl on 2017/9/8 0008.
 */
public interface CommodityEvaluationService {

    Pagination queryByPage(Pagination pagination, CommodityEvaluation commodityEvaluation);

    CommodityEvaluation queryDetailById(String id);

    RestMessage audit(CommodityEvaluation commodityEvaluation);
}
