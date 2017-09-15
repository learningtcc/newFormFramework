package com.drore.cloud.service;

import com.drore.cloud.model.CommodityEvaluation;
import com.drore.cloud.sdk.common.resp.RestMessage;

/**
 * Created by wangl on 2017/9/12 0012.
 */
public interface CommodityEvaluationService {

    RestMessage save(CommodityEvaluation commodityEvaluation);
}
