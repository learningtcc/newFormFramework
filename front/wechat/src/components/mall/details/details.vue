
<template>
    <div class="wrapper">
        <div class="a-banner">
            <swiper :options="swiperOption" ref="mySwiperA">
            <swiper-slide v-for="item in cms" :key="item.id">
              <a>
                <img :src="item.pic_url" alt=""> 
              </a>
            </swiper-slide>
            <div class="swiper-pagination"  slot="pagination"></div>
          </swiper>
      </div>
        <div class="cont_info">
            <div class="info_lt">
               <div class="title">{{detail.name}}</div>
               <div class="price">￥<em>{{detail.price}}</em>/{{detail.price_content}}</div> 
            </div>
            <div class="info_rt">
                <p class="scores">评分：<i>{{detail.points}}分</i></p>
                <div class="stars">
                    <el-rate v-model="starsValue" disabled text-color="#ff9900"></el-rate>
                </div>
            </div>
        </div>
        <div class="pro_info">
            <ul>
                <li>时间：{{detail.plucking_time}}</li><li>种类：{{detail.species_name}}</li><li>生产时间：{{detail.production_time | productDate}}</li><li>产地：{{detail.local}}</li><li>净含量：{{detail.standard}}</li><li>已售：56笔</li>
            </ul>
        </div>
        <div class="cont_detail">
            <div class="tab_h">
                <ul>
                    <li :class="{active:index == detailActive}" @click="checkDetailTab(index)" v-for="(item,index) in detailTabHead">
                        <a href="javascript:;">{{item.title}}</a>
                    </li>
                </ul>
            </div>
            <div class="tab_b" v-if="detailActive == 0">
                <div class="detail_wrap">
                    <div v-html="detail.details"></div>
                </div>
            </div>
            <div class="tab_b" v-show="detailActive == 1">
                <div class="comment_list">
                    <dl v-for="item in lists" :key="item.id">
                        <dt>
                            <img :src="item.user_image" alt="" class="avatar">
                        </dt>
                        <dd>
                            <div class="cmt_rt_top">
                                <span class="nickname">{{item.user_name}}</span>
                                <span class="stars">
                                    <el-rate :value="item.points" disabled text-color="#ff9900"></el-rate>
                                </span>
                            </div>
                            <div class="time">{{item.evaluation_time}}</div>
                            <div class="cmt_cont">
                                {{item.content}}
                            </div>
                            <div class="picList">
                                <img src="../../../assets/img/temp/details.jpg" alt="">
                            </div>
                        </dd>
                    </dl>
                    <infinite-loading :on-infinite="onInfinite" ref="infiniteLoading">
                        <span slot="no-more">
                          加载完
                        </span>
                        <span slot="no-results">
                          暂无
                        </span>
                    </infinite-loading>
                </div>
            </div>
        </div>
        <div class="fixed_bottom">
            <div class="bt_lt">
                <router-link :to="{path:'/storeDetail',query:{id:detail.store_id}}">
                    <i class="icon ico_shop"></i>店铺
                </router-link>
                <a href="javascript:;" @click="checkFavor" :class="{collected:favorCheck}">
                    <i class="icon ico_favor"></i><em class="word">收藏</em><em class="c_word">已收藏</em>
                </a>
            </div>
            <div class="btn_buy" @click="clickToBuy">立即购买</div>
            <div class="btn_cart" @click="addCart">加入购物车</div>
        </div>
    </div>
</template>


