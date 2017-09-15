<template>
    <div class="specialtyShop">
        <header class="header">
            <div class="banner swiper-container">
                <div class="swiper-wrapper">
                    <div class="swiper-slide" v-for="moreimg in banlist">
                      <img class="header-banner-img" :src="moreimg.image_url">
                    </div>
              </div>
                <div class="swiper-pagination"></div>
            </div>
        </header>   
        <div class="guideList">
            <dl v-for="lis in lists">
                <router-link :to="{path:'/productDetail',query:{id:lis.id}}">
                    <dt><img :src="lis.theme_pic"></dt>
                    <dd>
                        <p class="shopName">{{lis.name}}</p>
                        <p class="shopTime">采摘时间：{{lis.plucking_time}}<span>生产时间：{{lis.production_time}}</span></p>
                        <p class="shopAddress">产地：{{lis.local}}<span>净含量：{{lis.standard}}g</span></p>
                        <p class="shopPrice">价格：<span>{{lis.price}}</span> 元/{{lis.price_content}}</p>
                    </dd>
                </router-link>
            </dl>
            <infinite-loading :on-infinite="onInfinite" ref="infiniteLoading">
                  <span slot="no-more">
                      
                  </span>
                  <span slot="no-results">
                      暂无
                  </span>
              </infinite-loading>
        </div>
    </div>
</template>


<script>
    import InfiniteLoading from 'vue-infinite-loading';
    export default{
        components: {
          InfiniteLoading,
        },
        data(){
            return{
                lists:[],
                totalPage:1,
                currentPage:0,
                banlist:{},
            }
        },
        methods: {
           onInfinite() {//上拉加载
                //console.log("onInfinite");
                var self = this;
               
                if(self.currentPage < self.totalPage){

                  function load(){
                    ywData.list({'resource_name':'commodity_info','curpage': self.currentPage + 1,'pagesize':10,typeAll:{'is_deleted':'N'}},function(data){//列表
                         var listData = data.data;
                         self.lists = self.lists.concat(listData);
                         self.currentPage = data.current_page;
                         self.totalPage = data.total_page;
            
                        if(listData.length > 0){
                         self.$refs.infiniteLoading.$emit('$InfiniteLoading:loaded');
                        } else {
                         self.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');
                        }
                        
                    });
                  }

                  setTimeout(() => {
                    load();
                  },200);

                } else {
                  self.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');
                } 
            },
            getBenner(){
              var self = this;
              ywData.list({'resource_name':'advertising_info',typeAll:{'table_name':'commodity_info'}},function (data) {
                  if(data.success){
                      self.banlist = data.data;
                      self.$nextTick(function(){
                        var swiper = new Swiper('.banner', {
                          pagination: '.swiper-pagination',
                          paginationClickable: true,
                          spaceBetween: 30,
                          autoplay: 2500
                        });
                      })
                  }
              });
            },
        },
        mounted(){
            var self=this;
            self.getBenner();
        }
    }
</script>
<style lang="less" scoped>
    @import "../../../assets/css/specialtyShop.less";
</style>