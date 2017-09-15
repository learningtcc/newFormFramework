<template>
    <div class="teaList">
        <div class="teaBg">
            <div class="teaLine">
                <div class="teaH"></div>
                <span class="teaT"></span>
                <p class="line"></p>
                <div class="teaCon" v-for="lis in lists">
                  <router-link :to="{path:'/teaDetail',query:{id:lis.id}}">
                    <p class="span1"></p>
                    <div class="one"></div>
                    <div class="two"></div>
                    <div class="teaR">
                        <p class="teaTitle">{{lis.title}}</p>
                        <p class="teaCen">{{lis.introduction}}</p>
                    </div>
                  </router-link>
                </div>
                <infinite-loading :on-infinite="onInfinite" ref="infiniteLoading">
                  <span slot="no-more">
                      
                  </span>
                  <span slot="no-results">
                      暂无
                  </span>
              </infinite-loading>
            </div>
            <div class="clear"></div>
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
                    ywData.list({'resource_name':'tea_culture','curpage': self.currentPage + 1,'pagesize':10,typeAll:{}},function(data){//列表
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
    @import "../../../assets/css/teaList.less";
</style>