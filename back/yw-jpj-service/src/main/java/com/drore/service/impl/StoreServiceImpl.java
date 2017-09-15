package com.drore.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.enums.CommonEnum;
import com.drore.exception.CustomException;
import com.drore.model.CommodityInfo;
import com.drore.model.StoreInfo;
import com.drore.service.ClicksInfoService;
import com.drore.service.ImageInfoService;
import com.drore.service.StoreService;
import com.drore.util.LoginStoreUserUtil;
import com.drore.util.LoginSysUserUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：商家信息服务层接口                 <br/>
 * 作    者：wdz                         <br/>
 * 创建时间：2017/9/4  9:52           <br/>
 * 实现逻辑：景区添加商家信息，                <br/>
 * 修 改 人：                            <br/>
 * 修改时间：                            <br/>
 * 修改说明：                            <br/>
 * ${tags} 
 */

@Service
public class StoreServiceImpl implements StoreService {
    @Autowired
    CloudQueryRunner run;
    @Autowired
    ImageInfoService imageInfoService;
    @Autowired
    ClicksInfoService clicksInfoService;

    @Override
    public StoreInfo queryById(String id) {
        return run.queryOne(StoreInfo.class, StoreInfo.table, id);
    }

    @Override
    public StoreInfo queryDetailById(String id) {
        StoreInfo storeInfo =queryById(id);
        if(storeInfo==null){
            throw  new CustomException("未发现查找对象!");
        }
        storeInfo.setPics(imageInfoService.findPics(CommodityInfo.table,id));
        return  storeInfo;
    }

    @Override
    public StoreInfo queryDetailByStore() {
         return  queryDetailById(LoginStoreUserUtil.getStoreId());
    }

    @Override
    public synchronized StoreInfo queryDetailApiById(String id,String name,String memberId,String memberName){
        StoreInfo storeInfo = queryDetailById(id);
        storeInfo.setClicks(storeInfo.getClicks()+1);
        RestMessage restMessage = run.update(StoreInfo.table,id,storeInfo);
        if(!restMessage.isSuccess()){
            throw  new CustomException(restMessage.getMessage());
        }
        restMessage  = clicksInfoService.save(id, StoreInfo.table, name, CommonEnum.ClickSourceEnum.WEIXIN.getCode(), memberId, memberName);
        if(!restMessage.isSuccess()){
            throw  new CustomException("保存点击率信息报错" + restMessage.getMessage());
        }

        return  storeInfo;
    }

    @Override
    public RestMessage save(StoreInfo storeInfo) {
        if(CommonEnum.YesOrNo.YES.getCode().equals(storeInfo.getIsRelease())){//如果是默认发布
            storeInfo.setReleaseTime(new Date());//设置上架时间
            storeInfo.setReleaseId(LoginSysUserUtil.getUserId());
        }
        storeInfo.setCreator(LoginSysUserUtil.getUserId());
        storeInfo.setClicks(0);//设置默认点击率
        storeInfo.setIsHot(CommonEnum.YesOrNo.NO.getCode());//设置非热门
        RestMessage restMessage  =  run.insert(StoreInfo.table,storeInfo);
        if (!restMessage.isSuccess()) {
            return restMessage;
        }

        String id = restMessage.getId();
        List<String> list = storeInfo.getPics();
        if (CollectionUtils.isNotEmpty(list)) {//保存图集
            restMessage = imageInfoService.saveBatch(StoreInfo.table, id, list);
            if (!restMessage.isSuccess()) {
                throw new CustomException("保存图集失败");
            }
        }
        restMessage.setId(id);
        return restMessage;
    }

    @Override
    public RestMessage updateScenic(StoreInfo storeInfo) {
        String id = storeInfo.getId();
        StoreInfo old =queryById(id);
        if(old==null){
            throw  new CustomException("未发现查找对象!");
        }

        if(CommonEnum.YesOrNo.YES.getCode().equals(storeInfo.getIsRelease())){//如果是默认发布
            old.setReleaseTime(new Date());//设置上架时间
            old.setReleaseId(LoginSysUserUtil.getUserId());
        }
        old.setModifier(LoginSysUserUtil.getUserId());
        old.setAddress(storeInfo.getAddress());
        old.setContactPerson(storeInfo.getContactPerson());
        old.setName(storeInfo.getName());
        old.setContactPhone(storeInfo.getContactPhone());
        old.setBusinessLicenseUrl(storeInfo.getBusinessLicenseUrl());
        RestMessage restMessage  =  run.update(StoreInfo.table,id,old);
        if (!restMessage.isSuccess()) {
            return restMessage;
        }

        List<String> list = storeInfo.getPics();
        if (CollectionUtils.isNotEmpty(list)) {//保存图集
            restMessage = imageInfoService.saveBatch(StoreInfo.table, id, list);
            if (!restMessage.isSuccess()) {
                throw new CustomException("保存图集失败");
            }
        }
        restMessage.setId(id);
        return restMessage;
    }

