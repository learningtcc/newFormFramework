
<template>
    <div class="wrapper">
        <div class="shop_list">
            <ul>
                <li v-for="item in lists">
                    <router-link :to="{path:'/virtualShop',query:{id:item.id}}">
                        <img :src="item.theme_pic" alt="">
                        <span class="about">
                            <span class="tit">{{item.name}}</span>
                            <span class="price">价格：<em>{{item.price_low}}-{{item.price_high}}</em> {{item.price_unit}}</span>
                        </span>
                    </router-link>
                </li>
                <infinite-loading :on-infinite="onInfinite" ref="infiniteLoading">
                  <span slot="no-more">
                      加载完
                  </span>
                  <span slot="no-results">
                      暂无
                  </span>
              </infinite-loading>
            </ul>
            
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
                currentPage:0
            }
        },
        methods: {
           onInfinite() {//上拉加载
                //console.log("onInfinite");
                var self = this;
               
                if(self.currentPage < self.totalPage){

                  function load(){
                    ywData.list({'resource_name':'tea_species','curpage': self.currentPage + 1,'pagesize':10,typeAll:{'is_deleted':'N'}},function(data){//列表
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
</script>
<style lang="less" scoped>
    @import "../../../assets/css/onlineShop.less";
</style>
