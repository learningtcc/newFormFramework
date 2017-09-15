
<template>
    <div class="wrapper">
        <div class="aroundList">
            <ul>
                <li>
                    <router-link :to="{path:'/mapAroundDetail',query:{type:'超市'}}"> <em><img src="../../assets/img/map/around_shop.png" alt="" style="width:0.3548rem;"></em>超市</router-link>
                </li>
                <li>
                    <router-link :to="{path:'/mapAroundDetail',query:{type:'美食'}}"> <em><img src="../../assets/img/map/around_food.png" alt="" style="width:0.3548rem;"></em>美食</router-link>
                </li>
                <li>
                    <router-link :to="{path:'/mapAroundDetail',query:{type:'酒店'}}"> <em><img src="../../assets/img/map/around_hotel.png" alt="" style="width:0.3548rem;"></em>酒店</router-link>
                </li>
                <li>
                    <router-link :to="{path:'/mapAroundDetail',query:{type:'医院'}}"> <em><img src="../../assets/img/map/around_hospital.png" alt="" style="width:0.371rem;"></em>医院</router-link>
                </li>
                <li>
                    <router-link :to="{path:'/mapAroundDetail',query:{type:'网吧'}}"> <em><img src="../../assets/img/map/around_net.png" alt="" style="width:0.3548rem;"></em>网吧</router-link>
                </li>
                <li>
                    <router-link :to="{path:'/mapAroundDetail',query:{type:'厕所'}}"> <em><img src="../../assets/img/map/around_toilet.png" alt="" style="width:0.3065rem;"></em>厕所</router-link>
                </li>
                <li>
                    <router-link :to="{path:'/mapAroundDetail',query:{type:'景点'}}"> <em><img src="../../assets/img/map/around_spot.png" alt="" style="width:0.3629rem"></em>景点</router-link>
                </li>
                <li>
                    <router-link :to="{path:'/mapAroundDetail',query:{type:'药店'}}"> <em><img src="../../assets/img/map/around_chemist.png" alt="" style="width:0.371rem;"></em>药店</router-link>
                </li>
                <li>
                    <router-link :to="{path:'/mapAroundDetail',query:{type:'商场'}}"> <em><img src="../../assets/img/map/around_mall.png" alt="" style="width:0.3226rem;"></em>商场</router-link>
                </li>
            </ul>
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
    @import "../../assets/css/mapAroundList.less";
</style>
