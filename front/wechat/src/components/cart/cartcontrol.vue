
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
            },
            store: {
                type: Object
            },
            changeNum: {
                type: Function
            }
        },
        watch:{
            'tea.num'(){
                if(this.changeNum){
                    this.changeNum();
                }
                
            }
        },
        methods:{
            decreaseCart(){
                if(this.tea.num == 1){
                    return false
                }
                
                this.decreaseCartData();
            },
            //数量加
            addCart(){
                if(this.tea.num == 99){
                  return false
                }
                
                this.addCartData();
            },
            addCartData(){
                this.axios.post('/wechat/shopping_cart/add', querystring.stringify({//增加数量
                    'store_id': this.store.store_id,
                    'commodity_id': this.tea.commodity_id,
                    'num':1
                }))
                .then(res => {
                    if(res.data.success){
                        this.tea.num++;
                    } else {
                      weui.topTips(res.data.message,1000);//提示出错
                    }
                    
                })
            },
            decreaseCartData(){
                this.axios.post('/wechat/shopping_cart/subtract', querystring.stringify({//减少数量
                    'store_id': this.store.store_id,
                    'commodity_id': this.tea.commodity_id,
                    'num':1
                }))
                .then(res => {
                    if(res.data.success){
                        this.tea.num--;
                    } else {
                      weui.topTips(res.data.message,1000);//提示出错
                    }
                    
                })
            }
        },
        mounted(){
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/cart.less";
</style>
