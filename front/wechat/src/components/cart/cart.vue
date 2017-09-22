
<template>
    <div class="wrapper">
        <div class="cartShopList">
            <!-- 购物车为空 -->
            <div class="cart-no-data" v-if="cartLists == null">
                <p class="pic"><img src="../../assets/img/cart/nodata.png" alt=""></p>
                <p class="text">亲，你的购物车还没有宝贝哦～</p>
                <router-link :to="{path:'/handShop'}" class="btn-view">去逛逛</router-link>
            </div>
            <!-- /购物车为空 -->
            <dl v-for="(store,index) in cartLists">
                <dt class="header">
                    <div class="checkbox" :class="{active:store.storeChecked}" @click="selectStore(store)"></div><i></i><router-link :to="{path:'/storeDetail',query:{id:store.store_id}}"><span class="text">{{store.store_name}}</span><span class="arrow"></span></router-link>
                </dt>
                <dd v-for="item in store.goodmap" :key="item.id">
                    <div class="checkbox" :class="{active:item.checked}" @click="selectProducts(store, item)"></div>
                    <router-link :to="{path:'/productDetail',query:{id:item.commodity_id}}" class="pic">
                        <img :src="item.commodity_image" alt="">
                    </router-link>
                    <div class="info_rt">
                        <div class="delete" @click.stop.prevent="deleteOneGoods(store, item)">删除</div>
                        <router-link :to="{path:''}" class="tit">{{item.commodity_name}}</router-link>
                        <div class="price">
                            <em class="single">￥{{item.price}}</em>
                            <cartcontrol :tea="item" :store="store" :changeNum="loopSubmitGoods"></cartcontrol>
                        </div>
                    </div>
                </dd>
            </dl>
        </div>
        <div class="bottom_bars">
            <div class="bt_lt">
                合计：<em class="price">¥{{total}}</em>
            </div>
            <div class="btn_buy" @click="submitToFill">结算</div>
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
               cartLists:{},
               submitArr:[],
               submitObj:{},
               total:0
            }
        },
        methods: {
           selectProducts(store,item){//选择商品
                if(typeof(item.checked) == undefined){
                    this.$set(item,'checked',true);
                } else {
                    this.$set(item,'checked', !item.checked);
                    console.log(item.checked);
                }
                this.selectProductsChanged(store,item);
                if(item.checked){
                    this.unSelectExcept(store);
                }
                this.loopSubmitGoods();
           },
           selectProductsChanged(store,item){
                var goodmap = store.goodmap;
                var storeCheck = true;
                for(var key in goodmap){
                    if (!goodmap[key].checked) {
                        storeCheck = false;
                        break;
                    }
                }

               this.$set(store,'storeChecked',storeCheck); 
           },
           selectStore(store){//全选当前店铺商品
                if(typeof(store.storeChecked) == undefined){
                    this.$set(store,'storeChecked',true);
                } else {
                    this.$set(store,'storeChecked', !store.storeChecked);
                    for(var key in store.goodmap){
                        var goodlist = store.goodmap;

                        if(typeof(goodlist[key].checked) == undefined){
                            this.$set(goodlist[key],'checked',true);
                        } else {
                            this.$set(goodlist[key],'checked', store.storeChecked);
                            
                        }
                    }

                }
                if(store.storeChecked){
                    this.unSelectExcept(store);
                }
                this.loopSubmitGoods();
           },
           unSelectExcept(store){
                var storeId = store.store_id;
                for(var key in this.cartLists){
                    if (key == storeId) {
                        continue;
                    }
                    this.$set(this.cartLists[key],'storeChecked',false);
                    var goodmap = this.cartLists[key].goodmap;
                    for(i in goodmap){
                        this.$set(goodmap[i],'checked',false);
                    }
                }
           },
           deleteOneGoods(store,goods){//删除商品
                weui.dialog({
                      title: '',
                      content: '是否删除此商品',
                      buttons: [{
                          label: '取消',
                          type: 'default',
                          onClick:()=> { 

                          }
                      }, {
                          label: '确定',
                          type: 'primary',
                          onClick: ()=> {
                            this.axios.post('/wechat/shopping_cart/removeCommodity', querystring.stringify({//删除接口
                                'store_id': store.store_id,
                                'commodity_id':goods.commodity_id
                            }))
                            .then(res => {
                                if(res.data.success){
                                    weui.toast('删除成功', {//提示  
                                      duration: 1000,
                                      callback:()=>{
                                        window.location.reload();
                                      }
                                    });

                                } else {
                                  
                                }
                                
                            })
                          }
                      }]
                  });
                
           },
           loopSubmitGoods(){
                this.submitArr = [];
                this.submitObj = {};
                this.total = parseFloat(0);
                for(var key in this.cartLists){
                    var goodlist = this.cartLists[key].goodmap;
                    for(var goodskey in goodlist){
                        if(goodlist[goodskey].checked){
                            console.log(goodlist[goodskey].num);
                            this.total += goodlist[goodskey].price * goodlist[goodskey].num;
                            this.submitArr.push(goodlist[goodskey]);
                            if(this.submitObj.store == undefined){
                                this.submitObj.store = this.cartLists[key];
                            }
                        }
                    }
                }
                this.submitObj.goods = this.submitArr; 
                console.log(this.submitArr);
           },
           submitToFill(){//结算
                this.loopSubmitGoods();
                if(this.submitArr.length > 0){
                    this.$router.push({ path: '/fillOrder',query:{submitObj:JSON.stringify(this.submitObj)}});
                } else {
                    weui.topTips('您还没有选择商品',1000);//提示出错
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
                    this.cartLists = res.data.data;

                } else {
                  
                }
                
            })
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/cart.less";
</style>
