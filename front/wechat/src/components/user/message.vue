
<template>
    <div class="wrapper">
        <router-link :to="{path:'/messageDetail'}" class="system">
          <el-badge is-dot class="tit" :hidden="false">数据查询</el-badge>
          <span class="time">2017-08-30</span>
          <div class="msg_cont">
            <p class="cont">sdfsdfsfs</p>
            <span class="el-icon-delete2">删除</span>
          </div>
        </router-link>
        <router-link :to="{path:'/messageDetail'}" class="system">
          <el-badge is-dot class="tit" :hidden="false">数据查询</el-badge>
          <span class="time">2017-08-30</span>
          <div class="msg_cont">
            <p class="cont">sdfsdfsfs</p>
            <span class="el-icon-delete2">删除</span>
          </div>
        </router-link>
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
    @import "../../assets/css/message.less";
</style>
<style lang="less">
  .system .el-badge__content.is-fixed.is-dot{
      top:0.2419rem!important;
      right: -0.121rem;
  }
</style>
