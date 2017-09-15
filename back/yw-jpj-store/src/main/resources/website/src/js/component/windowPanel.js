import $ from 'jquery'
import Panel from 'component/panel'
import GridPanel from 'component/gridPanel'
import FormPanel from 'component/formPanel'

class WindowPanel extends Panel {
  constructor (opt) {
    super(opt)
    this.extraInit()
  }
  extraInit () {
    var self = this
    var totalPrototype = Object.getOwnPropertyNames(Object.getPrototypeOf(this.constructor.prototype)).concat(Object.getOwnPropertyNames(Object.getPrototypeOf(this)))

    totalPrototype.map(function (method) {
      if (/createWindow|createComponent/.test(method)) {
        self[method]()
      }
    })
    this.createDefineBtns({
      container: $(this.win).find('.layui-layer-btn')[0]
    })
    this.createItems()
  }
  createItems () {
    var isTrue
    for (let i in this.items) {
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
        switch (tempArr[i].xtype) {
          case 'FormPanel':
            this.items[tempArr[i].id] = new FormPanel(json)
            break
          case 'GridPanel':
            this.items[tempArr[i].id] = new GridPanel(json)
            break
          default:
            this.items[tempArr[i].id] = eval('new' + tempArr[i].xtype + '(json)')
            break
        }
      }
    }
  }
  createDefineBtns (opt) {
/* eslint-disable no-unused-vars */
    let container = null
    if (opt && opt.container) {
      container = opt.container
    } else {
      if (this.title) {
        container = this.title.className + 'Panel'
      } else {
        container = 'component'
      }
    }
    this.createPanel({
      name: 'defineBtns',
      tag: 'div',
      container: this['container'].parentNode
    })
    this.addExtraClass(this.defineBtns, ['layui-layer-btn'])
    this.addExtraClass(this.defineBtns, this.defineBtnsExtraClass)
    for (var btn in this.btns) {
      if (this.btns[btn] && this.btns[btn].constructor === Object) { this.setBtns(btn) }
    }
  }
}

export default WindowPanel
