/*
 * Copyright (C) 2017 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */

package com.drore.cloud.service.impl;

import com.drore.cloud.constant.ConstantEnum;
import com.drore.cloud.constant.LocalConstant;
import com.drore.cloud.model.AddressInfo;
import com.drore.cloud.model.MemberInfo;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.ThreadLocalHolder;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.cloud.service.AddressService;
import com.drore.cloud.util.SortUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/***
 *
 * @since:hy-api 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2017/03/23 14:31
 */

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private CloudQueryRunner run;

    private static String user_id = "6b5e8fcc07c54e33918f434aff5d3376"; //用户id（测试用）

    @Override
    public RestMessage save(AddressInfo addressInfo) {
        RestMessage restMessage=null;
//        MemberInfo memberInfo=(MemberInfo) ThreadLocalHolder.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER);
//        addressInfo.setMemberId(memberInfo.getId());
        addressInfo.setMemberId(user_id);
        //判断是否是默认地址
        if (StringUtils.equalsIgnoreCase(addressInfo.getIsDefault(), ConstantEnum.ToggleEnum.Y.name())){
            //新增并设置为默认地址
            updateAllDefault();
        }else{
            //判断是否为用户的首个地址
            Pagination<AddressInfo> myAddress=findMyAddress(1,1000);
            if(myAddress.getCount() == 0){
                addressInfo.setIsDefault("Y");  //首个地址为默认地址
            }
        }
        //保存是否新增或保存
        if (StringUtils.isNotBlank(addressInfo.getId())){
            restMessage=run.update("member_address",addressInfo.getId(),addressInfo);
        }else{
            restMessage=run.insert("member_address",addressInfo);
        }
        return restMessage;
    }

    public void updateAllDefault(){
        Pagination<AddressInfo> myAddress=findMyAddress(1,1000);
        if (myAddress!=null&&myAddress.getCount()>0){
            List<Map> updateLists= Lists.newArrayList();
            for (AddressInfo addressInfo:myAddress.getData()){
                updateLists.add(ImmutableMap.of("is_default", ConstantEnum.ToggleEnum.N.name(),"pkid",addressInfo.getId()));
            }
            run.updateBatch("member_address",updateLists);
        }
    }

    @Override
    public Pagination<AddressInfo> findMyAddress(int current_page, int page_size) {
//        MemberInfo memberInfo=(MemberInfo) ThreadLocalHolder.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER);
        RequestExample req=new RequestExample(page_size,current_page);
        RequestExample.Criteria ct = req.create();
//        ct.getMust().add(req.createParam().addTerm("user_id", memberInfo.getId()));
        ct.getMust().add(req.createParam().addTerm("member_id", user_id));
        HashMap map=new HashMap();
        map.put("is_default","desc");
//        map.put("create_time","desc");
        req.setSort(map);
        Pagination<AddressInfo> pa = run.queryListByExample(AddressInfo.class,"member_address",req);
        if(pa.getCount() == 1){
            //设为默认地址
            pa.getData().get(0).setIsDefault("Y");
        }
        return pa;
    }

    @Override
    public RestMessage delete(String addressId) {
        return run.delete("member_address",addressId);
    }

    /**
     * 设置为默认地址
     * @param id 主键id
     * @return
     */
    @Override
    public RestMessage defaultAddress(String id) {
        RestMessage rm = null;

        //获取用户信息

        String user_id = "6b5e8fcc07c54e33918f434aff5d3376";

        Map<String,Object> term = new HashMap<String,Object>();
        term.put("is_default","Y");
        term.put("member_id",user_id);
        Map addressInfo = run.queryFirstByRName("member_address",term);
        addressInfo.put("is_default","N");
        term.put("id",id);
        List<Map> addressList = new ArrayList<>();
        //追加pkid
        term.put("pkid",id);
        addressInfo.put("pkid",addressInfo.get("id"));
        addressList.add(term);
        addressList.add(addressInfo);
        rm = run.updateBatch("member_address",addressList);
        if(rm.isSuccess()){
            Map<String,Object> data = new HashMap<>();
            data.put("address_info",addressInfo);
            data.put("new_address_info",term);
            rm.setData(data);
        }else{
            rm.setMessage("用户地址更新异常,请检查");
        }
        return rm;
    }
}
