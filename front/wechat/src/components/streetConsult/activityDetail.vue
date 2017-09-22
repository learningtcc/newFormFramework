<template>
    <div class="activityDetail">
        <div class="playvideo">
          <div class="zy_media">
              <video>
                  <source :src="detail.video_url" type="video/mp4">
                  您的浏览器不支持HTML5视频
             </video>
          </div>
        </div>
        <div class="activityT">
            <p class="activityTitle">{{detail.title}}</p>
            <p class="activityTime">{{detail.top_time}}<span>浙江义乌精品街区</span></p>
        </div>
        <p class="interlayer"></p>
        <div class="activitySurvey">
            <h4>展会概况</h4>
            <div class="activityIntro" v-html="detail.description">
                <!-- <p>第12届中国（义乌）文化产品交易会，于4月27日至30日在义乌国际博览中心举行。设标准展位3251个，展览面积6万平方米。</p>
                <p>第12届中国（义乌）文化产品交易会，于4月27日至30日在义乌国际博览中心举行。设标准展位3251个，展览面积6万平方米。</p>
                <img src="../../assets/img/temp/activity1.png"> -->
            </div>
        </div>
    </div>
</template>


<script> 
import videojs from '../../../static/js/zy.media.min';
//var qs = require('qs');
    export default{
      components: {
          videojs,
        },
        data(){ 
          return{
              id : this.$route.query.id,
              detail:{},
          }
      },
        methods: {
           getDetail(){
              var self = this;
              ywData.detail({'resource_name':'street_culture_info','id':self.id},function (data) {
                  if(data.success){
                      self.detail = data.data;
                      zymedia('video',{autoplay: true});
                  }
              });
          }, 
          getaddClick(){
                var self=this;
                console.log(self.id);
                self.axios.get("/wechat/streetCulture/addClicks?id="+self.id
                ).then(function(res){
                    if(res.data.success){
                      self.list=res.data.data;
                    }else {
                      alert(res.data.message)
                    }
                },function(response){

              })
            }
        },
        mounted(){
            var self=this;
            self.getDetail();
            self.getaddClick();

        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/activityDetail.less";
    @import "../../../static/js/zy.media.min.css";
</style>