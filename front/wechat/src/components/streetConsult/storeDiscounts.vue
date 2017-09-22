<template>
    <div>
        <div class="loading" v-if="ishide"></div>
        <div class="storeDiscounts" v-if="ishide2 && lis.offer_status=='InHand'" v-for="lis in list.data">
            <router-link :to="{path:'/storeDetail',query:{id:lis.id}}">
                <div class="moneyOff">
                    <img :src="lis.image_url">
                    <div class="storeName">{{lis.store_name}}<span>还剩{{lis.surplus_time}}结束</span></div>
                    <p class="storeCon storeOff">{{lis.offer_name}}</p>
                </div>
            </router-link>
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
                pagesize:3,
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