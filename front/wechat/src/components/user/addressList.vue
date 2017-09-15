
<template>
    <div class="wrapper">
        <div class="address_list">
            <ul>
                <li v-for="(item,index) in lists" :key="item.id">
                    <div class="user-data" @click.stop="selectAddress(item)">
                      <div class="addrlist">
                        <span class="name">{{item.receiver}}</span>
                        <span class="phone">{{item.phone}}</span>
                      </div>
                      <p>{{item.receiptAdd}}{{item.address}}</p>
                    </div>
                    <div class="handle_bar">
                        <el-radio :label="item.id" v-model="defaultRadio" v-if="item.isDefault == 'Y'" class="default">{{getDefault(item)}}默认地址</el-radio>
                        <div @click.stop="clickDefault(item)"><el-radio :label="item.id" v-model="defaultRadio" v-if="item.isDefault == 'N'" class="default">设为默认</el-radio></div>
                        <p class="add-edit" @click="deleteAddr(item)">
                            <img src="../../assets/img/user/addr_delete.png" alt="">
                            <span>删除</span>
                        </p>
                        <p class="add-edit" @click="toEdit(item)">
                          <img src="../../assets/img/user/addr_edit.png" alt="">
                          <span> 编辑</span>
                        </p>
                    </div>
                </li>
                <infinite-loading :on-infinite="onInfinite" ref="infiniteLoading">
                    <span slot="no-more">
                      加载完
                    </span>
                    <span slot="no-results">
                      暂无
                    </span>
                </infinite-loading>
            </ul>
        </div>
        <div class="btn">
            <router-link :to="{path:'/addressNew'}" >新增地址</router-link>
        </div>
    </div>
</template>


<script>
    var querystring = require('querystring');
    import InfiniteLoading from 'vue-infinite-loading';
    import config from '../../../static/js/config/index.js'
    export default{
        components: {
            InfiniteLoading,
        },
        data(){
            return{
               defaultRadio:'0',
               lists:[],
               totalPage:1,
               currentPage:0,
               fillOrderId:this.$route.query.fillOrderId,
            }
        },
        methods: {
            onInfinite() {//上拉加载
              //console.log("onInfinite");
              var self = this;
             
              if(self.currentPage < self.totalPage){

                function load(){
                  self.axios.post(config.addressList, querystring.stringify({
                    'current_page':self.currentPage + 1,
                    'page_size':10
                  }))
                  .then(response => {
                    //console.log(response.data);
                   
                    if(response.data.success == true){
                      let record = response.data.data;
                      self.currentPage = record.current_page;
                      self.totalPage = record.total_page;
                      self.lists = self.lists.concat(record.data);
                      if(self.lists.length == 0){

                        self.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');
                        return;
                      }
                    }
                    self.$refs.infiniteLoading.$emit('$InfiniteLoading:loaded');
                    

                  })
                  .catch(error => {
                    self.$refs.infiniteLoading.$emit('$InfiniteLoading:loaded');
                    console.log(error);
                  });
                }

                setTimeout(() => {
                  load();
                },200);

              } else {
                self.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');
              } 
            },
            toEdit(item){//编辑地址
              this.$router.push({ path: '/addressNew', query: { id: item.id }})
            },
            linkToDetail(){//新增地址
                this.$router.push({ path: '/addressNew',query:{}});
            },
            deleteAddr(item){//是否删除
              weui.dialog({
                  title: '',
                  content: '是否删除此地址',
                  buttons: [{
                      label: '取消',
                      type: 'default',
                      onClick:()=> { 

                      }
                  }, {
                      label: '确定',
                      type: 'primary',
                      onClick: ()=> {
                        this.deleteData(item);
                      }
                  }]
              });
              
          },
          deleteData(item){//删除接口
            this.axios.post(config.deleteAddress, querystring.stringify({
              'addressId': item.id
            }))
            .then((res)=> {
              if(res.data.success){
                  var arr = this.lists;
                  var index = arr.indexOf(item)
                  if(index != -1){
                    arr.splice(index, 1);
                  }
                  weui.toast('删除成功', {//提示  
                      duration: 1000,
                      callback:()=>{
                        //self.$router.go(-1);
                        //self.$router.push({ path: '/user-address'});
                      }
                  });
              }
              
            })
            .catch((error)=> {
                
            });
          },
          selectAddress(item){//"提交订单"页面跳转过来选择地址
            if(!this.fillOrderId){
                return;
            } else {

                //this.$router.push({ path: '/fillOrder',query:{id:this.fillOrderId,addressId:item.id}});
                this.$root.$data.store.write("fillOrderAddressId" + this.fillOrderId, {fillId:this.fillOrderId,addressId:item.id});
                this.$router.go(-1);
            }
          },
          getDefault(item){//获取列表中默认地址
            this.defaultRadio = item.id;
          },
          clickDefault(item){
            this.axios.post('/wechat/user/defaultAddress', querystring.stringify({//点击默认地址
                'id':item.id
            }))
            .then(res => {

                if(res.data.success){
                    this.defaultRadio = res.data.data.new_address_info.id;
                    this.currentPage = 0;
                    this.totalPage = 1;
                    this.lists = [];
                    this.$refs.infiniteLoading.$emit('$InfiniteLoading:reset');
                    //window.location.reload();

                } else {
                  
                }
                
            })
          }
        },
        mounted(){
            
        }
    }
</script>
<style lang="less" scoped>
    @import "../../assets/css/addressList.less";
</style>
<style lang="less">
    .handle_bar{
        .is-checked +.el-radio__label{
            color: #2182f7;
        }
        .is-checked .el-radio__inner{
            background:#2182f7;
            &:after{
                width: 0.2058rem;
                height: 0.109rem;
                background: none;
                border-left:0.0484rem solid #fff;
                border-bottom:0.0484rem solid #fff;
                border-radius: 0;
                left: 18%;
                top:25%;
                transform:rotate(-45deg);
                -webkit-transform:rotate(-45deg); 
            }
        }
    }
</style>
