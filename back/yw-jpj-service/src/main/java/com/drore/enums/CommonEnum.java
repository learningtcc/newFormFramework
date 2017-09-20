package com.drore.enums;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明: 公共枚举 <br/>
 * 项目名称: drore-base <br/>
 * 创建日期: 2016年11月22日 上午10:59:31 <br/>
 * 作者: wdz
 */
public class CommonEnum {

    public enum  ClickSourceEnum {
        WEIXIN("微信", "weixin"),;
        private String name;
        private String code;

        private ClickSourceEnum(String name, String code) {
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return this.name;
        }

        public String getCode() {
            return code;
        }
    }



    /**
     * 广告栏目信息
     */
    public enum AdvertisingTypeEnum {
        STREET_CULTURE("街区文化", "street_culture_info"),
        STORE("商品", "commodity_info"), LEASE("招租信息", "lease_info"),
        THEME_ACTIVITIES("主题活动","theme_activities");
        private String name;
        private String table;

        private AdvertisingTypeEnum(String name, String table) {
            this.name = name;
            this.table = table;
        }

        public String getName() {
            return this.name;
        }

        public String getTable() {
            return this.table;
        }
    }

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

    public enum AuditStatus{

        PendingAudit("PendingAudit","待审核"),
        Audited("Audited","审核通过"),
        full_discount("AuditFail","审核未通过");

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
        private AuditStatus(String value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    public enum ThemeActivityType{

        Holidays("Holidays","节假日活动"),
        Culture("Culture","风俗文化"),
        Community("Community","社区治理");

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
        private ThemeActivityType(String value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    public enum ActiveState{

        NotBegin("NotBegin","未开始"),
        InHand("InHand","进行中"),
        HasEnd("HasEnd","已结束");

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
        private ActiveState(String value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    public enum ValidateResultEnum {
        SUCCESS("校验通过", 1), FAILED("校验失败", 0);
        private String msg;
        private int errorCode;

        private ValidateResultEnum(String msg, int errorCode) {
            this.msg = msg;
            this.errorCode = errorCode;
        }

        public String getMsg() {
            return this.msg;
        }

        public int getErrorCode() {
            return this.errorCode;
        }
    }

    public enum YesOrNo {
        YES("是", "Y"), NO("否", "N");
        // 成员变量
        private String code;
        private String name;

        private YesOrNo(String name, String code) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /***
     * 支付方式
     * @author xiaoymin
     *
     */
    public enum PayWayEnum {
        Alipay("alipay", "支付宝支付"), Wxpay("wxpay", "微信支付"), Cash("cash", "现金支付");
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

        private PayWayEnum(String value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    /***
     * 订单状态
     *
     */
    public enum OrderStatusEnum {
        HasCancel("HasCancel","已取消"),
        NO_PAY("NoPay","待付款"),
        HasPay("HasPay","已付款"),
        NO_RECEIVE("NoReceive","待收货"),
        REVEIVED("Received","已收货"),
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

        private OrderStatusEnum(String value, String name) {
            this.value = value;
            this.name = name;
        }
    }


    /**
     * 登录登出标记 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
     * 说明: <br/>
     * 项目名称: guided-assistant-service <br/>
     * 创建日期: 2016年8月19日 下午7:32:20 <br/>
     * 作者: wdz
     */
    public enum LoginInOut {
        IN("登录", 1), OUT("登出", 2);
        // 成员变量
        private int value;
        private String name;

        private LoginInOut(String name, int value) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }
    public enum MenuOrButton {
        MENU("菜单", 1), BUTTON("按钮", 2);
        // 成员变量
        private int code;
        private String name;

        private MenuOrButton(String name, int code) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }
}
