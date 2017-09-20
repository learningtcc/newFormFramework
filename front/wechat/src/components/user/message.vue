
<template>
    <div class="wrapper">
        <router-link :to="{path:'/messageDetail',query:{id:item.id}}" class="system" v-for="item in lists" :key="item.id">
          <el-badge is-dot class="tit" :hidden="isRead(item)">{{item.title}}</el-badge>
          <span class="time">{{item.time}}</span>
          <div class="msg_cont">
            <p class="cont" v-html="item.content"></p>
            <span class="el-icon-delete2" @click.stop.prevent="deleteNotice(item)">删除</span>
          </div>
        </router-link>
        <infinite-loading :on-infinite="onInfinite" ref="infiniteLoading">
            <span slot="no-more">
              加载完
            </span>
            <span slot="no-results">
              暂无
            </span>
        </infinite-loading>
    </div>
</template>


<script>
    var querystring = require('querystring');
    import InfiniteLoading from 'vue-infinite-loading';
    export default{
        components:{
          InfiniteLoading,
        },
        data(){
            return{
               lists:[],
               totalPage:1,
               currentPage:0,
            }
        },
        methods: {
            isRead(item){
              if(item.isRead == 'N'){
                return false;
              } else {
                return true;
              }
              
            },
            onInfinite() {//上拉加载
              //console.log("onInfinite");
              var self = this;
             
              if(this.currentPage < this.totalPage){

                function load(){
                  self.axios.post('/wechat/user/getUserMessageList', querystring.stringify({
                    'current_page':self.currentPage + 1,
                    'page_size':10
                  }))
                  .then(response => {
                    //console.log(response.data);
                   
                    if(response.data.success == true){
                      let record = response.data.data;
                      self.currentPage = record.current_page;
                      self.totalPage = record.total_page;
                      self.lists = self.lists.concat(record.data);
                      if(self.lists.length == 0){

                        self.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');
                        return;
                      }
                    }
                    self.$refs.infiniteLoading.$emit('$InfiniteLoading:loaded');
                    

                  })
                  .catch(error => {
                    self.$refs.infiniteLoading.$emit('$InfiniteLoading:loaded');
                    console.log(error);
                  });
                }

                setTimeout(() => {
                  load();
                },200);

              } else {
                self.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');
              } 
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
                      var arr = this.lists;
                      var index = arr.indexOf(item)
                      if(index != -1){
                        arr.splice(index, 1);
                      }
                      weui.toast('删除成功', {//提示  
                          duration: 1000,
                          callback:()=>{
                            //self.$router.go(-1);
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
