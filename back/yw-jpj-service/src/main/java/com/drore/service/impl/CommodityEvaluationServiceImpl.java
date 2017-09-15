package com.drore.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.enums.CommonEnum;
import com.drore.exception.CustomException;
import com.drore.model.CommodityEvaluation;
import com.drore.service.CommodityEvaluationService;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by wangl on 2017/9/8 0008.
 */
@Service
public class CommodityEvaluationServiceImpl implements CommodityEvaluationService{

    @Autowired
    private CloudQueryRunner run;

    @Override
    public CommodityEvaluation queryDetailById(String id) {
        CommodityEvaluation commodityEvaluation = run.queryOne(CommodityEvaluation.class, CommodityEvaluation.table, id);
        if(commodityEvaluation==null){
            throw  new CustomException("未发现查找对象!");
        }
        return  commodityEvaluation;
    }

    @Override
    public Pagination queryByPage(Pagination pagination, CommodityEvaluation commodityEvaluation) {
        StringBuffer sql = new StringBuffer("select * from "+ CommodityEvaluation.table);
        sql.append(" where is_deleted='"+ CommonEnum.YesOrNo.NO.getCode()+"' and audit_status='"+CommonEnum.AuditStatus.Audited+"'");
        //店铺名称
        if(StringUtils.isNotEmpty(commodityEvaluation.getOrderNo())){
            sql.append(" and order_no like '%"+commodityEvaluation.getOrderNo()+"%'");
        }
        //是否发布
        if(StringUtils.isNotEmpty(commodityEvaluation.getAuditStatus())){
            sql.append(" and audit_status ='"+commodityEvaluation.getAuditStatus()+"'");
        }
        return  run.sql(CommodityEvaluation.class,sql.toString(),pagination.getCurrent_page(),pagination.getPage_size());
    }

    @Override
    public RestMessage audit(CommodityEvaluation commodityEvaluation){
        String id = commodityEvaluation.getId();
        RestMessage restMessage = run.update(CommodityEvaluation.table, id, ImmutableMap.of("audit_status", commodityEvaluation.getAuditStatus()));
        return restMessage;
    }
}
