
<template>
    <div class="wrapper">
        <div class="logistic_hd">
            <span class="icon"></span>
            <div class="head_rt">
                <p class="name">快递公司：{{detail.logistics_company}}</p>
                <p>快递单号：{{detail.LogisticCode}}</p>
            </div>
        </div>
        <div class="logistic_info">
            <p class="hd">物流进程</p>
            <div class="logistic_list">
                <div class="list_in">
                    <ul>
                        <li v-for="item in lists">
                            <span class="tit">{{item.AcceptStation}}</span>
                            <span class="time">{{item.AcceptTime}}</span>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</template>


<script>
    var querystring = require('querystring');
    export default{
        components:{

        },
        data(){
            return{
               id:this.$route.query.id,
               lists:[],
               detail:{}
            }
        },
        methods: {
           
        },
        mounted(){
            var loading = weui.loading('加载中', {});
            this.axios.get('/wechat/Logistics/detail', {params:{
                'orderId':this.id
            }})
            .then(res => {
                loading.hide();//加载
                if(res.data.success){
                    this.detail = res.data.data;
                    this.lists = res.data.data.Traces;

                }
                
            })
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/myorder_logistics.less";
</style>
