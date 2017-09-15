
<template>
    <div class="wrapper">
        <div class="search_box">
            <input type="text" class="search_input" placeholder="搜索地址" v-model="searchText" />
            <span class="search_btn" @click="search">搜索</span>
        </div>
        <div id="allmap"></div>
        <div class="search_list">
            <div class="noData" v-if="searchResult.length == 0">暂无</div>
            <ul>
                <li v-for="(item,index) in searchResult" :key="item.uid" @click="clickToRoute(item)">
                    <div class="tit">{{item.title}}</div>
                    <div class="addr">{{item.address}}</div>
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
               map:{},
               local:{},
               lng:this.$route.query.lng,
               lat:this.$route.query.lat,
               searchText:'',
               searchResult:[]
            }
        },
        methods: {
           onSearchComplete(results){
                // 判断状态是否正确
                if (this.local.getStatus() == BMAP_STATUS_SUCCESS){
                    var s = [];
                    this.searchResult.length = 0;
                    for (var i = 0; i < results.getCurrentNumPois(); i ++){
                        s.push(results.getPoi(i).title + ", " + results.getPoi(i).address);
                        this.searchResult.push(results.getPoi(i));
                        console.log(results.getPoi(i));
                    }
                } else {
                    this.searchResult = [];
                }
            },
            search(){
                this.local.search(this.searchText);
            },
            clickToRoute(item){
                this.$router.push({ path: '/mapAroundRoute' ,query:{lng:item.point.lng,lat:item.point.lat}})
            }
        },
        mounted(){
            this.map = new BMap.Map("allmap");// 创建Map实例
            var mPoint = new BMap.Point(this.lng, this.lat);
            this.map.enableScrollWheelZoom();
            this.map.centerAndZoom(mPoint,15);
            var circle = new BMap.Circle(mPoint,3000,{fillColor:"blue", strokeWeight: 1 ,fillOpacity: 0.3, strokeOpacity: 0.3});
            this.map.addOverlay(circle);
            this.local =  new BMap.LocalSearch(this.map, {renderOptions: {map: this.map, autoViewport: false},onSearchComplete:this.onSearchComplete});
            this.local.search("商品");
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/mapSearchList.less";
</style>
