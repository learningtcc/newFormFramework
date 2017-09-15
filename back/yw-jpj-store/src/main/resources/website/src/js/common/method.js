import $ from 'jquery'
import layer from 'static/layer/layer'

// eval代替写法
export function myEval (fn) {
  let Fn = Function  // 一个变量指向Function，防止有些前端编译工具报错
  return new Fn('return ' + fn)()
}

export function getStyle (obj, attr) {
  if (obj.currentStyle) {
    return obj.currentStyle[attr]
  } else {
    return getComputedStyle(obj, false)[attr]
  }
}

export function addClass (obj, className) {
  if (obj.className) {
    if (new RegExp(/^ /).test(obj.className))obj.className = obj.className.replace(/^ +/, '')
    if (new RegExp(/ $/).test(obj.className))obj.className = obj.className.replace(/ +$/, '')
    if (new RegExp(/ +/g).test(obj.className))obj.className = obj.className.replace(/ +/g, ' ')
    var isRepeat = true
    for (var i = 0; i < obj.className.split(' ').length; i++) {
      if (obj.className.split(' ')[i] === className) {
        isRepeat = false
      }
    }
    if (isRepeat) {
      obj.className += ' ' + className
    }
  } else {
    obj.className = className
  }
}
export function myAddEvent (obj, sEvent, fn) {
  if (obj.attachEvent) {
    obj.attachEvent('on' + sEvent, function () {
      fn.call(obj)
    })
  } else {
    obj.addEventListener(sEvent, fn, false)
  }
}

// 时间戳转成特定日期格式
export function getFormatDate (opt) {
  let getDate = new Date(opt.timeStamp)
  let y = getDate.getFullYear()
  let m = getDate.getMonth() + 1
  let d = getDate.getDate()
  let h = getDate.getHours()
  let mm = getDate.getMinutes()
  let s = getDate.getSeconds()
  let getFormatDate = ''

  opt.format.replace(/yyyy|MM|dd|HH|mm|ss/g, function (a) {
    switch (a) {
      case 'yyyy':
        getFormatDate += y + '-'
        break
      case 'MM':
        getFormatDate += add0(m) + '-'
        break
      case 'dd':
        getFormatDate += add0(d) + ' '
        break
      case 'HH':
        getFormatDate += add0(h) + ':'
        break
      case 'mm':
        getFormatDate += add0(mm) + ':'
        break
      case 'ss':
        getFormatDate += add0(s)
        break
    }
  })
  return getFormatDate
}
function add0 (param) {
  return param < 10 ? ('0' + param) : param
}

var loginUser = {}
export function getMenuAuthority (options) {
  var returnObj = {}
  for (var attr in options) {
    if (!/sendMenu|ajaxConfig/.test(attr)) {
      returnObj[attr] = options[attr]
    }
  }
  if (options.ajaxConfig.url && options.ajaxConfig.url.length) {
    $.ajax({
      type: options.ajaxConfig.type || 'get',
      url: options.ajaxConfig.url,
      async: false,
      success: function (response) {
        if (response.isSuccess) {
          loginUser = response.data
          loginUser.allMethod = {}
          loginUser.isBasisAuthority = options.isBasisAuthority// 权限
          if (response.data.otherArray) {
            for (var i = 0; i < response.data.otherArray.length; i++) {
              let code = response.data.otherArray[i].code || response.data.otherArray[i].rightCode
              if (!loginUser.allMethod[code.split('_')[code.split('_').length - 2]]) {
                loginUser.allMethod[code.split('_')[code.split('_').length - 2]] = {}
              }
              loginUser.allMethod[code.split('_')[code.split('_').length - 2]][code.split('_')[code.split('_').length - 1]] = true
            }
          }
          var arr = [];
          (function setMenu (json, arr) {
            for (var i = 0; i < response.data.menuArray.length; i++) {
              for (var j = 0; j < json.length; j++) {
                if (response.data.menuArray[i].code === json[j].href.substring(1) || response.data.menuArray[i].rightCode === json[j].href.substring(1)) {
                  arr.push(json[j])
                  if (arr[arr.length - 1].children) {
                    var temp = arr[arr.length - 1].children
                    arr[arr.length - 1].children = []
                    setMenu(temp, arr[arr.length - 1].children)
                  }
                }
              }
            }
          })(options.sendMenu.btns, arr)
          returnObj.userName = response.data.userName
          returnObj.btns = arr
        } else {
          layer.alert(response.errorMessage, {title: '错误提示', shadeClose: true})
        }
      },
      error: function (response) {
        layer.alert('错误代码：' + response.status + '，请联系管理员', {title: '错误提示', shadeClose: true})
      }
    })
    return returnObj
  } else {
    layer.msg('请输入请求的url地址!', function () {})
  }
}
export { loginUser }

