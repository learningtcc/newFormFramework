<template>
    <div>
        <div class="loading" v-if="ishide"></div>
        <div class="storeDiscounts" v-if="ishide2" v-for="lis in list.data">
            <router-link :to="{path:'/storeDetail',query:{id:lis.id}}">
                <div class="moneyOff">
                    <img :src="lis.image_url">
                    <div class="storeName">{{lis.store_name}}<span>还剩{{lis.surplus_time}}结束</span></div>
                    <p class="storeCon storeOff">{{lis.offer_name}}</p>
                </div>
            </router-link>
            <!-- <div class="moneyOff">
                <img src="../../assets/img/temp/discounts1.png">
                <div class="storeName">义乌茶铺店铺优惠<span>还剩5天16小时结束</span></div>
                <div class="storeCon storeOff2">满888元扣100元</div>
            </div>
            <div class="moneyOff">
                <img src="../../assets/img/temp/discounts1.png">
                <div class="storeName">义乌茶铺店铺优惠<span>还剩5天16小时结束</span></div>
                <div class="storeCon storeOff3">店铺全场5折</div>
            </div> -->
        </div>
    </div>
</template>


<script> 
    var qs = require('qs');
    export default{
        data () {
            return {
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
                self.axios.get("/wechat/offerVoucher/list",
                    qs.stringify({
                        page_size:self.pagesize,
                        current_page:self.currentpage,
                    })
                ).then(function(res){
                    if(res.data.success){
                      self.ishide=false;
                      self.ishide2=true;
                      self.list=res.data.data;
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
<style lang="less" scoped>
    @import "../../assets/css/storeDiscounts.less";
</style>