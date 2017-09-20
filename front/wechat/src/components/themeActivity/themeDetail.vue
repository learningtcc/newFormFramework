<template>
    <div class="themeDetail">
        <header class="header">
            <div class="banner swiper-container">
                <div class="swiper-wrapper">
                    <div class="swiper-slide" v-for="moreimg in detail.image_info.data">
                      <img class="header-banner-img" :src="moreimg.pic_url">
                    </div>
              </div>
              <div class="swiper-pagination"></div>
            </div>
        </header>
        <div class="themeMain">
            <p class="themeName">{{detail.theme_activities.title}}</p>
            <p class="themeIntro" v-html="detail.theme_activities.explains"></p>
            <p class="themeTime">活动时间：{{detail.theme_activities.act_start_time}} ~ {{detail.theme_activities.act_end_time}}</p>
            <p class="themeAddress">活动地址：{{detail.theme_activities.address}}</p>
        </div>
        <p class="interlayer"></p>
        <div class="activityGoal">
            <div class="tit"><h3>活动目的</h3></div>
            <p class="activityGoalIntro">{{detail.theme_activities.act_aim}}</p>
        </div>
        <p class="interlayer"></p>
        <div class="activityGoal">
            <div class="tit"><h3>活动主题</h3></div> 
            <p class="activityGoalIntro">{{detail.theme_activities.act_theme}}</p>
        </div>
        <p class="interlayer"></p>
        <div class="activityGoal">
            <div class="tit"><h3 class="">时间与对象</h3></div>
            <p class="activityGoalIntro">{{detail.theme_activities.time_and_object}}</p>
        </div>
        <p class="interlayer"></p>
        <div class="activityGoal">
            <div class="tit"><h3 class="">活动具体内容</h3></div>
            <p class="activityGoalIntro">{{detail.theme_activities.act_content}}</p>
        </div>
    </div>
</template>

<script> 
    var qs = require('qs');
    export default{
        data () {
            return {
                id:this.$route.query.id,
                detail:{},
            }
        },
        methods: {
            getDetail(){
                var self=this;
                self.axios.get("/wechat/themeActivities/detail?id="+self.id

                ).then(function(res){
                    if(res.data.success){
                      self.detail=res.data.data;
                      self.$nextTick(function(){
                        var swiper = new Swiper('.banner', {
                          pagination: '.swiper-pagination',
                          paginationClickable: true,
                          spaceBetween: 30,
                          autoplay: 2500
                        });
                      })
                }
              },function(response){

              }) 
            },
          
        },
        mounted(){
          var self=this;
          self.getDetail()
        } 
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/themeDetail.less";
</style>