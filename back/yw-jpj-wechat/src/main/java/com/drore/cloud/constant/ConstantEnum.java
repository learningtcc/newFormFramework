/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */
package com.drore.cloud.constant;
/***
 * ConstantEnum
 * @since:cloud-service 1.0 
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2016年7月11日 下午3:34:30
 */
public class ConstantEnum {
	/***
	 * 用户注册 ，保存的手机发送验证码，或者邮箱注册链接验证码
	 */
	public static final String Member_Activity_Status="MEMBER_ACTIVITY_STATUS";
	/***
	 * 注册登录前
	 */
	public static final String Before_Member_login="Before_Member_login";

	/***
	 * 优惠类型
	 * @author 鲍恩撑
	 *
	 */
	public enum CouponEnum{

		discount("discount","打折"),full_cut("full_cut","满减"),full_discount("full_discount","满折");

		private String value;
		private String name;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		private CouponEnum(String value, String name) {
			this.value = value;
			this.name = name;
		}
	}


	/***
	 * 收藏类型
	 * @author 仁杰
	 *
	 */
	public enum CollectionEnum{

		STORE("Store","店铺"),COMMODITY("Commodity","商品");

		private String value;
		private String name;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		private CollectionEnum(String value, String name) {
			this.value = value;
			this.name = name;
		}
	}

	/***
	 * 终端来源
	 * @author xiaoymin
	 *
	 */
	public enum TerminalEnum{
		
		PC("Pc","pc网站"),WX("Wx","微信"),Wap("Wap","wap网站"),App("App","App客户端");
		
		private String value;
		private String name;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		private TerminalEnum(String value, String name) {
			this.value = value;
			this.name = name;
		}
	}
	
	

	/***
	 * 订单支付状态
	 * @author xiaoymin
	 *
	 */
	public enum OrderEnum {
		NO_PAY("NoPay","待付款"),
		HasPay("HasPay","已付款"),
        NoUse("NoUse","未使用"),
        HasUse("HasUse","已使用"),
		HasCancel("HasCancel","已取消"),
		NO_SEND("NoSend","待发货"),
		NO_RECEIVE("NoReceive","待收货"),
		REVEIVED("Received","已收货"),
		REFUND("Refund","申请退款中"),
		REFUNDED("Refunded","已退款"),
		RETURNNIG("Returning","退货中"),
		RETURNED("Returned","已退款已退货"),
		CANCEL("Cancel","订单过期"),
		Fineshed("Finished","交易完成");
		
		private String value;
		private String name;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		private OrderEnum(String value, String name) {
			this.value = value;
			this.name = name;
		}
	}
	/***
	 * 支付状态
	 * @author xiaoymin
	 *
	 */
	public enum PayEnum {
		HasPay("HasPay","已支付"),
		NoPay("NoPay","未支付");
		
		private String value;
		private String name;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		private PayEnum(String value, String name) {
			this.value = value;
			this.name = name;
		}
	}
	/***
	 * 是否
	 * @author xiaoymin
	 *
	 */
	public enum ToggleEnum{
		Y,N;
	}
	
	/***
	 * 支付方式
	 * @author xiaoymin
	 *
	 */
	public enum PayMethodEnum {
		Alipay("alipay","支付宝支付"),
		Wxpay("wxpay","微信支付"),
		Wxpay_QR_CODE("Wxpay_QR_CODE","微信二维码支付");
		private String value;
		private String name;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		private PayMethodEnum(String value, String name) {
			this.value = value;
			this.name = name;
		}
	}
	/****
	 * 订单产品类型
	 * @author xiaoymin
	 *
	 */
	public enum ProductEnum {

		hotel("hotel","酒店"),ticket("ticket","门票"),catering("catering","餐饮"),specialty("specialty","特产"),packages("packages","套餐"),recreation("recreation","娱乐");
		
		private String value;
		private String name;
		private ProductEnum(String value, String name) {
			this.value = value;
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		
		
	}
	
	/***
	 * 收货方式
	 * @author xiaoymin
	 *
	 */
	public enum ReceiptModeEnum{
		HomeDelivery("HomeDelivery","送货上门"),DoorSelf("DoorSelf","上门自取");
		private String value;
		private String name;
		private ReceiptModeEnum(String value, String name) {
			this.value = value;
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
	
	
	/****
	 * 物流状态
	 * @author xiaoymin
	 *
	 */
	public enum LogisticsEnum{
		NoSend("NoSend","待发货"),OnTheWay("OnTheWay","在途中"),SignFor("SignFor","已签收"),Problem("Problem","问题件");
		
		private String value;
		private String name;
		private LogisticsEnum(String value, String name) {
			this.value = value;
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
}
