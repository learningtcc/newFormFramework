
<template>
    <div class="wrapper">
        <router-link :to="{path:'/userInfo',query:{}}" class="u_head">
            <div class="avatar">
                <img src="../../assets/img/temp/detail_avatar.jpg" alt="">
            </div>
            <div class="nickname">无梦</div>
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
    @import "../../assets/css/userCenter.less";
</style>
