
<template>
    <div class="wrapper">
        <div class="succ_ico"></div>
        <p class="tips">下单成功，请及时转账给商家转账方式可查看订单详情</p>
        <!-- 支付方式 -->
        <div class="pay_type">
          <div class="head">支付方式</div>
          <div class="cont">
            <span class="tips_h">请转账到商家支付宝账号，二维码如下</span>
            <div id="qr_img">
                <!-- <img src="../../../assets/img/fillOrder/qrcode.jpg" alt="" class="qrcode" /> -->
            </div>
            <p class="shop_name">{{detail.name}}</p>
            <p class="tips">注：转账需要标注，义乌精品街区公众号及订单编号，方便商家进行查询核算</p>
          </div>
        </div>
        <!-- /支付方式 -->
        <div class="btn_succ">
          <router-link :to="{path:'/handShop'}" >继续下单</router-link>
         <router-link :to="{path:'/myorder_detail',query:{id:id}}" >查看订单</router-link>
        </div>
    </div>
</template>


<script> 
    export default{
        data(){
            return{
               id:this.$route.query.id,
               storeId:this.$route.query.storeId,
               detail:{}
            }
        },
        methods: {
           
        },
        mounted(){
            var loading = weui.loading('请稍等', {//loading
            });
            ywData.detail({'resource_name':'store_info',id:this.storeId,typeAll:{}},(data)=>{
                loading.hide();//加载完
                this.detail = data.data;
                var qrText = this.detail.alipay_url;//二维码路径
                  jQuery('#qr_img').qrcode({
                   render: "canvas",
                   text: qrText,
                   width: "130",//二维码的宽度
                   height: "130",//二维码的高度
                   background: "#ffffff",//二维码的后景色
                   foreground: "#000000",//二维码的前景色
                   src: '' //二维码中间的图片
                 });
            });
        }
    }
</script>
<style lang="less" scoped>
    @import "../../../assets/css/orderSucc.less";
</style>
<style lang="less">
  .qr_img img,.qr_img canvas{
    width: 3.5484rem!important;
    height: 3.5484rem!important;
    display: inline-block!important;
    border:0.1613rem solid #fff;

  }
</style>