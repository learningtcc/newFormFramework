<template>
  <section>
    <div class="userInfo">
      <div class="userInfoFix">
        <dl>
          <dt><span class="tit">昵称</span></dt>
          <dd>
            <input type="text" placeholder="请输入昵称" class="common_input" v-model="name">
          </dd>
        </dl>
      </div>
      <div class="submit_out">
        <div class="submit_btn" @click="submit">确认</div>
      </div>
      
    </div>
  </section>
</template>
<script>
  import config from '../../../static/js/config/index.js';
  var querystring = require('querystring');
  export default {
    data () {
      return {
        name:''
      }
    },
    methods:{
      submit(){
        var result = {error:false, msg:""};
        testEmpty(this.name, result, '昵称不能为空');
        if(result.error){
          weui.topTips(result.msg,1000);
          return;
        }
        this.axios.post(config.updateUserInfo, querystring.stringify({//提交个人信息
          'nickName':this.name
        }))
        .then(res => {
            if(res.data.success){
                weui.toast('保存成功', {//提示  
                    duration: 1000,
                    callback: () => {
                      this.$router.go(-1);
                    }
                });
            }else{
              weui.topTips('请求失败', 1000);
            }
            
        })
      }
    },
    mounted(){
      this.axios.post(config.userInfo, querystring.stringify({//个人信息
      }))
      .then(res => {
          if(res.data.success){

              this.name = res.data.data.nickName;

          } else {
            
          }
          
      })
    }
  }
</script>
<style scoped type="text/less" lang="less">
  @import "../../assets/css/userInfo.less";
</style>