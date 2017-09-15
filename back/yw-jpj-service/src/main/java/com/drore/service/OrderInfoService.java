package com.drore.service;

import com.drore.domain.vo.ClicksCountVo;
import com.drore.domain.vo.TradeCountVo;

import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:   订单信息服务层接口  <br/>
 * 项目名称: yw-jpj-service <br/>
 * 创建日期:  2017/9/4 16:13  <br/>
 * 作者:    wdz
 */
public interface OrderInfoService {

    /**
     * 功能描述：商品销量统计，此处是只要不是代付款的都计算                    <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/6  14:59           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags}
     * @param endDate  结束日期  yyyy-MM-dd
     * @param startDate  结束日期  yyyy-MM-dd
     */

    List<ClicksCountVo> countCommondiy(String startDate, String endDate);

    /**
     * 总的交易量分析
     * @param startDate
     * @param endDate
     * @return
     */
    List<TradeCountVo> totalTrade(String startDate, String endDate) throws Exception;


    /**
     * 商铺交易量分析
     * @param startDate
     * @param endDate
     * @return
     */
    List<TradeCountVo> totalTradeStore(String startDate, String endDate) throws Exception;
}