export function startMove (obj, json, fn) {
  clearInterval(obj.timer)
  obj.timer = setInterval(function () {
    var bStop = true
    for (var attr in json) {
      var cur
      if (attr === 'opacity') {
        cur = Math.round(parseFloat(getStyle(obj, attr)) * 100)
      } else if (attr === 'scale') {
        cur = obj.scale
      } else {
        cur = parseInt(getStyle(obj, attr))
      }
      var speed = (json[attr] - cur) / 8
      speed = speed > 0 ? Math.ceil(speed) : Math.floor(speed)
      if (json[attr] !== cur) { bStop = false }
      if (attr === 'opacity') {
        obj.style.filter = 'alpha(opacity:' + (cur + speed) + ')'
        obj.style.opacity = (cur + speed) / 100
      } else if (attr === 'scale') {
        obj.style.webkitTransform = obj.style.transform = 'scale(' + (cur + speed) / 100 + ')'
        obj.scale = cur + speed
      } else {
        obj.style[attr] = cur + speed + 'px'
      }
    }
    if (bStop) {
      clearInterval(obj.timer)
      if (fn)fn()
    }
  }, 16)
}

export function deleteClass (obj, className) {
  if (new RegExp(className, 'g').test(obj.className)) {
    obj.className = obj.className.replace(new RegExp('\\b' + className + '\\b', 'g'), '')
    if (new RegExp(/^ /).test(obj.className))obj.className = obj.className.replace(/^ +/, '')
    if (new RegExp(/ $/).test(obj.className))obj.className = obj.className.replace(/ +$/, '')
  }
}

export function checkActive (opt) {
  for (var i = 0; i < opt.table.querySelector('tbody').querySelectorAll('tr').length; i++) {
    opt.table.querySelector('tbody').querySelectorAll('tr')[i].style.backgroundColor = ''
    opt.table.querySelector('tbody').querySelectorAll('tr')[i].style.color = ''
  }
  opt.tr.style.backgroundColor = opt.backgroundColor
  opt.tr.style.color = 'white'
}

