package com.drore.cloud.controller;

import com.drore.cloud.constant.ConstantEnum;
import com.drore.cloud.constant.LocalConstant;
import com.drore.cloud.exception.MacroApiException;
import com.drore.cloud.model.MemberInfo;
import com.drore.cloud.model.OrderDetail;
import com.drore.cloud.model.OrderInfo;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.ThreadLocalHolder;
import com.drore.cloud.sdk.common.annotation.Login;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.common.util.DateUtil;
import com.drore.cloud.sdk.common.util.GsonUtil;
import com.drore.cloud.sdk.common.util.RandomKit;
import com.drore.cloud.service.OrderService;
import com.drore.cloud.util.CouponUtil;
import com.drore.cloud.vo.OrderVo;
import com.drore.cloud.vo.TotalVo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 仁杰 on 2017/9/6
 */
@Api(value = "订单模块")
@RestController
@RequestMapping("/wechat/order")
public class OrderController {
    @Autowired
    private CloudQueryRunner run;

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "获取总价信息",notes = "获取总价信息")
    @PostMapping("/get_total")
    public RestMessage getTotal(@Valid@ModelAttribute TotalVo vo){
        RestMessage rm=new RestMessage();
        //遍历商品信息
        BigDecimal total = new BigDecimal("0.0");
        JsonArray commodityList = new JsonParser().parse(vo.getCommodity_info()).getAsJsonArray();
        for (JsonElement commodityEle : commodityList){
            JsonObject commodityInfo = commodityEle.getAsJsonObject();
            String commodity_id = GsonUtil.toStringValue(commodityInfo.get("commodity_id"));    //商品id
            String commodity_num = GsonUtil.toStringValue(commodityInfo.get("commodity_num"));  //商品数量
            if(StringUtils.isNotBlank(commodity_id) && StringUtils.isNotBlank(commodity_num)){
                //计算商品打折前总价
                Map<String,Object> commodity = run.queryOne("commodity_info",commodity_id);
                if(commodity == null || commodity.size()==0){
                    throw new MacroApiException("无法获取商品信息，请检查商品id");
                }
                BigDecimal de_price = new BigDecimal(Objects.toString(commodity.get("price")));
                total = total.add(de_price.multiply(new BigDecimal(commodity_num)));
            }else{
                rm.setSuccess(false);
                rm.setMessage("参数异常,商品ID和数量不能为空");
                return rm;
            }
        }


        Map<String,Double> result = new HashMap<>();

        if(StringUtils.isNotBlank(vo.getOffer_voucher_id())){
            //获取优惠信息
            Map<String, Object> offer_voucher = run.queryOne("offer_voucher", vo.getOffer_voucher_id());
            if(offer_voucher.size()==0){
                throw new MacroApiException("无法获取优惠券信息，请检查优惠券id");
            }
            //计算打折信息
            result = CouponUtil.getAfterCouponPrice(offer_voucher, total);
        }else{
            //没有优惠信息,原价出售
            result.put("coupon_price",new BigDecimal("0.0").doubleValue());
            result.put("after_price",total.doubleValue());
            result.put("total_price",total.doubleValue());
        }
        rm.setData(result);
        return rm;
    }



    //订单填写界面
    @Login
    @ApiOperation(value = "创建订单",notes = "创建订单")
    @PostMapping("/create")
    public RestMessage create(@Valid@ModelAttribute OrderVo vo){
        RestMessage rm = new RestMessage();
        OrderInfo orderInfo = new OrderInfo();
        //到时候session里面取
        //获取用户信息
        MemberInfo memberInfo = (MemberInfo) ThreadLocalHolder.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER);
        orderInfo.setMemberId(memberInfo.getId());

        //收货地址信息(member_address)
        if(StringUtils.isNotBlank(vo.getMember_address_id())){
            Map<String, Object> member_address = run.queryOne("member_address", vo.getMember_address_id());
            if(member_address.size()>0){
                //收货人
                orderInfo.setReceiptPeople(Objects.toString(member_address.get("receiver")));
                //收货人电话
                orderInfo.setReceiptPhone(Objects.toString(member_address.get("phone")));
                //收货地址
                String receipt_add = Objects.toString(member_address.get("receipt_add"))+Objects.toString(member_address.get("address"));
                orderInfo.setReceiptAdd(Objects.toString(receipt_add));
            }
        }

        //多个商品


        Map<String, Object> commodity_info = run.queryOne("commodity_info", vo.getCommodity_id());
        BigDecimal price = new BigDecimal(Objects.toString(commodity_info.get("price")));
        BigDecimal total = price.multiply(new BigDecimal(vo.getBuy_num()));

        //商店信息
        Map<String, Object> store_info = run.queryOne("store_info",vo.getStore_id());
        //订单号
        orderInfo.setOrderNo(RandomKit.genOrderNo(StringUtils.upperCase("P")));
        //商家id
        orderInfo.setStoreId(Objects.toString(store_info.get("id")));
        orderInfo.setStoreName(Objects.toString(store_info.get("name")));
        //订单状态
        orderInfo.setOrderStatus(ConstantEnum.OrderEnum.NO_PAY.getValue());
        //收货方式，自提还是快递
        orderInfo.setReceiptWay(vo.getReceipt_way());
        orderInfo.setIsEvaluation("N");

        if(StringUtils.isNotBlank(vo.getOffer_voucher_id())){
            //店铺优惠信息(查询优惠卷表——offer_voucher)
            Map<String, Object> offer_voucher = run.queryOne("offer_voucher", vo.getOffer_voucher_id());
            if(offer_voucher.size()>0){
                //优惠类型
                orderInfo.setOfferVoucherType(Objects.toString(offer_voucher.get("type")));
                orderInfo.setOfferVoucherId(vo.getOffer_voucher_id());
                orderInfo.setOfferVoucherName(Objects.toString(offer_voucher.get("offer_name")));
                Map<String, Double> afterCouponPrice = CouponUtil.getAfterCouponPrice(offer_voucher, total);
                orderInfo.setTotal(afterCouponPrice.get("total_price"));
                orderInfo.setOfferVoucherPrice(afterCouponPrice.get("coupon_price"));
                orderInfo.setDisbursements(afterCouponPrice.get("after_price"));
            }
        } else{
            //无优惠卷,原价购买
            orderInfo.setTotal(total.doubleValue());
            orderInfo.setOfferVoucherPrice(new BigDecimal("0.0").doubleValue());
            orderInfo.setDisbursements(total.doubleValue());
        }

        orderInfo.setOrderTime(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));

        RestMessage order_info = run.insert("order_info", orderInfo);
        //目前只支持单个商品购买，加入购物车批量后 再说
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(order_info.getId());
        orderDetail.setCommodityPic(Objects.toString(commodity_info.get("theme_pic")));
        orderDetail.setCommodityId(vo.getCommodity_id());
        orderDetail.setCommodityName(Objects.toString(commodity_info.get("name")));
        orderDetail.setCommodityAmout(vo.getBuy_num());
        orderDetail.setCommodityPrice(new BigDecimal(Objects.toString(commodity_info.get("price"))).doubleValue());
        rm=run.insert("order_detail",orderDetail);
        if(rm.isSuccess()){
            Map<String,Object> data = new HashMap<>();
            data.put("order_id",orderDetail.getOrderId());
            rm.setData(data);
            rm.setMessage("下单成功");
        }
        //订单下成功后去redis 把相应的购物车信息清空
        //等待做

        return rm;
    }


    @ApiOperation(value = "我的订单-王璐",notes = "我的订单")
    @PostMapping("/getOrderList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_size",value = "每页条数",dataType = "int",required = true),
            @ApiImplicitParam(name = "current_page",value = "当前页",dataType = "int",required = true),
            @ApiImplicitParam(name = "condition",value = "All=全部,NoPay=代付款,HasPay=待发货,NoReceive=待收货",dataType = "string",required = true)
    })
    public RestMessage getOrderList(String condition,@RequestParam(value = "page_size", defaultValue = "10") Integer page_size,
                           @RequestParam(value = "current_page", defaultValue = "1") Integer current_page){
        RestMessage restMessage = orderService.getOrderList(condition, page_size, current_page);
        return restMessage;
    }


    @ApiOperation(value = "订单详情-王璐",notes = "订单详情")
    @PostMapping("/getOrderDetail")
    @ApiImplicitParam(name = "orderId",value = "订单id",dataType = "string",required = true)
    public RestMessage getOrderDetail(String orderId){
        RestMessage restMessage = new RestMessage();
        if (StringUtils.isEmpty(orderId)){
            restMessage.setSuccess(false);
            restMessage.setMessage("orderId不能为空");
            return restMessage;
        }
        restMessage = orderService.getOrderDetail(orderId);
        return restMessage;
    }


    @ApiOperation(value = "取消订单-王璐",notes = "取消订单")
    @PostMapping("/delete")
    @ApiImplicitParam(name = "orderId",value = "订单id",dataType = "string",required = true)
    public RestMessage hasCancel(String orderId){
        RestMessage restMessage = new RestMessage();
        if (StringUtils.isEmpty(orderId)){
            restMessage.setSuccess(false);
            restMessage.setMessage("orderId不能为空");
            return restMessage;
        }
        restMessage = orderService.hasCancel(orderId);
        return restMessage;
    }

    @ApiOperation(value = "收货-王璐",notes = "收货")
    @PostMapping("/receive")
    @ApiImplicitParam(name = "orderId",value = "订单id",dataType = "string",required = true)
    public RestMessage receive(String orderId){
        RestMessage restMessage = new RestMessage();
        if (StringUtils.isEmpty(orderId)){
            restMessage.setSuccess(false);
            restMessage.setMessage("orderId不能为空");
            return restMessage;
        }
        restMessage = orderService.receive(orderId);
        return restMessage;
    }
}
