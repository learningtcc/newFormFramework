package com.drore.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.domain.vo.ClicksCountVo;
import com.drore.enums.CommonEnum;
import com.drore.model.ClicksInfo;
import com.drore.service.ClicksInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:  点击服务层信息表   <br/>
 * 项目名称: yw-jpj-service <br/>
 * 创建日期:  2017/9/6 9:55  <br/>
 * 作者:    wdz
 */
@Service
public class ClicksInfoServiceImpl  implements ClicksInfoService{
    @Autowired
    private CloudQueryRunner run;
    @Override
    public RestMessage save(ClicksInfo clicksInfo) {
        return run.insert(ClicksInfo.table,clicksInfo);
    }

    @Override
    public RestMessage save(String tableId, String tableName, String name, String source, String memberId, String memberName) {
        ClicksInfo clicksInfo = new ClicksInfo();
        clicksInfo.setName(name);
        clicksInfo.setMemberId(memberId);
        clicksInfo.setMemberName(memberName);
        clicksInfo.setSource(CommonEnum.ClickSourceEnum.WEIXIN.getCode());
        clicksInfo.setTableId(tableId);
        clicksInfo.setTableName(tableName);
        return run.insert(ClicksInfo.table,clicksInfo);
    }

    @Override
    public List<ClicksCountVo> count(String tableName, String startDate, String endDate) {
        StringBuffer sql  = new StringBuffer(" select name,count(*) count from "+ClicksInfo.table+" where is_deleted='"
                + CommonEnum.YesOrNo.NO.getCode()+"'");
        sql.append(" and table_name='"+tableName+"'");
        sql.append(" and create_time >= '"+startDate+" 00:00:00'");
        sql.append(" and create_time <= '"+endDate+" 23:59:59'");
        sql.append(" group by table_id ");
        Pagination<ClicksCountVo> mapPagination = run.sql(ClicksCountVo.class,sql.toString(),1,1000);

        return  mapPagination.getData();


    }
}