<script>
    var querystring = require('querystring');
    import { swiper, swiperSlide } from 'vue-awesome-swiper';
    import InfiniteLoading from 'vue-infinite-loading';
    export default{
        components: {
            swiper,
            swiperSlide,
            InfiniteLoading,
        },
        data(){
            return{
                starsValue:4,
                swiperOption: {
                    notNextTick: true,
                    autoplay: 3000,
                    grabCursor: true,
                    setWrapperSize: true,
                    // autoHeight: true,
                    pagination: '.swiper-pagination',
                    paginationClickable: true,
                    mousewheelControl: true,
                    observeParents: true,
                    debugger: true,
                    loop: false,
                    paginationBulletRender(swiper, index, className) {
                        return `<span class="${className}" style='background:#fff;'></span>`;
                      }
                },
                detailTabHead:[
                    {title:'详情介绍'},
                    {title:'评价'}
                ],
                detailActive:0,
                favorCheck:false,
                collectionStatus:{
                    'Y':true,
                    'N':false,
                    false:'N',
                    true:'Y'
                },
                id:this.$route.query.id,
                cms:{},
                detail:{},
                lists:[],
                totalPage:1,
                currentPage:0,

            }
        },
        filters:{
            productDate(val){
                if(val == undefined){
                    return '';
                }
                return val.substring(0,4);
            },
            entitiestoUtf16(str){//表情解码
                 // 检测出形如&#12345;形式的字符串
                var strObj = utf16toEntities(str);
                var patt = /&#\d+;/g;
                var H,L,code;
                var arr = strObj.match(patt)||[];
                for (var i=0;i<arr.length;i++){
                    code = arr[i];
                    code=code.replace('&#','').replace(';','');
                    // 高位
                    H = Math.floor((code-0x10000) / 0x400)+0xD800;
                    // 低位
                    L = (code - 0x10000) % 0x400 + 0xDC00;
                    code = "&#"+code+";";
                    var s = String.fromCharCode(H,L);
                    strObj = strObj.replace(code,s);
                }
                return strObj;
            },
        },
        methods: {
           checkDetailTab(index){
                this.detailActive = index;
           },
           checkFavor(){//点击收藏
                var loading = weui.loading('请稍等', {//loading
                });
                this.axios.post('/wechat/user/collection', querystring.stringify({
                    'is_collection' : this.collectionStatus[this.favorCheck],
                    'commodity_id' : this.id,
                    'collection_type' : 'Commodity' 
                }))
                .then(res => {
                    loading.hide();//加载
                    if(res.data.success){
                       weui.toast(res.data.message, {//提示  
                          duration: 1000,
                          callback: () => {
                            var collectStatus = res.data.data.is_collection;
                            this.favorCheck = this.collectionStatus[collectStatus];
                          }
                        }); 
                    } else {
                      weui.topTips(res.data.message,1000);//提示出错
                    }
                    
                });
                this.favorCheck = !this.favorCheck;
           },
           clickToBuy(){
                this.$router.push({ path: '/fillOrder',query:{id:this.id}});
           },
           addCart(){//加入购物车
                var loading = weui.loading('加载中', {});
                this.axios.post('/wechat/shopping_cart/put', querystring.stringify({//详情
                    'store_id':this.id,
                    'commodity_id':this.detail.store_id,
                    'num':1
                }))
                .then(res => {
                    loading.hide();//加载
                    if(res.data.success){
                        weui.toast("加入购物车成功", {//提示  
                          duration: 1000,
                          callback: ()=> {
                            
                          }
                        });
                    } else {
                      weui.topTips(res.data.message,1000);//提示出错
                    }
                    
                })
           },
           previewImage(item,arrSrc){//预览图片
            var curSrc = item.thumbMedium;
            var arr = [];
            for(var i = 0; i < arrSrc.length; i++){
              arr.push(arrSrc[i].thumbMedium);
            }
            wx.previewImage({
                current: curSrc, // 当前显示图片的http链接
                urls: arr // 需要预览的图片http链接列表
            });
          },
           onInfinite() {//获取评论
              var self = this;
             
              if(self.currentPage < self.totalPage){

                function load(){
                  self.axios.post('/wechat/shop/evaluateList', querystring.stringify({
                    'commodity_id' : self.id,
                    'current_page' : self.currentPage + 1,
                    'page_size' : 10
                  }))
                  .then(response => {
                    console.log(response.data);
                   
                    if(response.data.success == true){
                      let record = response.data;
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
        },
        mounted(){
            var loading = weui.loading('加载中', {//loading
            });
            this.axios.post('/wechat/shop/detail', querystring.stringify({//详情
                'id':this.id
            }))
            .then(res => {
                loading.hide();//加载
                if(res.data.success){

                    this.detail = res.data.data.commodity_info;
                    this.favorCheck = this.collectionStatus[this.detail.is_collection];
                    this.cms = res.data.data.commodity_info.cms_material_info.data;

                } else {
                  
                }
                
            })
        }
    }
    function utf16toEntities(str){//emoji表情字符处理
    var patt=/[\ud800-\udbff][\udc00-\udfff]/g; // 检测utf16字符正则
        str = str.replace(patt, function(char){
            var H, L, code;
            if (char.length===2) {
                H = char.charCodeAt(0); // 取出高位
                L = char.charCodeAt(1); // 取出低位
                code = (H - 0xD800) * 0x400 + 0x10000 + L - 0xDC00; // 转换算法
                return "&#" + code + ";";
            } else {
                return char;
            }
        });
        return str;
  }
</script>
<style lang="less" scoped>
    @import "../../../assets/css/details.less";
</style>
<style lang="less">
    .stars{
        .el-rate__icon{
           font-size: 0.3871rem;
           margin-right:0.1129rem; 
        }
    }
    .tab_b{
        .detail_wrap{
            img{
                width:100%!important;
                height: auto!important;
            }
        }
    }
    .swiper-pagination-bullet{
        background: transparent!important;
        opacity: 1!important;
        box-sizing: border-box;
      }
      .swiper-container-horizontal > .swiper-pagination-bullets .swiper-pagination-bullet{
        margin: 0 0.0565rem!important;
        border:1px solid #fff;
      }
      .swiper-pagination-bullet-active{
        opacity: 1!important;
        background: #fff!important;
      }
</style>