    @Override
    public RestMessage update(StoreInfo storeInfo) {
        String id = storeInfo.getId();
        StoreInfo old =queryById(id);
        if(old==null){
            throw  new CustomException("未发现查找对象!");
        }

        if(CommonEnum.YesOrNo.YES.getCode().equals(storeInfo.getIsRelease())){//如果是默认发布
            old.setReleaseTime(new Date());//设置上架时间
        }

        old.setDimension(storeInfo.getDimension());
        old.setLongitude(storeInfo.getLongitude());
        old.setPayee(storeInfo.getPayee());
        old.setDescription(storeInfo.getDescription());
        old.setAddress(storeInfo.getAddress());
        old.setName(storeInfo.getName());
        old.setReleaseId(LoginStoreUserUtil.getUserId());
        old.setBankCard(storeInfo.getBankCard());
        old.setBankAddress(storeInfo.getBankAddress());
        old.setAlipayUrl(storeInfo.getAlipayUrl());
        old.setWeixinUrl(storeInfo.getWeixinUrl());
        old.setBusinessLicenseUrl(storeInfo.getBusinessLicenseUrl());
        old.setContactPhone(storeInfo.getContactPhone());
        old.setContactPerson(storeInfo.getContactPerson());
        old.setModifier(LoginStoreUserUtil.getUserId());

        RestMessage restMessage  =  run.update(StoreInfo.table,id,old);
        if (!restMessage.isSuccess()) {
            return restMessage;
        }
        List<String> list = storeInfo.getPics();
        if (CollectionUtils.isNotEmpty(list)) {//保存图集
            restMessage = imageInfoService.saveBatch(StoreInfo.table, id, list);
            if (!restMessage.isSuccess()) {
                throw new CustomException("保存图集失败");
            }
        }
        restMessage.setId(storeInfo.getId());
        return restMessage;


    }

    @Override
    public Pagination queryByPage(Pagination pagination, StoreInfo storeInfo) {
        StringBuffer sql = new StringBuffer("select * from "+StoreInfo.table);
        sql.append(" where is_deleted='"+ CommonEnum.YesOrNo.NO.getCode()+"'");
        //店铺名称
        if(StringUtils.isNotEmpty(storeInfo.getName())){
            sql.append(" and name like '%"+storeInfo.getName()+"%'");
        }
        //是否发布
        if(StringUtils.isNotEmpty(storeInfo.getIsRelease())){
            sql.append(" and is_release ='"+storeInfo.getIsRelease()+"'");
        }
         return  run.sql(StoreInfo.class,sql.toString(),pagination.getCurrent_page(),pagination.getPage_size());
    }

    @Override
    public List<StoreInfo> queryList() {
        StringBuffer sql = new StringBuffer("select id,name from "+StoreInfo.table);
        sql.append(" where is_deleted='"+ CommonEnum.YesOrNo.NO.getCode()+"'");
        Pagination pagination =   run.sql(StoreInfo.class,sql.toString(),1,1000);
        return  pagination.getData();
    }

    @Override
    public synchronized RestMessage updateHot(String id) {
        StoreInfo old =queryById(id);
        if(old == null){
            throw  new CustomException("未发现查找对象!");
        }

        if(CommonEnum.YesOrNo.YES.getCode().equalsIgnoreCase(old.getIsHot())){
            throw  new CustomException("已经热门商铺，无需重复设置!");
        }
        Map<String,Object> map  =new HashMap<String,Object>();
        map.put("is_hot", CommonEnum.YesOrNo.YES.getCode());
        Pagination pagination =  run.queryListByExample(StoreInfo.class,StoreInfo.table,map);
        if(CollectionUtils.isNotEmpty(pagination.getData())){
            List<StoreInfo> list = pagination.getData();
            for(StoreInfo storeInfo:list){
                storeInfo.setIsHot(CommonEnum.YesOrNo.NO.getCode());
                RestMessage restMessage = run.update(StoreInfo.table,storeInfo.getId(),storeInfo);
                if(! restMessage.isSuccess()){
                    throw  new CustomException("更新其它热门商家成非热门商家错误!"+restMessage.getErrorMessage());
                }
            }

        }
        old.setIsHot(CommonEnum.YesOrNo.YES.getCode());
        return  run.update(StoreInfo.table,id,old);



    }
}
