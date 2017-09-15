<template>
    <div class="hotShop">
        <div class="hotBtn">
            <input type="text" placeholder="输入搜索关键字" class="searchBtn" v-model="name">
            <span class="search_btn" @click="search">搜索</span>
        </div>
        <dl v-for="lis in lists">
            <router-link :to="{path:'/storeDetail',query:{id:lis.id}}">
                <dt><img :src="lis.theme_pic"></dt>
                <dd>
                    <p class="shopName">{{lis.name}}</p>
                    <p class="shopIntro">类型：{{lis.type}}</p>
                    <p class="shopPrice">价格：<span>{{lis.expenditure}}</span> 元起</p> 
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
                name:'',
            }
        },
        methods: {
           search(){
              this.current_page = 1;
              var params = {'name':this.name};
              this.onInfinite(params);
            },
           onInfinite(params) {//上拉加载
                //console.log("onInfinite");
                var self = this;
               
                if(self.currentPage < self.totalPage){

                  function load(){
                    ywData.list({'resource_name':'store_info','curpage': self.currentPage + 1,'pagesize':10,typeAll:{'is_hot':'Y'}},function(data){//列表
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
    @import "../../../assets/css/hotShop.less";
</style>