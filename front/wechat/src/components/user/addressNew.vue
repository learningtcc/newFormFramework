
<template>
    <div class="wrapper">
        <div class="addressNew">
            <ul class="user-add-new">
              <li>
                <div class="tit">联系人</div>
                <div class="address-content">
                  <input type="text" placeholder="请输入收货人姓名" class="txt_common" v-model="addressInfo.name">
                </div>
              </li>
              <li>
                <div class="tit">手机号</div>
                <div class="address-content">
                  <input type="tel" placeholder="请输入手机号" class="txt_common" maxlength="11" v-model="addressInfo.phone">
                </div>
              </li>
            </ul>
        </div>
        <div class="addressNew">
            <ul class="user-add-new">
              <li @click="cityPicker">
                <div class="tit">收货地</div>
                <div class="address-content">
                  <input type="text" class="txt_common" readonly="true" v-model="addressInfo.receipt_add">
                </div>
                <span class="arrow"></span>
              </li>
              <li class="addr">
                <div class="tit">详细地址</div>
                <div class="address-content">
                    <textarea name="" id="" placeholder="请输入详细地址" class="texta" v-model="addressInfo.address"></textarea>
                </div>
              </li>
            </ul>
        </div>
        <div class="addressNew">
            <ul class="user-add-new">
                <li>
                    <div class="tit">默认地址</div>
                    <div class="address-content addrCheck">
                      <input class="weui-switch" type="checkbox" v-model="addrCheck">
                    </div>
                </li>
            </ul>
        </div>
        <div class="btn">
            <span @click="submit">保存</span>
        </div>
    </div>
</template>


<script>
    var querystring = require('querystring');
    export default{
        components:{

        },
        data(){
            return{
              id:this.$route.query.id,
              addressInfo:{name:'',phone:'',receipt_add:'',address:''},
              addrCheck:true,
              is_default:{
                true:'Y',
                false:'N'
              }
            }
        },
        methods: {
           submit(){
              var result = {error:false, msg:""};
              testEmpty(this.addressInfo.address, result, '详细地址不能为空');
              testEmpty(this.addressInfo.receipt_add, result, '收货地不能为空');
              testTel(this.addressInfo.phone, result);
              testEmpty(this.addressInfo.phone, result, '手机号不能为空');

              testEmpty(this.addressInfo.name, result, '姓名不能为空');
              if(result.error){
                weui.topTips(result.msg,1000);//提示出错
                return;
              }
              //console.log(this.is_default[this.addrCheck]);
              
              var loading = weui.loading('请稍候', {//loading
              });
              var id = this.id == undefined ? '' : this.id;
              this.axios.post('/wechat/user/saveUpdate', querystring.stringify({
                id:id,
                receiptAdd:this.addressInfo.receipt_add,//收货地址
                address:this.addressInfo.address,//详细地址
                receiver:this.addressInfo.name,//收货人
                phone:this.addressInfo.phone,//联系方式
                isDefault:this.is_default[this.addrCheck]//是否默认地址
              }))
              .then(res => {
                  loading.hide();//加载
                  if(res.data.success){
                    console.log(0);
                    weui.toast('保存成功', {//提示  
                      duration: 1000,
                      callback: ()=>{
                        this.$router.go(-1);
                        //this.$router.push({ path: '/addressList'});
                      }
                    });

                  } else {
                    weui.topTips(res.data.message,1000);//提示出错
                  }
                  
              })
              
           },
           cityPicker(){//选择省市区
              var self = this;
              var pickerArea=[];
              for(let i = 0; i < pickercity.length; i++){
                var parent = {label:pickercity[i].name,value:i,children:[]};
                for(let j = 0;j < pickercity[i].city.length; j++){
                  var city = {label:pickercity[i].city[j].name,value:j,children:[]};
                  parent.children.push(city);
                  for(let k = 0; k < pickercity[i].city[j].area.length; k++){
                    city.children.push({label:pickercity[i].city[j].area[k],value:k});

                  }
                }
                pickerArea.push(parent);
              }

              weui.picker(pickerArea, {//选择器
                 defaultValue: [10,0,0],
                 onChange: function (result) {
                     //console.log(result)
                 },
                 onConfirm: function (result) {
                    console.log(result);
                    var province = result[0].label;
                    var city = result[1].label;
                    var area = result[2].label;
                    self.addressInfo.receipt_add = province + city + area;
                 },
                 id: 'cityPicker'
              });
              
            },
        },
        mounted(){
            if(this.id){
              ywData.detail({'resource_name':'member_address',id:this.id,typeAll:{}},(data)=>{
                var editDetail = data.data;
                this.addrCheck = editDetail.is_default == 'Y'? true : false;
                this.addressInfo = {name:editDetail.receiver,phone:editDetail.phone,receipt_add:editDetail.receipt_add,address:editDetail.address}
              });
              
            }
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/addressNew.less";
</style>
