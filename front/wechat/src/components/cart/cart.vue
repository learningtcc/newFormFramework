
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
                <dd>
                    <div class="checkbox active"></div>
                    <router-link :to="{path:''}" class="pic">
                        <img src="../../assets/img/temp/details.jpg" alt="">
                    </router-link>
                    <div class="info_rt">
                        <router-link :to="{path:''}" class="tit">西湖龙井茶西湖龙井茶西湖龙井茶西湖龙井茶</router-link>
                        <div class="price">
                            <em class="single">￥1209</em>
                            <div class="sub">
                              <span class="subtract">-</span>
                              <span class="numberText">1</span>
                              <span class="adds">+</span>
                            </div>
                        </div>
                    </div>
                </dd>
            </dl>
            <dl>
                <dt class="header">
                    <div class="checkbox active"></div><i></i><router-link :to="{path:''}"><span class="text">义乌小茶铺</span><span class="arrow"></span></router-link>
                </dt>
                <dd>
                    <div class="checkbox active"></div>
                    <router-link :to="{path:''}" class="pic">
                        <img src="../../assets/img/temp/details.jpg" alt="">
                    </router-link>
                    <div class="info_rt">
                        <router-link :to="{path:''}" class="tit">西湖龙井茶西湖龙井茶西湖龙井茶西湖龙井茶</router-link>
                        <div class="price">
                            <em class="single">￥1209</em>
                            <div class="sub">
                              <span class="subtract">-</span>
                              <span class="numberText">1</span>
                              <span class="adds">+</span>
                            </div>
                        </div>
                    </div>
                </dd>
                <dd>
                    <div class="checkbox"></div>
                    <router-link :to="{path:''}" class="pic">
                        <img src="../../assets/img/temp/details.jpg" alt="">
                    </router-link>
                    <div class="info_rt">
                        <router-link :to="{path:''}" class="tit">西湖龙井茶西湖龙井茶西湖龙井茶西湖龙井茶</router-link>
                        <div class="price">
                            <em class="single">￥1209</em>
                            <div class="sub">
                              <span class="subtract">-</span>
                              <span class="numberText">1</span>
                              <span class="adds">+</span>
                            </div>
                        </div>
                    </div>
                </dd>
                <dd>
                    <div class="checkbox"></div>
                    <router-link :to="{path:''}" class="pic">
                        <img src="../../assets/img/temp/details.jpg" alt="">
                    </router-link>
                    <div class="info_rt">
                        <router-link :to="{path:''}" class="tit">西湖龙井茶西湖龙井茶西湖龙井茶西湖龙井茶</router-link>
                        <div class="price">
                            <em class="single">￥1209</em>
                            <div class="sub">
                              <span class="subtract">-</span>
                              <span class="numberText">1</span>
                              <span class="adds">+</span>
                            </div>
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
    export default{
        components:{

        },
        data(){
            return{
               
            }
        },
        methods: {
           
        },
        mounted(){
            
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/cart.less";
</style>
