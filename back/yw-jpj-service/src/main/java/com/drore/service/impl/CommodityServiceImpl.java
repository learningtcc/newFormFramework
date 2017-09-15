package com.drore.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.domain.DataDictionary;
import com.drore.enums.CommonEnum;
import com.drore.exception.CustomException;
import com.drore.model.CommodityInfo;
import com.drore.model.StoreInfo;
import com.drore.model.TeaSpecies;
import com.drore.service.*;
import com.drore.util.LoginStoreUserUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 浙江卓锐科技股份有限公司 版权所有 ? Copyright 2017<br/>
 * 说明: 商品信息，商品是不需要进行审核的，直接上下架即可，但是存在一种特殊情况，景区可以对全部商品进行上下架处理<br/>
 * 项目名称:  <br/>
 * 创建日期: 2017/9/4 8:58<br/>
 * 作者: wdz
 */

@Service
public class CommodityServiceImpl implements CommodityService {
    @Autowired
    CloudQueryRunner run;
    @Autowired
    ImageInfoService imageInfoService;

    @Autowired
    ClicksInfoService clicksInfoService;


    @Autowired
    DataDictionaryService dataDictionaryService;

    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    StoreService storeService;

    @Autowired
    TeaSpeciesService teaSpeciesService;

    @Override
    public Pagination queryByPage(Pagination pagination, CommodityInfo commodityInfo) {
        StringBuffer sql = new StringBuffer(" select * from " + CommodityInfo.table);
        sql.append(" where is_deleted='" + CommonEnum.YesOrNo.NO.getCode() + "'");
        if (StringUtils.isNotEmpty(commodityInfo.getStoreId())) {
            sql.append(" and store_id ='" + commodityInfo.getStoreId() + "'");
        }

        if (StringUtils.isNotEmpty(commodityInfo.getName())) {
            sql.append(" and name like '%" + commodityInfo.getName() + "%'");
        }

        if (StringUtils.isNotEmpty(commodityInfo.getTypeId())) {
            sql.append(" and type_id ='" + commodityInfo.getTypeId() + "'");
        }


        pagination = run.sql(CommodityInfo.class, sql.toString(), pagination.getCurrent_page(), pagination.getPage_size());
        if (pagination.getData() != null) {
            List<CommodityInfo> list = pagination.getData();
            for (CommodityInfo temp : list) {
                if (StringUtils.isNotEmpty(temp.getPriceId())) {
                    DataDictionary data = dataDictionaryService.findById(temp.getPriceId());
                    if (data != null) {
                        temp.setPriceIdName(data.getName());
                    }
                }

                if (StringUtils.isNotEmpty(temp.getTypeId())) {
                    DataDictionary data = dataDictionaryService.findById(temp.getTypeId());
                    if (data != null) {
                        temp.setTypeIdName(data.getName());
                    }
                }

                if (StringUtils.isNotEmpty(temp.getSpeciesId())) {
                    TeaSpecies teaSpecies = teaSpeciesService.queryById(temp.getSpeciesId());
                    if (teaSpecies != null) {
                        temp.setSpeciesIdName(teaSpecies.getName());
                    }
                }


                if (StringUtils.isNotEmpty(commodityInfo.getStoreId())) {
                    StoreInfo storeInfo = storeService.queryById(temp.getStoreId());
                    if (storeInfo != null) {
                        temp.setStoreIdName(storeInfo.getName());
                    }
                }
            }
        }

        return pagination;
    }

    @Override
    public boolean exitShelves(String shelvesId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("shelves_id", shelvesId);
        CommodityInfo commodityInfo = run.queryFirstByRName(CommodityInfo.class, CommodityInfo.table, map);
        return commodityInfo == null ? false : true;
    }

    @Override
    public RestMessage deleteById(String id) {
        boolean result = orderDetailService.exitCommodityId(id);
        if (result) {
            throw new CustomException("已被下单商品不能删除，只能下架");
        }
        return run.delete(CommodityInfo.table, id);
    }

    @Override
    public RestMessage save(CommodityInfo commodityInfo) {
        if (CommonEnum.YesOrNo.YES.getCode().equals(commodityInfo.getIsShelves())) {//如果是默认新增并上架的
            commodityInfo.setShelvesTime(new Date());//设置上架时间
            commodityInfo.setShelvesId(LoginStoreUserUtil.getUserId());//设置上架人
        }

        commodityInfo.setStoreId(LoginStoreUserUtil.getStoreId());//设置所属商家
        commodityInfo.setCreator(LoginStoreUserUtil.getUserId());
        commodityInfo.setClicks(0);//设置默认点击率
        RestMessage restMessage = run.insert(CommodityInfo.table, commodityInfo);
        if (!restMessage.isSuccess()) {
            return restMessage;
        }
        String id = restMessage.getId();
        List<String> list = commodityInfo.getPics();
        if (CollectionUtils.isNotEmpty(list)) {//保存图集
            restMessage = imageInfoService.saveBatch(CommodityInfo.table, id, list);
            if (!restMessage.isSuccess()) {
                throw new CustomException("保存图集失败");
            }
        }
        restMessage.setId(id);
        return restMessage;
    }

