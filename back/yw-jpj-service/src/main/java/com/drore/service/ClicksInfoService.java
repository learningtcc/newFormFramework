package com.drore.service;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.domain.vo.ClicksCountVo;
import com.drore.model.ClicksInfo;

import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明: 点击信息表    <br/>
 * 项目名称: yw-jpj-service <br/>
 * 创建日期:  2017/9/6 9:51  <br/>
 * 作者:    wdz
 */
public interface ClicksInfoService {
     /**
      * 功能描述：新增点击信息                   <br/>
      * 作    者：wdz                         <br/>
      * 创建时间：2017/9/6  9:52           <br/>
      * 实现逻辑：详述实现逻辑                 <br/>
      * 修 改 人：                            <br/>
      * 修改时间：                            <br/>
      * 修改说明：                            <br/>
      * ${tags} 
      */
     
     RestMessage save(ClicksInfo clicksInfo);


    RestMessage save(String tableId,String tableName,String name,String source,String memberId,String memberName);

    /**
     * 查询对应日期内点击数
     * @param tableName
     * @param startDate  yyyy-MM-dd
     * @param endDate yyyy-MM-dd
     * @return
     */
    List<ClicksCountVo> count(String tableName, String startDate, String endDate);



}
