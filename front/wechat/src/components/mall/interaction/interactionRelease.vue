<template>
    <div class="interactionRelease">
        <div class="interactionTheme" @click="clickTitle(receiveType)" v-text="interactivThemeName"></div>
        <!--主题分类-->
        <div class="popup" v-show="receiveType.isShow">
            <div class="popup_wrap">
              <div class="popup_in">
                <div class="check_list">
                  <ul>
                    <li v-for="(list,index) in list.data">
                      <span class="tit" v-text="list.themeName">龙井茶</span>
                      <div class="checkbox" @click="tab(index)" :class="{'active':index==num}"></div>
                    </li>
                  </ul>
                   <div class="btn_wrap">
                  <span class="btn confirm" @click="clickConfirm(receiveType)">确定</span>
                  <span class="btn cancel" @click="clickPopup(receiveType)">取消</span>
                </div>
                </div>
              </div>
            </div>
            <div class="shadow" @click="clickPopup(receiveType)"></div>
        </div>
        <!--主题分类-->
        <input type="text" placeholder="请输入标题" class="interactionTitle" v-model='interactionTitle'>
        <div class="evaluate_cont">
            <textarea name="" id="" cols="30" rows="10" class="texta" placeholder="请输入互动内容" v-model="interactiveContent"></textarea>
            <div class="eva_add_pic">
                <span class="pic" v-for="(img,index) in logoimage">
                    <img :src="img" alt="" class="img">
                    <span class="del"><i @click="delimg(index)"></i></span>
                </span>
                <div class="add_btn" @click="loading"></div>
            </div>
        </div>
        <div class="postBtn"><a @click="comment(hintType)">发布</a></div>  
        
        <!--温馨提示-->
        <div class="popup" v-show="hintType.isShow">
        <div class="hintWarp">
          <div class="popup_in popupHint">
            <div class="title">温馨提示</div>
            <p class="cont">您提交的互动信息已经上传，工作人员会在1-2个工作日之内审核，审核通过您的互动将会发布在互动列表。</p>
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
  export default {
    components: {
    },
    data(){
      return{
        id:this.$route.query.id,
        list:{},
        logoimage:[],
        interactiveContent:"",
        interactionTitle:"",
        interactivThemeFk:"",
        num:0,
        picList:[],
        imgId:[],
        isshow:false,
        isfalse:false,
        receiveType:{
            isShow:false
          },
          hintType:{
            isShow:false
          },
        interactivThemeName:"请选择讨论主题分类",
        currentpage:1,
        pagesize:5,
      };
    },
    methods:{
      rou(item){
        var self = this;
        item.isShow = !item.isShow
        self.$router.push({path:'/interactionList',query:{id:self.id}})
      },
      clickPopup(item){
        item.isShow = !item.isShow
      },
      clickConfirm(item){
        var self=this;
        item.isShow = !item.isShow;
        self.interactivThemeName=self.list.data[self.num].themeName;
        self.interactivThemeFk=self.list.data[self.num].id;
      },
      delimg(index){
        var self = this;
        self.logoimage.splice(index,1);
        self.imgId.splice(index,1)
      },
      tab(index){
        var self = this;
        self.num=index;
      },
      clickTitle(item){
        item.isShow = !item.isShow;
        var self=this;
        self.axios.post("/wechat/interactive/getInteractiveThemeList",
            qs.stringify({
                page_size:self.pagesize,
                current_page:self.currentpage,
            })
        ).then(function(res){
            if(res.data.success){
              self.list=res.data.data;
            }else {
              alert(res.data.message)
            }
        },function(response){

        })
      },
      loading(){
        var self = this;
       
        //选择图片
        self.picList=[];
        wx.chooseImage({
          count: 9, // 默认9
          sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
          sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
          success: function (res) {
            var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
            console.log("aa"+localIds);
            //图片上传
            var i = 0,
              length = localIds.length;
             
            upload();
            function upload() {
              wx.uploadImage({
                localId : localIds[i],
                success : function(res) {
                  self.picList.push(res.serverId);
                  console.log("bb"+self.picList);
                  i++;
                  if (i < length) {
                    upload();
                  } else {
                    //上传到服务器
                    self.axios.post('/wechat/material/upload',
                      qs.stringify({
                        stuffix:'jpg',
                        'serverids[]':JSON.stringify(self.picList)
                      })).then(function(res){
                      var resData = res.data.data;
                      for(var i = 0;i<resData.length;i++){
                        self.logoimage.push(resData[i].url);
                        self.imgId.push(resData[i].id);
                      }
                    },function(response){

                    })
                  }
                },
                fail : function() {

                }
              });

            }
          }
        });
      },
      comment(item){
        var self = this;
        item.isShow = !item.isShow
        //表情转化字符串
        console.log(self);
        var interactiveContent=utf16toEntities(unescape(self.interactiveContent));
        if(self.interactivThemeName!=""&&interactiveContent!=""){
          self.isfalse=true;
          self.axios.post('/wechat/interactive/saveInteractiveContent',
            qs.stringify({
              interactivThemeFk:self.interactivThemeFk,//主题id
              interactivThemeName:self.interactivThemeName,//互动主题
              interactionTitle:self.interactionTitle,//互动标题
              interactiveContent:interactiveContent,//评论的内容
              picList:self.logoimage.join(",")
            })).then(function(res){
            if(res.data.success){
              self.isfalse=false;
              self.isshow=true;
            }
          },function(response){

          })
        }else {
          alert("内容和主题都必填！")
        }
        console.log(self.interactionTitle);
        //self.$router.push({path:'/interactionList',query:{id:self.id}})
      }
    },
    mounted(){
      var self = this;
      /*setDocumentTitle("发表互动");*/
      //微信授权
      var url = location.href.split('#')[0];
      self.axios.post('/macro/weixin/sign',qs.stringify({ url: url })).then(function(p){
        var resData = p.data.data;
        wx.config({
          debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
          appId: resData.appId, // 必填，企业号的唯一标识，此处填写企业号corpid
          timestamp: resData.timestamp, // 必填，生成签名的时间戳
          nonceStr: resData.noncestr, // 必填，生成签名的随机串
          signature: resData.Signa,// 必填，签名，见附录1
          jsApiList : [ 'chooseImage','previewImage','uploadImage','downloadImage']
          // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });
      },function(response){

      })

    }
  }
</script>
<style lang="less" scoped>
    @import "../../../assets/css/interactionRelease.less";
</style>