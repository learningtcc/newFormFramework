<template>
    <div>
        <div class="loading" v-if="ishide"></div>
        <div class="tenement" v-if="ishide2">
            <header class="header">
                <div class="banner swiper-container">
                    <div class="swiper-wrapper">
                        <div class="swiper-slide" v-for="moreimg in list.advertising_info.data">
                          <img class="header-banner-img" :src="moreimg.image_url">
                        </div>
                  </div>
                  <div class="swiper-pagination"></div>
                </div>
            </header>
            <div class="tenementSort">
                <p class="priceSort" @click="screenBtn">价格排序</p>
                <p class="areaSort">面积排序</p>
            </div>
            <div class="tenementCon" v-for="lis in list.lease_info.data">
                <router-link :to="{path:'/tenementDetail',query:{id:lis.id}}">
                    <img :src="lis.theme_pic">
                    <div class="tenDetail">
                        <p class="tenementName">{{lis.title}}</p>
                        <p class="tenementArea">面积：{{lis.area}}㎡ &nbsp;&nbsp;&nbsp;类型：{{lis.type}}</p>
                        <p class="tenementPrice">价格：<span>{{lis.price}}</span> 元/年</p>
                    </div>
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
                list:{},
                currentpage:1,
                pagesize:5,
                ishide:true,
                ishide2:false,
                condition:"price",
                sort:"desc"
            }
        },
        methods: {
            screenBtn(){
                 var self=this;
                 console.log(self.condition);
                 if(self.sort=='desc'){
                     self.condition='area';
                     self.sort="asc";
                     console.log(self.sort);
                     self.getlist()
                 }  else{
                    self.condition='price';
                    self.sort="desc";
                    self.getlist()
                 }
            },
            getlist(){
                var self=this;
                self.axios.get("/wechat/Lease/list",
                qs.stringify({
                    page_size:self.pagesize,
                    current_page:self.currentpage,
                    condition:self.condition,
                    sort:self.sort
                })
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
    @import "../../assets/css/tenement.less";
</style>