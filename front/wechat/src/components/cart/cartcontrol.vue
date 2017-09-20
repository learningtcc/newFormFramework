
<template>
    <div class="cartcontrol">
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
            }
        },
        methods:{
            decreaseCart(){
                if(this.tea.num == 1){
                    return false
                }
                this.tea.num--;

            },
            //数量加
            addCart(){
                if(this.tea.num == 99){
                  return false
                }
                this.tea.num++;
            },
        },
        created(){
            
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/cart.less";
</style>
