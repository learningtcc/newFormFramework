import $ from 'jquery'
import layer from 'static/layer/layer'
import Panel from 'component/panel'
import { WINDOW_CENTER } from 'common/commonClass'
import { Loading, myAddEvent, addClass } from 'common/method'
const Switchery = require('Switchery')

class GridPanel extends Panel {
  constructor (opt) {
    super(opt)

    this.extraInit()
  }
  extraInit () {
    this.container = document.getElementById(WINDOW_CENTER)
    this.createComponent({
      setBox: true
    })

    var self = this
    var totalPrototype = Object.getOwnPropertyNames(Object.getPrototypeOf(this.constructor.prototype)).concat(Object.getOwnPropertyNames(Object.getPrototypeOf(this)))

    totalPrototype.map(function (method) {
      if (/createTitle|createDefineBtns|createSearch/.test(method)) {
        self[method]()
      }
    })

    this.createItems()
    this.searchBtn.click()
  }
  createComponent (opt) {
    if ((this && this.setBox) || (opt && opt.setBox)) {
      this.createPanel({
        name: this.className + 'box',
        tag: 'div',
        container: this.container || opt.container
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
    // style = {...this.style}
    this.addExtraClass(this[this.className], this.extraClass)
    if (this.data) this[this.className].setAttribute('uid', this.data.id)
    this.component = this[this.className]
  }
  createDefineBtns (opt) {
    var container
    if (this.title) {
      container = this.title.className + 'Panel'
    } else {
      container = 'component'
    }
    this.createPanel({
      name: 'defineBtns',
      tag: 'div',
      container: container
    })
    this.addExtraClass(this.defineBtns, this.defineBtnsExtraClass)
    for (var btn in this.btns) {
      if (this.btns[btn] && this.btns[btn].constructor === Object) { this.setBtns(btn) }
    }
  }
  refresh (opt) {
    var dataJson = this.searchBox.dataJson
    var searchFields = this.searchBox.querySelectorAll('[name]')
    for (var i = 0; i < searchFields.length; i++) {
      if (/^input$|^textarea$/i.test(searchFields[i].tagName)) {
        if (!dataJson) {
          searchFields[i].value = ''
        } else if (searchFields[i].name && dataJson && dataJson[i] && dataJson[i].name && (searchFields[i].name === dataJson[i].name)) {
          searchFields[i].value = dataJson[i].value
        }
      } else if (/^select$/i.test(searchFields[i].tagName)) {
        if (!dataJson) {
          searchFields[i].querySelectorAll('option')[0].selected = true
        } else if (searchFields[i].name && dataJson && dataJson[i] && dataJson[i].name && (searchFields[i].name === dataJson[i].name)) {
          for (var j = 0; j < searchFields[i].querySelectorAll('option').length; j++) {
            if (searchFields[i].querySelectorAll('option')[j].value === dataJson[i].value) {
              searchFields[i].querySelectorAll('option')[j].selected = true
            }
          }
        }
      }
    }
    if (this.loading) { return false }
    if (this.table && this.table.parentNode) {
      this.table.parentNode.removeChild(this.table)
      this.pageBox.parentNode.removeChild(this.pageBox)
    }
    Loading.on(this[this.className])
    if (this['errorMessage'] && this['errorMessage'].parentNode) {
      this['errorMessage'].parentNode.removeChild(this['errorMessage'])
    }
    this.loading = true
    var params = {
      pageSize: this.pageSize,
      pageNo: this.pageNo
    }
    if (dataJson) {
      for (let i = 0; i < dataJson.length; i++) {
// params.push(dataJson[i]);
        params[dataJson[i].name] = dataJson[i].value
      }
    }
    var _this = this
    $.ajax({
      url: this.ajaxUrl,
      data: params,
      type: this.ajaxType || 'post',
      success: function (response) {
        if (_this.formatData) {
          response = _this.formatData({
            panel: _this,
            response: response
          })
          if (!response) {
            var msg = '修改后的响应值为空，检查formatData方法是否有返回值'
            layer.alert(msg)
            console.log(msg)
            return false
          }
        };
        if (response.isSuccess) {
          if (response.root || (response.data && (response.data.length || (response.data.root && response.data.root.length && response.data.root.length !== 0)))) {
            var data = response.root || response.data
            _this.setAttributes({
              target: _this[_this.className],
              attrs: {
                uid: 'uid'
              }
            })
            _this.data = data.root ? data.root : data
            if (_this.pageNo) { _this.pageNo = data.pageNo }
            if (_this.pageSize) { _this.pageSize = data.pageSize }
            _this.total = data.total

            var totalPrototype = Object.getOwnPropertyNames(Object.getPrototypeOf(_this.constructor.prototype)).concat(Object.getOwnPropertyNames(Object.getPrototypeOf(_this)))

            totalPrototype.map(function (method) {
              if (!/createPanel|createComponent|createDefineBtns|createTitle|createSearch|refresh|createItems|createWindow/i.test(method)) {
                if (/create/i.test(method)) {
                  _this[method]()
                }
              }
            })

            _this.gridBox.style.display = 'block'
          } else {
            _this.createPanel({
              name: 'errorMessage',
              tag: 'div',
              container: _this.component
            })
            _this['errorMessage'].innerHTML = '数据为空'
            addClass(_this['errorMessage'], 'ibox-content')
            layer.msg(_this['errorMessage'].innerHTML, function () {})
          }
          if (_this.render) {
            _this.render({
              owner: _this.owner,
              panel: _this,
              component: _this.component,
              table: _this.table,
              response: response
            })
          }
          if (_this.table && _this.table.querySelectorAll('.js-switch')) {
            var defaultSet = {
              color: 'steelblue'
            }
            _this.fields.map((item, index, list) => {
              if (item.type === 'switch') {
                if (item.switchSet) {
                  for (var attr in item.switchSet) {
                    defaultSet[attr] = item.switchSet[attr]
                  }
                }
              }
            })
            for (var i = 0; i < _this.table.querySelectorAll('.js-switch').length; i++) {
/* eslint-disable no-new */
              new Switchery(_this.table.querySelectorAll('.js-switch')[i], defaultSet)
            }
          }
        } else if (response.errorMessage) {
          layer.msg(response.errorMessage, function () {})
        } else {
          // layer.msg('登录失效，请重新登录', function () {})
          // setTimeout(function () {
          //   window.location.href = '/login.html'
          // }, 2000)
        }
        Loading.off(_this[_this.className])
        _this.loading = false
      },
      error: function (response) {
        Loading.off(_this[_this.className])
        _this.loading = false
        layer.alert('错误代码:' + response.status + '，请联系管理员', {title: '错误', shadeClose: true})
      }
    })
  }
  createSearch () {
    var _this = this
    this.createPanel({
      name: 'gridBox',
      tag: 'div',
      noClass: true
    })
    this.addExtraClass(this.gridBox, this.gridBoxExtraClass)
    this.gridBox.style.display = 'none'
    this.createPanel({
      name: 'searchBox',
      tag: 'form',
      container: 'gridBox',
      noClass: true
    })
    if (this.search && this.search.fields && this.search.fields.length && this.search.btn) {
      this.addExtraClass(this.searchBox, this.search.extraClass)
      for (var i = 0; i < this.search.fields.length; i++) {
        if (this.search.fields[i].type === 'select') {
          this.createPanel({
            name: this.search.fields[i].name,
            tag: 'select',
            container: 'searchBox',
            noClass: true
          })
          this[this.search.fields[i].name].setAttribute('index', i)
          this[this.search.fields[i].name].placeholder = this.search.fields[i].text
          this[this.search.fields[i].name].field = this.search.fields[i]
          for (var key in this.search.fields[i].value) {
            this.createPanel({
              name: this.search.fields[i].name + 'option' + key,
              tag: 'option',
              container: this.search.fields[i].name,
              noClass: true
            })
            this[this.search.fields[i].name + 'option' + key].innerHTML = key
            this[this.search.fields[i].name + 'option' + key].value = this.search.fields[i].value[key]
          }
          if (this.search.fields[i].async) {
            // var _this = this
            if (!this.selects) { this.selects = [] }
            this.selects.push(_this.search.fields[i])
            this.selectAsync({
              field: this.search.fields[i]
            })
          }
          if (this.search.fields[i].change) {
            this[this.search.fields[i].name].change = this.search.fields[i].change
            myAddEvent(this[this.search.fields[i].name], 'change', function () {
              this.change({
                panel: _this,
                input: this
              })
            })
            this.search.fields[i].change({
              panel: this,
              input: this[this.search.fields[i].name]
            })
          }
          if (this.search.fields[i].linkage) {
            this[this.search.fields[i].name].linkage = this.search.fields[i].linkage
            this[this.search.fields[i].name].onchange = function () {
              for (var attr in this.linkage) {
                if (this.value) {
                  if (_this.searchBox.querySelector('[name="' + attr + '"]').field.async.data.send.constructor !== Object) {
                    var json = {}
                    json[_this.searchBox.querySelector('[name="' + attr + '"]').field.async.data.send] = this.value
                    _this.searchBox.querySelector('[name="' + attr + '"]').field.async.data.send = json
                  } else {
                    for (var i in _this.searchBox.querySelector('[name="' + attr + '"]').field.async.data.send) {
                      _this.searchBox.querySelector('[name="' + attr + '"]').field.async.data.send[i] = this.value
                    }
                  }
                  _this.selectAsync({
                    field: _this.searchBox.querySelector('[name=' + attr + ']').field,
                    isChange: true
                  })
                } else {
                  for (let i = 1; i < _this.searchBox.querySelector('[name=' + attr + ']').children.length; i++) {
                    _this.searchBox.querySelector('[name=' + attr + ']').removeChild(_this.searchBox.querySelector('[name=' + attr + ']').children[i])
                  }
                  if (_this.searchBox.querySelector('[name=' + attr + ']').linkage) { $(_this.searchBox.querySelector('[name=' + attr + ']')).change() } else { _this.searchBtn.click() }
                }
              }
            }
          } else {
            this[this.search.fields[i].name].onchange = function () {
              _this.searchBtn.click()
            }
          }
        } else {
          this.createPanel({
            name: this.search.fields[i].name,
            tag: 'input',
            container: 'searchBox',
            noClass: true
          })
          if (/date|time/.test(this.search.fields[i].type)) {
            _this.layStartTime = {
              choose: function (datas) {
                _this.layEndTime.min = datas // 开始日选好后，重置结束日的最小日期
                _this.layEndTime.start = datas // 将结束日的初始值设定为开始日
                _this.searchBtn.click()
              }
            }
            _this.layEndTime = {
              choose: function (datas) {
                _this.layStartTime.max = datas // 结束日选好后，重置开始日的最大日期
                _this.searchBtn.click()
              }
            }
            if (/time/.test(this.search.fields[i].type)) {
              _this.layStartTime.istime = _this.layEndTime.istime = true
              _this.layStartTime.format = _this.layEndTime.format = this.search.fields[i].format || 'YYYY-MM-DD hh:mm:ss'
            }
            var constra = {
              startTime: function () {
                return _this.layStartTime
              },
              endTime: function () {
                return _this.layEndTime
              }
            }
            this[this.search.fields[i].name].onclick = (ev) => {
/* eslint-disable no-undef */
              layui.use('laydate', function () {
                var laydate = layui.laydate
                laydate((function () {
                  if (/start/.test(ev.target.name)) {
                    constra.startTime().elem = ev.target
                    return constra.startTime()
                  } else if (/end/.test(ev.target.name)) {
                    constra.endTime().elem = ev.target
                    return constra.endTime()
                  } else {
                    return {
                      elem: ev.target,
                      event: 'focus'
                    }
                  }
                })())
              })
            }
          }
          this[this.search.fields[i].name].type = 'text'
          this[this.search.fields[i].name].placeholder = this.search.fields[i].text
          if (this.search.fields[i].value && this.search.fields[i].value.constructor === String) { this[this.search.fields[i].name].value = this.search.fields[i].value }
        }
        if (this.search.fields[i].hidden === true) { this[this.search.fields[i].name].style.display = 'none' }
        this[this.search.fields[i].name].name = this.search.fields[i].name
        this.addExtraClass(this[this.search.fields[i].name], this.search.fields[i].extraClass)
        this[this.search.fields[i].name].onkeydown = function (ev) {
          var oEvent = ev || event
          if (oEvent.keyCode === 13) {
            oEvent.preventDefault()
            _this.searchBtn.click()
          }
        }
      }
    }
    this.createPanel({
      name: 'searchBtn',
      tag: 'span',
      container: 'searchBox',
      noClass: true
    })
    if (!this.search || !this.search.fields || !this.search.fields.length) {
      this.searchBtn.style.display = 'none'
    } else {
      var allHidden = true
      for (let i = 0; i < this.search.fields.length; i++) {
        if (!this.search.fields[i].hidden || this.search.fields[i].hidden !== true) {
          allHidden = false
        }
      }
      if (allHidden) {
        this.searchBtn.style.display = 'none'
      }
    }

    if (this.search && this.search.btn) {
      this.searchBtn.innerHTML = this.search.btn.html ? this.search.btn.html : ''
      this.addExtraClass(this.searchBtn, this.search.btn.extraClass)
    } else {
      this.addExtraClass(this.searchBtn, ['fa', 'fa-search', 'input-group-addon', 'page-search'])
    }
    this.searchBtn.onclick = function () {
      if (_this.pageNo) { _this.pageNo = 1 }
      _this.searchBox.dataJson = $(_this.searchBox).serializeArray()
      if (_this.search && _this.search.btn && _this.search.btn.click) {
        _this.search.btn.click({
          btn: this,
          form: _this.searchBox,
          panel: _this
        })
      } else { _this.refresh() }
    }
  }
  createGrid () {
    this.createPanel({
      name: 'table',
      tag: 'table',
      container: 'gridBox',
      noClass: true
    })
    this['table'].className = 'table-hover'
    this.createPanel({
      name: 'thead',
      tag: 'thead',
      container: 'table',
      noClass: true
    })
    this.createPanel({
      name: 'theadTr',
      tag: 'tr',
      container: 'thead',
      noClass: true
    })
    this['theadTr'].style.fontWeight = 'bold'
    for (var i = 0; i < this.fields.length; i++) {
      if (!this.fields[i]) {
        this.fields.splice(i, 1)
      } else {
        if (this.fields[i].type === 'operations') {
          var noAttr = true
          if (Object.keys(this.fields[i].operationBtns).length > 0) {
            noAttr = false
          }
          if (noAttr) { this.fields.splice(i, 1) }
        }
      }
    }
    for (let i = 0; i < this.fields.length; i++) {
      this.createPanel({
        name: 'theadTd' + i,
        tag: 'td',
        container: 'theadTr',
        noClass: true
      })
      if (this.tableExtraClass) { this.addExtraClass(this['theadTd' + i], this.tableExtraClass.theadTd) }
      if (this.fields[i].text) { this['theadTd' + i].innerHTML = this.fields[i].text }
      if (this.fields[i].name) { this['theadTd' + i].setAttribute('name', this.fields[i].name) }
      if (this.fields[i].style) { this['theadTd' + i].style = this.fields[i].style }
      this['theadTd' + i].setAttribute('index', i)
      this['theadTd' + i].style.whiteSpace = 'nowrap'
    }
    this.createPanel({
      name: 'tbody',
      tag: 'tbody',
      container: 'table',
      noClass: true
    })
    var arr = ['table', 'thead', 'theadTr', 'tbody']
    if (this.tableExtraClass) {
      for (let i = 0; i < arr.length; i++) {
        this.addExtraClass(this[arr[i]], this.tableExtraClass[arr[i]])
      }
    }
    var _this = this
    for (let i = 0; i < this.data.length; i++) {
      this.createPanel({
        name: 'tbodyTr' + i,
        tag: 'tr',
        container: 'tbody',
        noClass: true
      })
      this['tbodyTr' + i].setAttribute('uid', this.data[i].id)
      if (this.extraAttr) {
        for (let attr in this.extraAttr) {
          if (this.data[i][this.extraAttr[attr]]) { this['tbodyTr' + i].setAttribute(attr, this.data[i][this.extraAttr[attr]]) }
        }
      }
      if (this.tableExtraClass) { this.addExtraClass(this['tbodyTr' + i], this.tableExtraClass.tbodyTr) }
      for (var j = 0; j < this.fields.length; j++) {
        this.createPanel({
          name: 'tbodyTd' + i + '-' + j,
          tag: 'td',
          container: 'tbodyTr' + i,
          noClass: true
        })
        if (this.tableExtraClass) { this.addExtraClass(this['tbodyTd' + i + '-' + j], this.tableExtraClass.tbodyTd) }
        if (this.fields[j].type) {
          if (this.fields[j].type === 'checkbox') {
            if (i === 0 && j === 0) {
              this.createPanel({
                name: 'theadCheckbox' + j,
                tag: 'input',
                container: 'theadTd' + j,
                noClass: true
              })
              this['theadCheckbox' + j].type = 'checkbox'
              this['theadCheckbox' + j].onclick = function () {
                var aCheckbox = _this['tbody'].getElementsByTagName('input')
                for (var i = 0; i < aCheckbox.length; i++) {
                  aCheckbox[i].checked = this.checked
                }
              }
            }
            this.createPanel({
              name: 'tbodyCheckbox' + i + '-' + j,
              tag: 'input',
              container: 'tbodyTd' + i + '-' + j,
              noClass: true
            })
            this['tbodyCheckbox' + i + '-' + j].type = 'checkbox'
          } else if (this.fields[j].type === 'increment') {
            if (i === 0 && j === 1 && !this.fields[j].text) {
              this['theadTd' + j].innerHTML = '序号'
            }
            if (this.pageSize) { this['tbodyTd' + i + '-' + j].innerHTML = this.pageSize * (this.pageNo - 1) + i + 1 } else { this['tbodyTd' + i + '-' + j].innerHTML = i + 1 }
          } else if (this.fields[j].type === 'switch') {
            this.createPanel({
              name: 'switch' + i + '-' + j,
              tag: 'input',
              container: 'tbodyTd' + i + '-' + j,
              noClass: true
            })
            this['switch' + i + '-' + j].type = 'checkbox'
            this['switch' + i + '-' + j].name = this.fields[j].name
            addClass(this['switch' + i + '-' + j], 'js-switch')
            if (this.data[i][this.fields[j].name]) {
              if (this.fields[j].checked === this.data[i][this.fields[j].name]) {
                this['switch' + i + '-' + j].setAttribute('checked', true)
              } else {
                this['switch' + i + '-' + j].removeAttribute('checked')
              }
            } else {
              if (this.fields[j].default) {
                this['switch' + i + '-' + j].setAttribute('checked', this.fields[j].default)
              }
            }
            for (var events in this.fields[j].events) {
              myAddEvent(this['switch' + i + '-' + j], events, function (ev) {
                var oEvent = ev || window.event
                for (var i = 0; i < _this.fields.length; i++) {
                  if (_this.fields[i].name === this.name) {
                    _this.fields[i].events[oEvent.type]({
                      panel: _this,
                      owner: _this.owner,
                      tr: $(this).parents('tr')[0],
                      btn: this
                    })
                  }
                }
              })
            }
          } else if (this.fields[j].type === 'inputText') {
            this.createPanel({
              name: 'inputText' + i + '-' + j,
              tag: 'input',
              container: 'tbodyTd' + i + '-' + j,
              noClass: true
            })
            this['inputText' + i + '-' + j].type = 'text'
            this['inputText' + i + '-' + j].name = this.fields[j].name
          } else if (this.fields[j].type === 'operations' && this.fields[j].operationBtns) {
            for (var k in this.fields[j].operationBtns) {
              if (this.fields[j].operationBtns[k].constructor !== Object) {
                delete this.fields[j].operationBtns[k]
              }
            }
            if (this.clearOperationBtns) {
              var allMatch = true
              for (let attr in this.clearOperationBtns) {
                if (this.data[i][attr] !== this.clearOperationBtns[attr]) {
                  allMatch = false
                }
              }
              if (allMatch === true) {
                break
              }
            }
            for (let k in this.fields[j].operationBtns) {
              this.createPanel({
                name: 'btn' + i + '-' + j + '-' + k,
                tag: 'button',
                container: 'tbodyTd' + i + '-' + j,
                noClass: true
              })
              this['btn' + i + '-' + j + '-' + k].setAttribute('methodName', k)
              if (this.fields[j].operationBtns[k].text) { this['btn' + i + '-' + j + '-' + k].innerHTML = this.fields[j].operationBtns[k].text }
              this.addExtraClass(this['btn' + i + '-' + j + '-' + k], this.fields[j].operationBtns[k].extraClass)
              if (this.fields[j].operationBtns[k].events) {
                for (let events in this.fields[j].operationBtns[k].events) {
                  this['btn' + i + '-' + j + '-' + k].index = j + '-' + k
                  myAddEvent(this['btn' + i + '-' + j + '-' + k], events, function (ev) {
                    var oEvent = ev || event
                    oEvent.cancelBubble = true
                    if (/click/i.test(oEvent.type)) {
                      _this.setAttributes({
                        target: _this.component,
                        origin: this.parentNode.parentNode,
                        attrs: {
                          uid: this.parentNode.parentNode.getAttribute('uid') || ''
                        }
                      })
                    }
                    _this.fields[this.index.split('-')[0]].operationBtns[this.index.split('-')[1]].events[oEvent.type]({
                      panel: _this,
                      tr: $(this).parents('tr')[0],
                      btn: this
                    })
                  })
                }
              }
            }
          } else if (this.fields[j].type === 'enumeration') {
              if (this.data[i][this.fields[j].name] && this.selectAsyncParams && this.selectAsyncParams[this.fields[j].name]) {
                let option = null
                for (let arr in this.selectAsyncParams[this.fields[j].name]) {
                  if (arr === this.data[i][this.fields[j].name]) {
                    option = this.selectAsyncParams[this.fields[j].name][arr]
                  }
                }
                if (option) {
                  this['tbodyTd' + i + '-' + j].innerHTML = option
                }
              }
            }
        } else {
          this['tbodyTd' + i + '-' + j].style.wordBreak = 'break-all'

          if (this.fields[j].data) {
            this['tbodyTd' + i + '-' + j].innerHTML = this.fields[j].data[this.data[i][this.fields[j].name]] ? this.fields[j].data[this.data[i][this.fields[j].name]] : ''
          } else if (this.data[i][this.fields[j].name] || this.data[i][this.fields[j].name] === 0) {
            this['tbodyTd' + i + '-' + j].innerHTML = this.data[i][this.fields[j].name]
          }
          if (this.fields[j].value) { this['tbodyTd' + i + '-' + j].innerHTML = this.fields[j].value }
        }
      }
    }
  }
  createPages () {
    var _this = this
    this.createPanel({
      name: 'pageBox',
      tag: 'div',
      container: 'gridBox'
// noClass:true
    })
    this.createPanel({
      name: 'pages',
      tag: 'ul',
      container: 'pageBox',
      noClass: true
    })
    this.createPanel({
      name: 'pageInputContainer',
      tag: 'div',
      container: 'pageBox',
      noClass: true
    })
    if (!this.total || !this.pageSize || !this.pageNo) { return false }
    if (this.total > 5) {
      if (this.pageNo > 2) {
        if (this.pageNo + 3 <= this.total) {
          for (var i = this.pageNo - 3; i < this.pageNo + 2; i++) {
            let li = document.createElement('li')
            this.pages.appendChild(li)
            let a = document.createElement('a')
            li.appendChild(a)
            a.isNumber = true
            a.innerHTML = i + 1
            a.setAttribute('index', i + 1)
          }
        } else {
          for (let i = this.total - 5; i < this.total; i++) {
            let li = document.createElement('li')
            this.pages.appendChild(li)
            let a = document.createElement('a')
            li.appendChild(a)
            a.isNumber = true
            a.innerHTML = i + 1
            a.setAttribute('index', i + 1)
          }
        }
      } else {
        for (let i = 0; i < 5; i++) {
          let li = document.createElement('li')
          this.pages.appendChild(li)
          let a = document.createElement('a')
          li.appendChild(a)
          a.isNumber = true
          a.innerHTML = i + 1
          a.setAttribute('index', i + 1)
        }
      }
      let li = document.createElement('li')
      this.pages.insertBefore(li, this.pages.children[0])
      let a = document.createElement('a')
      li.appendChild(a)
      a.innerHTML = '首页'
      a.setAttribute('index', 1)

      li = document.createElement('li')
      this.pages.insertBefore(li, this.pages.children[1])
      a = document.createElement('a')
      li.appendChild(a)
      a.innerHTML = '<'
      a.setAttribute('index', this.pageNo > 1 ? this.pageNo - 1 : 1)

      li = document.createElement('li')
      this.pages.appendChild(li)
      a = document.createElement('a')
      li.appendChild(a)
      a.innerHTML = '>'
      a.setAttribute('index', this.pageNo < this.total - 1 ? this.pageNo + 1 : this.total)

      li = document.createElement('li')
      this.pages.appendChild(li)
      a = document.createElement('a')
      li.appendChild(a)
      a.innerHTML = '尾页'
      a.setAttribute('index', this.total)
    } else {
      for (let i = 0; i < this.total; i++) {
        let li = document.createElement('li')
        this.pages.appendChild(li)
        let a = document.createElement('a')
        li.appendChild(a)
        a.isNumber = true
        a.innerHTML = i + 1
        a.setAttribute('index', i + 1)
      }
    }
    this.pageSizeNumber = document.createElement('div')
    this.pageBox.appendChild(this.pageSizeNumber)
    this.pageSizeNumber.innerHTML = '每页上限<b>' + this.pageSize + '</b>条'
    this.pageSizeNumber.style.position = 'relative'
    var btns = document.createElement('div')
    this.pageSizeNumber.appendChild(btns)
    var arr = [5, 10, 20, 30, 50, 100]
    for (let i = 0; i < 6; i++) {
      var btn = document.createElement('span')
      btns.appendChild(btn)
      btn.innerHTML = arr[i]
      btn.onclick = function () {
        _this.pageNo = 1
        _this.pageSize = this.innerHTML
        _this.refresh()
      }
    }
// --------------------------------一页显示几个----------------------------------------------------
    this.pageSizeInput = document.createElement('input')
    btns.appendChild(this.pageSizeInput)
//  this.pageSizeInput.placeholder='输入每页上限按回车'
    this.pageSizeInput.type = 'number'
//  this.pageSizeInput.style.display='none';
    this.pageSizeInput.onkeydown = function (ev) {
      var oEvent = ev || event
      if (oEvent.keyCode === 190) { oEvent.preventDefault() }
    }
    this.pageSizeInput.onkeyup = function (ev) {
      var oEvent = ev || event
      if (oEvent.keyCode === 13) {
        if (this.value >= 1) {
          _this.pageSize = parseInt(this.value)
          if (_this.pageSize > 100) { _this.pageSize = 100 }
          _this.pageNo = 1
          _this.refresh()
        }
      }
    }
    // _this.pageSizeTimer
// ------------------------------------------------------------------------------------
    this.allPage = document.createElement('div')
    this.pageBox.appendChild(this.allPage)
    this.allPage.innerHTML = '共<b>' + this.total + '</b>页'
    this.pageNoInput = document.createElement('input')
    this.pageInputContainer.appendChild(this.pageNoInput)
//  this.pageNoInput.placeholder='输入第几页按回车';
    this.pageNoInput.type = 'number'
    this.pageNoInput.onkeydown = function (ev) {
      var oEvent = ev || event
      if (oEvent.keyCode === 190) { oEvent.preventDefault() }
    }
    this.pageNoInput.onkeyup = function (ev) {
      var oEvent = ev || event
      this.value = parseInt(this.value)
      if (oEvent.keyCode === 13) {
        _this.pageBtn.click()
      }
    }
    this.pageBtn = document.createElement('span')
    this.pageInputContainer.appendChild(this.pageBtn)
    this.pageBtn.innerHTML = 'GO'
    this.pageBtn.onclick = function () {
      if (_this.pageNoInput.value) {
        _this.pageNoInput.value = parseInt(_this.pageNoInput.value)
        if (_this.pageNoInput.value > _this.total) {
          _this.pageNoInput.value = _this.total
        } else if (_this.pageNoInput.value < 1) {
          _this.pageNoInput.value = 1
        }
        _this.pageNo = _this.pageNoInput.value
        _this.refresh()
      }
    }
    for (let i = 0; i < this.pages.children.length; i++) {
      if (!/a/i.test(this.pages.children[i].children[0].tagName)) return false
      if (this.pages.children[i].children[0].getAttribute('index') === this.pageNo) {
        if (this.pages.children[i].children[0].isNumber) {
          this.pages.children[i].className = 'active'
        } else {
          this.pages.children[i].className = 'disabled'
        }
        continue
      };
      this.pages.children[i].onclick = function () {
        _this.pageNo = this.children[0].getAttribute('index')
        _this.refresh()
      }
    }
    if (this.pageExtraClass) {
      for (var property in this.pageExtraClass) {
        this.addExtraClass(this[property], this.pageExtraClass[property])
      }
    }
  }
  selectAsync (opt) {
    this[opt.field.name].innerHTML = '<option value="">全部' + this[opt.field.name].placeholder + '</option>'
    var _this = this

    if (opt.field.async.data.send.constructor === String) { return false }
    var data = opt.field.async.data.send

    this[opt.field.name].innerHTML = '<option value="">读取中..</option>'
    $.ajax({
      url: opt.field.async.url,
      data: data,
      type: opt.field.async.type || 'post',
      async: false,
      success: function (response) {
        if (response.data && response.data.length) {
          if (!(_this.selectAsyncParams && Object.keys(_this.selectAsyncParams).length)) {
            _this.selectAsyncParams = {}
          }
          _this.selectAsyncParams[opt.field.name] = {}

          for (var i = 0; i < response.data.length; i++) {
            _this.createPanel({
              name: opt.field.name + 'option' + i,
              tag: 'option',
              container: _this[opt.field.name],
              noClass: true
            })
            let keys = response.data[i][opt.field.async.data.set]
            let values = response.data[i].name
            _this.selectAsyncParams[opt.field.name][keys] = values
            _this[opt.field.name + 'option' + i].innerHTML = values
            _this[opt.field.name + 'option' + i].value = keys
// for(var attr in opt.field.async.data){
// _this[opt.field.name+'option'+i][opt.field.async.data[attr].type]=response.data[i][attr];
// }
          }
        }
        _this[opt.field.name].querySelector('option').innerHTML = '全部' + _this[opt.field.name].placeholder
        if (_this[opt.field.name].field.linkage) {
          $(_this[opt.field.name]).change()
        }
        if (opt.isChange) { _this.searchBtn.click() }
      },
      error: function (response) {
        layer.alert('错误代码:' + response.status + '，请联系管理员', {title: '错误', shadeClose: true})
      }
    })
  }
  hideOtherRows (opt) {
    var rows = this.table.tBodies[0].rows
    for (var i = 0; i < rows.length; i++) {
      if (rows[i] !== opt.tr) { rows[i].style.display = 'none' }
    }
    this.pageBox.style.display = 'none'
  }
}

export default GridPanel
