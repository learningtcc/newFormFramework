package com.drore.cloud.service.impl;

import com.drore.cloud.model.CommodityEvaluation;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.service.CommodityEvaluationService;
import com.drore.cloud.service.ImageInfoService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wangl on 2017/9/12 0012.
 */
@Service
public class CommodityEvaluationServiceImpl implements CommodityEvaluationService{

    @Autowired
    private CloudQueryRunner run;

    @Autowired
    private ImageInfoService imageInfoService;

    public RestMessage save(CommodityEvaluation commodityEvaluation){
        List<String> picList = commodityEvaluation.getPicList();
        commodityEvaluation.setPicList(null);
        String orderId = commodityEvaluation.getOrderId();
        //更新订单状态为已完成,评价状态为已评价
        run.update("order_info",orderId,ImmutableMap.of("order_status","Finished","is_evaluation","Y"));
        Map<String, Object> order_info = run.queryOne("order_info", orderId);
        String store_id = String.valueOf(order_info.get("store_id"));
        String member_id = String.valueOf(order_info.get("member_id"));
        String order_no = String.valueOf(order_info.get("order_no"));

        Map order_detail = run.queryFirstByRName("order_detail", ImmutableMap.of("order_id", orderId));
        String commodity_name = String.valueOf(order_detail.get("commodity_name"));
        String commodity_id = String.valueOf(order_detail.get("commodity_id"));

        Map<String, Object> member_info = run.queryOne("member_info", member_id);
        String nick_name = String.valueOf(member_info.get("nick_name"));
        String head_image_url = String.valueOf(member_info.get("head_image_url"));
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Date date = new Date();
        commodityEvaluation.setEvaluationTime(bartDateFormat.format(date));
        commodityEvaluation.setStoreId(store_id);
        commodityEvaluation.setUserName(nick_name);
        commodityEvaluation.setUserImage(head_image_url);
        commodityEvaluation.setCommodityName(commodity_name);
        commodityEvaluation.setUserId(member_id);
        commodityEvaluation.setCommodityId(commodity_id);
        commodityEvaluation.setOrderNo(order_no);

        RestMessage restMessage = run.insert("commodity_evaluation", commodityEvaluation);
        if (restMessage.isSuccess()){
            //保存图集
            imageInfoService.saveBatch("commodity_evaluation", restMessage.getId(), picList);
            restMessage.setMessage("保存成功");
        }else {
            restMessage.setMessage("保存失败");
        }
        return restMessage;
    }
}
