<template>
    <div>
    <div class="loading" v-if="ishide"></div>
      <div class="virtualShop" v-if="ishide2">
        <header class="header">
            <div class="banner swiper-container">
                <div class="swiper-wrapper">
                    <div class="swiper-slide" v-for="moreimg in list.advertising_List">
                      <img class="header-banner-img" :src="moreimg.image_url">
                    </div>
              </div>
                <div class="swiper-pagination"></div>
            </div>
        </header>  
        <div class="guideList">
            <dl v-for="lis in list.online_list" v-if="lis.store_info!=null">
                <router-link :to="{path:'/productDetail',query:{id:lis.id}}">
                    <dt><img :src="lis.store_info.theme_pic"></dt>
                    <dd>
                        <p class="shopName">{{lis.store_info.name}}</p>
                        <p class="shopTime">{{lis.name}}</p>
                        <p class="shopPrice">价格：<span>{{lis.price}}</span> 元/{{lis.price_content}}</p>
                    </dd>
                </router-link>
            </dl>
        </div>
    </div>
    </div>
</template>

<script> 
    var qs = require('qs');
    export default{
        data () {
            return {
                id : this.$route.query.id,
                list:{},
                currentpage:1,
                pagesize:5,
                ishide:true,
                ishide2:false,
            }
        },
        methods: {
            getlist(){
                var self=this;
                console.log(self.id);
                self.axios.post("/wechat/shop/onlineList",
                    qs.stringify({
                        page_size:self.pagesize,
                        current_page:self.currentpage,
                        id:self.id,
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
                          autoplay: 2500000
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
<!--<script>
    import InfiniteLoading from 'vue-infinite-loading';
    export default{
        components: {
          InfiniteLoading,
        },
        data(){
            return{
                lists:[],
                totalPage:1,
                currentPage:0
            }
        },
        methods: {
           onInfinite() {//上拉加载
                //console.log("onInfinite");
                var self = this;
               
                if(self.currentPage < self.totalPage){

                  function load(){
                    ywData.list({'resource_name':'store_info','curpage': self.currentPage + 1,'pagesize':10,typeAll:{'is_deleted':'N'}},function(data){//列表
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
            }
        },
        mounted(){
            
        }
    }
</script>-->
<style lang="less" scoped>
    @import "../../../assets/css/virtualShop.less";
</style>