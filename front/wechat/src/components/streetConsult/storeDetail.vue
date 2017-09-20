<template>
    <div>
        <div class="loading" v-if="ishide"></div>
        <div class="storeDetail" v-if="ishide2">
          <header class="header">
              <div class="banner swiper-container">
                  <div class="swiper-wrapper">
                      <div class="swiper-slide" v-for="moreimg in detail.image_info.data">
                        <img class="header-banner-img" :src="moreimg.pic_url">
                      </div>
                  </div>
                  <div class="swiper-pagination"></div>
              </div>
          </header>
            <div class="storeN">
                <div class="store1" v-if="detail.store_info">
                    <div class="storeName">{{detail.store_info.name}}</div>
                    <div class="storeArea">面积：{{detail.store_info.area}}㎡   类型：{{detail.store_info.type}} </div>
                    <div class="storeAddress">地址：{{detail.store_info.address}}</div>
                </div>
                <div class="storeCode" @click="clickPopup(receiveType)"></div>
                <!-- 收货方式 -->
              <div class="popup" v-show="receiveType.isShow">
                <div class="popup_wrap">
                  <div class="popup_in">
                    <dl class="title">
                        <dt><img :src="detail.store_info.theme_pic"></dt>
                        <dd>
                            <p class="storeTitle">{{detail.store_info.name}}</p>
                            <p class="storeI">这里有茶叶，品类众多，价格优惠 </p>
                            <p class="storeT">快来加我微信/加群/关注店铺公众号</p>
                        </dd>
                    </dl>
                    <div class="check_list">
                        <img :src="detail.store_info.weixin_url">
                      <!-- <p class="storeC"></p> -->
                      <p class="shao">扫一扫二维码，加我微信</p>
                    </div>
                  </div>
                </div>
                <div class="shadow" @click="clickPopup(receiveType)"></div>
              </div>
              <!-- /收货方式 -->
            </div>
            <div class="storeIntro" v-if="detail.store_info">
                <h4>商铺简介</h4>
                <div class="introCon">{{detail.store_info.remark}}</div>
            </div>
            <div class="storeDis">
                <h4>商铺优惠<span>(两种以上的优惠不可同时使用)</span></h4>
                <p class="discount" v-for="lis in detail.offer_voucher.data">
                    <span class="moneyminus">{{lis.type | typeFormate}}</span>{{lis.offer_name}}<span class="discountEnd">{{lis.surplus_time}}后结束！</span>
                </p>
            </div>
            <div class="storeCommodity">
                <h4>商铺商品</h4>
                <div class="storeList">
                    <dl v-for="storeLis in detail.commodity_info.data">
                      <router-link :to="{path:'/productDetail',query:{id:storeLis.id}}">
                        <dt><img :src="storeLis.theme_pic"></dt>
                        <dd>
                            <p class="storeName">{{storeLis.name}}</p>
                            <p class="specification">{{storeLis.standard}}g/包   <span>产地：{{storeLis.local}}</span></p>
                            <p class="storePrice"><span>¥ {{storeLis.price}}</span>/斤</p>
                        </dd>
                      </router-link>
                    </dl>
                </div>
            </div>
            <div class="clear"></div>
            <div class="storeBtn">
                <p class="leaveBtn" @click="tels(detail)">联系店铺</p>
                <a href="javascript:;" @click="checkFavor" :class="{collected:favorCheck}">
                    <i class="icon ico_favor"></i><em class="word">收藏</em><em class="c_word">已收藏</em>
                </a>
                <!-- <p class="collectionBtn">收藏</p> -->
                <router-link :to="{path:'/mapAroundRoute',query:{lng:detail.store_info.longitude,lat:detail.store_info.dimension,title:detail.store_info.name}}">
                  <p class="relationBtn">前往店铺</p>
                </router-link>
            </div>
        </div>
    </div>
</template>


<script> 
    var qs = require('qs');
    export default{
        data () {
            return {
                id:this.$route.query.id,
                detail:{},
                currentpage:1,
                pagesize:5,
                ishide:true,
                ishide2:false,
                receiveType:{
                    isShow:false
                },
                favorCheck:false,
                collectionStatus:{
                    'Y':true,
                    'N':false,
                    false:'N',
                    true:'Y'
                },
            }
        },
        filters:{
          typeFormate(val){
            if(val == undefined){
              return ''
            }
            switch(val)
            {
              case 'discount':
                return '折扣';
                break;
              case 'full_discount':
                return '满减';
                break;
              case 'full_cut':
                return '满减';
                break;  
            }
          }
        },
        methods: {
            tels(detail){
              //打电话
              var self=this;
              if(detail.store_info.contact_phone!=""&&detail.store_info.contact_phone!=null){
                window.location.href = 'tel:' + detail.store_info.contact_phone;
              }else {
                alert("暂无商家电话")
              }
            },
            checkFavor(){//点击收藏
                /*var loading = weui.loading('请稍等', {//loading
                });*/
                this.axios.post('/wechat/user/collection', qs.stringify({
                    'is_collection' : this.collectionStatus[this.favorCheck],
                    'commodity_id' : this.id,
                    'collection_type' : 'Store' 
                }))
                .then(res => {
                    /*loading.hide();//加载*/
                    if(res.data.success){
                       weui.toast(res.data.message, {//提示  
                          duration: 1000,
                          callback: () => {
                            var collectStatus = res.data.data.is_collection;
                            console.log(collectStatus);
                            this.favorCheck = this.collectionStatus[collectStatus];
                          }
                        }); 
                    } else {
                      weui.topTips(res.data.message,1000);//提示出错
                    }
                    
                });
                this.favorCheck = !this.favorCheck;
           },
            getDetail(){
                var self=this;
                self.axios.get("/wechat/offerVoucher/detail?storeId="+self.id

                ).then(function(res){
                    if(res.data.success){
                      self.ishide=false;
                      self.ishide2=true;
                      self.detail=res.data.data;
                      self.favorCheck = self.collectionStatus[self.detail.is_collection];
                      self.$nextTick(function(){
                        var swiper = new Swiper('.banner', {
                          pagination: '.swiper-pagination',
                          paginationClickable: true,
                          spaceBetween: 30,
                          autoplay: 2500
                        });
                      })
                }else {
                  self.ishide=false;
                  self.ishide2=true;
                  alert(res.data.message)
                }
              },function(response){

              }) 
            },
            clickPopup(item){
            item.isShow = !item.isShow
          }
        },
        mounted(){
          var self=this;
          self.getDetail()
        } 
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/storeDetail.less";
</style>