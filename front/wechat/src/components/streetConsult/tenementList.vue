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
                <p class="priceSort" @click="priceBtn">价格排序</p>
                <p class="areaSort" @click="areaBtn">面积排序</p>
            </div>
            <div class="tenementCon" v-for="lis in list.lease_info.data">
                <router-link :to="{path:'/tenementDetail',query:{id:lis.id}}">
                    <img :src="lis.theme_pic">
                    <div class="tenDetail">
                        <p class="tenementName">{{lis.title}}</p>
                        <p class="tenementArea">面积：{{lis.area}}㎡ &nbsp;&nbsp;&nbsp;类型：{{lis.type | typeFormate}}</p>
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
                pagesize:6,
                ishide:true,
                ishide2:false,
                condition:"price",
                sort:"desc"
            }
        },
        filters:{
          typeFormate(val){
            if(val == undefined){
              return ''
            }
            switch(val)
            {
              case 'commercial_complex':
                return '商业综合体';
                break;
              case 'shop_type':
                return '门店';
                break;  
            }
          }
        },
        methods: {
            priceBtn(){
              var self=this;
              self.condition='price';
              if (self.sort=="desc") {
                self.sort="asc"
              } else {
                self.sort="desc"
                }
              self.getlist()
            },
            areaBtn(){
              var self=this;
              self.condition='area';
              if (self.sort=="desc") {
                self.sort="asc"
              } else {
                self.sort="desc"
              }
              self.getlist()
            },
            getlist(){
                var self=this;
                console.log(self.currentpage);
                self.axios.get("/wechat/Lease/list",{params:{
                    page_size:self.pagesize,
                    current_page:self.currentpage,
                    condition:self.condition,
                    sort:self.sort
                }}).then(function(res){
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