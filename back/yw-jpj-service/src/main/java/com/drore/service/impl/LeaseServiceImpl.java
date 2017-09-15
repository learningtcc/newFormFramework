package com.drore.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.exception.CustomException;
import com.drore.model.LeaseInfo;
import com.drore.model.StoreInfo;
import com.drore.service.DataDictionaryService;
import com.drore.service.ImageInfoService;
import com.drore.service.LeaseService;
import com.drore.util.PageUtil;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wangl on 2017/9/1 0001.
 */
@Service
public class LeaseServiceImpl implements LeaseService {

    @Autowired
    private CloudQueryRunner run;

    @Autowired
    private ImageInfoService imageInfoService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    public RestMessage addOrUpdate(LeaseInfo leaseInfo){
        RestMessage restMessage;
        List<String> list = leaseInfo.getPics();
        String id = leaseInfo.getId();
        leaseInfo.setPics(null);
        if (null != id && !"".equals(id)){
            restMessage = run.update("lease_info", id, leaseInfo);
            if (restMessage.isSuccess()){
                restMessage.setMessage("更新成功");
                if (CollectionUtils.isNotEmpty(list)) {//保存图集
                    restMessage = imageInfoService.saveBatch(LeaseInfo.table, id, list);
                    if (!restMessage.isSuccess()) {
                        throw new CustomException("保存图集失败");
                    }
                }
            }
        }else {
            leaseInfo.setIsTop("N");
            leaseInfo.setIsRelease("N");
            leaseInfo.setIsUsing("Y");
            //登录未做，暂时写死
            leaseInfo.setMerchantPk("develop"/*LoginStoreUserUtil.getStoreId()*/);
            restMessage = run.insert("lease_info", leaseInfo);
            if (restMessage.isSuccess()){
                restMessage.setMessage("添加成功");
                if (CollectionUtils.isNotEmpty(list)) {//保存图集
                    restMessage = imageInfoService.saveBatch(LeaseInfo.table, id, list);
                    if (!restMessage.isSuccess()) {
                        throw new CustomException("保存图集失败");
                    }
                }

            }
        }
        return restMessage;
    }

    public RestMessage setTop(String id){
        RestMessage restMessage;
        Pagination<Map> pagination = run.queryListByExample("lease_info",1, Integer.MAX_VALUE);
        List<Map> data = pagination.getData();
        for (int i = 0; i < data.size(); i++) {
            String id1 = String.valueOf(data.get(i).get("id"));
            run.update("lease_info",id1, ImmutableMap.of("is_top", "N"));
        }
        restMessage = run.update("lease_info", id, ImmutableMap.of("is_top", "Y"));
        if (restMessage.isSuccess()){
            restMessage.setMessage("置顶成功");
        }
        return restMessage;
    }

    public Pagination leasingList(Pagination pagination,LeaseInfo leaseInfo){
        RestMessage restMessage = new RestMessage();

        //动态拼接sql语句
        StringBuffer sql = new StringBuffer("select * from lease_info where is_deleted='N'");
        if (StringUtils.isNotEmpty(leaseInfo.getTitle())){
            sql.append(" and title like '%" + leaseInfo.getTitle() + "%'");
        }
        if (StringUtils.isNotEmpty(leaseInfo.getStartTime())){
            sql.append(" and release_time >= '"+leaseInfo.getStartTime()+" 00:00:00'");
        }
        if (StringUtils.isNotEmpty(leaseInfo.getEndTime())){
            sql.append(" and release_time <= '"+leaseInfo.getEndTime()+" 23:59:59'");
        }
        sql.append(" order by release_time desc");
        Pagination<LeaseInfo> p = run.sql(LeaseInfo.class, sql.toString(), pagination.getCurrent_page(), pagination.getPage_size());
        List<LeaseInfo> data = p.getData();
        for (int i = 0; i < data.size(); i++) {
            String merchant_pk = String.valueOf(data.get(i).getMerchantPk());
            StoreInfo storeInfo = run.queryOne(StoreInfo.class, StoreInfo.table, merchant_pk);
            p.getData().get(i).setStoreName(storeInfo.getName());
        }
        return p;
    }

