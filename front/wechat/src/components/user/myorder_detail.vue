
<template>
    <div class="wrapper">
        <!-- 地址 -->
        <div class="addr_box">
            <dl class="info">
               <dt>
                   <span class="name">收货人：{{orderInfo.receipt_people}}</span>
                   <span class="tel">{{orderInfo.receipt_phone}}</span>
               </dt>
               <dd class="addr">
                   {{orderInfo.receipt_add}}
               </dd>
            </dl>
        </div>
        <!-- /地址 -->
        <router-link :to="{path:'/myorder_payType',query:{storeId:orderInfo.store_id}}" class="receive_type">
          <span class="tit">支付方式</span>
          <span class="result" v-if="orderInfo.order_status">{{orderStatus[orderInfo.order_status].label}}<em></em></span>
        </router-link>
        <!-- 订单信息 -->
        <div class="order_info">
            <dl>
              <dt class="header">
                  <div class="left">
                    <i></i><router-link :to="{path:'/storeDetail',query:{id:orderInfo.store_id}}"><span class="text">{{orderInfo.store_name}}</span><span class="arrow"></span></router-link>
                  </div>
                  <div class="status" v-if="orderInfo.order_status">{{orderStatus[orderInfo.order_status].label}}</div>
              </dt>
            </dl>
            <router-link :to="{path:'/productDetail',query:{id:item.commodity_id}}" v-for="item in orderDetail" :key="item.commodity_id">
              <dl class="goods_info">
                <dt>
                  <img :src="item.commodity_pic" height="280" width="380" alt="">
                </dt>
                <dd>
                  <div class="tit">{{item.commodity_name}}</div>
                  <div class="price">
                    <span class="unit">￥{{item.commodity_price}}</span>
                    <em class="num">x{{item.commodity_amout}}</em>
                  </div>
                  <!-- 评价 -->
                  <!-- <div class="bt-btn" v-if="isEvaluateBtnShow()">
                    <a href="javascript:;" v-if="orderInfo.order_status" @click.stop.prevent="orderStatus[orderInfo.order_status].click()" class="btn">{{orderStatus[orderInfo.order_status].btnName}}</a>
                  </div> -->
                  <!-- /评价 -->
                </dd>
              </dl>
            </router-link>
            <div class="cont_list">
                <dl>
                  <dt>商品总价</dt>
                  <dd>￥{{orderInfo.total}}</dd>
                </dl>
                <dl>
                  <dt>运费(快递)</dt>
                  <dd>￥{{orderInfo.fee}}</dd>
                </dl>
                <dl>
                  <dt>店铺优惠</dt>
                  <dd>-￥{{orderInfo.offer_voucher_price}}</dd>
                </dl>
                <div class="total_price">
                  <span class="title">实付款</span>
                  <span class="text">￥{{orderInfo.disbursements}}</span>
                </div>
            </div>
        </div>
        <!-- /订单信息 -->
        <!-- 物流信息 -->
        <router-link :to="{path:'/myorder_logistics',query:{id:id}}" class="receive_type" v-if="islogisticsShow()">
          <span class="tit">物流信息</span>
          <span class="con" v-if="orderInfo.order_status">{{orderStatus[orderInfo.order_status].label}}<em></em></span>
        </router-link>
        <!-- /物流信息 -->
        <div class="order_about">
          <p>订单编号：{{orderInfo.order_no}}</p>
          <p>创建时间：{{orderInfo.create_time}}</p>
          <p v-if="orderInfo.pay_time">支付时间：{{orderInfo.pay_time}}</p>
        </div>
        <div class="fixed_bottom" v-if="isBtnshow()">
          <div class="order_btn" v-if="orderInfo.order_status" @click="orderStatus[orderInfo.order_status].click()">{{orderStatus[orderInfo.order_status].btnName}}</div>
        </div>
        <div class="fixed_bottom" v-if="isEvaluateBtnShow()">
          <div class="order_btn" v-if="orderInfo.order_status" @click="orderStatus[orderInfo.order_status].click()">{{orderStatus[orderInfo.order_status].btnName}}</div>
        </div>
    </div>
</template>


