<template>
    <div class="myInteraction">
        <div class="loading" v-if="ishide"></div>
        <div class="interactionCon" v-if="ishide2" v-for="(list,index) in list">
            <div class="interactionHead">
                <router-link :to="{path:'/interactionDetail',query:{id:list.id}}">
                  <img :src="list.headImageUrl">
                </router-link>
                <router-link :to="{path:'/interactionDetail',query:{id:list.id}}">
                  <div class="interactionName">
                      <p class="interactionN">{{list.publisherNickname}}</p>
                      <p class="interactionTime">{{list.publisherTime}}</p>
                  </div>
                 </router-link>
                <p class="delete" @click="delelist(list,index)"></p>
            </div>
         
            <div class="interactionIntro">
              <router-link :to="{path:'/interactionDetail',query:{id:list.id}}">
                <div class="interactionTitle">{{list.interactionTitle}}</div>
                <div class="interactionC">{{list.interactiveContent}}</div>
              </router-link>
                <div>
                    <img v-for="listImg in list.picList" :src="listImg" @click="bigimgs(list)">
                </div>            
                <p class="comment">{{list.commentNum}}</p>
            </div>
        </div>
        <router-link :to="{path:'/myInteractionR',query:{}}">
            <div class="posted"></div>
        </router-link>
    </div>
</template>


<script>
  var qs = require('qs');
  export default {
    data () {
      return {
        ishide:true,
        ishide2:false,
        curpages:1,
        pagesizes:5,
        loading:false,
        load:false,
        list:[],
        isLoading:true
      }
    },
    methods:{
      delelist(list,index){
        var self=this;
        console.log(index);
        self.load=true;
        self.axios.post("/wechat/interactive/deleteInteractiveContent",
          qs.stringify({
            id:list.id
          })
        ).then(function(res){
          console.log(res.data);
          if(res.data.success){
            self.load=false;
            self.list.splice(index,1)
          }else {
            self.load=false;
            alert(res.data.message)
          }

        },function(response){

        })
      },
      bigimgs(list){
        var self=this;
        var hotimg=[];
        for(var i=0;i<list.cms_material_info.length;i++){
          hotimg.push(list.cms_material_info[i].url);
          if(i==list.cms_material_info.length-1){
            wx.previewImage({
              current: '', // 当前显示图片的http链接
              urls:hotimg // 需要预览的图片http链接列表
            });
          }
        }
      },
      getData(){
        var self=this;
        self.axios.post("/wechat/user/userInteractive",
          qs.stringify({
            //to_examine:"CHECK_PASS",
            current_page:self.curpages,
            page_size:self.pagesizes
          })
        ).then(function(res){
          if(res.data.success){
            self.ishide=false;
            self.ishide2=true;
            self.list=res.data.data.data;
            /*if(self.curpages==1){
              self.list=res.data.data.data;
              self.isLoading=true;
              if(data.total_page==1){
                self.isLoading=false;
              }
            }else {
              if(self.curpages>=data.total_page){
                self.list = self.list.concat(data.data);
                self.isLoading=false;
              }else {
                self.list = self.list.concat(data.data);
                self.isLoading=true;
              }
            }*/
          }else {
            self.ishide=false;
            self.ishide2=true;
            alert(res.data.message)
          }
        },function(response){

        })
      }
    },
    mounted() {
      var self=this;
      setDocumentTitle("我的互动");
      self.getData();
      /*var pageloader = new Pagination({
        container: '.myInteraction',
        containerScroll:true,
        loadMoreBottom: 50,
        loadMore: function () {
          if (self.isLoading) {
            self.isLoading=false;
            self.curpages++;
            self.getList();
          }
        }
      }).init();*/
      //微信授权
      /*var url = location.href.split('#')[0];
      self.axios.post('/wx/macro/weixin/sign',qs.stringify({ url: url })).then(function(p){
        var resData = p.data.data;
        wx.config({
          debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
          appId: resData.appId, // 必填，企业号的唯一标识，此处填写企业号corpid
          timestamp: resData.timestamp, // 必填，生成签名的时间戳
          nonceStr: resData.noncestr, // 必
          // 填，生成签名的随机串
          signature: resData.Signa,// 必填，签名，见附录1
          jsApiList : ['previewImage']
          // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });
      },function(response){

      })*/

    }
  }
</script>
<style lang="less" scoped>
    @import "../../assets/css/myInteraction.less";
</style>