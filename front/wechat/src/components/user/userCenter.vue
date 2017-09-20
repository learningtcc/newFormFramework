
<template>
    <div class="wrapper">
        <router-link :to="{path:'/userInfo',query:{}}" class="u_head">
            <div class="avatar">
                <img :src="detail.headImageUrl" alt="">
            </div>
            <div class="nickname">{{detail.nickName}}</div>
            <span class="arrowR"></span>
        </router-link>
        <div class="userCenter_list">
            <ul>
              <li>
                <router-link :to="{path:'/myOrder'}" >
                  <em class="icon"><img src="../../assets/img/user/ucenter_order.png" alt=""></em>
                  我的订单
                  <p class="arrow"></p>
                </router-link>
              </li>
              <li>
                <router-link :to="{path:'/myFavor'}" >
                  <em class="icon"><img src="../../assets/img/user/ucenter_favor.png" alt=""></em>
                  我的收藏
                  <p class="arrow"></p>
                </router-link>
              </li>
              <li>
                <router-link :to="{path:'/myInteraction'}" >
                  <em class="icon"><img src="../../assets/img/user/ucenter_interaction.png" alt=""></em>
                  我的互动
                  <p class="arrow"></p>
                </router-link>
              </li>
            </ul>
        </div>
        <div class="userCenter_list">
            <ul>
              <li>
                <router-link :to="{path:'/addressList'}" >
                  <em class="icon"><img src="../../assets/img/user/ucenter_addr.png" alt=""></em>
                  地址管理
                  <p class="arrow"></p>
                </router-link>
              </li>
              <li>
                <router-link :to="{path:'/myMessage'}" >
                  <em class="icon"><img src="../../assets/img/user/ucenter_message.png" alt=""></em>
                  消息通知
                  <p class="arrow"></p>
                </router-link>
              </li>
            </ul>
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
               detail:{}
            }
        },
        methods: {
           
        },
        mounted(){
            var loading = weui.loading('加载中', {//loading
            });
            this.axios.post('/wechat/user/userInfo', querystring.stringify({//个人信息
            }))
            .then(res => {
                loading.hide();//加载
                if(res.data.success){

                    this.detail = res.data.data;

                } else {
                  
                }
                
            })
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/userCenter.less";
</style>
