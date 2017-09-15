<template>
    <div class="wrapper">
        <div class="map_top">
           <div class="view_more" @click="clickToNameList"></div>
           <div class="search_wrap" @click="linkSearch">
               <div class="search_btn"></div>
               <input type="text" class="txt_search" readonly="true" v-model="searchText">
           </div>
        </div>
        <div class="mapView" id="allmap">
            
        </div>
        <div class="location" @click="getLocation()"></div>
        <div class="zoom">
            <ul>
                <li @click="zoomIn" :class="{unclick:zoomDefault == 19}">+</li>
                <li @click="zoomOut" :class="{unclick:zoomDefault == 3}">-</li>
            </ul>
        </div>
        <div class="map_bottom">
            <div class="tit">{{activeMapInfo.title}}</div>
            <p class="addr">{{activeMapInfo.address}}</p>
            <div class="location_icon" @click="linkRoute"></div>
        </div>
    </div>
</template>


<script>
    export default{
        components:{

        },
        data(){
            return{
               searchText:this.$route.query.type,
               type:this.$route.query.type,
               map:{},
               geolocation:{},
               lng:'',
               lat:'',
               zoomDefault:15,
               searchResult:[],
               activeMapInfo:{},
               loading:{}
            }
        },
        methods: {
           clickToNameList(){
                this.$router.push({ path: '/mapAroundNameList' ,query:{lng:this.lng,lat:this.lat,type:this.type}})
           },
           zoomIn(){//放大
                if(this.zoomDefault < 19){
                    this.map.setZoom(++this.zoomDefault);
                }
                
            },
            zoomOut(){//缩小
                if(this.zoomDefault > 3){
                    this.map.setZoom(--this.zoomDefault);
                }
                
            },
            linkSearch(){
                this.$router.push({ path: '/mapSearchList' ,query:{lng:this.activeMapInfo.point.lng,lat:this.activeMapInfo.point.lat}});
            },
            linkRoute(){//跳转到去这里页面
                this.$router.push({ path: '/mapAroundRoute' ,query:{lng:this.activeMapInfo.point.lng,lat:this.activeMapInfo.point.lat,title:this.activeMapInfo.title,mylng:this.lng,mylat:this.lat}});
            },
            onSearchComplete(results){//本地检索数据接口
                // 判断状态是否正确
                if (this.local.getStatus() == BMAP_STATUS_SUCCESS){
                    var s = [];
                    this.searchResult.length = 0;
                    for (var i = 0; i < results.getCurrentNumPois(); i ++){
                        s.push(results.getPoi(i).title + ", " + results.getPoi(i).address);
                        this.searchResult.push(results.getPoi(i));
                        //console.log(results.getPoi(i));
                    }
                }
            },
           getLocation(initFunc){//定位
                var self = this;
                this.geolocation.getCurrentPosition(function(r){
                    if(this.getStatus() == BMAP_STATUS_SUCCESS){
                        var mk = new BMap.Marker(r.point);
                        self.map.addOverlay(mk);
                        self.map.panTo(r.point);
                        self.lng = r.point.lng;
                        self.lat = r.point.lat;
                        console.log(self.lng+'   '+self.lat);
                        self.loading.hide();//加载中 
                        if(initFunc){
                            initFunc(self.lng,self.lat);
                        }
                    }
                    else {
                        alert('failed'+this.getStatus());
                    }        
                },{enableHighAccuracy: true});

            },
            onInfoHtmlSet(a,b){
                console.log(a);
                this.activeMapInfo = a;
            },
            getInitMap(lng,lat){
                // http://localhost:8050/#/mapAroundRoute?lng=120.213647&lat=30.265591&title=%E4%B9%90%E8%B4%AD%E8%B6%85%E5%B8%82%28%E5%BA%86%E6%98%A5%E5%BA%97%29
               
                var point = new BMap.Point(lng,lat);
                this.map.centerAndZoom(point,this.zoomDefault);
                this.map.enableScrollWheelZoom(true);
                //var circle = new BMap.Circle(point,1000,{fillColor:"blue", strokeWeight: 1 ,fillOpacity: 0.1, strokeOpacity: 0});
                //this.map.addOverlay(circle);
                //
                
                this.local =  new BMap.LocalSearch(this.map, {renderOptions: {map: this.map, autoViewport: false},onInfoHtmlSet:this.onInfoHtmlSet,onSearchComplete:this.onSearchComplete});
                //this.local.search(decodeURI(this.type));
                //console.log(decodeURI(this.type));
                this.local.searchNearby(decodeURI(this.type),point,1000);
            },

        },
        mounted(){
            this.map = new BMap.Map("allmap");
            this.geolocation = new BMap.Geolocation();
            this.getLocation(this.getInitMap);
            this.loading = weui.loading('加载中', {//loading
            });
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/mapAroundDetail.less";
</style>
