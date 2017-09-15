
<template>
    <div class="wrapper">
        <router-link :to="{path:'/mapSearchList',query:{lng:lng,lat:lat}}" v-if="lng && lat" class="search_box">搜索地址</router-link>
        <div class="mapView" id="allmap" style="height:100%;">
            
        </div>
        <div class="location" @click="getLocation"></div>
        <div class="zoom">
            <ul>
                <li @click="zoomIn" :class="{unclick:zoomDefault == 19}">+</li>
                <li @click="zoomOut" :class="{unclick:zoomDefault == 3}">-</li>
            </ul>
        </div>
        <router-link :to="{path:'/mapAroundList'}" class="search_around"><i></i>搜周边</router-link>
    </div>
</template>


<script> 
    export default{
        components:{

        },
        data(){
            return{
                map:{},
                zoomDefault:17,
                geolocation:{},
                lng:'',
                lat:''
            }
        },
        methods: {
           addMarker(point){
                var marker = new BMap.Marker(point);
                this.map.addOverlay(marker);
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
            getLocation(){//定位
                var self = this;
                this.geolocation.getCurrentPosition(function(r){
                    if(this.getStatus() == BMAP_STATUS_SUCCESS){
                        var mk = new BMap.Marker(r.point);
                        self.map.addOverlay(mk);
                        self.map.panTo(r.point);
                        self.lng = r.point.lng;
                        self.lat = r.point.lat;
                    }
                    else {
                        alert('failed'+this.getStatus());
                    }        
                },{enableHighAccuracy: true})
            }
        },
        mounted(){
            this.map = new BMap.Map("allmap");
            var point = new BMap.Point(120.114225,29.340851);
            this.map.centerAndZoom(point, this.zoomDefault);
            this.addMarker(point);
            this.geolocation = new BMap.Geolocation();
            this.getLocation();
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/map.less";
</style>
