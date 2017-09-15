var wxInited = false;
// {url, onReady, onError}
function wxSign(axios,param){
    if (wxInited) {
        wx.ready(param.onReady);
        wx.error(param.onError);
        return;
    }

    axios.post('/macro/weixin/sign', "url=" + param.url)
    .then(function (response) {//签名
        console.log(response);
        if (response != null && response.data != null) {
            wxInit(response.data);
            wx.ready(param.onReady);
            wx.error(param.onError);
            wxInited = true;
        } else {
            if (param.onError != null) {
                param.onError("sign接口失败");
            }
        }
    })
    .catch(function (error) {
        if (param.onError != null) {
            param.onError(error);
        }
    });
}

function wxInit(response){
    //console.log(response);
    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: response.appId, // 必填，公众号的唯一标识
        timestamp: response.timestamp, // 必填，生成签名的时间戳
        nonceStr: response.noncestr, // 必填，生成签名的随机串
        signature: response.Signa,// 必填，签名，见附录1
        jsApiList: ['chooseImage','previewImage','uploadImage','downloadImage','getLocalImgData','chooseWXPay','getLocation','openLocation','scanQRCode'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });

    //console.log("login complete")
}


