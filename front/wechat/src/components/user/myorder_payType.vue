
<template>
    <div class="wrapper">
        <div class="pay_hd">支付方式</div>
        <div class="p_cont">
            <div class="p_top">请转账到商家支付宝账号，二维码如下</div>
            <div class="qrcode" id="qr_img">
                <!-- <img src="../../assets/img/fillOrder/qrcode.jpg" alt="" > -->
            </div>
            <div class="shop_name">{{detail.name}}</div>
            <p class="tips">注：转账需要标注，义乌精品街区公众号及订单编号，方便商家进行查询核算</p>
        </div>
        <!-- <div class="btn">
            <a href="javascript:;" @click="sumitPay">已转账</a>
        </div> -->
    </div>
</template>


<script> 
    export default{
        components:{

        },
        data(){
            return{
               detail:{},
               id:this.$route.query.storeId
            }
        },
        methods: {
           sumitPay(){
                weui.dialog({
                    title: '确认已转账给商家？',
                    content: '如确认商家会在1-2个工作日内确认，并发货。',
                    buttons: [{
                        label: '取消',
                        type: 'default',
                        onClick: () => {}
                    }, {
                        label: '确定',
                        type: 'primary',
                        onClick: () => {
                            
                        }
                    }]
                });
           }
        },
        mounted(){
            var loading = weui.loading('请稍等', {//loading
            });
            ywData.detail({'resource_name':'store_info',id:this.id,typeAll:{}},(data)=>{
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
    @import "../../assets/css/myorder_payType.less";
</style>
<style lang="less">
  .qr_img img,.qr_img canvas{
    width: 3.5484rem!important;
    height: 3.5484rem!important;
    display: inline-block!important;
    border:0.1613rem solid #fff;

  }
</style>
