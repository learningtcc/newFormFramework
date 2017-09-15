<template>
    <div>
        <div class="loading" v-if="ishide"></div>
        <div class="tenementDetail" v-if="ishide2">
        <header class="header">
            <div class="banner swiper-container">
                <div class="swiper-wrapper">
                    <div class="swiper-slide" v-for="moreimg in detail.advertising_info.data">
                      <img class="header-banner-img" :src="moreimg.image_url">
                    </div>
                </div>
                <div class="swiper-pagination"></div>
            </div>
        </header>   
        <div class="tenementMain">
            <div class="tenementLeft">
                <p class="tenementName">{{detail.lease_info.title}}</p>
                <p class="tenementArea">面积：{{detail.lease_info.area}}㎡ &nbsp;&nbsp;&nbsp;类型：{{detail.lease_info.type}}</p>
                <p class="tenementPrice">价格：<span>{{detail.lease_info.price}}</span> 元/年&nbsp; (<span>{{detail.lease_info.price}}</span>元/㎡ 天)</p>
                <p class="tenementAddress">地址：<span>{{detail.lease_info.address}} </span></p>
                <p class="tenementTime">{{detail.lease_info.releaseTime}}</p>
            </div>
            <div class="tenementRight" @click="tels(detail)"></div>
        </div>
        <div class="tenementIntro">
            <H4>商铺简介</H4>
            <div class="tenementContent">{{detail.lease_info.describes}}</div>
        </div>
        <div class="moreStore">
            <h4>更多商铺</h4>
            <div class="storeList">
                <dl v-for="lis in detail.commodity_info.data">
                  <!-- <router-link :to="{path:'/tenementDetail',query:{id:lis.id}}"> -->
                    <dt><img :src="lis.theme_pic"></dt>
                    <dd>
                        <p class="storeName">{{lis.name}}</p>
                        <p class="storePrice">价格：<span>{{lis.price}}</span> 元/年</p>
                    </dd>
                  <!-- </router-link> -->
                </dl>
                
            </div>
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
            }
        },
        methods: {
            tels(detail){
              //打电话
              var self=this;
              if(detail.lease_info.contactTel!=""&&detail.lease_info.contactTel!=null){
                window.location.href = 'tel:' + detail.lease_info.contactTel;
              }else {
                alert("暂无商家电话")
              }
            },
            /*storeBtn(){
              var self=this;
              console.log(self.detail.commodity_info.data[0].id);
              self.$router.push({path:'/tenementDetail',query:{id:detail.commodity_info.data.id}})
            },*/
            getDetail(){
                var self=this;
                self.axios.get("/wechat/Lease/detail?id="+self.id
                ).then(function(res){
                    if(res.data.success){
                      self.ishide=false;
                      self.ishide2=true;
                      self.detail=res.data.data;
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
            }
        },
        mounted(){
          var self=this;
          self.getDetail()
        } 
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/tenementDetail.less";
</style>