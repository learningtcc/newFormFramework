package com.drore.service.impl;

import com.drore.domain.vo.ClicksCountVo;
import com.drore.model.CommodityInfo;
import com.drore.model.StoreInfo;
import com.drore.model.StreetCultureInfo;
import com.drore.model.ThemeActivities;
import com.drore.service.ClicksInfoService;
import com.drore.service.CommodityService;
import com.drore.service.StatisticalService;
import com.drore.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:    所有的统计接口 <br/>
 * 项目名称: yw-jpj-service <br/>
 * 创建日期:  2017/9/6 9:34  <br/>
 * 作者:    wdz
 */
@Service
public class StatisticalServiceImpl implements StatisticalService {
    @Autowired
    ClicksInfoService clicksInfoService;
    @Autowired
    CommodityService commodityService;

    @Autowired
    StoreService storeService;


    /**
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public  List<ClicksCountVo> streetCulture(String startDate, String endDate){
        return   clicksInfoService.count(StreetCultureInfo.table,startDate,endDate);
    }

    @Override
    public List<ClicksCountVo> themeActivities(String startDate, String endDate) {
        return   clicksInfoService.count(ThemeActivities.table,startDate,endDate);
    }

    @Override
    public List<ClicksCountVo> commodity(String startDate, String endDate) {
        return   clicksInfoService.count(CommodityInfo.table,startDate,endDate);
    }

    @Override
    public List<ClicksCountVo> storeCommodity(String startDate, String endDate) {

        return   clicksInfoService.count(StoreInfo.table,startDate,endDate);
    }
}
