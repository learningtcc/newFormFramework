<template>
    <div class="interactionDetail">
        <div class="interactionCon">
            <div class="interactionHead">
                <img :src="detail.interactiveContent.headImageUrl">
                <div class="interactionName">
                    <p class="interactionN">{{detail.interactiveContent.publisherNickname}}</p>
                    <p class="interactionTime">{{detail.interactiveContent.publisherTime}}</p>
                </div>
            </div>
            <div class="interactionIntro">
                <div class="interactionTitle">{{detail.interactiveContent.interactionTitle}}</div>
                  <div class="interactionCon">{{detail.interactiveContent.interactiveContent}}</div>
                <div>
                    <img v-for="listImg in detail.interactiveContent.picList" :src="listImg" @click="bigimgs(detail)">
                </div>
                <p class="comment">{{detail.interactiveContent.commentNum}}</p>
            </div>
        </div>
        <div class="commentList">
            <p class="commentNum">共有<span>{{detail.interactiveContent.commentNum}}条</span>评论</p>
            <div class="commentCon" v-for="commentList in detail.interactiveEvaluation.data">
                <div class="interactionHead">
                    <img :src="detail.interactiveContent.headImageUrl">
                    <div class="interactionName">
                        <p class="interactionN">{{commentList.reviewerNickname}}</p>
                        <p class="interactionTime">{{commentList.evaluationTime}}</p>
                    </div>
                </div>
                <div class="commentIntro">{{commentList.evaluationContent}}</div>
            </div>
        </div>
        <div class="publishComment">
            <input type="text" placeholder="发表评论..." v-model="evaluationContent">
            <div class="pink-btn" @click="coms(hintType)">发 表</div>
        </div>
        <!--温馨提示-->
        <div class="popup" v-show="hintType.isShow">
        <div class="hintWarp">
          <div class="popupHint">
            <div class="title">温馨提示</div>
            <p class="cont">您提交的评论信息已经上传，工作人员会在1-2个工作日之内审核，审核通过您的评论将会发布在评论列表。</p>
            <div class="btn_wrap">
              <span class="btn confirm" @click="rou(hintType)">确定</span>
            </div>
          </div>
        </div>
        <div class="shadow"></div>
      </div>

    </div>
</template>

<script> 
    var qs = require('qs');
    export default{
        components:{

        },
        data(){
            return{
               id: this.$route.query.id,
               detail:{},
               currentpage:1,
               pagesize:5,
               ishide:true,
               ishide2:false,
               hotimg:[],
               load:false,
               evaluationContent:"",
               hintType:{
                isShow:false
              },
            }
        },
        methods: {
            rou(item){
                var self = this;
                item.isShow = !item.isShow
              },
            bigimgs(detail){
              var self=this;
              for(var i=0;i<detail.interactiveContent.picList.length;i++){
                self.hotimg.push(detail.interactiveContent.picList[i]);
                console.log(detail.interactiveContent.picList[i]);
                console.log(self.hotimg)
                if(i==detail.interactiveContent.picList.length-1){
                  wx.previewImage({
                    current: '', // 当前显示图片的http链接
                    urls: self.hotimg // 需要预览的图片http链接列表
                  });
                }
              }
            },
           getDetail(){
                var self=this;
                self.axios.post("/wechat/interactive/getInteractiveContent?id="+self.id
                ).then(function(res){
                    if(res.data.success){
                        self.ishide=false;
                        self.ishide2=true;
                        self.detail=res.data.data;
                    }else {
                        self.ishide=false;
                        self.ishide2=true;
                        alert(res.data.message);
                    }
                })
           },
           coms(item){
            var self=this;
            item.isShow = !item.isShow;
            self.load=true;
            var evaluationContent=utf16toEntities(unescape(self.evaluationContent));
            self.axios.post("/wechat/interactive/saveInteractiveEvaluation",
              qs.stringify({
                interactiveContentFk:self.id,
                evaluationContent:evaluationContent

              })
            ).then(function(res){
              if(res.data.success){
                self.load=false;
                self.isshow=true;
                self.evaluationContent="";
              }else {
                self.evaluationContent="";
                alert(res.data.message)
              }

            },function(response){

            })
          } 
        },
        mounted(){
            var self=this;
            self.getDetail();
        }
    }
</script>
<style lang="less" scoped>
    @import "../../../assets/css/interactionDetail.less";
</style>