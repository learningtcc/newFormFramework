<template>
    <div class="guide">
        <div class="hotBtn">
            <input type="text" placeholder="输入搜索关键字" class="searchBtn" v-model="name">
            <span class="search_btn" @click="search">搜索</span>
        </div>
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
        <div class="guideScreen">
            <P class="classify" @click="classifyBtn" :class="{screenA:favorCheck}">分类筛选</P>
            <p class="kind" @click="kindBtn" :class="{screenA:kindCheck}">商品种类</p>
        </div>

        <!--分类和商品筛选-->
        <div class="guideBg" :class="{guideBg1:favorCheck}">
          <ul class="classify" @click="classifyBtn" :class="{classify1:favorCheck}">
            <li class="active">销量</li>
            <li>人气</li>
            <li>价格</li>
          </ul>
          <div class="guideB"></div>
        </div>
        <div class="guideBg" :class="{guideBg1:kindCheck}">
          <ul class="shopT" @click="kindBtn" :class="{shopT1:kindCheck}">
            <li>
              <p>茶具</p>
              <div>
                <span>茶壶</span>
                <span>杯子</span>
              </div>
            </li>
            <li>
              <p>茶叶</p>
              <div>
                <span>龙井茶</span>
                <span>普洱茶</span>
              </div>
            </li>
          </ul>
          <div class="guideB"></div>
        </div>
        <!--分类和商品筛选-->

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
                favorCheck:false,
                kindCheck:false,
                banlist:{},
                name:"",
                tab:[
                  {"text":"Holidays","value":"销量"},
                  {"text":"Community","value":"人气"},
                  {"text":"Culture","value":"价格"}
                ],
            }
        },
        methods: {
            search(){
              this.currentPage = 0;
              this.totalPage = 1;
              this.lists = [];
              this.$refs.infiniteLoading.$emit('$InfiniteLoading:reset');
            },
            classifyBtn(){
                this.favorCheck = !this.favorCheck;
            },
            kindBtn(){
                this.kindCheck = !this.kindCheck;
            },
           onInfinite() {//上拉加载
                //console.log("onInfinite");
                var self = this;
               
                if(self.currentPage < self.totalPage){

                  function load(){
                    ywData.list({'resource_name':'commodity_info','curpage': self.currentPage + 1,'pagesize':10,typeAll:{'is_deleted':'N',name:self.name}},function(data){//列表
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
              ywData.list({'resource_name':'advertising_info',typeAll:{'table_name':'lease_info'}},function (data) {
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
    @import "../../../assets/css/guide.less";
</style>