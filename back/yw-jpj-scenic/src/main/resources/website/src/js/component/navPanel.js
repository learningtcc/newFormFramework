import Panel from 'component/panel'
import { myAddEvent } from 'common/method'

class NavPanel extends Panel {
  // constructor (opt) {
  //   super(opt)
  // }
  init (opt) {
    for (var attr in opt) {
      this[attr] = opt[attr]
    }
    if (this.container && typeof this.container === 'string') {
      this.container = document.getElementById(this.container)
      if (!this.container) {
        this.container = this.defaultContainer
      }
    } else {
      this.container = this.defaultContainer
    }

    var self = this
    var totalPrototype = Object.getOwnPropertyNames(Object.getPrototypeOf(this.constructor.prototype)).concat(Object.getOwnPropertyNames(Object.getPrototypeOf(this)))

    totalPrototype.map(function (item, i, list) {
      if (/createComponent/i.test(item)) {
        self[item](self)
      }
      if (/createLogo|createBtns/i.test(item)) {
        self[item]()
      }
    })
  }
  createLogo () {
    if (this.logo) {
      this.createPanel({
        name: 'logoPanel',
        tag: 'h1'
      })
      this.addExtraClass(this.logoPanel, this.logo.extraClass)
      this.logoPanel.innerHTML = this.logo.html
    }
  }
  createBtns () {
    this.createPanel({
      name: 'btnsPanelBox',
      tag: 'div'
    })
    this.addExtraClass(this.btnsPanelBox, this.btnsPanelBoxExtraClass)
    if (!this.btns) { return false }
    var _this = this
    this.createPanel({
      name: 'btnsPanel',
      tag: 'ul',
      container: 'btnsPanelBox'
    })
    this.addExtraClass(this.btnsPanel, this.btnsPanelExtraClass)
    for (var i = 0; i < this.btns.length; i++) {
      this.createPanel({
        name: this.btns[i].className,
        tag: 'li',
        container: 'btnsPanel'
      })
      this.createPanel({
        name: this.btns[i].className + 'A',
        tag: 'a',
        container: this.btns[i].className
      })

      if (this.btns[i].icon) {
        this.createPanel({
          name: 'iconPanel' + i,
          tag: 'i',
          container: this.btns[i].className + 'A'
        })
        this.addExtraClass(this['iconPanel' + i], this.btns[i].icon)
      }
      if (this.btns[i].text) {
        this.createPanel({
          name: this.btns[i].className + 'Span',
          tag: 'span',
          container: this.btns[i].className + 'A'
        })
        this[this.btns[i].className + 'Span'].innerHTML = this.btns[i].text
      }
      if (this.btns[i].render) {
        this.btns[i].render({
          li: this[this.btns[i].className],
          panel: _this
        })
      }
      this.addExtraClass(this[this.btns[i].className], this.btns[i].extraClass)
      this[this.btns[i].className].index = i
      if (this.btns[i].events) {
        for (var events in this.btns[i].events) {
          myAddEvent(this[this.btns[i].className], events, function (ev) {
            var oEvent = ev || event
            oEvent.cancelBubble = true
            _this.btns[this.index].events[oEvent.type]({
              panel: _this,
              owner: _this.owner,
              btn: this,
              component: _this[_this.className]
            })
            if (_this.target) {
              window.location.hash = _this.btns[this.index].className
            }
          })
        }
      }
    }
    if (this.render) {
      this.render({
        owner: this.owner,
        panel: this,
        ul: this.btnsPanel
      })
    }
  }
}

export default NavPanel
