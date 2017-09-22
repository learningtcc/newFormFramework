
<template>
    <div class="fillcontrol">
        <div class="sub">
          <span class="subtract" @click="decreaseCart">-</span>
          <span class="numberText">{{tea.num}}</span>
          <span class="adds" @click="addCart">+</span>
        </div>
    </div>
</template>


<script>
    var querystring = require('querystring');
    export default{
        props: {
            tea: {
                type: Object
            },
            getTotal: {
                type: Function
            }
        },
        watch:{
            'tea.num'(val){
                if(this.tea.num){
                    this.$set(this.tea,'num',this.tea.num);
                    this.getTotal();
                }
            }
        },
        methods:{
            decreaseCart(){
                if(this.tea.num){

                    if(this.tea.num == 1){
                        return false
                    }
                    this.tea.num--;
                    
                }
            },
            //数量加
            addCart(){
                if(this.tea.num){

                    if(this.tea.num == 99){
                      return false
                    }
                    this.tea.num++;
                }
            },
        },
        mounted(){
            
        }
    }
</script>
<style lang="less" scoped>
    @import "../../../assets/css/fillOrder.less";
</style>
