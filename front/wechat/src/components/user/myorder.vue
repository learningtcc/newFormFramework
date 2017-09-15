
<template>
    <div class="wrapper">
        <div class="order_tab">
            <ul>
                <li :class="{active:tab == index}" v-for="(type,index) in types" :key="type.key" @click="check(type,index)"><a href="javascript:;">{{type.name}}</a></li>
            </ul>
        </div>
        <div class="order_list" v-for="(type,index) in types" v-show="index == tab">
            <dl v-for="item in types[index].list" @click="clickToDetail(item)">
                <dt>
                    <router-link :to="{path:'/storeDetail',query:{id:item.store_id}}" class="shop_name">{{item.store_name}}</router-link>
                    <span class="status">{{orderStatus[item.order_status].label}}</span>
                </dt>
                <dd v-for="detail in item.order_detail">
                    <div class="pic">
                        <img :src="detail.commodity_pic" alt="">
                    </div>
                    <div class="cont_r">
                        <div class="info_l">
                            <span class="tit">{{detail.commodity_name}}</span>
                            <span class="num">x{{detail.commodity_amout}}</span>
                        </div>
                        <div class="info_r">
                            <div class="price">￥1209</div>
                            <div class="original_price">￥1520</div>
                            <div class="extra">运费 ￥{{item.fee}}</div>
                        </div>
                    </div>
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
                starVal:null,
                types:[
                    {name:"全部",key:"All",list:[],req:false,curpage:0,totalPage:1,infinite:()=>{ this.onInfinite(this.types[0], 0) }},
                    {name:"待付款",key:"NoPay",list:[],req:false,curpage:0,totalPage:1,infinite:()=>{ this.onInfinite(this.types[1], 1) }},
                    {name:"待发货",key:"HasPay",list:[],req:false,curpage:0,totalPage:1,infinite:()=>{ this.onInfinite(this.types[2], 2) }},
                    {name:"待收货",key:"NoReceive",list:[],req:false,curpage:0,totalPage:1,infinite:()=>{ this.onInfinite(this.types[3], 3) }},
                ],
                tab:0,
                orderStatus:{
                    'NoPay':{label:'待付款', click:this.clickNoPay, btnName:'取消订单'},
                    'HasPay':{label:'待发货', click:this.clickHasPay,btnName:'再来一单'},
                    'NoUse':{label:'未使用', click:this.clickHasPay,btnName:'再来一单'},
                    'HasUse':{label:'已使用', click:this.clickHasPay,btnName:'再来一单'},
                    'HasCancel':{label:'已取消', click:this.clickHasPay,btnName:'再来一单'},
                    'NoSend':{label:'待发货', click:this.clickHasPay,btnName:'再来一单'},
                    'NoReceive':{label:'待收货', click:this.clickHasPay,btnName:'再来一单'},
                    'Received':{label:'已收货', click:this.clickHasPay,btnName:'已收货'},
                    'Refund':{label:'申请退款中', click:this.clickRefund,btnName:'申请退款中'},
                    'Refunded':{label:'已退款', click:this.clickHasPay,btnName:'再来一单'},
                    'Returning':{label:'退货中', click:this.clickHasPay,btnName:'再来一单'},
                    'Returned':{label:'已退款已退货', click:this.clickHasPay,btnName:'再来一单'},
                    'Cancel':{label:'订单过期', click:this.clickHasPay,btnName:'再来一单'},
                    'Finished':{label:'交易完成', click:this.clickHasPay,btnName:'再来一单'},
                    'NoExchange':{label:'未兑换', click:this.clickHasPay,btnName:'再来一单'},
                    'HasExchange':{label:'已兑换', click:this.clickHasPay,btnName:'再来一单'}
                  },
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
                    this.axios.post('/wechat/order/getOrderList', querystring.stringify({
                      'condition':curType.key,
                      'current_page': curpage + 1,
                      'page_size':10
                    }))
                    .then(res => {
                      // loading.hide();
                      if(res.data.success){
                        let data = res.data;
                        curType.curpage = data.data.current_page;
                        curType.totalPage = data.data.total_page;
                        curType.list = curlist.concat(data.data.data);
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
                this.$router.push({ path: '/myorder_detail',query:{id:item.id}});
            }
        },
        mounted(){
            
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/myorder.less";
</style>