<script> 
    var querystring = require('querystring');
    export default{
        components:{

        },
        data(){
            return{
              id:this.$route.query.id,
              orderInfo:{},
              orderDetail:[],
              orderStatus:{
                'NoPay':{label:'待付款', click:this.clickNoPay, btnName:'取消订单'},
                'HasPay':{label:'待发货', click:this.clickHasPay,btnName:'待发货'},
                'NoUse':{label:'未使用', click:this.clickHasPay,btnName:'再来一单'},
                'HasUse':{label:'已使用', click:this.clickHasPay,btnName:'再来一单'},
                'HasCancel':{label:'已取消', click:this.clickHasPay,btnName:'已取消'},
                'NoSend':{label:'待发货', click:this.clickHasPay,btnName:'再来一单'},
                'NoReceive':{label:'待收货', click:this.clickReceived,btnName:'确认收货'},
                'Received':{label:'已收货', click:this.clickEvaluate,btnName:'评价'},
                'Refund':{label:'申请退款中', click:this.clickRefund,btnName:'申请退款中'},
                'Refunded':{label:'已退款', click:this.clickHasPay,btnName:'再来一单'},
                'Returning':{label:'退货中', click:this.clickHasPay,btnName:'再来一单'},
                'Returned':{label:'已退款已退货', click:this.clickHasPay,btnName:'再来一单'},
                'Cancel':{label:'订单过期', click:this.clickHasPay,btnName:'订单过期'},
                'Finished':{label:'交易完成', click:this.clickEvaluate,btnName:'评价'},
              },
            }
        },
        methods: {
          getMyOrderDetail(){
            var loading = weui.loading('加载中', {//loading
            });
            this.axios.post('/wechat/order/getOrderDetail', querystring.stringify({//详情
                'orderId':this.id
            }))
            .then(res => {
                loading.hide();//加载完
                if(res.data.success){
                  var data = res.data.data;
                  this.orderInfo = data.order_info;
                  this.orderDetail = data.order_detail.data;
                }
            })
          },
          islogisticsShow(){//物流信息显示
            if(this.orderInfo.order_status){
              if(this.orderInfo.order_status == 'NoReceive' || this.orderInfo.order_status == 'Received' || this.orderInfo.order_status == 'Finished'){
                return true;
              } else {
                return false;
              }
            } else {
              return;
            }
          },
          isBtnshow(){//是否显示按钮
            if(this.orderInfo.order_status){
              if(this.orderInfo.order_status == 'NoPay' || this.orderInfo.order_status == 'NoReceive'){
                return true;
              } else {
                return false;
              }
            } else {
              return;
            }
          },
          isEvaluateBtnShow(){//是否显示评价按钮
            if(this.orderInfo.order_status){
              if(this.orderInfo.order_status == 'Received' || this.orderInfo.order_status == 'Finished'){
                  return this.orderInfo.is_evaluation == 'N';
              } else {
                return false;
              }
            } else {
              return;
            }
          },
          clickNoPay(){//未付款时取消订单
            weui.confirm('是否取消订单', {
                buttons: [{
                    label: '取消',
                    type: 'default',
                    onClick: () => {}
                }, {
                    label: '确定',
                    type: 'primary',
                    onClick: () => {
                      var loading = weui.loading('加载中', {});
                      this.axios.post('/wechat/order/delete', querystring.stringify({//取消订单
                          'orderId':this.id
                      }))
                      .then(res => {
                          loading.hide();//加载完
                          if(res.data.success){
                            weui.toast(res.data.message, {//提示  
                              duration: 1000,
                              callback: ()=>{
                                window.location.reload();
                              }
                            });
                          } else {
                            weui.topTips(res.data.message,1000);//提示出错
                          }
                          
                      })
                    }
                }]
            });
            
          },
          clickReceived(){//待收货时收货
            weui.confirm('是否确认收货', {
                buttons: [{
                    label: '取消',
                    type: 'default',
                    onClick: () => {}
                }, {
                    label: '确定',
                    type: 'primary',
                    onClick: () => {
                      var loading = weui.loading('加载中', {});
                      this.axios.post('/wechat/order/receive', querystring.stringify({//收货
                          'orderId':this.id
                      }))
                      .then(res => {
                          loading.hide();//加载完
                          if(res.data.success){
                            weui.toast(res.data.message, {//提示  
                              duration: 1000,
                              callback: ()=>{
                                window.location.reload();
                              }
                            });
                          } else {
                            weui.topTips(res.data.message,1000);//提示出错
                          }
                          
                      })
                    }
                }]
            });
          },
          clickEvaluate(){//评价按钮
            this.$router.push({ path: '/myorder_evaluate',query:{id:this.id}});
          }

        },
        mounted(){
            this.getMyOrderDetail();
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/myorder_detail.less";
</style>
