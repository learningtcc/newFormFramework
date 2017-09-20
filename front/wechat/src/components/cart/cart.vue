
<template>
    <div class="wrapper">
        <div class="cartShopList">
            <!-- 购物车为空 -->
            <div class="cart-no-data" v-if="false">
                <p class="pic"><img src="../../assets/img/cart/nodata.png" alt=""></p>
                <p class="text">亲，你的购物车还没有宝贝哦～</p>
                <router-link :to="{path:'/handShop'}" class="btn-view">去逛逛</router-link>
            </div>
            <!-- /购物车为空 -->
            <dl>
                <dt class="header">
                    <div class="checkbox active"></div><i></i><router-link :to="{path:''}"><span class="text">义乌小茶铺</span><span class="arrow"></span></router-link>
                </dt>
                <dd v-for="item in lists" :key="item.id">
                    <div class="checkbox" :class="{active:item.checked}" @click="selectProducts(item)"></div>
                    <router-link :to="{path:''}" class="pic">
                        <img src="../../assets/img/temp/details.jpg" alt="">
                    </router-link>
                    <div class="info_rt">
                        <div class="delete">删除</div>
                        <router-link :to="{path:''}" class="tit">西湖龙井茶西湖龙井茶西湖龙井茶西湖龙井茶</router-link>
                        <div class="price">
                            <em class="single">￥1209</em>
                            <cartcontrol :tea="item"></cartcontrol>
                        </div>
                    </div>
                </dd>
            </dl>
        </div>
        <div class="bottom_bars">
            <div class="bt_lt">
                合计：<em class="price">¥45</em>
            </div>
            <div class="btn_buy">结算</div>
        </div>
    </div>
</template>


<script>
    var querystring = require('querystring');
    import cartcontrol from './cartcontrol'
    export default{
        components:{
            cartcontrol
        },
        data(){
            return{
               lists:[
                    {id:"123", num:2},
                    {id:"12356", num:1},
               ],
               cartLists:[
                    {}
               ]
            }
        },
        methods: {
           selectProducts(item){
                if(typeof(item.checked) == undefined){
                    this.$set(item,'checked',true);
                } else {
                    this.$set(item,'checked', !item.checked);
                    console.log(item.checked);
                }
           }
        },
        mounted(){
            var loading = weui.loading('加载中', {});
            this.axios.post('/wechat/shopping_cart/list', querystring.stringify({//购物车列表
                
            }))
            .then(res => {
                loading.hide();//加载
                if(res.data.success){


                } else {
                  
                }
                
            })
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/cart.less";
</style>
