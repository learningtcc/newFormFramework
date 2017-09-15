import $ from 'jquery'
import layer from 'static/layer/layer'
import Panel from 'component/panel'
import { WINDOW_CENTER } from 'common/commonClass'
import { Loading, myAddEvent, addClass } from 'common/method'
var echarts = require('echarts')

class ChartPanel extends Panel {
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
      if (/createTitle|createSearch/.test(method)) {
        self[method]()
      }
    })

    this.createItems()
    this.searchBtn.click()
  }
  refresh (opt) {
    var dataJson = this.searchBox.dataJson
    var searchFields = this.searchBox.querySelectorAll('[name]')
    for (let i = 0; i < searchFields.length; i++) {
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
    Loading.on(this[this.className])
    if (this['errorMessage'] && this['errorMessage'].parentNode) {
      this['errorMessage'].parentNode.removeChild(this['errorMessage'])
    }
    this.loading = true
    var params = {}
    if (dataJson) {
      for (let i = 0; i < dataJson.length; i++) { // params.push(dataJson[i]);
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
            _this.ajaxSuccess({
              panel: _this,
              response: response
            })

            var totalPrototype = Object.getOwnPropertyNames(Object.getPrototypeOf(_this.constructor.prototype)).concat(Object.getOwnPropertyNames(Object.getPrototypeOf(_this)))

            totalPrototype.map(function (method) {
              if (!/createPanel|createComponent|createDefineBtns|createTitle|createSearch|refresh|createItems|createWindow/i.test(method)) {
                if (/create/i.test(method)) {
                  _this[method]()
                }
              }
            }) // _this.gridBox.style.display='block';
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
        } else if (response.errorMessage) {
          layer.msg(response.errorMessage, function () {})
        } else {
          layer.msg('登录失效，请重新登录', function () {})
          setTimeout(function () {
            window.location.href = '/login.htm'
          }, 2000)
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
    let _this = this
    this.createPanel({
      name: 'gridBox',
      tag: 'div',
      noClass: true
    })
    this.addExtraClass(this.gridBox, this.gridBoxExtraClass) // this.gridBox.style.display='none';
    this.createPanel({
      name: 'searchBox',
      tag: 'form',
      container: 'gridBox',
      noClass: true
    })
    if (this.search && this.search.fields && this.search.fields.length && this.search.btn) {
      this.addExtraClass(this.searchBox, this.search.extraClass)
// this.searchBox.style.display='none';
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
            let _this = this
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
          if (/date|time|startTime|endTime/.test(this.search.fields[i].type)) {
            _this.layStartTime = {
              event: 'focus',
              choose: function (datas) {
                _this.layEndTime.min = datas // 开始日选好后，重置结束日的最小日期
                _this.layEndTime.start = datas // 将结束日的初始值设定为开始日
                _this.searchBtn.click()
              }
            }
            _this.layEndTime = {
              event: 'focus',
              choose: function (datas) {
                _this.layStartTime.max = datas // 结束日选好后，重置开始日的最大日期
                _this.searchBtn.click()
              }
            }
            if (/time/.test(this.search.fields[i].type)) {
              _this.layStartTime.istime = _this.layEndTime.istime = true
              _this.layStartTime.format = _this.layEndTime.format = 'YYYY-MM-DD hh:mm:ss'
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
          if (this.search.fields[i].hidden === true) { this[this.search.fields[i].name].style.display = 'none' }
          if (this.search.fields[i].value && this.search.fields[i].value.constructor === String) { this[this.search.fields[i].name].value = this.search.fields[i].value }
        }
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
  createChart () {
    this.createPanel({
      name: this.id + 'Entity',
      tag: 'div',
      container: this.gridBox,
      id: this.id + 'Entity'
    })
    document.getElementById(this.id + 'Entity').style.height = this.chartHeight + 'px'
    document.getElementById(this.id + 'Entity').style.marginTop = 40 + 'px'
    var myChart = echarts.init(document.getElementById(this.id + 'Entity'))
    myChart.setOption(this.chartOptions)
  }
}

export default ChartPanel
