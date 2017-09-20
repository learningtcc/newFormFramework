<template>
    <div class="wrapper">
        <div class="type_tab" v-if="isTripModeShow">
            <ul>
                <li :class="{active:typeActive == index}" v-for="(item,index) in typeList" @click="checkTypeTab(item,index),typeList[index].click(item,index)">
                    <a href="javascript:;"></a>
                </li>
            </ul>
        </div>
        <div class="mapView" id="allmap">
            
        </div>
        <div class="route_bottom">
            <div class="route-location-wrap">
                <div class="location" @click="getLocationAndFocus"></div>
                <div class="zoom">
                    <ul>
                        <li @click="zoomIn" :class="{unclick:zoomDefault == 19}">+</li>
                        <li @click="zoomOut" :class="{unclick:zoomDefault == 3}">-</li>
                    </ul>
                </div>
            </div>
            
            <div class="routelist" v-if="isTripModeShow">
                <!-- bus -->
                <ul class="weui-flex" v-if="typeActive == 0">
                    <li class="weui-flex__item" v-if="transitOutput.length == 0"><div class="tit">暂无</div></li>
                    <li class="weui-flex__item" v-for="item in typeFilters(transitOutput)">
                        <div class="tit">{{item.title | busLine}}</div>
                        <div class="time">{{item.duration}}</div>
                        <div class="distance">{{item.distance}}</div>
                    </li>
                </ul>
                <!-- taxi -->
                <ul class="weui-flex" v-if="typeActive == 1">
                    <li class="weui-flex__item" v-if="drivingOutput.length == 0"><div class="tit">暂无</div></li>
                    <li class="weui-flex__item" v-for="(item,index) in drivingOutput">
                        <div class="tit" v-if="index == 0">{{item.titleA}}</div>
                        <div class="tit" v-if="index == 1">{{item.titleB}}</div>
                        <div class="time">{{item.duration}}</div>
                        <div class="distance">{{item.distance}}</div>
                    </li>
                </ul>
                <!-- walking -->
                <ul class="weui-flex" v-if="typeActive == 2">
                    <li class="weui-flex__item" v-if="walkingOutput.duration != undefined">
                        <div class="tit">步行路线</div>
                        <div class="time">{{walkingOutput.duration}}</div>
                        <div class="distance">{{walkingOutput.distance}}</div>
                    </li>
                </ul>
            </div>
            <div class="info_about" v-if="isTripModeShow">
                <div class="top">
                    <!-- <span class="traffic_light">途经红绿灯 23个</span> -->
                    <span class="taxi_fare" v-if="typeActive == 1">打车 {{taxiFare.totalFare}}元</span>
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
              map:{},
              lng:this.$route.query.lng,
              lat:this.$route.query.lat,
              title:this.$route.query.title,
              mylng:this.$route.query.mylng,
              mylat:this.$route.query.mylat,
              zoomDefault:15,
              geolocation:{},
              marker:{},
              geoc:{},
              addComp:{},
              mk:null,
              typeList:[
                    {name:'公交',active:false,click:this.clickBusInfo},
                    {name:'驾车',active:false,click:this.clickCarInfo},
                    {name:'步行',active:false,click:this.clickWalkingInfo},
                ],
               typeActive:0,
               isTripModeShow:false,
               transit:{},
               transitOutput:[],
               driving:{},
               drivingOutput:[],
               walking:{},
               walkingOutput:{},
               taxiFare:{}

            }
        },
        filters:{
            busLine(val){
                if(val == undefined){
                    return '';
                }
                return val.split('(')[0];
            }
        },
        methods: {
            checkTypeTab(item,index){
                this.typeActive = index;
            },
            clickToTripMode(){//点击去这里，跳到出行方式
                console.log("haha");
                this.isTripModeShow = true;
                this.marker.closeInfoWindow();
                this.marker.removeEventListener("click", this.clickInfo);
                this.getBusInfo();
            },
            clickBusInfo(item,index){//公交
                if(!item.active){
                    item.active = !item.active;
                    this.getBusInfo();
                }
                //清空清除最近一次检索的驾车结果
                if(this.driving.clearResults != undefined){
                    this.driving.clearResults();
                }
                
                this.typeList[1].active = false;
                //清空清除最近一次检索的步行结果
                if(this.walking.clearResults != undefined){
                    this.walking.clearResults();
                }
                this.typeList[2].active = false;
            },
            clickCarInfo(item,index){//驾车路线
                if(!item.active){
                    item.active = !item.active;
                    this.getDrivingInfo();
                }
                //清空清除最近一次检索的公交结果
                if(this.transit.clearResults != undefined){
                    this.transit.clearResults();
                }
                this.typeList[0].active = false;
                //清空清除最近一次检索的步行结果
                if(this.walking.clearResults != undefined){
                    this.walking.clearResults();
                }
                this.typeList[2].active = false;
            },
            clickWalkingInfo(item,index){//步行路线
                if(!item.active){
                    item.active = !item.active;
                    this.getWalkingInfo();
                }
                //清空清除最近一次检索的公交结果
                if(this.transit.clearResults != undefined){
                    this.transit.clearResults();
                }
                this.typeList[0].active = false;
                //清空清除最近一次检索的驾车结果
                if(this.driving.clearResults != undefined){
                    this.driving.clearResults();
                }
                this.typeList[1].active = false;
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
            clickInfo(){//弹出信息窗口
                var self = this;
                var opts = {
                    width : 260,     // 信息窗口宽度
                };
                var addr = self.addComp.province + self.addComp.city + self.addComp.district + self.addComp.street + self.addComp.streetNumber;
                var sContent =
                    "<div class='mapAroundRouteTips'><h4 style='margin:0 0 5px 0;padding:0.2em 0;display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 2;overflow: hidden;'>" + self.title+ "</h4>" +  
                    "<p style='margin:0;line-height:1.5;font-size:13px;overflow: hidden; white-space: nowrap; text-overflow: ellipsis;'>"+ addr +"</p></div>" + "<div class='mapRouteBtn' id='mapRouteBtn'><em></em>去这里</div><div style='clear:both'></div>";
                var infoWindow = new BMap.InfoWindow(sContent, opts);  // 创建信息窗口对象         
                infoWindow.addEventListener("open", function(){
                    console.log("infoWindow open:");
                    console.log(document.getElementById("mapRouteBtn"));
                    document.getElementById("mapRouteBtn").onclick = self.clickToTripMode;
                });
                infoWindow.addEventListener("click", function(){
                    console.log("infoWindow click:");
                });
                self.marker.openInfoWindow(infoWindow);
                   
                
            },
           theLocation(){// 用经纬度设置地图中心点
                if(this.lng && this.lat){
                    var self = this;
                    this.map.clearOverlays(); 
                    var new_point = new BMap.Point(this.lng,this.lat);
                    this.marker = new BMap.Marker(new_point);  // 创建标注
                    this.map.addOverlay(this.marker);              // 将标注添加到地图中
                    this.map.panTo(new_point);
                    this.geoc = new BMap.Geocoder();
                    this.geoc.getLocation(new_point, function(rs){
                        self.addComp = rs.addressComponents;
                        console.log(self.marker);
                        self.clickInfo();
                        self.marker.addEventListener("click", self.clickInfo);
                    });
                    
                    
                }
            },
            typeFilters(arr){
                if(arr == undefined){
                    return [];
                }
                return arr.slice(0,5);
            },
            getLocationAndFocus() {
                var self = this;
                self.getLocation(function(){
                    var p = new BMap.Point(self.mylng, self.mylat);
                    if (!self.mk) {
                        self.mk = new BMap.Marker(p);
                        self.map.addOverlay(self.mk);
                    }
                   
                    self.map.panTo(p);
                });
            },
            getLocation(fn){//定位
                var self = this;
                this.geolocation.getCurrentPosition(function(r){
                    if(this.getStatus() == BMAP_STATUS_SUCCESS){
                        self.mylng = r.point.lng;
                        self.mylat = r.point.lat;
                        fn();
                    }
                    else {
                        alert('failed'+this.getStatus());
                    }        
                },{enableHighAccuracy: true})
            },
            ensureMyPoint(fn){
                if(this.mylng && this.mylat){
                    fn();
                } else {
                    this.getLocation(fn);
                }
            },
            busSearchComplete(results){
                if (this.transit.getStatus() != BMAP_STATUS_SUCCESS){
                    return ;
                }
                var planNum = parseInt(results.getNumPlans());
                var planArr = [];
                for(var i = 0; i < planNum ; i++){
                    var plan = results.getPlan(i);
                    var title = plan.getNumLines() > 0 ? plan.getLine(0).title : "";
                    planArr.push({duration:plan.getDuration(true),distance:plan.getDistance(true),title:title});
                }
                this.transitOutput = planArr;
                console.log(results.getPlan(0).getLine(0));
                console.log("busNumPlans : " + results.getNumPlans());
            },
            getBusInfo(){
                this.ensureMyPoint(this.getBusInfoCall);
            },
            getBusInfoCall(){//公交
                var p1 = new BMap.Point(this.lng,this.lat);
                var p2 = new BMap.Point(this.mylng,this.mylat);
                this.transit = new BMap.TransitRoute(this.map, {renderOptions: {map: this.map},onSearchComplete: this.busSearchComplete
                });
                this.transit.search(p2, p1);
            },
            drivingSearchComplete(results){//驾车回调
                if (this.driving.getStatus() != BMAP_STATUS_SUCCESS){
                    return ;
                }
                console.log(results);
                this.taxiFare = results.taxiFare.day;
                var planNum = parseInt(results.getNumPlans());
                var planArr = [];
                var fast;
                var short;
                for(var i = 0; i < planNum ; i++){
                    var plan = results.getPlan(i);
                    var obj = {duration:plan.getDuration(true)
                        , distance:plan.getDistance(true)
                        , durationValue: plan.getDuration(false)
                        , distanceValue:plan.getDistance(false)
                    };
                    if (fast) {
                        fast = fast.durationValue < obj.durationValue ? fast : obj;
                    } else {
                        fast = obj
                    }

                    if (short) {
                        short = short.distanceValue < short.distanceValue ? short : obj;
                    } else {
                        short = obj;
                    }
                }

                if (fast) {
                    fast.titleA = "较快路线";
                    planArr.push(fast);
                }

                if (short) {
                    short.titleB = "较短路线";
                    planArr.push(short);
                }

                this.drivingOutput = planArr;
                console.log("drivingPlanNum:" + results.getNumPlans());
                // var drivingOutput = '';
                // drivingOutput += plan.getDuration(true) + "\n";                //获取时间
                // drivingOutput += "总路程为：" ;
                // drivingOutput += plan.getDistance(true) + "\n";//获取距离

                
            },
            getDrivingInfo(){
                this.ensureMyPoint(this.getDrivingInfoCall);
            },
            getDrivingInfoCall(){//驾车
                var p1 = new BMap.Point(this.lng,this.lat);
                var p2 = new BMap.Point(this.mylng,this.mylat);
                this.driving = new BMap.DrivingRoute(this.map, {renderOptions:{map: this.map, autoViewport: true},onSearchComplete: this.drivingSearchComplete});
                this.driving.search(p2, p1);
            },
            walkingSearchComplete(results){//步行回调
                if (this.walking.getStatus() != BMAP_STATUS_SUCCESS){
                    return ;
                }

                var plan = results.getPlan(0);
                if(plan){
                    this.walkingOutput = {duration:plan.getDuration(true),distance:plan.getDistance(true)};
                }
                console.log(results.getPlan(0));
                console.log("walkPlanNum : " + results.getNumPlans());
                
            },
            getWalkingInfo(){
               this.ensureMyPoint(this.getWalkingInfoCall); 
            },
            getWalkingInfoCall(){//步行
                var p1 = new BMap.Point(this.lng,this.lat);
                var p2 = new BMap.Point(this.mylng,this.mylat);
                this.walking = new BMap.WalkingRoute(this.map, {renderOptions: {map: this.map, autoViewport: true},onSearchComplete: this.walkingSearchComplete});
                this.walking.search(p2, p1);
            }
        },
        mounted(){
            if(!this.title){
                this.title = '';
            }
            this.map = new BMap.Map("allmap");
            var point = new BMap.Point(this.lng,this.lat);
            this.map.centerAndZoom(point, this.zoomDefault);
            this.map.enableScrollWheelZoom(true);
            this.theLocation();
            this.geolocation = new BMap.Geolocation();
        }
            
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/mapAroundRoute.less";
</style>
<style lang="less">
    .anchorBL{
        display: none;
    }
    .mapAroundRouteTips{
        width: 3.0645rem;
        float: left;
    }
    .mapRouteBtn{
        width: 2.4194rem;
        height: 0.9677rem;
        line-height: 0.9677rem;
        margin:0.121rem 0.4839rem 0 0;
        background: #222;
        color: #fff;
        font-size: 0.3871rem;
        text-align:center;
        border-radius:0.0968rem;
        -webkit-border-radius:0.0968rem;
        float: right;
        em{
            width: 0.5323rem;
            height: 100%;
            margin: 0 0.1774rem 0 0;
            display:inline-block;
            vertical-align: top;
            background: url(../../assets/img/map/mapRoutebtn.png) center center no-repeat;
            background-size: 100% auto;
        }
    }
    .panoInfoBox{
        visibility: hidden;
    }
</style>
