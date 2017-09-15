<template>
    <div class="streetService">
        <router-link :to="{path:'/storeDiscounts',query:{}}">
            <div class="commodity">
                <img src="../../assets/img/temp/street/discountsImg.png">
                <div class="commodityName serviceZ">商品优惠  &nbsp;→</div>
                <div class="conSanjiao commoditySan"><p>优惠</p></div>
            </div>
        </router-link>
        <router-link :to="{path:'/activityList',query:{}}">
            <div class="commodity">
                <img src="../../assets/img/temp/street/activityImg.png">
                <div class="activityName serviceZ">街区文化活动  &nbsp;→</div>
                <div class="conSanjiao activitySan"><p>活动</p></div>
            </div>
        </router-link>
        <router-link :to="{path:'/tenementList',query:{}}">
            <div class="commodity">
                <img src="../../assets/img/temp/street/tenementImg.png">
                <div class="tenementName serviceZ">旺铺招租  &nbsp;→</div>
                <div class="conSanjiao tenementSan"><p>旺铺</p></div>
            </div>
        </router-link>
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
    @import "../../assets/css/streetService.less";
</style>