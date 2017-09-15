package com.drore.service;

import com.drore.domain.vo.ClicksCountVo;

import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:   所有的统计接口  <br/>
 * 项目名称: yw-jpj-service <br/>
 * 创建日期:  2017/9/6 9:33  <br/>
 * 作者:    wdz
 */
public interface StatisticalService {
    /**
     * 街区文化统计
     * @param startDate
     * @param endDate
     * @return
     */
    List<ClicksCountVo> streetCulture(String startDate, String endDate);

    /**
     * 主题活动点击
     * @param startDate
     * @param endDate
     * @return
     */
    List<ClicksCountVo> themeActivities(String startDate, String endDate);

    /**
     * 商品访问量分析
     * @param startDate
     * @param endDate
     * @return
     */
    List<ClicksCountVo> commodity(String startDate, String endDate);



    /**
     * 店铺访问量
     * @param startDate
     * @param endDate
     * @return
     */
    List<ClicksCountVo> storeCommodity(String startDate, String endDate);



}
