<template>
    <div class="teaDetail">
        <!-- <img src="../../../assets/img/temp/shopBan.png"> -->
        <div class="teaIntro">
            {{detail.description}}
        </div>
    </div>
</template>


<script> 
    export default{
        data(){ 
          return{
              id : this.$route.query.id,
              detail:{},
          }
      },
        methods: {
           getDetail(){
              var self = this;
              ywData.detail({'resource_name':'tea_culture','id':self.id},function (data) {
                  if(data.success){
                      self.detail = data.data;
                  }
              });
          }, 
        },
        mounted(){
            var self=this;
            self.getDetail();
        }
    }
</script>
<style lang="less" scoped>
    @import "../../../assets/css/teaDetail.less";
</style>