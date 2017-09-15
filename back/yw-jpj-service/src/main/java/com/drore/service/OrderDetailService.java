package com.drore.service;

import com.drore.model.OrderDetail;

import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:   订单详情服务层接口  <br/>
 * 项目名称: yw-jpj-service <br/>
 * 创建日期:  2017/9/4 11:09  <br/>
 * 作者:    wdz
 */
public interface OrderDetailService {
      /**
       * 功能描述：判断是否存在商品的订单详情                   <br/>
       * 作    者：wdz                         <br/>
       * 创建时间：2017/9/4  11:10           <br/>
       * 实现逻辑：详述实现逻辑                 <br/>
       * 修 改 人：                            <br/>
       * 修改时间：                            <br/>
       * 修改说明：                            <br/>
       * ${tags}
       */

      boolean exitCommodityId(String commodityId);

      /**
       * 功能描述：根据订单id获取相关订单详情                   <br/>
       * 作    者：wdz                         <br/>
       * 创建时间：2017/9/4  11:11           <br/>
       * 实现逻辑：详述实现逻辑                 <br/>
       * 修 改 人：                            <br/>
       * 修改时间：                            <br/>
       * 修改说明：                            <br/>
       * ${tags}
       */

      List<OrderDetail> queryByOrderId(String orderId);
}