    @Override
    public RestMessage update(CommodityInfo commodityInfo) {
        String id = commodityInfo.getId();
        CommodityInfo old = queryById(id);
        if (old == null) {
            throw new CustomException("未发现查找对象!");
        }
        if (CommonEnum.YesOrNo.YES.getCode().equals(old.getIsShelves())) {
            throw new CustomException("只有下架商品才能进行编辑!");
        }


        if (CommonEnum.YesOrNo.YES.getCode().equals(commodityInfo.getIsShelves())) {//如果是默认修改保存并上架的
            old.setShelvesTime(new Date());//设置上架时间
            old.setShelvesId(LoginStoreUserUtil.getUserId());//设置上架人
        }

        old.setName(commodityInfo.getName());
        old.setTypeId(commodityInfo.getTypeId());
        old.setDetails(commodityInfo.getDetails());
        old.setIsFeatures(commodityInfo.getIsFeatures());
        old.setPrice(commodityInfo.getPrice());
        old.setPriceId(commodityInfo.getPriceId());
        old.setSpeciesId(commodityInfo.getSpeciesId());
        old.setStandard(commodityInfo.getStandard());
        old.setModifier(LoginStoreUserUtil.getUserId());

        RestMessage restMessage = run.update(CommodityInfo.table, id, old);
        if (!restMessage.isSuccess()) {
            return restMessage;
        }
        List<String> list = commodityInfo.getPics();
        if (CollectionUtils.isNotEmpty(list)) {//保存图集
            restMessage = imageInfoService.saveBatch(CommodityInfo.table, id, list);
            if (!restMessage.isSuccess()) {
                throw new CustomException("保存图集失败");
            }
        }
        return restMessage;
    }

    @Override
    public CommodityInfo queryById(String id) {
        return run.queryOne(CommodityInfo.class, CommodityInfo.table, id);
    }

    @Override
    public CommodityInfo queryDetailById(String id) {
        CommodityInfo commodityInfo = queryById(id);
        if (commodityInfo == null) {
            throw new CustomException("未发现查找对象!");
        }

        if (StringUtils.isNotEmpty(commodityInfo.getPriceId())) {
            DataDictionary data = dataDictionaryService.findById(commodityInfo.getPriceId());
            if (data != null) {
                commodityInfo.setPriceIdName(data.getName());
            }
        }

        if (StringUtils.isNotEmpty(commodityInfo.getTypeId())) {
            DataDictionary data = dataDictionaryService.findById(commodityInfo.getTypeId());
            if (data != null) {
                commodityInfo.setTypeIdName(data.getName());
            }
        }

        if (StringUtils.isNotEmpty(commodityInfo.getSpeciesId())) {
            TeaSpecies teaSpecies = teaSpeciesService.queryById(commodityInfo.getSpeciesId());
            if (teaSpecies != null) {
                commodityInfo.setSpeciesIdName(teaSpecies.getName());
            }
        }


        if (StringUtils.isNotEmpty(commodityInfo.getStoreId())) {
            StoreInfo storeInfo = storeService.queryById(commodityInfo.getStoreId());
            if (storeInfo != null) {
                commodityInfo.setStoreIdName(storeInfo.getName());
            }
        }

        commodityInfo.setPics(imageInfoService.findPics(CommodityInfo.table, id));
        return commodityInfo;
    }

    @Override
    public synchronized CommodityInfo queryDetailByIdApi(String id, String name, String memberId, String memberName) {
        CommodityInfo commodityInfo = queryDetailById(id);
        commodityInfo.setClicks(commodityInfo.getClicks() + 1);
        RestMessage restMessage = run.update(CommodityInfo.table, id, commodityInfo);
        if (!restMessage.isSuccess()) {
            throw new CustomException(restMessage.getMessage());
        }


        restMessage = clicksInfoService.save(id, CommodityInfo.table, name, CommonEnum.ClickSourceEnum.WEIXIN.getCode(), memberId, memberName);
        if (!restMessage.isSuccess()) {
            throw new CustomException("保存点击率信息报错" + restMessage.getMessage());
        }

        return commodityInfo;
    }

    @Override
    public RestMessage onOffShelves(String id, String shelvesId) {
        CommodityInfo commodityInfo = queryById(id);
        if (commodityInfo == null) {
            throw new CustomException("未发现查找对象!");
        }
        if (commodityInfo.getIsShelves().equals(CommonEnum.YesOrNo.YES.getCode())) {
            commodityInfo.setIsShelves(CommonEnum.YesOrNo.NO.getCode());
        } else {
            commodityInfo.setIsShelves(CommonEnum.YesOrNo.YES.getCode());
        }

        commodityInfo.setShelvesId(shelvesId);
        commodityInfo.setShelvesTime(new Date());
        RestMessage restMessage = run.update(CommodityInfo.table, id, commodityInfo);
        return restMessage;
    }

    @Override
    public List<TeaSpecies> queryListByTypeId(String typeId) {
        Map<String,Object> term = new HashMap<String,Object>();
        if(StringUtils.isNotEmpty(typeId)){
            term.put("type_id",typeId);
        }
        Pagination pagination =   run.queryListByExample(TeaSpecies.class,TeaSpecies.table,term,null,null,null,1,10000);
        return  pagination.getData();
    }


}
