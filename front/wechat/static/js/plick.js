var _heigh=$(window).height();
(function ($) {
    $.extend({
        UrlUpdateParams: function (url, name, value) {
            var r = url;
            if (r != null && r != 'undefined' && r != "") {
                value = encodeURIComponent(value);
                var reg = new RegExp("(^|)" + name + "=([^&]*)(|$)");
                var tmp = name + "=" + value;
                if (url.match(reg) != null) {
                    r = url.replace(eval(reg), tmp);
                }
                else {
                    if (url.match("[\?]")) {
                        r = url + "&" + tmp;
                    } else {
                        r = url + "?" + tmp;
                    }
                }
            }
            window.location.replace(r) ;
        }




    });
})(jQuery);
function utf16toEntities(str) {
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
var setDocumentTitle = function (title) {
  document.title = title;
  var ua = navigator.userAgent;//浏览器发送的用户代理标题：
  //判断手机型号  navigator.userAgent.match(/ip(hone|od|ad)/i)
  //判断是否微信打开     navigator.userAgent.match(/\bMicroMessenger\/([\d\.]+)/)
  if (/\bMicroMessenger\/([\d\.]+)/.test(ua) && /ip(hone|od|ad)/i.test(ua)) {
    var i = document.createElement('iframe');
    i.src = '/favicon.ico';
    i.style.display = 'none';
    i.onload = function () {
      setTimeout(function () {
        i.remove();
      }, 9);
    };
    document.body.appendChild(i);
  }
};
//时间戳转换
(function($) {
  $.extend({
    myTime: {
      /**
       * 当前时间戳
       * @return <int>    unix时间戳(秒)
       */
      CurTime: function(){
        return Date.parse(new Date())/1000;

      },
      /**
       * 日期 转换为 Unix时间戳
       * @param <string> 2014-01-01 20:20:20 日期格式
       * @return <int>    unix时间戳(秒)
       */
      DateToUnix: function(string) {
        var f = string.split(' ', 2);
        var d = (f[0] ? f[0] : '').split('-', 3);
        var t = (f[1] ? f[1] : '').split(':', 3);
        return (new Date(
            parseInt(d[0], 10) || null,
            (parseInt(d[1], 10) || 1) - 1,
            parseInt(d[2], 10) || null,
            parseInt(t[0], 10) || null,
            parseInt(t[1], 10) || null,
            parseInt(t[2], 10) || null
          )).getTime() / 1000;
      },
      /**
       * 时间戳转换日期
       * @param <int> unixTime  待时间戳(秒)
       * @param <bool> isFull  返回完整时间(Y-m-d 或者 Y-m-d H:i:s)
       * @param <int> timeZone  时区
       */
      UnixToDate: function(unixTime, isFull, timeZone) {
        if (typeof (timeZone) == 'number')
        {
          unixTime = parseInt(unixTime) + parseInt(timeZone) * 60 * 60;
        }
        var time = new Date(unixTime * 1000);
        var ymdhis = "";
        ymdhis += time.getFullYear() + "-";
        ymdhis += (time.getMonth()+1) + "-";
        ymdhis += time.getDate();
        if (isFull === true)
        {
          ymdhis += " " + (time.getHours()+1 < 10 ? '0'+(time.getHours()) : time.getHours()) + ":";
          ymdhis += (time.getMinutes()+1 < 10 ? '0'+(time.getMinutes()) : time.getMinutes()) + ":";
          ymdhis += (time.getSeconds()+1 < 10 ? '0'+(time.getSeconds()) : time.getSeconds());
        }
        return ymdhis;
      }
    }

  });

})(jQuery);
//调用方式
/*document.write($.myTime.DateToUnix('2016-04-12 10:49:59')+'<br>');
 document.write($.myTime.UnixToDate(1460429399));*/
function RefreshOnce(){
  if (location.href.indexOf("&xyz=") < 0) {
    location.href = location.href + "&xyz=" + Math.random();
  }
}
