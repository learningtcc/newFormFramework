package com.drore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.common.util.DateUtil;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.enums.CommonEnum;
import com.drore.model.OfferVoucher;
import com.drore.service.OfferVoucherService;
import com.drore.util.LoginStoreUserUtil;
import com.drore.util.PageUtil;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/**
 * Created by zhangh on 2017/9/4 0001.
 */

@Service
public class OfferVoucherServiceImpl implements OfferVoucherService {

    @Autowired
    private CloudQueryRunner run;

    @Override
    public PageUtil getOfferVoucherList(OfferVoucher voucher, Integer page_size, Integer current_page) {

        //动态拼接sql语句
        StringBuffer sql = new StringBuffer("select * from offer_voucher where 1=1");
        sql.append(" and is_deleted = 'N'");
        if(StringUtils.isNotEmpty(voucher.getStoreName())){
            sql.append(" and store_name like '%" + voucher.getStoreName() + "%'");
        }
        if(StringUtils.isNotEmpty(voucher.getType())){
            sql.append(" and type = '" + voucher.getType() + "'");
        }
        if(StringUtils.isNotEmpty(voucher.getOfferStatus())){
            sql.append(" and offer_status = '" + voucher.getOfferStatus() + "'");
        }
        if(StringUtils.isNotEmpty(voucher.getStartTime())){
            sql.append(" and publish_time >= '"+ voucher.getStartTime() +"'");
        }
        if(StringUtils.isNotEmpty(voucher.getEndTime())){
            sql.append(" and publish_time <= '"+ voucher.getEndTime() +"'");
        }
        sql.append(" order by create_time desc");
        Pagination<OfferVoucher> voucherPagination = run.sql(OfferVoucher.class, sql.toString(), current_page, page_size);
        PageUtil pageUtil = new PageUtil(voucherPagination);
        return pageUtil;
    }

    @Override
    public RestMessage queryOfferVoucher(String id) {
        RestMessage restMessage = new RestMessage();
        OfferVoucher offerVoucher = run.queryOne(OfferVoucher.class, OfferVoucher.table, id);
        restMessage.setData(offerVoucher);
        return restMessage;
    }

    @Override
    public RestMessage judgeOfferVoucherState() {
        RestMessage rm=null;
        try {
            //每次处理3000条
            RequestExample requestExample=new RequestExample(3000,1);
            //模糊查询
            //创建Criteria
            RequestExample.Criteria rc = requestExample.create();
            RequestExample.Param pa = requestExample.createParam();
            pa.addTerm("offer_status","HasEnd");
            rc.getMustNot().add(pa);

            Pagination<OfferVoucher> map = run.queryListByExample(OfferVoucher.class,OfferVoucher.table,requestExample);
            if (map!=null&&map.getCount()>0) {
                List<OfferVoucher> offerVouchers  = map.getData();
                //判断订单是否失效
                String now = DateUtil.format("yyyy-MM-dd HH:mm:ss");

                for (OfferVoucher offerVoucher : offerVouchers) {
                    //是否已经开始
                    if (DateUtil.compareTime(Objects.toString(offerVoucher.getStartTime()),now) < 0) {
                        //进行中
                        offerVoucher.setOfferStatus("InHand");
                        offerVoucher.setSurplusTime(get_service_time(offerVoucher.getEndTime()));
                        run.update(OfferVoucher.table,offerVoucher.getId(),offerVoucher);
                    }
                    //是否结束
                    if (DateUtil.compareTime(Objects.toString(offerVoucher.getEndTime()),now) < 0) {
                        //已结束
                        offerVoucher.setOfferStatus("HasEnd");
                        run.update(OfferVoucher.table,offerVoucher.getId(),offerVoucher);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rm;
    }

    @Override
    public List<JSONObject> getCouponType() {
        CommonEnum.CouponEnum [] couponEnums = CommonEnum.CouponEnum.values();
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (CommonEnum.CouponEnum ord : couponEnums) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", ord.getValue());
            jsonObject.put("name", ord.getName());
            list.add(jsonObject);
        }
        return  list;
    }

    @Override
    public List<JSONObject> getCouponState() {
        CommonEnum.ActiveState [] activeState = CommonEnum.ActiveState.values();
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (CommonEnum.ActiveState ord : activeState) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", ord.getValue());
            jsonObject.put("name", ord.getName());
            list.add(jsonObject);
        }
        return  list;
    }

    /**
     *计算剩余服务时间
     * @param endTime
     * @return
     */
    public String get_service_time(String endTime){
        String result="";
        try {
            Date end_time = DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss");
            Date now_time = new Date(System.currentTimeMillis());
            long diff =  end_time.getTime()- now_time.getTime();
            long  days= diff / (1000 * 60 * 60 * 24);
            long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
            System.out.println(""+days+"天"+hours+"小时");
            if(days!=0){
                result=result+""+days+"天";
            }
            if (hours!=0){
                result=result+""+hours+"小时";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public RestMessage addOrUpdate(OfferVoucher offerVoucher){
        RestMessage restMessage;
        String id = offerVoucher.getId();
        if (null != id && !"".equals(id)){
            if (CommonEnum.CouponEnum.full_discount.getValue().equals(offerVoucher.getType())){
                offerVoucher.setOfferName("满"+offerVoucher.getFull()+"元"+offerVoucher.getDiscount()+"折优惠");
            }else if (CommonEnum.CouponEnum.full_cut.getValue().equals(offerVoucher.getType())){
                offerVoucher.setOfferName("满"+offerVoucher.getFull()+"元减"+offerVoucher.getLess()+"元");
            }else {
                offerVoucher.setOfferName("全场"+offerVoucher.getDiscount()+"折");
            }
            restMessage = run.update(OfferVoucher.table, id, offerVoucher);
            if (restMessage.isSuccess()){
                restMessage.setMessage("更新成功");
            }
        }else {
            if (CommonEnum.CouponEnum.full_discount.getValue().equals(offerVoucher.getType())){
                offerVoucher.setOfferName("满"+offerVoucher.getFull()+"元"+offerVoucher.getDiscount()+"折优惠");
            }else if (CommonEnum.CouponEnum.full_cut.getValue().equals(offerVoucher.getType())){
                offerVoucher.setOfferName("满"+offerVoucher.getFull()+"元减"+offerVoucher.getLess()+"元");
            }else {
                offerVoucher.setOfferName("全场"+offerVoucher.getDiscount()+"折");
            }
            offerVoucher.setIsEnable("Y");
            offerVoucher.setStoreId(LoginStoreUserUtil.getStoreId());
            //测试商户名称
            offerVoucher.setStoreName("小二上茶");
            offerVoucher.setOfferStatus(CommonEnum.ActiveState.NotBegin.getValue());
            offerVoucher.setPublishTime(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            restMessage = run.insert(OfferVoucher.table, offerVoucher);
            if (restMessage.isSuccess()){
                restMessage.setMessage("添加成功");
            }
        }
        return restMessage;
    }

    public RestMessage over(String id){
        RestMessage restMessage = run.update(OfferVoucher.table, id, ImmutableMap.of("offer_status", CommonEnum.ActiveState.HasEnd.getValue()));
        return restMessage;
    }
}