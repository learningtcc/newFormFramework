<template>
    <div>
        <div class="loading" v-if="ishide"></div>
        <div class="theme" v-if="ishide2">
            <div class="themeSearch">
                <div class="screen" @click="classifyBtn" :class="{screenA:favorCheck}">分类筛选</div>
                <input type="text" placeholder="输入搜索关键字" class="searchBtn">
            </div>
            <!--分类筛选-->
            <div class="classifyBg" @click="classifyBtn" :class="{classifyBg1:favorCheck1}">
                <ul>
                    <li class="active">节假日活动</li>
                    <li>风俗文化</li>
                    <li>社区治理</li>
                </ul>
                <div class="classifyB"></div>
            </div>
            <!--分类筛选-->
            <p class="centre"></p>
            <h4 class="newA">最新活动</h4>
            <router-link :to="{path:'/themeDetail',query:{id:list.newest_theme_activities.id}}">
            <div class="themeNew">
                <img class="themeImg" :src="list.newest_theme_activities.theme_pic">
                 <p class="themeHot">{{list.newest_theme_activities.type_name}}</p>
            </div>
            <div class="themeCon">
                <p class="themeTime">{{list.newest_theme_activities.act_start_time}} ~ {{list.newest_theme_activities.act_end_time}}</p>
                <p class="themeName">{{list.newest_theme_activities.title}}</p>
                <p class="themeIntro">{{list.newest_theme_activities.explains}}</p>
            </div>
            </router-link>
            <div class="moreActivitiy">
                <h4>更多活动</h4>
                <dl v-for="lis in list.theme_activities.data">
                <router-link :to="{path:'/themeDetail',query:{id:lis.id}}">
                    <dt>
                        <img :src="lis.theme_pic">
                        <span>{{lis.type_name}}</span>
                    </dt>
                    <dd>
                        <p class="activityName">{{lis.title}}</p>
                        <p class="activityIntro">
                            {{lis.explains}}
                        </p>
                        <p class="activityTime">{{lis.act_start_time}}-{{lis.act_end_time}}</p>
                        <p class="activityLook">{{lis.clicks}}</p>
                    </dd>
                    </router-link>
                </dl>
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
                favorCheck:false,
                favorCheck1:false,
                type:"",

            }
        },
        methods: {
            classifyBtn(){
                this.favorCheck = !this.favorCheck;
                this.favorCheck1 = !this.favorCheck1;
            },
            getlist(){
                var self=this;
                self.axios.get("/wechat/themeActivities/list",
                    qs.stringify({
                        page_size:self.pagesize,
                        current_page:self.currentpage,
                        type:self.type,
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
    @import "../../assets/css/theme.less";
</style>