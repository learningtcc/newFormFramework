package com.drore.cloud.service.impl;

import com.drore.cloud.constant.ConstantEnum;
import com.drore.cloud.constant.LocalConstant;
import com.drore.cloud.model.MemberInfo;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.ThreadLocalHolder;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.cloud.service.UserService;
import com.drore.cloud.util.SortUtils;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 仁杰 on 2017/9/7
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private CloudQueryRunner run;

    /**
     *  用户收藏
     * @param commodity_id  店铺/商品id
     * @param collection_type   收藏类型
     * @param is_collection 是否收藏
     * @return
     */
    @Override
    public RestMessage collection(String commodity_id,String collection_type,String is_collection) {
        RestMessage rm = new RestMessage();
        if(StringUtils.isNotBlank(commodity_id) && StringUtils.isNotBlank(collection_type)){
            if(ConstantEnum.CollectionEnum.COMMODITY.getValue().equals(collection_type) ||
                    ConstantEnum.CollectionEnum.STORE.getValue().equals(collection_type)){
                //获取用户信息
//                MemberInfo memberInfo = (MemberInfo) ThreadLocalHolder.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER);
                String user_id = "6b5e8fcc07c54e33918f434aff5d3376";
                //判断用户收藏/取消收藏
                Map<String,Object> data = new HashMap<String,Object>();
//                data.put("user_id",memberInfo.getId());
                data.put("user_id",user_id);
                data.put("collection_type",collection_type);
                data.put("commodity_id",commodity_id);
                if("Y".equals(is_collection)){
                    //取消收藏
                    Map userColl = run.queryFirstByRName("user_collection",data);
                    if(userColl != null && userColl.size() > 0){
                        rm = run.delete("user_collection",userColl.get("id").toString());
                        if(rm.isSuccess()){
                            Map<String,Object> newData = new HashMap<String,Object>();
                            newData.put("is_collection","N");
                            rm.setData(newData);
                            rm.setMessage("取消收藏成功");
                        }
                    }else{
                        rm.setSuccess(false);
                        rm.setMessage("参数异常,请检查");
                    }
                    return rm;
                }else if("N".equals(is_collection)){
                    //收藏
                    rm = run.insert("user_collection",data);
                    if(rm.isSuccess()){
                        Map<String,Object> newData = new HashMap<String,Object>();
                        newData.put("is_collection","Y");
                        rm.setData(newData);
                        rm.setMessage("收藏成功");
                    }
                }
            }else{
                rm.setSuccess(false);
                rm.setMessage("未知的收藏类型,请检查");
            }
            return rm;
        }else{
            rm.setSuccess(false);
            rm.setMessage("收藏目标id或者收藏类型为null,请检查");
        }
        return rm;
    }


    /**
     * 我的收藏
     * @param collection_type 收藏类型
     * @return
     */
    @Override
    public Pagination<Map> userCollection(String collection_type,Integer current_page,Integer page_size) {
        Pagination<Map> pm = new Pagination<Map>();
        if(ConstantEnum.CollectionEnum.STORE.getValue().equals(collection_type) ||
                ConstantEnum.CollectionEnum.COMMODITY.getValue().equals(collection_type)){
            //获取用户信息
//            MemberInfo memberInfo = (MemberInfo) ThreadLocalHolder.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER);
            String user_id = "6b5e8fcc07c54e33918f434aff5d3376";
            RequestExample req = SortUtils.sort("create_time","desc",page_size,current_page);
            RequestExample.Criteria rc = req.create();
//            rc.getMust().add(req.createParam().addTerm("user_id",memberInfo.getId()).addTerm("collection_type",collection_type));
            rc.getMust().add(req.createParam().addTerm("user_id",user_id).addTerm("collection_type",collection_type));
            Pagination<Map> collectPa = run.queryListByExample("user_collection",req);
            if(collectPa.getCount() > 0){
                //根据收藏类型追加对应的信息
                if(ConstantEnum.CollectionEnum.STORE.getValue().equals(collection_type)){
                    //追加店铺信息
                    for (Map collectMap : collectPa.getData()){
                        collectMap.put("store_info",run.queryOne("store_info",collectMap.get("commodity_id").toString()));
                    }
                }else{
                    //追加商品信息
                    for (Map collectMap : collectPa.getData()){
                        collectMap.put("commodity_info",run.queryOne("commodity_info",collectMap.get("commodity_id").toString()));
                    }
                }
            }
            return collectPa;
        }else{
            pm.setSuccess(false);
            pm.setMessage("未知的收藏类型,请检查");
            return pm;
        }
    }

    @Override
    public RestMessage userInfo() {
        MemberInfo sessionMemberInfo = (MemberInfo) ThreadLocalHolder.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER);
        //测试id
        String id= "58b9a959ecef4a22a666b067eb8c6113";
        if(sessionMemberInfo != null){
            id = sessionMemberInfo.getId();
        }
        RestMessage restMessage = new RestMessage();
        MemberInfo memberInfo = run.queryOne(MemberInfo.class, MemberInfo.table, id);
        restMessage.setData(memberInfo);
        return restMessage;
    }

    @Override
    public RestMessage updateUserNickName(String nickName) {
        MemberInfo sessionMemberInfo = (MemberInfo) ThreadLocalHolder.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER);
        //测试id
        String id= "58b9a959ecef4a22a666b067eb8c6113";
        if(sessionMemberInfo != null){
            id = sessionMemberInfo.getId();
        }
        return run .update(MemberInfo.table,id, ImmutableMap.of("nick_name",nickName));
    }

    @Override
    public RestMessage updateUserTel(String tel) {
        MemberInfo sessionMemberInfo = (MemberInfo) ThreadLocalHolder.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER);
        //测试id
        String id= "58b9a959ecef4a22a666b067eb8c6113";
        if(sessionMemberInfo != null){
            id = sessionMemberInfo.getId();
        }
        return run .update(MemberInfo.table,id, ImmutableMap.of("tel",tel));
    }
}
