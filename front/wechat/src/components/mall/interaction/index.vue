
<template>
    <div class="wrapper">
        <div class="interaction_box">
            <div class="list">
                <div class="list_in">
                    <router-link :to="{path:'/interactionList',query:{id:item.id}}" class="dl" v-for="item in list.data" :key="item.id">
                        <span class="dt"><img :src="item.themePic" alt=""></span>
                        <span class="dd">{{item.themeName}}</span>
                    </router-link>
                </div>
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
            getlist(){
                var self=this;
                self.axios.post("/wechat/interactive/getInteractiveThemeList",
                    qs.stringify({
                        page_size:self.pagesize,
                        current_page:self.currentpage,
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
    @import "../../../assets/css/interaction.less";
</style>
