package com.drore.cloud.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.cloud.service.IndexService;
import com.drore.cloud.util.ListsUtils;
import com.drore.cloud.util.SortUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 仁杰 on 2017/9/5
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private CloudQueryRunner run;

    /**
     * 商城首页
     * @return
     */
    @Override
    public RestMessage index() {
        RestMessage rm = new RestMessage();

        Map<String,Object> data = new HashMap<String,Object>();
        //追加广告信息
        data.put("advertising_list",run.queryListByExample("advertising_info").getData());
        //追加特色商品信息
        Map<String,Object> term = new HashMap<String,Object>();
        term.put("is_features","Y");
        data.put("feature_list",run.queryListByExample("commodity_info",term).getData());
        //追加热门商铺信息
        Map<String,Object> term2 = new HashMap<String,Object>();
        term2.put("is_hot","Y");
        data.put("store_list",run.queryListByExample("store_info",term2).getData());

        rm.setData(data);
        return rm;
    }

    /**
     * 查询出售客户指定茶的商铺列表
     * @param id    茶种类主键id
     * @param current_page
     * @param page_size
     * @return
     */
    @Override
    public RestMessage online(String id,Integer current_page,Integer page_size) {
        RestMessage rm = new RestMessage();
//        Pagination<Map> pm = new Pagination<>();
        Map<String,Object> data = new HashMap<>();
        //追加广告信息
        RequestExample req2 = SortUtils.sort("create_time","desc",100,1);
        req2.create().getMust().add(req2.createParam().addTerm("table_name","store_info"));
        data.put("advertising_List",run.queryListByExample("advertising_info",req2).getData());
        //查询该种类茶的所有商品
        RequestExample req = new RequestExample(page_size,current_page);
        req.create().getMust().add(req.createParam().addTerm("species_id",id));
        List<Map> commodityList =  run.queryListByExample("commodity_info",req).getData();
        if(commodityList.size() > 0){
            //每个商铺同一品种的茶只展示一条
            List<Map> newCommoditys = ListsUtils.deleteRepetition(commodityList,"species_id,store_id");
            for (Map commodity : newCommoditys ){
                //追加商铺信息
                commodity.put("store_info",run.queryOne("store_info",commodity.get("store_id").toString()));
            }
            data.put("online_list",newCommoditys);
        }
        rm.setSuccess(true);
        rm.setData(data);
        return rm;
    }
}
