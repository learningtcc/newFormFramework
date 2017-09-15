
<template>
    <div class="wrapper">
        <div class="score">
            <span class="tit">为订单打分</span>
            <span class="eva_stars">
                <el-rate v-model="starVal" void-color="#ccc"></el-rate>
            </span>
        </div>
        <div class="evaluate_cont">
            <textarea name="" id="" cols="30" rows="10" class="texta" placeholder="请输入您的评价" v-model="content"></textarea>
            <div class="eva_add_pic">
                <span class="pic" v-for="(type,index) in imgArr">
                    <img :src="type" alt="" class="img">
                    <span class="del" @click="deleteImage(index)"><i></i></span>
                </span>
                <div class="add_btn" @click="chooseImage" v-if="imgArr.length < 8"></div>
            </div>
        </div>
        <div class="btn">
            <a @click="submit">提交评价</a>
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
                starVal:5,
                content:'',
                imgArr:[],
                uploadQueue:[],
                serverIds:[],//图片集合
                picList:[]
            }
        },
        methods: {
            utf16toEntities(str){//emoji表情字符处理
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
            },
            deleteImage(index){

                this.imgArr.splice(index,1);
            },
            chooseImage(){//选图片
                var self = this;

                wx.chooseImage({
                    count: 9 - this.imgArr.length, // 默认9
                    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
                    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                    success: res => {
                        var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                        for(let i = 0;i < localIds.length; i++){
                          this.imgArr.push(localIds[i]);

                        }
                        

                    }
                });
            },
            uploadMedia(){          
                for(let i = 0; i < self.imgArr.length; i++){
                  this.uploadQueue.push(this.imgArr[i]);
                }
                this.serverIds = [];
                this.uploadImage(this.uploadQueue);
            },
            uploadImage(uploadQueue){//上传图片

                if(uploadQueue.length == 0){//上传完

                    this.submit();//提交数据
                } else {
                  var localId = uploadQueue[0];

                  wx.uploadImage({
                    localId: localId, // 需要上传的图片的本地ID，由chooseImage接口获得
                    isShowProgressTips: 1, // 默认为1，显示进度提示
                    success: res => {
                        var serverId = res.serverId; // 返回图片的服务器端ID
                        this.serverIds.push(serverId);
                        // if(this.serverIds === ''){
                        //   this.serverIds = serverId;
                        // }else{
                        //   this.serverIds = this.serverIds + ',' + serverId;
                        // }
                        uploadQueue.splice(0, 1);
                        this.uploadImage(uploadQueue);

                    }
                  });
                }

            },
            uploadMaterial(){//微信上传
                this.axios.post('/wechat/material/upload', querystring.stringify({//提交评价
                    'serverids[]':this.serverIds.join(','),
                    'stuffix':'jpg'
                }))
                .then(res => {

                    if(res.data.success){

                    }
                    
                })
            },
            submit(){//提交评价
                var result = {error:false, msg:""};
                testEmpty(this.content, result, '您的评价不能为空');
                if(result.error){
                    weui.topTips(result.msg,1000);//提示出错
                    return;
                }
                var loading = weui.loading('请稍等', {});
                this.axios.post('/wechat/commodityEvaluation/save', querystring.stringify({//提交评价
                    'orderId':this.id,
                    'content':this.utf16toEntities(this.content),
                    'points':this.starVal,
                    'picList':''
                }))
                .then(res => {
                    loading.hide();//加载完
                    if(res.data.success){

                        weui.toast(res.data.message, {//提示  
                          duration: 1000,
                          callback: ()=> {
                            this.$router.go(-1);
                          }
                        });
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
    @import "../../assets/css/myorder_evaluate.less";
</style>
<style lang="less">
    .eva_stars{
        .el-icon-star-off:before{
          content: "\E621";
        }
      }
</style>
