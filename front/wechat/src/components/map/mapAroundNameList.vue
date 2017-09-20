
<template>
    <div class="wrapper">
        <div id="allmap"></div>
        <div class="searchNameList">
            <ul>
                <li v-if="searchResult.length == 0"><div class="noData">暂无</div></li>
                <li v-for="item in searchResult">
                    <router-link :to="{path:'/mapAroundRoute',query:{lng:item.point.lng,lat:item.point.lat}}">
                        <div class="tit">{{item.title}}</div>
                        <div class="addr">
                            <span class="text">{{item.address}}</span>
                            <!-- <span class="distance">距离：10公里</span> -->
                        </div>
                    </router-link>
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
               lng:this.$route.query.lng,
               lat:this.$route.query.lat,
               type:this.$route.query.type,
               map:{},
               local:{},
               searchResult:[],
               zoomDefault:15
            }
        },
        methods: {
           onSearchComplete(results){//本地检索数据接口
                // 判断状态是否正确
                if (this.local.getStatus() == BMAP_STATUS_SUCCESS){
                    var s = [];
                    this.searchResult.length = 0;
                    for (var i = 0; i < results.getCurrentNumPois(); i ++){
                        s.push(results.getPoi(i).title + ", " + results.getPoi(i).address);
                        this.searchResult.push(results.getPoi(i));
                        console.log(results.getPoi(i));
                    }
                }
            },
        },
        mounted(){
            this.map = new BMap.Map("allmap");
            var point = new BMap.Point(this.lng,this.lat);
            this.map.centerAndZoom(point, this.zoomDefault);
            this.local =  new BMap.LocalSearch(this.map, {renderOptions: {map: this.map, autoViewport: false},onSearchComplete:this.onSearchComplete});
            this.local.searchNearby(decodeURI(this.type),point,1000);
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/mapAroundNameList.less";
</style>
