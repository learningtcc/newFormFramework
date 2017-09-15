<template>
  <section>
    <div class="userInfo">
      <div class="userInfoFix">
        <dl>
          <dt><span class="tit">电话</span></dt>
          <dd>
            <input type="text" placeholder="请输入电话" class="common_input" v-model="tel" maxlength="11">
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
        tel:''
      }
    },
    methods:{
      submit(){
        var result = {error:false, msg:""};
        testTel(this.tel, result);
        testEmpty(this.tel, result, '手机号不能为空');
        if(result.error){
          weui.topTips(result.msg,1000);
          return;
        }
        this.axios.post(config.updateUserTel, querystring.stringify({//提交电话
          'tel':this.tel
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

              this.tel = res.data.data.tel;

          } else {
            
          }
          
      })
    }
  }
</script>
<style scoped type="text/less" lang="less">
  @import "../../assets/css/userInfo.less";
</style>