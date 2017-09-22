<template>
    <div>
      <div class="loading" v-if="ishide"></div>
      <div class="interactionList" v-if="ishide2">
          <div class="interactionCon" v-for="lis in list.data">
            <router-link :to="{path:'/interactionDetail',query:{id:lis.id}}">
              <div class="interactionHead">
                  <img :src="(lis.headImageUrl==''||lis.headImageUrl==null)?defalutImg:lis.headImageUrl">
                  <div class="interactionName">
                      <p class="interactionN">{{lis.publisherNickname}}</p>
                      <p class="interactionTime">{{lis.publisherTime}}</p>
                  </div>
              </div>
            </router-link>
              <div class="interactionIntro">
                <router-link :to="{path:'/interactionDetail',query:{id:lis.id}}">
                  <div class="interactionTitle">{{lis.interactionTitle}}</div>
                  <div class="interactionC">{{lis.interactiveContent}}</div>
                </router-link>
                  <div>
                      <img v-for="listImg in lis.picList" :src="listImg" @click="bigimgs(lis)">
                  </div>
                  <p class="comment">{{lis.commentNum}}</p>
              </div>
          </div>
          <div class="posted" @click="postedBtn"></div>
      </div>
    </div>
</template>


<script> 
    var qs = require('qs');
    export default{
        data () {
            return {
                defalutImg:'/static/img/face.jpg',
                list:{},
                currentpage:1,
                pagesize:5,
                ishide:true,
                ishide2:false,
                interactivThemeFk:this.$route.query.id,
                hotimg:[],
            }
        },
        methods: {
            bigimgs(lis){
              var self=this;
              for(var i=0;i<lis.picList.length;i++){
                self.hotimg.push(lis.picList[i]);
                //console.log(lis.picList[i]);
                //console.log(lis.picList.length)
                if(i==lis.picList.length-1){
                  wx.previewImage({
                    current: '', // 当前显示图片的http链接
                    urls: self.hotimg // 需要预览的图片http链接列表
                  });
                }
              }
            },
            postedBtn(){
              var self=this;
              self.$router.push({path:'/interactionRelease',query:{id:self.interactivThemeFk}})
            },
            getlist(){
                var self=this;
                self.axios.post("/wechat/interactive/getInteractiveContentList?id="+self.interactivThemeFk,
                    qs.stringify({
                        page_size:self.pagesize,
                        current_page:self.currentpage,
                        interactivThemeFk:self.interactivThemeFk,
                    })
                ).then(function(res){
                    if(res.data.success){
                      self.ishide=false;
                      self.ishide2=true;
                      self.list=res.data.data;
                    }else {
                      self.ishide=false;
                      self.ishide2=true;
                      alert(res.data.message)
                    }
                },function(response){

              })
            }
        },
        mounted(){
           var self=this;
           self.getlist()
        }
    }
</script>
<style lang="less" scoped>
    @import "../../../assets/css/interactionList.less";
</style>