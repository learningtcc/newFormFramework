package com.drore.cloud.service.impl;

import com.drore.cloud.constant.LocalConstant;
import com.drore.cloud.model.MemberInfo;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.ThreadLocalHolder;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.cloud.service.CommodityService;
import com.drore.cloud.util.SortUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 仁杰 on 2017/9/7
 */
@Service
public class CommodityServiceImpl implements CommodityService {

    @Autowired
    private CloudQueryRunner run;

    /**
     * 查询商品详情
     * @param commodityId 商品id
     * @return
     */
    @Override
    public RestMessage commodityDetail(String commodityId) {
        RestMessage rm = new RestMessage();
        Map<String,Object> data = new HashMap<String,Object>();

        Map<String,Object> commodityInfo = run.queryOne("commodity_info",commodityId);  //商品信息表——commodity_info
        //查询茶叶种类
        if(commodityInfo.get("species_id") != null && "".equals(commodityInfo.get("species_id").toString())){
            //茶种类信息表——tea_species
            Map<String,Object> speciesMap = run.queryOne("tea_species",commodityInfo.get("species_id").toString());
            if (speciesMap != null && speciesMap.size() > 0){
                commodityInfo.put("species_name",speciesMap.get("name"));   //追加商品(茶叶)种类名称
            }else{
                rm.setSuccess(false);
                rm.setMessage("未知的商品种类,请检查");
                return rm;
            }
        }else{
            commodityInfo.put("species_name","未知的茶类");
//            rm.setSuccess(false);
//            rm.setMessage("商品种类id不能为null");
//            return rm;
        }
        //判断用户是否收藏过
//        MemberInfo memberInfo = (MemberInfo) ThreadLocalHolder.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER);
        String user_id = "6b5e8fcc07c54e33918f434aff5d3376";
//        if(memberInfo != null){
            //查询用户收藏表
            Map<String,Object> term2 = new HashMap<String,Object>();
//            term2.put("user_id",memberInfo.getId());
            term2.put("user_id",user_id);
            term2.put("commodity_id",commodityId);
            Map CollectionMap = run.queryFirstByRName("user_collection",term2);
            if(CollectionMap != null && CollectionMap.size() > 0){
                //已收藏
                commodityInfo.put("is_collection","Y");
            }else{
                //未收藏
                commodityInfo.put("is_collection","N");
            }
//        }
        //追加商品图集信息
        RequestExample req = SortUtils.sort("create_time","asc",100,1);
        req.create().getMust().add(req.createParam().addTerm("table_name","commodity_info").addTerm("table_pk",commodityId));
        commodityInfo.put("cms_material_info",run.queryListByExample("image_info",req));

        data.put("commodity_info",commodityInfo);

        rm.setSuccess(true);
        rm.setData(data);
        return rm;
    }


    /**
     * 商品评价列表
     * @param commodity_id 商品id
     * @param current_page
     * @param page_size
     * @return
     */
    @Override
    public Pagination<Map> evaluateList(String commodity_id, Integer current_page, Integer page_size) {
        Pagination<Map> pm = new Pagination<Map>();
        if(StringUtils.isNotBlank(commodity_id)){
            RequestExample req = SortUtils.sort("create_time","desc",page_size,current_page);
            req.create().getMust().add(req.createParam().addTerm("commodity_id",commodity_id));
            pm= run.queryListByExample("commodity_evaluation",req);
        }else{
            pm.setSuccess(false);
            pm.setMessage("商品id不能为null,请检查");
        }
        return pm;
    }
}
