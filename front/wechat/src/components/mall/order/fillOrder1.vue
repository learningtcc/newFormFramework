
<template>
    <div class="wrapper">
      <div class="receive_type" @click="clickPopup(receiveType)">
        <span class="tit">收货方式</span>
        <span class="result"><i>{{receiveType.arr[receiveType.realTab].name}}</i><em></em></span>
      </div>
      <!-- 收货方式 -->
      <div class="popup fillPopup" v-show="receiveType.isShow">
        <div class="popup_wrap">
          <div class="popup_in">
            <div class="title">收货方式</div>
            <div class="check_list">
              <ul>
                <li v-for="(item,index) in receiveType.arr" @click.stop="checkTag(receiveType,index)">
                  <span class="tit">{{item.name}}</span>
                  <div class="checkbox" :class="{active : receiveType.tab == index}"></div>
                </li>
              </ul>
            </div>
            <div class="btn_wrap">
              <span class="btn confirm" @click="clickConfirm(receiveType),clickPopup(receiveType)">确定</span>
              <span class="btn cancel" @click="clickPopup(receiveType)">取消</span>
            </div>
          </div>
        </div>
        <div class="shadow" @click="clickPopup(receiveType)"></div>
      </div>
      <!-- /收货方式 -->
        <!-- 地址 -->
        <div class="addr_box" v-show="receiveType.realTab == 0" @click="ChooseAddress">
            <dl v-if="addressList.length == 0">
              <dt class="noData">暂无地址，请去新增地址</dt>
            </dl>
            <dl class="info" v-else>
               <dt>
                   <span class="name">收货人：{{addressList[0].receiver}}</span>
                   <span class="tel">{{addressList[0].phone}}</span>
               </dt>
               <dd class="addr" v-if="addressList[0].receiptAdd">
                   {{addressList[0].receiptAdd + addressList[0].address}}
               </dd>
               <dd class="addr" v-else>
                   {{addressList[0].receipt_add + addressList[0].address}}
               </dd>
            </dl>
        </div>
        <!-- /地址 -->
        <!-- 订单信息 -->
        <div class="order_info">
            <dl class="goods_shop">
              <dt class="header">
                  <div class="checkbox active"></div><i></i><a><span class="text">{{storeDetail.name}}</span></a>
              </dt>
            </dl>
            <dl class="goods_info">
              <dt>
                <img :src="detail.theme_pic" height="280" width="380" alt="">
              </dt>
              <dd>
                <div class="tit">{{detail.name}}</div>
                <div class="price">
                  <span class="unit">{{detail.price}}元/{{detail.price_content}}</span>
                  <em class="num">x{{number}}</em>
                </div>
              </dd>
            </dl>
            <div class="cont_list">
                <dl>
                  <dt>购买数量</dt>
                  <dd>
                    <div class="sub">
                      <span class="subtract" @click="subtract" :class="{off:number==1}">-</span>
                      <span class="numberText" v-model="number">{{number}}</span>
                      <span class="adds" @click="adds" :class="{off:number==99}">+</span>
                    </div>
                  </dd>
                </dl>
                <dl @click="clickPopup(shopDiscount)" v-if="shopDiscount.arr.length > 0">
                  <dt>店铺优惠</dt>
                  <dd>
                    <span>{{shopDiscount.arr[shopDiscount.realTab].name}}</span>
                    <span class="arrowR"></span>
                  </dd>
                </dl>
                <!-- <dl>
                  <dt>配送方式</dt>
                  <dd>快递费￥5</dd>
                </dl>
                <dl>
                  <dt>商品总计</dt>
                  <dd>共50g</dd>
                </dl> -->
                <dl>
                  <dt>商品原价</dt>
                  <dd>￥{{totalObj.totalPrice}}</dd>
                </dl>
                <dl>
                  <dt>促销优惠</dt>
                  <dd>-￥{{totalObj.couponPrice}}</dd>
                </dl>
                <div class="total_price">
                  共{{number}}件商品  小计：<em>￥{{totalObj.afterPrice}}</em>
                </div>
            </div>
            <!-- 店铺优惠 -->
            <div class="popup" v-show="shopDiscount.isShow">
              <div class="popup_wrap">
                <div class="popup_in">
                  <div class="title">店铺优惠</div>
                  <div class="check_list">
                    <ul>
                      <li v-for="(item,index) in shopDiscount.arr" @click="checkTag(shopDiscount,index)">
                        <span class="tit">{{item.name}}</span>
                        <div class="checkbox" :class="{active:shopDiscount.tab == index}"></div>
                      </li>
                    </ul>
                  </div>
                  <div class="btn_wrap">
                    <span class="btn confirm" @click="clickConfirm(shopDiscount),clickPopup(shopDiscount)">确定</span>
                    <span class="btn cancel" @click="clickPopup(shopDiscount)">取消</span>
                  </div>
                </div>
              </div>
              <div class="shadow" @click="clickPopup(shopDiscount)"></div>
            </div>
            <!-- /店铺优惠 -->
        </div>
        <!-- /订单信息 -->
        <!-- 支付方式 -->
        <!-- <div class="pay_type">
          <div class="head">支付方式</div>
          <div class="cont">
            <span class="tips_h">请转账到商家支付宝账号，二维码如下</span>
            <img src="../../../assets/img/fillOrder/qrcode.jpg" alt="" class="qrcode" />
            <p class="shop_name">义乌小茶铺</p>
            <p class="tips">注：转账需要标注，义乌精品街区公众号及订单编号，方便商家进行查询核算</p>
          </div>
        </div> -->
        <!-- /支付方式 -->
        <div class="fixed_bottom">
          <div class="price_total">合计：<em>¥{{totalObj.afterPrice}}</em></div>
          <div class="btns" @click="submit">立即下单</div>
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
              addressId:"",
              detail:{},
              storeDetail:{},
              addressList:[],
              offerList:[],
              number:1,
              receiveType:{
                isShow:false,
                arr:[
                  {name:'送货上门', key:'express'},
                  {name:'现场自提', key:'offline'},
                ],
                tab:0,
                realTab:0,
              },
              shopDiscount:{
                isShow:false,
                arr:[],
                tab:0,
                realTab:0,
              },
              totalObj:{}
            }
        },
        watch:{
          number(val){
            this.getTotal();
          },
          'shopDiscount.realTab'(val){
            this.getTotal();
          }
        },
        methods: {
            //数量减
          subtract(){
            if(this.number==1){
              return false
            }
            this.number--;

          },
          //数量加
          adds(){
            if(this.number==99){
              return false
            }
            this.number++;
            
          },
          clickPopup(item){//弹出框
            if(!item.isShow){
              item.tab = item.realTab;
            }
            item.isShow = !item.isShow
          },
          checkTag(item,index){//选择标签
            item.tab = index;
          },
          clickConfirm(item){//弹框确认按钮
            item.realTab = item.tab;
          },
          getStoreDetail(storeId){//店铺信息
            ywData.detail({'resource_name':'store_info',id:storeId,typeAll:{}},(data)=>{
                this.storeDetail = data.data;
                
            });
          },
          getProductDetail(){//获取商品详情
            var loading = weui.loading('加载中', {//loading
            });
            this.axios.post('/wechat/shop/detail', querystring.stringify({//详情
                'id':this.id
            }))
            .then(res => {
                loading.hide();//加载
                if(res.data.success){

                    this.detail = res.data.data.commodity_info;
                    this.getCouponDetail(this.detail.store_id);
                    this.getStoreDetail(this.detail.store_id);
                }
            })
          },
          getCouponDetail(storeId){//店铺优惠券列表
            //this.shopDiscount.arr = [];
            ywData.list({'resource_name':'offer_voucher',typeAll:{store_id:storeId}},(data)=>{
              var offerList = data.data;
              for(var i = 0; i < offerList.length; i++){
                var couponArr = {name:offerList[i].offer_name, key:offerList[i].type, id:offerList[i].id};
                this.shopDiscount.arr.push(couponArr);
              }
              this.getTotal();
            });
          },
          getAddressList(){//获取地址列表
            this.axios.post('/wechat/user/addressList', querystring.stringify({//详情

            }))
            .then(res => {
                if(res.data.success){
                    var data = res.data.data.data; 
                    this.addressList.splice(0,this.addressList.length);
                    if(data.length > 0){
                      this.addressList.push(data[0]);
                    } else {
                      this.addressList = [];
                    }
                    
                }
            })
          },
          getAddressDetail(){//获取地址详情

            ywData.detail({'resource_name':'member_address',id:this.addressId,typeAll:{}},(data)=>{
              this.addressList.splice(0,this.addressList.length);
              this.addressList.push(data.data);
            });
          },
          ChooseAddress(){//跳转选择地址
            this.writeStore();
            this.$router.push({ path: '/addressList',query:{fillOrderId:this.id}});
          },
          writeStore(){
            var record = {number:this.number, receiveRealTab:this.receiveType.realTab,shopDiscountRealTab:this.shopDiscount.realTab};
            this.$root.$data.store.write("fillOrder" + this.id, record);
          },
          readAddressId(){//获取选择后的地址id
            var addressStore = this.$root.$data.store.read("fillOrderAddressId" + this.id);
            if(addressStore){
              this.addressId = addressStore.addressId;
            } else {
              this.addressId == ''
            }
          },
          getTotal(){//获取总价信息
            var couponId = '';
            if(this.shopDiscount.arr.length > 0){
              couponId = this.shopDiscount.arr[this.shopDiscount.realTab].id;
            }
            this.axios.post('/wechat/order/get_total', querystring.stringify({//获取总价信息
              commodity_id : this.id,//商品id
              buy_num : this.number,//购买数量
              offer_voucher_id : couponId//优惠券id
            }))
            .then(res => {
                if(res.data.success){
                    var data = res.data.data;
                    this.totalObj = {couponPrice : data.coupon_price, totalPrice : data.total_price, afterPrice : data.after_price};
                    
                }
            })
          },
          submit(){//提交订单
            var result = {error:false, msg:""};
            if(this.receiveType.realTab == 0){
              testEmpty(this.addressList, result, '地址收货地址不能为空');
            }
            
            if(result.error){
              weui.topTips(result.msg,1000);//提示出错
              return;
            }

            var loading = weui.loading('请稍候', {//loading
            });

            var receiveType = this.receiveType.arr[this.receiveType.realTab].key;
            var addrId = '';
            if(receiveType == 'express'){//收货方式为快递
              addrId = this.addressList[0].id;
            } else {
              addrId = '';
            }
            var discountId = '';
            if(this.shopDiscount.arr.length > 0){
              var discountId = this.shopDiscount.arr[this.shopDiscount.realTab].id;
            }
            
            this.axios.post('/wechat/order/create', querystring.stringify({//创建订单接口
              receipt_way : this.receiveType.arr[this.receiveType.realTab].key, //收货方式:自提offline 快递 express
              member_address_id : addrId, //会员地址信息id,收货方式为自提 可不传
              commodity_id : this.id, //商品id
              buy_num : this.number, //购买数量
              offer_voucher_id : discountId//优惠券id,可为空
            }))
            .then(res => {
                loading.hide();//加载完
                if(res.data.success){
                    weui.toast(res.data.message, {//提示  
                      duration: 1000,
                      callback: ()=> {
                        var orderId = res.data.data.order_id;
                        this.$router.push({ path: '/orderSucc',query:{id:orderId,storeId:this.detail.store_id}});
                      }
                    });
                } else {
                  weui.topTips(res.data.message,1000);//提示出错
                }
            })
          }
        },
        mounted(){
            this.getProductDetail();
            this.readAddressId();//选择的地址
            if(this.addressId){
              this.getAddressDetail();
            } else {
              this.getAddressList();
            }
            
            //this.$root.$data.store.clear("fillOrder" + this.id);
            //console.log(this.$root.$data.store.read("fillOrderAddressId" + this.id));
        }
    }
</script>
<style lang="less" scoped>
    @import "../../../assets/css/fillOrder.less";
</style>