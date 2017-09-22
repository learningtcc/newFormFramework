<template>
    <div>
        <div class="loading" v-if="ishide"></div>
        <div class="activityList" v-if="ishide2">
            <header class="header">
                <div class="banner swiper-container">
                    <div class="swiper-wrapper">
                        <div class="swiper-slide" v-for="moreimg in list.advertising_info.data">
                            <router-link :to="moreimg.link_url">
                                <img class="header-banner-img" :src="moreimg.image_url">
                            </router-link>
                        </div>
                  </div>
                    <div class="swiper-pagination"></div>
                </div>
            </header>   
            <div class="activitySubject">
                <H4>文化专题</H4>
                <router-link :to="{path:'/activityDetail',query:{id:list.hot_street_culture_info.id}}">
                    <div class="subjectMain">
                        <img :src="list.hot_street_culture_info.image_url">
                        <div class="conSanjiao"><p>热门</p></div>
                        <p class="activityIn" v-html="list.hot_street_culture_info.description"></p>
                    </div>
                </router-link>
            </div>
            <div class="newActivty" v-for="lis in list.street_culture_info.data">
                <router-link :to="{path:'/activityDetail',query:{id:lis.id}}">
                <dl>
                    <dt><img :src="lis.image_url"></dt>
                    <dd>
                        <p class="activityName">{{lis.title}}</p>
                        <p class="activityIntro" v-html="lis.description"></p>
                        <p class="activityTime">{{lis.create_time}}<span>{{lis.clicks}}</span></p>
                    </dd>
                </dl>
                </router-link>
            </div>
        </div>
    </div>
</template>


<script> 
    var qs = require('qs');
    export default{
        data () {
            return {
                list:{},
                currentpage:1,
                pagesize:5,
                ishide:true,
                ishide2:false,
            }
        },
        methods: {
            /*advertis(item){
                var self = this;
                var link_url = item.link_url;
                console.log(link_url);
                window.location=link_url;
            },*/
            getlist(){
                var self=this;
                self.axios.get("/wechat/streetCulture/list",
                    qs.stringify({
                        page_size:self.pagesize,
                        current_page:self.currentpage,
                    })
                ).then(function(res){
                    if(res.data.success){
                      self.ishide=false;
                      self.ishide2=true;
                      self.list=res.data.data;
                      self.$nextTick(function(){
                        var swiper = new Swiper('.banner', {
                          pagination: '.swiper-pagination',
                          paginationClickable: true,
                          spaceBetween: 30,
                          autoplay: 2500
                        });
                      })
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
    @import "../../assets/css/activityList.less";
</style>