import $ from 'jquery'
import layer from 'static/layer/layer'
import { addClass, myAddEvent } from 'common/method'

class Panel {
  constructor (opt) {
    this.defaultContainer = document.body
    this.init(opt)
  }
  init (opt) {
    for (let attr in opt) {
      this[attr] = opt[attr]
    }

    this.container = this.container ? this.container : this.defaultContainer

    var isTrue
    for (let i in this.items) {
      if (i) {
        isTrue = true
        break
      }
    }
    if (!isTrue) { this.items = {} } else {
      var items = {}
      for (let i = 0; i < this.items.length; i++) {
        items[this.items[i].id] = this.items[i]
      }
      this.items = items
    }
  }
  createPanel (opt) {
    var _this = opt._this ? opt._this : this
    opt.container = opt.container ? opt.container : _this.className
    _this[opt.name] = document.createElement(opt.tag)
    var nowContainer
    if (opt.container.constructor === String) {
      nowContainer = _this[opt.container]
    } else {
      nowContainer = opt.container
    }
    if (opt.insertBefore) {
      nowContainer.insertBefore(_this[opt.name], opt.insertBefore)
    } else {
      nowContainer.appendChild(_this[opt.name])
    }
    if (opt.name && !opt.noClass)_this[opt.name].className = opt.name
    if (opt.id && opt.id.length) {
      _this[opt.name].setAttribute('id', opt.id)
    }
  }
  setAttributes (opt) {
    for (var i in opt.attrs) {
      if (i && opt.attrs[i]) {
        opt.target.setAttribute(i, opt.attrs[i])
      }
    }
  }
  setBtns (btn) {
    this.createPanel({
      name: btn,
      tag: 'a',
      container: 'defineBtns'
    })
    this.addExtraClass(this[btn], this.btns[btn].extraClass)
    if (this.btns[btn].html) { this[btn].innerHTML = this.btns[btn].html }
    var _this = this
    if (this.btns[btn].events) {
      for (var events in this.btns[btn].events) {
        myAddEvent(this[btn], events, function (ev) {
          var oEvent = ev || event
          oEvent.cancelBubble = true
          _this.btns[btn].events[oEvent.type]({
            btn: this,
            panel: _this,
            owner: _this.owner
          })
          if (oEvent.type === 'click' && btn === 'closeBtn') {
            _this.container.onscroll = document.onscroll = window.onresize = null
            clearInterval(_this.timer)
          }
        })
      }
    }
  }
  addItem (opt) {
    var json = {
      owner: this,
      ownerBtn: this
    }
    for (var i in opt.config) {
      json[i] = opt.config[i]
    }
    if (opt.alias) {
      json.alias = opt.alias
      this.items[opt.alias] = eval(opt.xtype)(json)
// this.items[opt.alias]=types[opt.xtype](json);
    } else {
      this.items[opt.xtype] = eval(opt.xtype)(json)
// this.items[opt.xtype]=types[opt.xtype](json);
    }
  }
  createWindow () {
    if (!this.id) { this.id = 'layerWindow' }
    if (!this.className) { this.className = 'layerWindow' }
    this.winId = this.id + 'Win' || 'win'
    var winOffsetTop = 76
    var layerConfig = {
      type: 1,
      title: '信息',
      closeBtn: 1, // 不显示关闭按钮
      shift: 2,
      offset: winOffsetTop + 'px', // top
      area: '600px', // width
      id: this.winId,
      cancel: function () {
        layer.closeAll()
      }
    }
    if (this.layerConfig && this.layerConfig.constructor === Object) {
      for (var attr in this.layerConfig) {
        layerConfig[attr] = this.layerConfig[attr]
      }
    }
    layer.open(layerConfig)
    document.querySelector('#' + this.winId).parentNode.onmousewheel = function () {
      if (document.documentElement.clientHeight - this.offsetHeight - winOffsetTop <= 0) {
        layer.closeAll('tips')
        if (event.wheelDelta > 0) {
          if (this.offsetTop <= winOffsetTop && this.offsetTop > 15) {
            this.style.top = winOffsetTop + 'px'
          } else {
            this.style.top = parseInt(getComputedStyle(this, false)['top']) + 35 + 'px'
          }
        } else {
          if (document.documentElement.clientHeight - this.offsetTop - this.offsetHeight >= 50) {
            this.style.top = this.offsetTop + 'px'
          } else {
            this.style.top = parseInt(getComputedStyle(this, false)['top']) - 35 + 'px'
          }
        }
      }
    }
    this.win = $('#' + this.winId).parents('.layui-layer')[0]
    this.container = $('#' + this.winId)[0]
    $(this.win).find('.layui-layer-btn').html('')

    if (this.render) {
      this.render({
        container: this.container,
        owner: this.owner
      })
    }
  }
  createComponent (opt) {
    if ((this && this.setBox) || (opt && opt.setBox)) {
      this.createPanel({
        name: this.className + 'box',
        tag: 'div',
        container: 'container'
      })
      if (this.id) { this[this.className + 'box'].id = this.id }
      if (this.boxExtraClass) this.addExtraClass(this[this.className + 'box'], this.boxExtraClass)
      this.createPanel({
        name: this.className,
        tag: 'div',
        container: this.className + 'box'
      })
    } else {
      this.createPanel({
        name: this.className,
        tag: 'div',
        container: 'container'
      })
      if (this.id) { this[this.className].id = this.id }
    }
    // for (var attr in this.style) {
    //   style[attr] = this.style[attr]
    // }
    this.addExtraClass(this[this.className], this.extraClass)
    if (this.data) this[this.className].setAttribute('uid', this.data.id)
    this.component = this[this.className]
  }
  createItems () {
    var isTrue
    for (var i in this.items) {
      if (i) {
        isTrue = true
        break
      }
    }
    if (isTrue) {
      var tempArr = this.items
      for (let i in tempArr) {
        var json = {}
        json.container = this[this.className]
        for (var property in tempArr[i]) {
          json[property] = tempArr[i][property]
        }
        json.owner = this
        if (tempArr[i] && tempArr[i].xtype) {
          this.items[tempArr[i].id] = eval('new ' + tempArr[i].xtype + '(json)')
        }
      }
    }
  }
  clearAllItem () {
    (function removeItem (panel) {
      for (var i in panel) {
        $('#' + i).remove()
// delete this.items[i];
        if (panel[i].items) { removeItem(panel[i].items) }
      }
    })(this.items)
  }
  addExtraClass (obj, extraClass) {
    if (obj && extraClass) {
      for (var i = 0; i < extraClass.length; i++) {
        addClass(obj, extraClass[i])
      }
    }
  }
  createTitle () {
    if (!this.title) { return false }
    this.title.className = this.title.className || 'title'
    this.createPanel({
      name: this.title.className + 'Panel',
      tag: 'div'
    })
    this.titleBox = this[this.title.className + 'Panel']
    // for (let attr in this.style) {
    //   style[attr] = this.style[attr]
    // }
    this[this.title.className + 'Panel'].className = this.title.className + ' ' + this[this.title.className + 'Panel'].className
    this.createPanel({
      name: 'titleH',
      tag: 'h5',
      container: this.title.className + 'Panel'
    })
    this.titleH.innerHTML = this.title.innerHTML
    this.addExtraClass(this[this.title.className + 'Panel'], this.title.extraClass)
  }
}

export default Panel
