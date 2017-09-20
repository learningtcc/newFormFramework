
<template>
    <div class="wrapper">
        <div class="m_tab">
            <ul>
                <li :class="{active:tab == index}" v-for="(type,index) in types" @click="check(type,index)">
                    <a href="javascript:;">{{type.name}}</a>
                </li>
            </ul>
        </div>
        <div class="m_lists" v-show="index == tab" v-for="(type,index) in types">
            <dl v-for="item in types[0].list" @click="clickToDetail(item)" :key="item.id" v-show="index == 0">
                <dt v-if="item.store_info"><img :src="item.store_info.theme_pic"></dt>
                <dd v-if="item.store_info">
                    <p class="shopName">{{item.store_info.name}}</p>
                    <p class="shopTime" v-html="item.store_info.description"></p>
                    <p class="shopPrice">价格：<span>{{item.store_info.expenditure}}</span> 元起</p>
                </dd>
            </dl>
            <dl v-for="item in types[1].list" @click="clickToDetail(item)" :key="item.id" v-show="index == 1">
                <dt v-if="item.commodity_info"><img :src="item.commodity_info.theme_pic"></dt>
                <dd v-if="item.commodity_info">
                    <p class="shopName">{{item.commodity_info.name}}</p>
                    <p class="shopTime">{{item.commodity_info.plucking_time}}</p>
                    <p class="shopPrice">价格：<span>{{item.commodity_info.price}}</span> 元/{{item.commodity_info.price_content}}</p>
                </dd>
            </dl>
            <infinite-loading :on-infinite="types[index].infinite" ref="infiniteLoading">
                <span slot="no-more">
                加载完
                </span>
                <span slot="no-results">
                暂无
                </span>
            </infinite-loading>
        </div>
    </div>
</template>


<script>
    var querystring = require('querystring');
    import InfiniteLoading from 'vue-infinite-loading';
    export default{
        components: {
            InfiniteLoading,
        },
        data(){
            return{
                types:[
                    {name:"店铺",key:"Store",list:[],req:false,curpage:0,totalPage:1,infinite:()=>{ this.onInfinite(this.types[0], 0) }},
                    {name:"商品",key:"Commodity",list:[],req:false,curpage:0,totalPage:1,infinite:()=>{ this.onInfinite(this.types[1], 1) }},
                ],
                tab:0
            }
        },
        methods: {
            onInfinite(curType, index) {//上拉加载
                //var self = this;

                if (index != this.tab) {
                  return;
                }

                var infiniteLoading = this.$refs.infiniteLoading[index];
                var curlist = curType.list;
                var curpage = curType.curpage;
                var totalpage = curType.totalPage;

                if(curpage < totalpage){
                  //console.log("curpage:" + curpage + ",index:" + index);
                  //console.log("totalpage:" + totalpage + ",index:" + index);
                  setTimeout(() => {
                    this.axios.post('/wechat/user/userCollection', querystring.stringify({
                      'collection_type':curType.key,
                      'current_page': curpage + 1,
                      'page_size':10
                    }))
                    .then(res => {
                      // loading.hide();
                      if(res.data.success){
                        let data = res.data;
                        curType.curpage = data.current_page;
                        curType.totalPage = data.total_page;
                        curType.list = curlist.concat(data.data);
                        if(curType.list.length == 0){

                          infiniteLoading.$emit('$InfiniteLoading:complete');
                          return;
                        }
                      }
                      
                      infiniteLoading.$emit('$InfiniteLoading:loaded');

                    })
                    .catch(error => {
                      infiniteLoading.$emit('$InfiniteLoading:loaded');
                    });
                             
                  },200);
                    

                } else {
                  infiniteLoading.$emit('$InfiniteLoading:complete');
                }
            },
           check(type,index){

                this.tab = index;
                type.infinite();
            },
            clickToDetail(item){
                if(item.collection_type == 'Commodity'){
                    this.$router.push({ path: '/productDetail',query:{id:item.commodity_id}});
                } else {
                    this.$router.push({ path: '/storeDetail',query:{id:item.store_info.id}});
                }
                
            },
        },
        mounted(){
            
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/myFavor.less";
</style>
