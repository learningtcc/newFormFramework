package com.drore.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.domain.vo.ClicksCountVo;
import com.drore.domain.vo.TradeCountVo;
import com.drore.enums.CommonEnum;
import com.drore.model.OrderDetail;
import com.drore.model.OrderInfo;
import com.drore.service.OrderInfoService;
import com.drore.util.DateTimeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:  订单信息服务层实现   <br/>
 * 项目名称: yw-jpj-service <br/>
 * 创建日期:  2017/9/4 16:13  <br/>
 * 作者:    wdz
 */
@Service
public class OrderInfoServiceImpl implements OrderInfoService {
    @Autowired
    private CloudQueryRunner run;


    @Override
    public List<ClicksCountVo> countCommondiy(String startDate, String endDate) {
        StringBuffer sql  = new StringBuffer(" select de.commodity_name name ,sum(de.commodity_amout) count  ");
        sql.append(" from "+ OrderDetail.table+" de,"+ OrderInfo.table+" od where de.is_deleted='"+ CommonEnum.YesOrNo.NO.getCode()+"' ");
        sql.append(" and od.id = de.order_id ");
        sql.append(" and od.order_time  >= '"+startDate+" 00:00:00'");
        sql.append(" and od.order_time <= '"+endDate+" 23:59:59'");
        sql.append(" and od.order_status != '"+ CommonEnum.OrderStatusEnum.NO_PAY+"' ");
        sql.append(" group by de.commodity_id ");
        Pagination<ClicksCountVo> mapPagination = run.sql(ClicksCountVo.class,sql.toString(),1,1000);
        return  mapPagination.getData();
    }

    @Override
    public List<TradeCountVo> totalTrade(String startDate, String endDate) throws Exception {

        Date startTime  =DateTimeUtil.strToDate(startDate,DateTimeUtil.NORMALFORMAT);
        Date endTime  =DateTimeUtil.strToDate(endDate,DateTimeUtil.NORMALFORMAT);
        List<Date> list = DateTimeUtil.dateSplit(startTime,endTime);
        List<TradeCountVo> tradeCountVoList = null;
        if(CollectionUtils.isEmpty(list)){
            return  tradeCountVoList;
        }
        List<String> listStr = DateTimeUtil.listDateToStr(list,DateTimeUtil.NORMALFORMAT);
        StringBuffer sql = new StringBuffer("SELECT  COALESCE(sum(total),0) money,count(*) count FROM "+OrderInfo.table);
        sql.append(" where is_deleted='"+ CommonEnum.YesOrNo.NO.getCode()+"'");
        sql.append(" and  order_status != '"+ CommonEnum.OrderStatusEnum.NO_PAY+"' ");

        tradeCountVoList = new ArrayList<TradeCountVo>();
        for(String day:listStr){
             TradeCountVo tradeCountVo = new TradeCountVo();
             sql.append("AND  order_time >= '"+day+" 00:00:00' ");
             sql.append("AND  order_time <= '"+day+" 23:59:59' ");
             Pagination<TradeCountVo> mapPagination = run.sql(TradeCountVo.class,sql.toString(),1,1);
            if(mapPagination.getData()!=null){
                tradeCountVo = mapPagination.getData().get(0);
            }
            tradeCountVo.setDay(day);
            tradeCountVoList.add(tradeCountVo);

         }
        return tradeCountVoList;
    }

    @Override
    public List<TradeCountVo> totalTradeStore(String startDate, String endDate) throws Exception {

        StringBuffer sql = new StringBuffer("SELECT store_name storeName, COALESCE(sum(total),0) money,count(*) count FROM "+OrderInfo.table);
        sql.append(" where is_deleted='"+ CommonEnum.YesOrNo.NO.getCode()+"'");
        sql.append(" and  order_status != '"+ CommonEnum.OrderStatusEnum.NO_PAY+"' ");
        List<TradeCountVo> list = null;
            sql.append("AND  order_time >= '"+startDate+" 00:00:00' ");
            sql.append("AND  order_time <= '"+endDate+" 23:59:59' ");
        sql.append(" group by store_id ");
            Pagination<TradeCountVo> mapPagination = run.sql(TradeCountVo.class,sql.toString(),1,1);
            if(mapPagination.getData()!=null){
                list = mapPagination.getData();
            }
        return list;
    }
}
