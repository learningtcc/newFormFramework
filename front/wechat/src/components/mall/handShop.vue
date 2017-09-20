<template>
    <div>
        <div class="loading" v-if="ishide"></div>
        <div class="handShop" v-if="ishide2">
            <header class="header">
                <div class="banner swiper-container">
                    <div class="swiper-wrapper">
                        <div class="swiper-slide" v-for="moreimg in list.advertising_list">
                          <img class="header-banner-img" :src="moreimg.image_url">
                        </div>
                  </div>
                  <div class="swiper-pagination"></div>
                </div>
            </header>
            <div class="shopNav">
                <dl>
                    <router-link :to="{path:'/specialtyShop',query:{}}">
                        <dt><img src="../../assets/img/temp/shop1.png"></dt>
                        <dd>特色商品</dd>
                    </router-link>
                </dl>
                <dl>
                    <router-link :to="{path:'/onlineshop/list',query:{}}">
                        <dt><img src="../../assets/img/temp/shop2.png"></dt>
                        <dd>网上商铺</dd>
                    </router-link>
                </dl>
                <dl>
                    <router-link :to="{path:'/interaction/index',query:{}}">
                        <dt><img src="../../assets/img/temp/shop3.png"></dt>
                        <dd>互动圈</dd>
                    </router-link>
                </dl>
                <dl>
                    <router-link :to="{path:'/guide',query:{}}">
                        <dt><img src="../../assets/img/temp/shop4.png"></dt>
                        <dd>导购指南</dd>
                    </router-link>
                </dl>
                <dl>
                    <router-link :to="{path:'/teaList',query:{}}">
                        <dt><img src="../../assets/img/temp/shop5.png"></dt>
                        <dd>供应链</dd>
                    </router-link>
                </dl>
            </div>
            <div class="shopFeature">
                <h4>特色商品<router-link :to="{path:'/specialtyShop',query:{}}"><span>查看更多</span></router-link></h4>
                <div class="shopBan swiper-container">
                    <div class="shopList swiper-wrapper">
                        <dl v-for="lis in list.feature_list" v-if="list.feature_list.is_features = 'Y'" class="swiper-slide">
                            <router-link :to="{path:'/productDetail',query:{id:lis.id}}">
                                <dt><img :src="lis.theme_pic"></dt>
                                <dd>
                                    <p class="shopName">{{lis.name}}</p>
                                    <p class="shopIntro">{{lis.production_time}}年{{lis.plucking_time}}</p>
                                    <p class="shopPrice">{{lis.price}}元/{{lis.standard}}g</p>
                                </dd>
                            </router-link>
                        </dl>
                    </div>
                </div>
            </div>
            <div class="hotShop">
                <h4>热门商铺<router-link :to="{path:'/hotShop',query:{}}"><span>查看更多</span></router-link></h4>
                <dl v-for="storeLis in list.store_list">
                    <router-link :to="{path:'/onlineshop/list',query:{}}">
                        <dt><img src="../../assets/img/temp/shopList1.png"></dt>
                        <dd>
                            <p class="shopName">{{storeLis.name}}</p>
                            <p class="shopIntro">类型：{{storeLis.type}}</p>
                            <p class="shopPrice">价格：<span>{{storeLis.expenditure}}</span> 元起</p> 
                        </dd>
                    </router-link>
                </dl>
            </div>
            <div class="posted"></div>
        </div>
    </div>
</template>


<script> 
    var qs = require('qs');
    export default{
        data () {
            return {
                list:{},
                currentpage:1,
                pagesize:5,
                ishide:true,
                ishide2:false,
                /*condition:"price",
                sort:"desc"*/
            }
        },
        methods: {
            getlist(){
                var self=this;
                self.axios.post("/wechat/shop/index",
                /*qs.stringify({
                    page_size:self.pagesize,
                    current_page:self.currentpage,
                    condition:self.condition,
                    sort:self.sort
                })*/
              ).then(function(res){
                if(res.data.success){
                  self.ishide=false;
                  self.ishide2=true;
                  self.list=res.data.data;
                  self.$nextTick(function(){
                    var swiper = new Swiper('.banner', {
                      pagination: '.swiper-pagination',
                      paginationClickable: true,
                      spaceBetween: 30,
                      autoplay: 2500
                    });
                  });
                  self.$nextTick(function(){
                    var swiper2 = new Swiper('.shopBan', {
                      slidesPerView: 'auto',
                      spaceBetween: 16,
                      observer:true,
                      observeParents:true,
                    });
                  })

                }else {
                  self.ishide=false;
                  self.ishide2=true;
                  alert(res.data.message)
                }
              },function(response){

              })
            }
        },
        mounted(){
            var self=this;
            self.getlist()
        
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/handShop.less";
</style>