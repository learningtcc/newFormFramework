
<template>
    <div class="wrapper">
        <div class="type_tab">
            <ul>
                <li :class="{active:typeActive == index}" v-for="(item,index) in typeList" @click="checkTypeTab(item,index)">
                    <a href="javascript:;"></a>
                </li>
            </ul>
        </div>
        <div class="mapView">
            <img src="../../assets/img/temp/map.jpg" style="width:100%;height:100%;" alt="">
        </div>
        <div class="location"></div>
        <div class="zoom">
            <ul>
                <li>+</li>
                <li>-</li>
            </ul>
        </div>
        <div class="route_bottom">
            <div class="routelist">
                <ul class="weui-flex">
                    <li class="weui-flex__item">
                        <div class="tit">较快路线</div>
                        <div class="time">48分钟</div>
                        <div class="distance">24.3公里</div>
                    </li>
                    <li class="weui-flex__item">
                        <div class="tit">常规路线</div>
                        <div class="time">48分钟</div>
                        <div class="distance">24.3公里</div>
                    </li>
                </ul>
            </div>
            <div class="info_about">
                <div class="top">
                    <span class="traffic_light">途经红绿灯 23个</span>
                    <span class="taxi_fare">打车 83元</span>
                </div>
                <div class="nav_guide">开始导航</div>
            </div>
        </div>
    </div>
</template>


<script> 
    export default{
        components:{

        },
        data(){
            return{
               typeList:[
                    {name:'公交'},
                    {name:'驾车'},
                    {name:'步行'},
               ],
               typeActive:0
            }
        },
        methods: {
           checkTypeTab(item,index){
                this.typeActive = index;
           }
        },
        mounted(){
            
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/mapAroundType.less";
</style>
