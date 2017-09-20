package com.drore.cloud.service.impl;

import com.drore.cloud.constant.ConstantEnum;
import com.drore.cloud.constant.LocalConstant;
import com.drore.cloud.model.MemberInfo;
import com.drore.cloud.model.MessageInfo;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                MemberInfo memberInfo = (MemberInfo) ThreadLocalHolder.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER);
                //判断用户收藏/取消收藏
                Map<String,Object> data = new HashMap<String,Object>();
                data.put("user_id",memberInfo.getId());
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
            MemberInfo memberInfo = (MemberInfo) ThreadLocalHolder.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER);
            RequestExample req = SortUtils.sort("create_time","desc",page_size,current_page);
            RequestExample.Criteria rc = req.create();
            rc.getMust().add(req.createParam().addTerm("user_id",memberInfo.getId()).addTerm("collection_type",collection_type));
            //rc.getMust().add(req.createParam().addTerm("user_id","cb6b9f5927614fd99f83ac4f060d83cb").addTerm("collection_type",collection_type));
            Pagination<Map> collectPa = run.queryListByExample("user_collection",req);
            if(collectPa.getCount() > 0){
                //店铺不存在要去除的收藏信息
                List<Map> removeCollectList = new ArrayList<Map>();
                //根据收藏类型追加对应的信息
                if(ConstantEnum.CollectionEnum.STORE.getValue().equals(collection_type)){
                    //追加店铺信息
                    for (Map collectMap : collectPa.getData()){
                        Map<String, Object> storeInfo = run.queryOne("store_info", collectMap.get("commodity_id").toString());
                        if(storeInfo != null){
                            collectMap.put("store_info",storeInfo);
                        }else{
                            removeCollectList.add(collectMap);
                        }
                    }
                    for(Map removeCollect : removeCollectList){
                        collectPa.getData().remove(removeCollect);
                    }
                }else{
                    //追加商品信息
                    for (Map collectMap : collectPa.getData()){
                        Map<String, Object> commodityInfo = run.queryOne("commodity_info", collectMap.get("commodity_id").toString());
                        if(commodityInfo != null){
                            collectMap.put("commodity_info",commodityInfo);
                        }else{
                            removeCollectList.add(collectMap);
                        }
                    }
                    for(Map removeCollect : removeCollectList){
                        collectPa.getData().remove(removeCollect);
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
        RestMessage restMessage = new RestMessage();
        MemberInfo memberInfo = run.queryOne(MemberInfo.class, MemberInfo.table, sessionMemberInfo.getId());
        restMessage.setData(memberInfo);
        return restMessage;
    }

    @Override
    public RestMessage updateUserNickName(String nickName) {
        MemberInfo sessionMemberInfo = (MemberInfo) ThreadLocalHolder.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER);
        return run .update(MemberInfo.table,sessionMemberInfo.getId(), ImmutableMap.of("nick_name",nickName));
    }

    @Override
    public RestMessage updateUserTel(String tel) {
        MemberInfo sessionMemberInfo = (MemberInfo) ThreadLocalHolder.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER);
        return run .update(MemberInfo.table,sessionMemberInfo.getId(), ImmutableMap.of("tel",tel));
    }


    @Override
    public RestMessage getUserMessageList(Integer current_page,Integer page_size) {
        RestMessage restMessage = new RestMessage();
        MemberInfo sessionMemberInfo = (MemberInfo) ThreadLocalHolder.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER);
        String id= "58b9a959ecef4a22a666b067eb8c6113";
        if(sessionMemberInfo != null){
            id = sessionMemberInfo.getId();
        }
        RequestExample requestExample=new RequestExample(page_size,current_page);
        //创建Criteria
        RequestExample.Criteria rc = requestExample.create();
        RequestExample.Param pa = requestExample.createParam();
        pa.addTerm("recipient",id);
        rc.getMust().add(pa);
        requestExample.addSort("create_time","desc");
        Pagination<MessageInfo> messageInfoPagination = run.queryListByExample(MessageInfo.class, MessageInfo.table, requestExample);
        List message = new ArrayList();
        List readMessage = new ArrayList();
        for(MessageInfo messageInfo : messageInfoPagination.getData()){
            if("N".equals(messageInfo.getIsRead())){
                message.add(messageInfo);
            }else{
                readMessage.add(messageInfo);
            }
        }
        message.addAll(readMessage);
        messageInfoPagination.setData(message);
        restMessage.setData(messageInfoPagination);
        return restMessage;
    }

    @Override
    public RestMessage getUserMessage(String id) {
        RestMessage restMessage = new RestMessage();
        MessageInfo messageInfo = run.queryOne(MessageInfo.class, MessageInfo.table, id);
        restMessage.setData(messageInfo);
        //标记已读
        run.update(MessageInfo.table,id,ImmutableMap.of("is_read","Y"));
        return restMessage;
    }

    @Override
    public RestMessage deleteUserMessage(String id) {
        return run.delete(MessageInfo.table,id);
    }
}