    public RestMessage list(LeaseInfo leaseInfo, Integer pageSize, Integer pageNo){
        RestMessage restMessage = new RestMessage();
        StringBuffer sql = new StringBuffer("select * from lease_info where is_deleted='N'");
        String title = leaseInfo.getTitle();
        String type = leaseInfo.getType();
        String storeName = leaseInfo.getStoreName();
        String endTime = leaseInfo.getEndTime();
        String startTime = leaseInfo.getStartTime();
        if (null != title && !"".equals(title)){
            sql.append(" and title like '%" + title + "%'");
        }
        if (null != type && !"".equals(type)){
            sql.append(" and type = '"+type+"'");
        }
        if (null != startTime && !"".equals(startTime)){
            sql.append(" and release_time >= '"+startTime+" 00:00:00'");
        }
        if (null != endTime && !"".equals(endTime)){
            sql.append(" and  release_time <= '"+endTime+" 23:59:59'");
        }
        System.out.println(sql.toString());
        Pagination<Map> pagination = run.sql(sql.toString(), pageNo, pageSize);
        List<Map> data = pagination.getData();
        for (int i = 0; i < data.size(); i++) {
            String merchant_pk = String.valueOf(data.get(i).get("merchant_pk"));
            String type_1 = String.valueOf(data.get(i).get("type"));
            Map map = run.queryFirstByRName("data_dictionary", ImmutableMap.of("code", type_1));
            String typeName = String.valueOf(map.get("name"));
            pagination.getData().get(i).put("type_name",typeName);

            StringBuffer sql_1 = new StringBuffer("select * from store_info where id='"+merchant_pk+"'");
            if (null != storeName && !"".equals(storeName)){
                sql_1.append(" and name like '%" + storeName + "%'");
            }
            Pagination<Map> pagination_1 = run.sql(sql_1.toString());
            List<Map> data1 = pagination_1.getData();
            if (!data1.isEmpty()){
                for (int j = 0; j < data1.size(); j++) {
                    pagination.getData().get(i).put("store_name",pagination_1.getData().get(j).get("name"));
                }
            }else {
                pagination.getData().remove(i);
            }
        }

        PageUtil pageUtil = new PageUtil(pagination);

        restMessage.setData(pageUtil);
        return restMessage;
    }

    public RestMessage setIsUsing(String id, String status) {
        RestMessage restMessage = run.update("lease_info", id, ImmutableMap.of("is_using", status));
        return restMessage;
    }

    public RestMessage detail(String id){
        RestMessage restMessage = new RestMessage();
        LeaseInfo leaseInfo = run.queryOne(LeaseInfo.class, LeaseInfo.table, id);
        List<String> pics = imageInfoService.findPics(leaseInfo.table, leaseInfo.getId());
        leaseInfo.setPics(pics);
        restMessage.setData(leaseInfo);
        return restMessage;
    }

    public RestMessage setIsPublish(String id,String status){
        RestMessage restMessage = new RestMessage();
        //发布
        if ("Y".equals(status)){
            //登录未做，暂时写死
            restMessage = run.update("lease_info", id, ImmutableMap.of("publisher","ZHANGSAN" /*LoginStoreUserUtil.getStoreUser().getName()*/,
                    "is_release", "Y", "release_time", new Date()));
            if (restMessage.isSuccess()){
                restMessage.setMessage("发布成功");
            }else {
                restMessage.setMessage("发布失败");
            }
        }
        if ("N".equals(status)){
            restMessage = run.update("lease_info", id, ImmutableMap.of("is_release", "N"));
            if (restMessage.isSuccess()){
                restMessage.setMessage("撤下成功");
            }else {
                restMessage.setMessage("撤下失败");
            }
        }
        return restMessage;
    }
}
