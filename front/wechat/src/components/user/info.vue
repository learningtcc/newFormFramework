
<template>
    <div class="wrapper">
        <div class="person">
          <div class="head">
            <p>头像</p>
            <img :src="detail.headImageUrl">
          </div>
          <router-link :to="{path:'/userInfoName'}" class="info_list">
            <p>昵称</p>
            <span><em>{{detail.nickName}}</em><i class="arrow"></i></span>
          </router-link>
          <div class="info_list">
            <p>性别</p>
            <span><em>{{detail.sex}}</em></span>
          </div>
          <div class="info_list">
            <p>地区</p>
            <span><em>{{detail.province + detail.city}}</em></span>
          </div>
          <router-link :to="{path:'/userInfoTel'}" class="info_list">
            <p>联系电话</p>
            <span><em>{{detail.tel}}</em><i class="arrow"></i></span>
          </router-link>
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
               detail:{},
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
    @import "../../assets/css/userInfo.less";
</style>