// 身份证校验
export function validateId (opt) {
  var json = {}
  var re = /^\d{17}(\d|X|x)$/
  var idNum = opt.thisValue.toUpperCase() || ''
  if (re.test(idNum)) {
    var weight = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2]    // 十七位数字本体码权重
    var validate = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2']    // mod11,对应校验码字符值
    var sum = 0
    var mode = 0
    var id17 = idNum.substring(0, 17)
    for (var i = 0; i < 17; i++) {
      sum = sum + parseInt(id17.substring(i, i + 1) * weight[i])
    }
    mode = sum % 11

    var c = idNum[17]
    json.success = (c === validate[mode])
  } else {
    json.success = false
  }
  if (json.success === false) { json.errorMessage = '身份证格式错误' }
  json.placeholder = ''
  return json
}
// 网址
export function validateUrl (opt) {
  var json = {}
  var re = /((https|http|ftp|rtsp|mms):\/\/)?(([0-9a-z_!~*'().&=+$%-]+:)?[0-9a-z_!~*'().&=+$%-]+@)?(([0-9]{1,3}\.){3}[0-9]{1,3}|([0-9a-z_!~*'()-]+\.)*([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\.[a-z]{2,6})(:[0-9]{1,4})?((\/?)|(\/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+\/?)/g
  if (re.test(opt.thisValue)) {
    json.success = true
  } else {
    json.success = false
    json.errorMessage = '网址格式错误'
    json.placeholder = ''
  }
  return json
}
// 邮箱
export function validateEmail (opt) {
  var json = {}
  var re = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
  if (re.test(opt.thisValue)) {
    json.success = true
  } else {
    json.success = false
    json.errorMessage = '邮箱格式错误'
    json.placeholder = ''
  }
  return json
}
// 长度校验
export function validateLength (opt) {
  var json = {}
  if (opt.thisValue && opt.thisValue.length >= opt.min && opt.thisValue.length <= opt.max) {
    json.success = true
  } else {
    json.success = false
    json.errorMessage = '字数限制:' + opt.min + '~' + opt.max
    json.placeholder = opt.min + '~' + opt.max + '字'
  }
  return json
}
// 数字长度校验
export function validateNumLength (opt) {
  var json = {}
  if (opt.thisValue.length >= opt.min && opt.thisValue.length <= opt.max && /^\d+$/.test(opt.thisValue)) { // && opt.thisValue != 0
    json.success = true
  } else {
    json.success = false
    json.errorMessage = '必须为' + opt.min + '~' + opt.max + '位整数'// 正
    json.placeholder = opt.min + '~' + opt.max + '位整数'// 正
  }
  return json
}
// 数字大小校验
export function validateNumSize (opt) {
  var json = {}
  if (opt.thisValue >= opt.min && opt.thisValue <= opt.max && /^\d+$/.test(opt.thisValue) && opt.thisValue !== 0) {
    json.success = true
  } else {
    json.success = false
    json.errorMessage = '必须为' + opt.min + '~' + opt.max + '以内正整数'
    json.placeholder = opt.min + '~' + opt.max + '以内正整数'
  }
  return json
}
// 正整数数字校验
export function validateNumber (opt) {
  var json = {}
  if (/^\d+$/.test(opt.thisValue) && opt.thisValue !== 0) {
    json.success = true
  } else {
    json.success = false
    json.errorMessage = '必须为正整数'
    json.placeholder = '正整数'
  }
  return json
}
// 字母数字下划线
export function validateSpecialRule (opt) {
  var regEx = /(^[A-Za-z0-9_]+$)/
  var json = {}
  if (regEx.test(opt.thisValue)) {
    json.success = true
  } else {
    json.success = false
    json.errorMessage = '必须为英文或数字'
    json.placeholder = '英文或数字'
  }
  return json
}
// 不能含有数字特殊字符
export function validateNoNumAndSpecial (opt) {
  var json = {}
  var re = new RegExp('((?=[x21-x7e]+)[^A-Za-z0-9])')
  if (!re.test(opt.thisValue)) {
    json.success = true
  } else {
    json.success = false
    json.errorMessage = '不能有数字或特殊字符'
    json.placeholder = '中文/英文'
  }
  return json
}
// 不能为中文校验
export function validateNoChinese (opt) { // var regEx = /^[\u4E00-\u9FA5]+$/;
  var regEx = /[\u4E00-\u9FA5]/
  var json = {}
  if (!regEx.test(opt.thisValue)) {
    json.success = true
  } else {
    json.success = false
    json.errorMessage = '不能有中文'
    json.placeholder = '非中文'
  }
  return json
}
// 手机号校验
export function validatePhone (opt) {
  var regEx1 = /^1[3|4|5|7|8]\d{9}$/
  var regEx2 = /^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/
  var json = {}
  if (regEx1.test(opt.thisValue) || regEx2.test(opt.thisValue)) {
    json.success = true
  } else {
    json.success = false
    json.errorMessage = '电话号码或手机号码格式错误'
    json.placeholder = ''
  }
  return json
}
// 办公室电话/座机校验
export function validateOfficePhone (opt) {
  var regEx = /^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/
  var json = {}
  if (regEx.test(opt.thisValue)) {
    json.success = true
  } else {
    json.success = false
    json.errorMessage = '座机号码填写错误'
    json.placeholder = ''
  }
  return json
}
// 传真校验
export function validateFax (opt) {
  var regEx = /^(\d{3,4}-)?\d{7,8}$/
  var json = {}
  if (regEx.test(opt.thisValue)) {
    json.success = true
  } else {
    json.success = false
    json.errorMessage = '传真号码格式错误'
    json.placeholder = ''
  }
  return json
}
export function validateEmpty (opt) {
  var json = {}
  if (opt.thisValue) {
    json.success = true
  } else {
    json.success = false
    json.errorMessage = '不能为空'
    json.placeholder = ''
  }
  return json
}

var Loading = (function ($) {
  return {
    on: function (dom) {
      $(dom).find($('.loading')).remove()
      if (!$(dom).hasClass('loading')) {
        $(dom).append('<div class="loading"><i class="fa fa-spinner fa-spin"></i></div>')
      }
    },
    off: function (dom) {
      $(dom).find($('.loading')).remove()
    }
  }
})($)

export { Loading }
