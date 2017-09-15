package com.drore.cloud.util;


import com.drore.cloud.constant.ConstantEnum;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/***
 * 浙江卓锐科技股份有限公司 版权所有@Copyright 2016
 * 说明
 * 优惠券工具
 * @since:cloud-ims 1.0
 * @author <a href="mailto:baoec@drore.com">baoec@drore.com </a> 
 * 2017/09/07 13:30
 */

public class CouponUtil {

    //根据优惠信息 进行折扣
    public static Map<String,Double> getAfterCouponPrice(Map offer_voucher, BigDecimal total){
        Map<String,Double> map = Maps.newHashMap();

        BigDecimal after_price=new BigDecimal(0);
        BigDecimal coupon_price=new BigDecimal(0);
        String type = Objects.toString(offer_voucher.get("type"));
        if(type.equals(ConstantEnum.CouponEnum.discount.getValue())){
            //直接折扣
            BigDecimal discount = new BigDecimal(Objects.toString(offer_voucher.get("discount"))).multiply(new BigDecimal("0.1"));
            after_price = total.multiply(discount);
            coupon_price=total.subtract(after_price);

        }else if(type.equals(ConstantEnum.CouponEnum.full_cut.getValue())){
            //满减
            BigDecimal full = new BigDecimal(Objects.toString(offer_voucher.get("full")));
            if(total.subtract(full).doubleValue()>=0){
                BigDecimal less = new BigDecimal(Objects.toString(offer_voucher.get("less")));
                after_price = total.subtract(less);
                coupon_price=less;
            }else {
                after_price=total;
            }
        }else if(type.equals(ConstantEnum.CouponEnum.full_discount.getValue())){
            //满折
            BigDecimal full = new BigDecimal(Objects.toString(offer_voucher.get("full")));
            if(total.subtract(full).doubleValue()>=0){
                BigDecimal discount = new BigDecimal(Objects.toString(offer_voucher.get("discount"))).multiply(new BigDecimal("0.1"));
                after_price = total.multiply(discount);
                coupon_price=total.subtract(after_price);
            }else {
                after_price=total;
            }
        }
        map.put("coupon_price",coupon_price.doubleValue());
        map.put("after_price",after_price.doubleValue());
        map.put("total_price",total.doubleValue());
        return map;
    }
}
