
<template>
    <div class="wrapper">
        <div class="msg_d">
          <h3>{{detail.title}}</h3>
          <p>{{detail.time}} <span class="el-icon-delete2" @click="deleteNotice(detail)">  删除</span></p>
        </div>
        <p class="msg_cont" v-html="detail.content"></p>
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
                detail:{}
            }
        },
        methods: {
           getDetail(){
                //var loading = weui.loading('加载中', {});
                this.axios.post('/wechat/user/getUserMessage', querystring.stringify({//详情
                    'id':this.id
                }))
                .then(res => {
                    //loading.hide();//加载
                    if(res.data.success){

                        this.detail = res.data.data;

                    } else {
                      
                    }
                    
                })
           },
           deleteNotice(item){//是否删除消息
              weui.dialog({
                  title: '',
                  content: '是否删除此消息',
                  buttons: [{
                      label: '取消',
                      type: 'default',
                      onClick:()=> { 

                      }
                  }, {
                      label: '确定',
                      type: 'primary',
                      onClick: ()=> {
                        this.deleteNoticeData(item);
                      }
                  }]
              });
            },
            deleteNoticeData(item){
                this.axios.post("/wechat/user/deleteUserMessage", querystring.stringify({
                  'id': item.id
                }))
                .then(res => {
                  if(res.data.success){
                      weui.toast('删除成功', {//提示  
                          duration: 1000,
                          callback:()=>{
                            this.$router.go(-1);
                            //self.$router.push({ path: '/user-address'});
                          }
                      });
                  }
                  
                })
                .catch(error => {
                    
                });
            }
        },
        mounted(){
            this.getDetail();
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/message.less";
</style>
