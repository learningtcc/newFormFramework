import $ from 'jquery'
import layer from 'static/layer/layer'
/* eslint-disable no-unused-vars */
import jForm from 'static/jquery.form'
import Panel from 'component/panel'
import * as getMethods from 'common/method'
import WangEditor from 'wangEditor'
import { Z_INDEX_HIGH } from 'common/commonClass'
let editor = null

class FormPanel extends Panel {
  constructor (opt) {
    super(opt)

    if (this.id && document.getElementById(this.id)) { document.getElementById(this.id).parentNode.removeChild(document.getElementById(this.id)) }

    this.extraInit()
  }
  extraInit () {
    this.queryData = this.data
    let container
    if (this.layerConfig) {
      this.createWindow()
      this.createComponent()
      container = $(this.win).find('.layui-layer-btn')[0]
    } else {
      if (this.container) {
        if (typeof this.container === 'string') {
          this.container = document.getElementById(this.container)
        }
      }
      this.createComponent({
        setBox: true
      })
      this.createTitle()
      if (this.title && this.title.className) {
        container = this.title.className + 'Panel'
      } else { container = this.component }
    }
    this.createPanel({
      name: 'defineBtns',
      tag: 'div',
      container: container
    })
    this.addExtraClass(this.defineBtns, this.defineBtnsExtraClass)
    if ((this.data && this.data.id) || this.mustQuery) {
      this.refresh()
    } else {
      this.auto()
      if (this.render) {
        this.render({
          owner: this.owner,
          panel: this,
          component: this.component
        })
      }
    }
    for (var btns in this.btns) {
      this.setBtns(btns)
    }
    this.createItems()
  }
  createForm () {
    this.createPanel({
      name: 'formContainer',
      tag: 'form', // noClass:true,
      container: this.className
    })
    this.addExtraClass(this.formContainer, this.formExtraClass)
    this.mustValidate = []
  }
  createField () {
// let _this = this
    this.selects = []
    if (this.fields && this.fields.length) {
      for (var i = 0; i < this.fields.length; i++) {
        if (this.fields[i].column) {
          var columns = this.fields[i].column
          var fieldBoxDivColumn = 'field-boxDiv-column' + i
          this.createPanel({
            name: fieldBoxDivColumn,
            tag: 'div',
            container: 'formContainer',
            noClass: true
          })
          for (let property in this.fields[i].extraClass) {
            this.addExtraClass(this['field-' + property + i], this.fields[i].extraClass[property])
          }
          for (let n = 0; n < columns.length; n++) {
            this.createPanel({
              name: 'field-boxDiv' + i + n,
              tag: 'div',
              container: fieldBoxDivColumn,
              noClass: true
            })
            for (let property in this.fields[i].extraClass) {
              this.addExtraClass(this['field-' + property + i + n], this.fields[i].extraClass[property])
            }
            if (this.fieldExtraClass) { this.addExtraClass(this[fieldBoxDivColumn], this.fieldExtraClass.column) }
// 重复-------------------------------------------------------------------------------------------
            this.setField({
              fields: columns[n],
              number: '' + i + n
            })
// 重复结束---------------------------------------------------------------------------------------
          }
        } else {
          this.createPanel({
            name: 'field-boxDiv' + i,
            tag: 'div',
            container: 'formContainer',
            noClass: true
          })
// 重复-----------------------------------------------------------------------------------------------
          this.setField({
            fields: this.fields[i],
            number: i
          })
// 重复结束-------------------------------------------------------------------------------------------
        }
      }
    }
  }
  setBtns (btn) {
    if (!this.btns[btn]) { return false }
    this.createPanel({
      name: btn,
      tag: 'a',
      container: 'defineBtns'
    })
    this.addExtraClass(this[btn], this.btns[btn].extraClass)
    if (this.btns[btn].html) this[btn].innerHTML = this.btns[btn].html
    var _this = this
    this[btn].onclick = function () {
      if (btn === 'edit') { // 表单提交
        _this.validatesFn()
      } else {
        _this.btns[btn].events.click({
          owner: _this.owner,
          component: _this.formContainer,
          panel: _this,
          btn: this
        })
      }
      if (btn === 'closeBtn') {
        _this.container.onscroll = document.onscroll = window.onresize = null
        clearInterval(_this.timer)
      }
    }

    if (this.owner.constructor.name === 'TreePanel' && btn !== 'edit') {
      if (this.btns[btn].events) {
        for (var events in this.btns[btn].events) {
          getMethods.myAddEvent(this[btn], events, function (ev) {
            var oEvent = ev || event
            oEvent.cancelBubble = true
            _this.btns[btn].events[oEvent.type]({
              btn: this,
              panel: _this,
              owner: _this.owner
            })
          })
        }
      }
    }
  }
  refresh () {
    // layer.closeAll('tips');
    this.mustValidate = []
    var _this = this
    _this.container.onscroll = document.onscroll = window.onresize = null
    clearInterval(_this.timer)
    if (this.loading) { return false }
    if (this.formContainer) {
      this.formContainer.parentNode.removeChild(this.formContainer)
    }
    if ((this.data && this.data.id) || this.mustQuery) {
      getMethods.Loading.on(this[this.className])
      this.loading = true
      $.ajax({
        url: _this.queryUrl,
        data: this.queryData,
        type: _this.queryType || 'post',
        success: function (response) {
          if (response.isSuccess) {
            if (_this.formatData) {
              _this.formatData({
                panel: _this,
                response: response
              })
            }
            _this.data = response.data
            if (!_this.noFirst) {
              _this.fields.unshift({
                label: 'id',
                hidden: true,
                name: 'id'
              })
              _this.noFirst = true
            }
            if (this.isWindow && response.data.name) { _this.titleH.innerHTML = response.data.name }
            _this.auto()
          } else if (response.errorMessage) {
            layer.closeAll()
            console.log(_this.owner.constructor)
            if (_this.owner.constructor === 'WindowPanel') {
              _this.owner.clearAllItem()
            } else {
              $('#' + _this.id).remove()
            }
            layer.msg(response.errorMessage, function () {})
          } else {
            // layer.msg('登录失效，请重新登录', function () {})
            // setTimeout(function () {
            //   window.location.href = '/login.html'
            // }, 2000)
          }
          if (_this.render) {
            _this.render({
              owner: _this.owner,
              panel: _this,
              component: _this.component,
              response: response
            })
          }
          getMethods.Loading.off(_this[_this.className])
          _this.loading = false
        },
        error: function (response) {
          getMethods.Loading.off(_this[_this.className])
          _this.loading = false
          layer.alert('错误代码:' + response.status + '，请联系管理员', {title: '错误', shadeClose: true})
        }
      })
    } else {
      _this.auto()
      if (this.render) {
        this.render({
          owner: this.owner,
          panel: this,
          component: this.component
        })
      }
    }
  }
  auto () {
    var self = this
    var totalPrototype = Object.getOwnPropertyNames(Object.getPrototypeOf(this.constructor.prototype)).concat(Object.getOwnPropertyNames(Object.getPrototypeOf(this)))

    totalPrototype.map(function (method) {
      if (/addExtraClass|createForm|createField/.test(method)) {
        self[method]()
      }
    })
    var _this = this
    var target = [{
      obj: this.container,
      event: 'scroll'
    }, {
      obj: document,
      event: 'scroll'
    }, {
      obj: window,
      event: 'resize'
    }]
    if (this.owner && this.owner.id === 'layerWindow') {
      target.push({
        obj: this.owner.container,
        event: 'scroll'
      })
    }
    function autoValidate () {
      layer.closeAll('tips')
      clearInterval(_this.timer)
      _this.timer = setTimeout(function () {
        for (var i = 0; i < _this.mustValidate.length; i++) {
          $(_this.mustValidate[i]).blur()
        }
      }, 200)
    }
    for (var i = 0; i < target.length; i++) {
      getMethods.myAddEvent(target[i].obj, target[i].event, function () {
        autoValidate()
      })
    }
    if (document.querySelector('.layui-layer-title')) {
      document.querySelector('.layui-layer-title').onmousedown = function () {
        document.onmouseup = function () {
          clearInterval(_this.timer)
          _this.timer = setTimeout(function () {
            for (var i = 0; i < _this.mustValidate.length; i++) {
              $(_this.mustValidate[i]).blur()
            }
          }, 200)
          this.onmouseup = null
        }
      }
    }
    if (this.selects) {
      for (let i = 0; i < this.selects.length; i++) {
// if(this.selects[i].linkage||this.selects[i].async.mustGet){
        this.selectAsync({
          obj: this.selects[i]
        })
      }
    }
  }
  validatorBlur (opt) {
    var _this = this
    var obj = opt.obj
    var point
    if (obj.offsetTop === 0 && obj.offsetLeft === 0) {
      if (opt.obj.parentNode.querySelector('[name="' + opt.obj.name + 'Name' + '"]')) {
        point = opt.obj.parentNode.querySelector('[name="' + opt.obj.name + 'Name' + '"]')
      } else if (opt.obj.parentNode.querySelector('a')) {
        point = opt.obj.parentNode.querySelector('a')
      }
    } else {
      point = opt.obj
    }
    var validator = opt.validator
    if (validator) { var async = validator.async }
    var dropDown = opt.dropDown
    if (obj.isTouch) {
      return false
    } else {
      obj.isTouch = true
    }
    setTimeout(function () { obj.isTouch = false }, 100)
    if (validator) {
      if (!validator.validateRule) {
        validator.validateRule = {}
      }
      if (opt.obj.tagName === 'DIV' && opt.obj.getAttribute('type') === 'richTextEditor') {
        validator.validateRule.thisValue = editor.txt.text()
      } else {
        validator.validateRule.thisValue = obj.value
      }
    }
    var alltestAdopt = true
    obj.errorMessage = []
    obj.placeholderMsg = []
    if (validator && validator.type) {
      for (var i = 0; i < validator.type.length; i++) {
        var result
        if (validator.type.constructor === Array) {
          result = getMethods[validator.type[i]](validator.validateRule)
// result=eval(validator.type[i]+"(validator.validateRule)");
        } else if (validator.type.constructor === Function) {
          result = validator.type(validator.validateRule)
        }
        if (!result.success) {
          if (!obj.errorMessage) { obj.errorMessage = [] }
          obj.errorMessage.push(result.errorMessage)
          if (result.placeholder) { obj.placeholderMsg.push(result.placeholder) }
          alltestAdopt = false
        }
      }
    }
    obj.setAttribute('isReady', false)
    if (alltestAdopt) {
      obj.setAttribute('validator', true)
      layer.close(obj.tips)
    } else {
      obj.setAttribute('validator', false)
      layer.close(obj.tips)
      if (obj.getAttribute('allowEmpty') === 'true' && !obj.value) {
// obj.errorMessage='允许为空';
      } else if (obj.getAttribute('allowEmpty') === 'true' && obj.value) {
// obj.errorMessage+=';允许为空';
        obj.tips = layer.tips(obj.errorMessage, point, {time: 0, tipsMore: true})
      } else {
        obj.tips = layer.tips(obj.errorMessage, point, {time: 0, tipsMore: true})
      }

      if (obj.isOpen) {
        if (!obj.placeholder) { obj.placeholder = obj.placeholderMsg ? obj.placeholderMsg : obj.errorMessage }
        obj.isOpen = false
        layer.close(obj.tips)
      }
    }
    obj.setAttribute('isReady', true)

    if (alltestAdopt && async && async.url && /false/.test(obj.getAttribute('async'))) {
      var data = {}
      for (var attr in async.data) {
        if (async.data[attr] === 'value') {
          data[attr] = obj.value
        } else {
          data[attr] = async.data[attr]
        }
      }
      if (obj.isOpen) {
        obj.isOpen = false
      } else {
        obj.tips = layer.tips('正在查询', obj, {time: 0, tipsMore: true})
      }
      $.ajax({
        url: async.url,
        type: async.type || 'post',
        data: data,
        success: function (response) {
          if (_this.component.offsetHeight === 0) {
            return false
          }
          if (response.isSuccess) {
            obj.setAttribute('isReady', true)
            obj.setAttribute('async', true)
            if (obj.tips) {
              layer.close(obj.tips)
              obj.tips = layer.tips('校验成功', obj, {time: 2000, tipsMore: true})
            }
          } else {
            obj.setAttribute('isReady', true)
            obj.setAttribute('async', false)
            if (obj.tips) {
              layer.close(obj.tips)
              obj.tips = layer.tips('编码已存在', obj, {time: 0, tipsMore: true})
            }
          }
        },
        error: function (response) {
          layer.alert('错误代码:' + response.status + '，请联系管理员', {title: '错误', shadeClose: true})
        }
      })
    } else if (!alltestAdopt && async && async.url && /false/.test(obj.getAttribute('async'))) {
      _this.asyncFailure = true
    }
    if (dropDown && dropDown === true) {
      obj.setAttribute('isReady', false)
      setTimeout(function () {
        obj.setAttribute('isReady', true)
      }, 200)
    }
  }
  setField (opt) {
    var _this = this
    var fields = opt.fields
    var fieldBoxDiv = 'field-boxDiv' + opt.number
    var fieldLabel = 'field-label' + opt.number
    var fieldDiv = 'field-div' + opt.number
    if (fields.hidden) { this[fieldBoxDiv].style.display = 'none' }
    this.createPanel({
      name: fieldLabel,
      tag: 'label',
      container: fieldBoxDiv,
      noClass: true
    })
    if (this.data && this.data.id && fields.name === 'userName') {
      fields.r = true
      delete fields.validator
    }
    if (opt.fields.required === true) {
      this[fieldLabel].innerHTML = '<strong style="color:red;font-weight:normal;">*</strong>'
      if (!fields.validator && fields.type !== 'radio') {
        fields.validator = {}
        if (fields.readonly !== true) { fields.validator.type = ['validateEmpty'] }
      }
    } else {
// this[fieldInputText].setAttribute('allowEmpty',true);
      opt.fields.allowEmpty = true
    }
    this[fieldLabel].innerHTML += fields.label
    this.createPanel({
      name: fieldDiv,
      tag: 'div',
      container: fieldBoxDiv,
      noClass: true
    })

    if (fields.tagName === 'textarea') {
      var fieldTextarea = 'field-textarea' + opt.number
      this.createPanel({
        name: fieldTextarea,
        tag: 'textarea',
        container: fieldDiv,
        noClass: true
      })
      this['field-' + opt.number] = this[fieldTextarea]
      this[fieldTextarea].name = fields.name
      this[fieldTextarea].cols = fields.cols
      this[fieldTextarea].rows = fields.rows
      if (this.data && this.data[fields.name]) { this[fieldTextarea].value = decodeURIComponent(this.data[fields.name]) }
    } else if (fields.tagName === 'select') {
      var fieldSelect = 'field-select' + opt.number
      this.createPanel({
        name: fieldSelect,
        tag: 'select',
        container: fieldDiv,
        noClass: true
      })
      this['field-' + opt.number] = this[fieldSelect]
      this[fieldSelect].name = fields.name

      // 枚举类型
      if (fields.type === 'enumeration' && this.owner.owner.selectAsyncParams && this.owner.owner.selectAsyncParams[fields.name]) {
        let obj = this.owner.owner.selectAsyncParams[fields.name]
        $(this[fieldSelect]).append('<option value="">请选择</option>')
        for (let attr in obj) {
          $(this[fieldSelect]).append(`<option value="${attr}">${obj[attr]}</option>`)
        }
      } else if (fields.async) {
        this[fieldSelect].fields = fields
        this[fieldSelect].async = fields.async
        this[fieldSelect].setAttribute('index', opt.number)
        this.selects.push(this[fieldSelect])
      }
      if (this.data && this.data[fields.name]) {
        $(this[fieldSelect]).val(this.data[fields.name])
        this[fieldSelect].selectValue = this.data[fields.name]
      }
      if (fields.readonly === true) {
        this[fieldSelect].setAttribute('disabled', true)
        this[fieldSelect].setAttribute('isreadonly', true)
      }
      if (fields.linkage) {
        this[fieldSelect].linkage = fields.linkage
        this[fieldSelect].onchange = function () {
          for (var attr in this.linkage) {
            for (var property in _this.formContainer.querySelector('[name="' + attr + '"]').async.data) {
              if (_this.formContainer.querySelector('[name="' + attr + '"]').async.data[property].type === 'value') {
                _this.formContainer.querySelector('[name=' + attr + ']').async.data[property].value = this.value
              }
            }
            _this.selectAsync({
              origin: this,
              obj: _this.formContainer.querySelector('[name=' + attr + ']')
            })
          }
        }
      }
    } else {
      if (fields.type === 'radio') {
        for (var j = 0; j < fields.option.length; j++) {
          var fieldInputRadioLabel = 'field-inputRadioLabel' + opt.number + j
          this.createPanel({
            name: fieldInputRadioLabel,
            tag: 'label',
            container: fieldDiv,
            noClass: true
          })
          var fieldInputRadio = 'field-inputRadio' + opt.number + j
          this.createPanel({
            name: fieldInputRadio,
            tag: 'input',
            container: fieldInputRadioLabel,
            noClass: true
          })
          $(this[fieldInputRadioLabel]).append(fields.option[j].html)
          this[fieldInputRadio].type = 'radio'
          this[fieldInputRadio].value = fields.option[j].value
          this[fieldInputRadio].name = fields.name
          this[fieldInputRadio].id = fields.name + j
          if (this.data && (this.data[fields.name] || this.data[fields.name] === 0)) {
            this[fieldInputRadio].checked = this[fieldInputRadio].value === this.data[fields.name]
          } else if (this[fieldInputRadio].getAttribute('value') === fields.value) {
            this[fieldInputRadio].checked = true
          } else {
            if (!j) { this[fieldInputRadio].checked = true }
          }
          if ((fields.requireReadonly || (fields.readonly && this.data.id)) && !this[fieldInputRadio].checked) {
            this[fieldInputRadio].disabled = true
          }
          if (this.fieldExtraClass) {
            this.addExtraClass(this[fieldInputRadioLabel], this.fieldExtraClass.inputRadioLabel)
            this.addExtraClass(this[fieldInputRadio], this.fieldExtraClass.inputRadio)
          }
        }
// }else if(fields.type=='file'){
      } else if (/img|xml|pem|video/i.test(fields.type)) {
        var fieldInputText = 'field-inputText' + opt.number
        this.createPanel({
          name: fieldInputText,
          tag: 'input',
          container: fieldDiv,
          noClass: true
        })
        this[fieldInputText].name = fields.name
        if (this.data && this.data[fields.name]) {
          this[fieldInputText].value = this.data[fields.name]
        } else {
          this[fieldInputText].value = fields.value ? fields.value : ''
        }
        this['field-' + opt.number] = this[fieldInputText]
        this[fieldInputText].style.display = 'none'
        var fieldFileIframe = 'field-fileIframe' + opt.number
        this.createPanel({
          name: fieldFileIframe,
          tag: 'iframe',
          container: fieldDiv,
          noClass: true
        })
        this[fieldFileIframe].style.display = 'none'
        this[fieldFileIframe].name = 'iframe'
        var fieldFileForm = 'field-fileForm' + opt.number
        this.createPanel({
          name: fieldFileForm,
          tag: 'form',
          container: fieldDiv,
          noClass: true
        })
        this[fieldFileForm].target = 'iframe'
        this[fieldFileForm].method = 'post'
        var actionType = {
          img: '/common/uploadImage',
          xml: '/common/uploadXml',
          pem: '/common/uploadPem',
          video: '/common/uploadVideo'
        }
        if (fields.type) {
          this[fieldFileForm].action = actionType[fields.type]
        }
        if (/img/i.test(fields.type)) {
          this[fieldFileForm].style.position = 'relative'
          this[fieldFileForm].style.zIndex = Z_INDEX_HIGH
          var fieldFileA = 'field-fileA' + opt.number
          this.createPanel({
            name: fieldFileA,
            tag: 'a',
            container: fieldFileForm,
            noClass: true
          })
          this[fieldFileA].className = 'a-upload'
          this[fieldFileA].innerHTML = '上传图片'
          if (fields.readonly === true) { this[fieldFileA].style.display = 'none' }
          var fieldInputImgA = 'field-inputImgA' + opt.number
          this.createPanel({
            name: fieldInputImgA,
            tag: 'a',
            container: fieldFileForm,
            noClass: true
          })
          if (this.data && this.data[fields.name]) {
            this[fieldInputImgA].href = this.data[fields.name]
          } else if (fields.value) {
            this[fieldInputImgA].href = fields.value
          }
          this[fieldInputImgA].target = '_blank'
          var fieldInputImg = 'field-inputImg' + opt.number
          this.createPanel({
            name: fieldInputImg,
            tag: 'img',
            container: fieldInputImgA,
            noClass: true
          })
          this[fieldInputImg].height = 30
          if (this.data && this.data[fields.name]) {
            this[fieldInputImg].src = this.data[fields.name]
          } else if (fields.value) {
            this[fieldInputImg].src = fields.value
          }
          this[fieldInputImg].style.position = 'absolute'
          this[fieldInputImg].style.top = 0
          this[fieldInputImg].style.left = this[fieldFileA].offsetWidth + 10 + 'px'
          this[fieldInputImg].style.transition = '0.2s'
          var fieldInputFile = 'field-img' + opt.number
          this.createPanel({
            name: fieldInputFile,
            tag: 'input',
            container: fieldFileA,
            noClass: true
          })
          this[fieldInputFile].type = 'file'
          this[fieldInputFile].name = 'imageFile'// fields.name;
          this[fieldInputFile].title = ' '
          this[fieldInputFile].accept = 'image/jpg'
          this[fieldInputImg].onmouseenter = function () {
            this.height = 150
            this.style.top = -75 + 'px'
          }
          this[fieldInputImg].onmouseleave = function () {
            this.height = 30
            this.style.top = 0
          }
          this[fieldInputFile].onchange = function () {
            if (!this.value) {
              return false
            }
            if (!/jpg|png/.test(this.value.split('.')[this.value.split('.').length - 1])) {
              alert('格式不正确,请上传jpg,png格式图片')
              return false
            }
            var that = this
            $($(this).parents('form')[0]).ajaxSubmit({
              success: function (response) {
                if (response.isSuccess) {
                  $($(that).parents('div')[0]).find('input')[0].value = $($(that).parents('form')[0]).find('a')[1].href = $($(that).parents('form')[0]).find('img')[0].src = (response.data && response.data.length) ? response.data[0].url : (response.resourceUrl || '')
                  $(that.parentNode.parentNode.parentNode.querySelector('input')).blur()
                } else {
                  layer.alert(response.errorMessage)
                }
              },
              error: function () {
                alert('提交失败')
// thisValue.value='';
              }
            })
          }
        } else if (/xml|pem/i.test(fields.type)) {
          let fieldFileA = 'field-fileA' + opt.number
          this.createPanel({
            name: fieldFileA,
            tag: 'a',
            container: fieldFileForm,
            noClass: true
          })
          this[fieldFileA].className = 'a-upload'
          this[fieldFileA].innerHTML = '上传' + fields.type
          this[fieldFileA].style.marginRight = 10 + 'px'
          if (fields.readonly === true) { this[fieldFileA].style.display = 'none' }
          let fieldInputFile = 'field' + fields.type + opt.number
          this.createPanel({
            name: fieldInputFile,
            tag: 'input',
            container: fieldFileA,
            noClass: true
          })
          this[fieldInputFile].type = 'file'
          this[fieldInputFile].name = fields.type + 'File'// fields.name;
          this[fieldInputFile].title = ' '
          this[fieldInputFile].accept = 'text/' + fields.type + '， application/' + fields.type
          var fieldInputXmlA = 'field-input' + fields.type + 'A' + opt.number
          this.createPanel({
            name: fieldInputXmlA,
            tag: 'a',
            container: fieldFileForm,
            noClass: true
          })
          this[fieldInputXmlA].oldHtml = ' 查看' + fields.type
          this[fieldInputXmlA].style.lineHeight = 20 + 'px'
          if (this.data && this.data[fields.name]) {
            this[fieldInputXmlA].href = this.data[fields.name]
            this[fieldInputXmlA].className = 'fa fa-eye a-upload font-success'
            this[fieldInputXmlA].innerHTML = this[fieldInputXmlA].oldHtml
          } else if (fields.value) {
            this[fieldInputXmlA].href = fields.value
          }
          this[fieldInputXmlA].target = '_blank'
          this[fieldInputFile].onchange = function () {
            if (!this.value) {
              return false
            }
            if (!/xml|pem/.test(this.value.split('.')[this.value.split('.').length - 1])) {
              alert('格式不正确,请上传' + fields.type + '格式文件')
              return false
            }
            var that = this
            $($(that).parents('form')[0]).find('a')[1].innerHTML = ' 正在上传' + fields.type
            $($(that).parents('form')[0]).find('a')[1].removeAttribute('href')
            $($(this).parents('form')[0]).ajaxSubmit({
              success: function (response) {
                if (response.isSuccess) {
                  $($(that).parents('div')[0]).find('input')[0].value = $($(that).parents('form')[0]).find('a')[1].href = response.data.url
                  $($(that).parents('form')[0]).find('a')[1].className = 'fa fa-eye a-upload font-success'
// $($(that).parents('form')[0]).find('a')[1].style.marginLeft=10+'px';
                  $($(that).parents('form')[0]).find('a')[1].innerHTML = $($(that).parents('form')[0]).find('a')[1].oldHtml
                  $(that.parentNode.parentNode.parentNode.querySelector('input')).blur()
                } else {
                  layer.alert(response.errorMessage)
                }
              },
              error: function () {
                alert('提交失败')
// thisValue.value='';
              }
            })
          }
        } else if (/video/i.test(fields.type)) {
          this[fieldFileForm].style.position = 'relative'
          this[fieldFileForm].style.zIndex = Z_INDEX_HIGH
          let fieldFileA = 'field-fileA' + opt.number
          this.createPanel({
            name: fieldFileA,
            tag: 'a',
            container: fieldFileForm,
            noClass: true
          })
          this[fieldFileA].className = 'a-upload'
          this[fieldFileA].innerHTML = '上传视频'
          if (fields.readonly === true) { this[fieldFileA].style.display = 'none' }
          let fieldInputImgA = 'field-inputImgA' + opt.number
          this.createPanel({
            name: fieldInputImgA,
            tag: 'a',
            container: fieldFileForm,
            noClass: true
          })
          if (this.data && this.data[fields.name]) {
            this[fieldInputImgA].href = this.data[fields.name]
            this[fieldInputImgA].innerHTML = '点此查看已上传视频'
          } else if (fields.value) {
            this[fieldInputImgA].href = fields.value
            this[fieldInputImgA].innerHTML = '点此查看已上传视频'
          }
          this[fieldInputImgA].target = '_blank'
          this[fieldInputImgA].setAttribute('name', 'video')
          let fieldInputImg = 'field-inputImg' + opt.number
          let fieldInputFile = 'field-img' + opt.number
          this.createPanel({
            name: fieldInputFile,
            tag: 'input',
            container: fieldFileA,
            noClass: true
          })
          this[fieldInputFile].type = 'file'
          this[fieldInputFile].name = 'imageVideo'// fields.name;
          this[fieldInputFile].title = ''
          this[fieldInputFile].accept = 'image/jpg'
          this[fieldInputFile].onchange = function () {
            $(this).parent().next().text('上传中...')
            // this.parentNode.innerHTML = this.parentNode.innerHTML.replace('视频','中...')
            if (!this.value) {
              return false
            }
            if (!/mp4/.test(this.value.split('.')[this.value.split('.').length - 1])) {
              alert('格式不正确,请上传mp4格式视频')
              return false
            }
            var that = this
            $($(this).parents('form')[0]).ajaxSubmit({
              success: function (response) {
                if (response.isSuccess) {
                  $(that).parent().next().text($(that).val().split('\\')[$(that).val().split('\\').length - 1] + ',点此查看')
                  $(that).parents('div').eq(0).find('input').eq(0).val(response.resourceUrl)
                  $(that).parents('form').eq(0).find('a[name="video"]').attr('href', response.resourceUrl)
                  $(that.parentNode.parentNode.parentNode.querySelector('input')).blur()
                } else {
                  layer.alert(response.errorMessage)
                }
              },
              error: function () {
                alert('提交失败')
              }
            })
          }
        }
      } else if (/richTextEditor/i.test(fields.type)) {
        let fieldInputText = 'field-inputText' + opt.number
        this.createPanel({
          name: fieldInputText,
          tag: 'div',
          container: fieldDiv,
          noClass: true,
          id: fieldInputText
        })
        let fieldInputTextHidden = 'field-inputText-hidden' + opt.number
        this.createPanel({
          name: fieldInputTextHidden,
          tag: 'input',
          container: fieldDiv,
          noClass: true,
          id: fieldInputTextHidden
        })
        this[fieldInputTextHidden] = document.getElementById(fieldInputTextHidden)
        this[fieldInputTextHidden].style.display = 'none'
        this[fieldInputTextHidden].name = fields.name

        this['field-' + opt.number] = this[fieldInputText]
        this[fieldInputText].style.width = (fields.area && fields.area.width) ? fields.area.width : '100%'
        this[fieldInputText].style.height = (fields.area && fields.area.height) ? fields.area.height : '350px'
        this[fieldInputText].style.border = 'none'
        this[fieldInputText].setAttribute('name', fields.name)
        this[fieldInputText].setAttribute('type', 'richTextEditor')
        this[fieldInputText].value = ''
        editor = new WangEditor(document.getElementById(fieldInputText))
        editor.customConfig.uploadImgShowBase64 = true   // 使用 base64 保存图片
        editor.customConfig.onchange = function (html) {
            // html 即变化之后的内容
            $(document.getElementById(fieldInputTextHidden)).val(html)
        }
        editor.create()
        if (fields.readonly) {
          // 禁用编辑功能
          editor.$textElem.attr('contenteditable', false)
        }
        if (this.data && Object.keys(this.data).length && fields.name && this.data[fields.name]) {
          editor.txt.html(this.data[fields.name])
          this[fieldInputText].value = this.data[fields.name]
          this[fieldInputTextHidden].value = this.data[fields.name]
        }
      } else if (/multigraph/i.test(fields.type)) {
        let fieldInputText = 'field-inputText' + opt.number
        this.createPanel({
          name: fieldInputText,
          tag: 'input',
          container: fieldDiv,
          noClass: true
        })
        this[fieldInputText].name = fields.name

        if (this.data && this.data[fields.name]) {
          this[fieldInputText].value = this.data[fields.name]
        } else {
          this[fieldInputText].value = fields.value ? fields.value : ''
        }
        this['field-' + opt.number] = this[fieldInputText]
        this[fieldInputText].style.display = 'none'
        let fieldFileIframe = 'field-fileIframe' + opt.number
        this.createPanel({
          name: fieldFileIframe,
          tag: 'iframe',
          container: fieldDiv,
          noClass: true
        })
        this[fieldFileIframe].style.display = 'none'
        this[fieldFileIframe].name = 'iframe'
        let fieldFileForm = 'field-fileForm' + opt.number
        this.createPanel({
          name: fieldFileForm,
          tag: 'form',
          container: fieldDiv,
          noClass: true
        })
        this[fieldFileForm].target = 'iframe'
        this[fieldFileForm].method = 'post'
        this[fieldFileForm].action = '/common/uploadImage'
        this[fieldFileForm].style.position = 'relative'
        this[fieldFileForm].style.zIndex = Z_INDEX_HIGH
        let fieldFileA = 'field-fileA' + opt.number
        this.createPanel({
          name: fieldFileA,
          tag: 'a',
          container: fieldFileForm,
          noClass: true
        })
        this[fieldFileA].className = 'a-upload'
        this[fieldFileA].innerHTML = '上传图片'
        if (fields.readonly === true) { this[fieldFileA].style.display = 'none' }
        let fieldInputImgA = 'field-inputImgA' + opt.number
        this.createPanel({
          name: fieldInputImgA,
          tag: 'a',
          container: fieldFileForm,
          noClass: true
        })
        if (this.data && this.data[fields.name]) {
          this[fieldInputImgA].href = this.data[fields.name]
        } else if (fields.value) {
          this[fieldInputImgA].href = fields.value
        }
        this[fieldInputImgA].target = '_blank'
        let thisColumn = null
        $(this.formContainer).children().map((index, item) => {
          $(item).find('form[target="iframe"]').map((index1, item1) => {
            if (item1 === this[fieldFileForm]) {
              thisColumn = index
            }
          })
        })

        let imgArr = []
        if (this.data && this.data[fields.name] && this.data[fields.name].length) {
          imgArr = this.data[fields.name].slice(0)
        }

        this[fieldFileA].value = imgArr
        $(this.formContainer).children().eq(thisColumn).after(`<ul id="fieldInputImgList" style="overflow:hidden;width:${fields.imgListWidth ? fields.imgListWidth : '80%'};margin:0 auto 10px;" name="fieldInputImgList"></ul>`)
        let liStrStart = `<li style="float:left;position:relative;width:160px;height:160px;padding:5px;margin:0 5px;border:1px solid #ccc;border-radius:4px;">
            <span style="display:table-cell;width:150px;height:150px;vertical-align:middle;text-align:center;">`
        let liStrEnd = `</span></li>`
        // let deleteThis = (ev) => {
        //   imgArr.splice($('ul#fieldInputImgList a.fa-trash').index(), 1)
        //   $(ev.target).parent().parent().remove()
        // }
        $(document).on('click', 'ul#fieldInputImgList a.fa-trash', (ev) =>{
          imgArr.splice($('ul#fieldInputImgList a.fa-trash').index(), 1)
          $(ev.target).parent().parent().remove()
        })

        if (this.data && Object.keys(this.data).length && imgArr && imgArr.length) {
          imgArr.map((item, index) => {
            let str = liStrStart + `<img style="max-width:100%;" src="${item || ''}" />`
            if (fields.readonly !== true) {
              str += `<a style="position:absolute;top:5px;right:5px;font-size:16px;color:red;cursor:pointer" class="fa fa-trash"></a>`
            }
            str += liStrEnd
            $(this.formContainer).find('ul#fieldInputImgList').eq(0).append(str)
          })
        }

        let fieldInputFile = 'field-img' + opt.number
        this.createPanel({
          name: fieldInputFile,
          tag: 'input',
          container: fieldFileA,
          noClass: true
        })
        this[fieldInputFile].type = 'file'
        this[fieldInputFile].name = 'imageFile'// fields.name;
        this[fieldInputFile].title = ' '
        this[fieldInputFile].accept = 'image/jpg'
        this[fieldInputFile].setAttribute('multiple', 'multiple')
        this[fieldInputFile].onchange = function () {
          if (!this.value) {
            return false
          }
          if (!/jpg|png/.test(this.value.split('.')[this.value.split('.').length - 1])) {
            alert('格式不正确,请上传jpg,png格式图片')
            return false
          }
          var that = this
          $($(this).parents('form')[0]).ajaxSubmit({
            success: function (response) {
              if (response.isSuccess) {
                if (response.data && response.data.length) {
                  response.data.map((item, index) => {
                    imgArr = imgArr.concat(item.url)
                    $($(that).parents('div')[0]).find('input')[0].value = imgArr
                    let str = liStrStart + `<img style="max-width:100%;" src="${item.url ? item.url : ''}" />
                          <a style="position:absolute;top:5px;right:5px;font-size:16px;color:red;cursor:pointer" class="fa fa-trash"></a>` + liStrEnd
                    $('ul#fieldInputImgList').append(str)
                  })
                }

                $(that.parentNode.parentNode.parentNode.querySelector('input')).blur()
              } else {
                layer.alert(response.errorMessage)
              }
            },
            error: function () {
              alert('提交失败')
            }
          })
        }
      } else if (/map/i.test(fields.type)) {
        let fieldMap = 'field-map' + opt.number
        let thisId = fields.name ? fields.name : 'fields' + 'Map'
        this.createPanel({
          name: fieldMap,
          tag: 'div',
          container: fieldDiv,
          noClass: true,
          id: thisId
        })
        this['field-' + opt.number] = this[fieldMap]
        this[fieldMap].style.width = (fields.area && fields.area.width) ? fields.area.width : '100%'
        this[fieldMap].style.height = (fields.area && fields.area.height) ? fields.area.height : '350px'
/* eslint-disable no-undef */
        let map = new BMap.Map(thisId)    // 创建Map实例
        let longitude = (this.data && this.data[fields.location.longitude]) ? this.data[fields.location.longitude] : ''
        let latitude = (this.data && this.data[fields.location.latitude]) ? this.data[fields.location.latitude] : ''
        map.centerAndZoom(new BMap.Point(longitude || 116.404, latitude || 39.915), 17)  // 初始化地图,设置中心点坐标和地图级别
        map.enableScrollWheelZoom(true)     // 开启鼠标滚轮缩放
        // console.log(BMap);
        this.MAP = map
        this.BMAP = BMap
        if (fields.readonly !== true && fields.myEvent) {
          let _this = this
          map.addEventListener('click', function (e) {
            fields.myEvent({
              owner: _this.owner,
              panel: _this,
              component: _this.component,
              fields: fields,
              data: _this.data,
              e: e
            })
          })
        }
      } else if (/download/i.test(fields.type)) {
        let fieldA = 'field-a' + opt.number
        this.createPanel({
          name: fieldA,
          tag: 'a',
          container: fieldDiv,
          noClass: true
        })
        this['field-' + opt.number] = this[fieldA]
        this[fieldA].innerHTML = fields.fileInnerHTML ? fields.fileInnerHTML : '点击下载'
        this[fieldA].target = '_blank'
        this[fieldA].href = (this.data&&this.data[fields.name]) ? this.data[fields.name] : ''
      } else {
        if (!fields.type) {
          fields.type = 'text'
        }
        let fieldInputText = 'field-inputText' + opt.number
        this.createPanel({
          name: fieldInputText,
          tag: 'input',
          container: fieldDiv,
          noClass: true
        })
        this['field-' + opt.number] = this[fieldInputText]
        var tempType = fields.type ? fields.type : 'text'
        this[fieldInputText].setAttribute('type', tempType)
        if (/date|time/.test(fields.type)) {
          this['field-' + opt.number].type = 'text'
          this['field-' + opt.number].dropDown = true
          _this.layStartTime = {
            max: (function () {
              if (_this.data && _this.data['endDate']) { return _this.data['endDate'] }
            })(),
            event: 'focus',
            istime: /time/.test(fields.type),
            format: /time/.test(fields.type) ? 'YYYY-MM-DD hh:mm:ss' : (fields.format || 'YYYY-MM-DD'),
            choose: function (datas) {
              _this.layEndTime.min = datas // 开始日选好后，重置结束日的最小日期
              _this.layEndTime.start = datas // 将结束日的初始值设定为开始日
            }
          }
          _this.layEndTime = {
            min: (function () {
              if (_this.data && _this.data['startDate']) { return _this.data['startDate'] }
            })(),
            event: 'focus',
            istime: /time/.test(fields.type),
            format: /time/.test(fields.type) ? 'YYYY-MM-DD hh:mm:ss' : (fields.format || 'YYYY-MM-DD'),
            choose: function (datas) {
              _this.layStartTime.max = datas // 结束日选好后，重置开始日的最大日期
            }
          }
          var constra = {
            startDate: function () {
              return _this.layStartTime
            },
            endDate: function () {
              return _this.layEndTime
            }
          }
          var self = this
/* eslint-disable no-undef */
          layui.use('laydate', function () {
            var laydate = layui.laydate

            self['field-' + opt.number].onclick = (ev) => {
              laydate((function () {
                if (/start/.test(ev.target.name)) {
                  constra.startDate().elem = ev.target
                  return constra.startDate()
                } else if (/end/.test(ev.target.name)) {
                  constra.endDate().elem = ev.target
                  return constra.endDate()
                } else {
                  return {
                    elem: ev.target,
                    event: 'focus',
                    istime: /time/.test(fields.type),
                    format: /time/.test(fields.type) ? 'YYYY-MM-DD hh:mm:ss' : (fields.format || 'YYYY-MM-DD'),
                  }
                }
              })())
            }
          })
        } else if (fields.type === 'icon') {
          var str = '["fa fa-500px","fa fa-amazon","fa fa-balance-scale","fa fa-battery-0","fa fa-battery-1","fa fa-battery-2","fa fa-battery-3","fa fa-battery-4","fa fa-battery-empty","fa fa-battery-full","fa fa-battery-half","fa fa-battery-quarter","fa fa-battery-three-quarters","fa fa-black-tie","fa fa-calendar-check-o","fa fa-calendar-minus-o","fa fa-calendar-plus-o","fa fa-calendar-times-o","fa fa-cc-diners-club","fa fa-cc-jcb","fa fa-chrome","fa fa-clone","fa fa-commenting","fa fa-commenting-o","fa fa-contao","fa fa-creative-commons","fa fa-expeditedssl","fa fa-firefox","fa fa-fonticons","fa fa-genderless","fa fa-get-pocket","fa fa-gg","fa fa-gg-circle","fa fa-hand-grab-o","fa fa-hand-lizard-o","fa fa-hand-paper-o","fa fa-hand-peace-o","fa fa-hand-pointer-o","fa fa-hand-rock-o","fa fa-hand-scissors-o","fa fa-hand-spock-o","fa fa-hand-stop-o","fa fa-hourglass","fa fa-hourglass-1","fa fa-hourglass-2","fa fa-hourglass-3","fa fa-hourglass-end","fa fa-hourglass-half","fa fa-hourglass-o","fa fa-hourglass-start","fa fa-houzz","fa fa-i-cursor","fa fa-industry","fa fa-internet-explorer","fa fa-map","fa fa-map-o","fa fa-map-pin","fa fa-map-signs","fa fa-mouse-pointer","fa fa-object-group","fa fa-object-ungroup","fa fa-odnoklassniki","fa fa-odnoklassniki-square","fa fa-opencart","fa fa-opera","fa fa-optin-monster","fa fa-registered","fa fa-safari","fa fa-sticky-note","fa fa-sticky-note-o","fa fa-television","fa fa-trademark","fa fa-tripadvisor","fa fa-tv","fa fa-vimeo","fa fa-wikipedia-w","fa fa-y-combinator","fa fa-yc","fa fa-adjust","fa fa-anchor","fa fa-archive","fa fa-area-chart","fa fa-arrows","fa fa-arrows-h","fa fa-arrows-v","fa fa-asterisk","fa fa-at","fa fa-automobile","fa fa-balance-scale","fa fa-ban","fa fa-bank","fa fa-bar-chart","fa fa-bar-chart-o","fa fa-barcode","fa fa-bars","fa fa-battery-0","fa fa-battery-1","fa fa-battery-2","fa fa-battery-3","fa fa-battery-4","fa fa-battery-empty","fa fa-battery-full","fa fa-battery-half","fa fa-battery-quarter","fa fa-battery-three-quarters","fa fa-bed","fa fa-beer","fa fa-bell","fa fa-bell-o","fa fa-bell-slash","fa fa-bell-slash-o","fa fa-bicycle","fa fa-binoculars","fa fa-birthday-cake","fa fa-bolt","fa fa-bomb","fa fa-book","fa fa-bookmark","fa fa-bookmark-o","fa fa-briefcase","fa fa-bug","fa fa-building","fa fa-building-o","fa fa-bullhorn","fa fa-bullseye","fa fa-bus","fa fa-cab","fa fa-calculator","fa fa-calendar","fa fa-calendar-check-o","fa fa-calendar-minus-o","fa fa-calendar-o","fa fa-calendar-plus-o","fa fa-calendar-times-o","fa fa-camera","fa fa-camera-retro","fa fa-car","fa fa-caret-square-o-down","fa fa-caret-square-o-left","fa fa-caret-square-o-right","fa fa-caret-square-o-up","fa fa-cart-arrow-down","fa fa-cart-plus","fa fa-cc","fa fa-certificate","fa fa-check","fa fa-check-circle","fa fa-check-circle-o","fa fa-check-square","fa fa-check-square-o","fa fa-child","fa fa-circle","fa fa-circle-o","fa fa-circle-o-notch","fa fa-circle-thin","fa fa-clock-o","fa fa-clone","fa fa-close","fa fa-cloud","fa fa-cloud-download","fa fa-cloud-upload","fa fa-code","fa fa-code-fork","fa fa-coffee","fa fa-cog","fa fa-cogs","fa fa-comment","fa fa-comment-o","fa fa-commenting","fa fa-commenting-o","fa fa-comments","fa fa-comments-o","fa fa-compass","fa fa-copyright","fa fa-creative-commons","fa fa-credit-card","fa fa-crop","fa fa-crosshairs","fa fa-cube","fa fa-cubes","fa fa-cutlery","fa fa-dashboard","fa fa-database","fa fa-desktop","fa fa-diamond","fa fa-dot-circle-o","fa fa-download","fa fa-edit","fa fa-ellipsis-h","fa fa-ellipsis-v","fa fa-envelope","fa fa-envelope-o","fa fa-envelope-square","fa fa-eraser","fa fa-exchange","fa fa-exclamation","fa fa-exclamation-circle","fa fa-exclamation-triangle","fa fa-external-link","fa fa-external-link-square","fa fa-eye","fa fa-eye-slash","fa fa-eyedropper","fa fa-fax","fa fa-feed","fa fa-female","fa fa-fighter-jet","fa fa-file-archive-o","fa fa-file-audio-o","fa fa-file-code-o","fa fa-file-excel-o","fa fa-file-image-o","fa fa-file-movie-o","fa fa-file-pdf-o","fa fa-file-photo-o","fa fa-file-picture-o","fa fa-file-powerpoint-o","fa fa-file-sound-o","fa fa-file-video-o","fa fa-file-word-o","fa fa-file-zip-o","fa fa-film","fa fa-filter","fa fa-fire","fa fa-fire-extinguisher","fa fa-flag","fa fa-flag-checkered","fa fa-flag-o","fa fa-flash","fa fa-flask","fa fa-folder","fa fa-folder-o","fa fa-folder-open","fa fa-folder-open-o","fa fa-frown-o","fa fa-futbol-o","fa fa-gamepad","fa fa-gavel","fa fa-gear","fa fa-gears","fa fa-gift","fa fa-glass","fa fa-globe","fa fa-graduation-cap","fa fa-group","fa fa-hand-grab-o","fa fa-hand-lizard-o","fa fa-hand-paper-o","fa fa-hand-peace-o","fa fa-hand-pointer-o","fa fa-hand-rock-o","fa fa-hand-scissors-o","fa fa-hand-spock-o","fa fa-hand-stop-o","fa fa-hdd-o","fa fa-headphones","fa fa-heart","fa fa-heart-o","fa fa-heartbeat","fa fa-history","fa fa-home","fa fa-hotel","fa fa-hourglass","fa fa-hourglass-1","fa fa-hourglass-2","fa fa-hourglass-3","fa fa-hourglass-end","fa fa-hourglass-half","fa fa-hourglass-o","fa fa-hourglass-start","fa fa-i-cursor","fa fa-image","fa fa-inbox","fa fa-industry","fa fa-info","fa fa-info-circle","fa fa-institution","fa fa-key","fa fa-keyboard-o","fa fa-language","fa fa-laptop","fa fa-leaf","fa fa-legal","fa fa-lemon-o","fa fa-level-down","fa fa-level-up","fa fa-life-bouy","fa fa-life-buoy","fa fa-life-ring","fa fa-life-saver","fa fa-lightbulb-o","fa fa-line-chart","fa fa-location-arrow","fa fa-lock","fa fa-magic","fa fa-magnet","fa fa-mail-forward","fa fa-mail-reply","fa fa-mail-reply-all","fa fa-male","fa fa-map","fa fa-map-marker","fa fa-map-o","fa fa-map-pin","fa fa-map-signs","fa fa-meh-o","fa fa-microphone","fa fa-microphone-slash","fa fa-minus","fa fa-minus-circle","fa fa-minus-square","fa fa-minus-square-o","fa fa-mobile","fa fa-mobile-phone","fa fa-money","fa fa-moon-o","fa fa-mortar-board","fa fa-motorcycle","fa fa-mouse-pointer","fa fa-music","fa fa-navicon","fa fa-newspaper-o","fa fa-object-group","fa fa-object-ungroup","fa fa-paint-brush","fa fa-paper-plane","fa fa-paper-plane-o","fa fa-paw","fa fa-pencil","fa fa-pencil-square","fa fa-pencil-square-o","fa fa-phone","fa fa-phone-square","fa fa-photo","fa fa-picture-o","fa fa-pie-chart","fa fa-plane","fa fa-plug","fa fa-plus","fa fa-plus-circle","fa fa-plus-square","fa fa-plus-square-o","fa fa-power-off","fa fa-print","fa fa-puzzle-piece","fa fa-qrcode","fa fa-question","fa fa-question-circle","fa fa-quote-left","fa fa-quote-right","fa fa-random","fa fa-recycle","fa fa-refresh","fa fa-registered","fa fa-remove","fa fa-reorder","fa fa-reply","fa fa-reply-all","fa fa-retweet","fa fa-road","fa fa-rocket","fa fa-rss","fa fa-rss-square","fa fa-search","fa fa-search-minus","fa fa-search-plus","fa fa-send","fa fa-send-o","fa fa-server","fa fa-share","fa fa-share-alt","fa fa-share-alt-square","fa fa-share-square","fa fa-share-square-o","fa fa-shield","fa fa-ship","fa fa-shopping-cart","fa fa-sign-in","fa fa-sign-out","fa fa-signal","fa fa-sitemap","fa fa-sliders","fa fa-smile-o","fa fa-soccer-ball-o","fa fa-sort","fa fa-sort-alpha-asc","fa fa-sort-alpha-desc","fa fa-sort-amount-asc","fa fa-sort-amount-desc","fa fa-sort-asc","fa fa-sort-desc","fa fa-sort-down","fa fa-sort-numeric-asc","fa fa-sort-numeric-desc","fa fa-sort-up","fa fa-space-shuttle","fa fa-spinner","fa fa-spoon","fa fa-square","fa fa-square-o","fa fa-star","fa fa-star-half","fa fa-star-half-empty","fa fa-star-half-full","fa fa-star-half-o","fa fa-star-o","fa fa-sticky-note","fa fa-sticky-note-o","fa fa-street-view","fa fa-suitcase","fa fa-sun-o","fa fa-support","fa fa-tablet","fa fa-tachometer","fa fa-tag","fa fa-tags","fa fa-tasks","fa fa-taxi","fa fa-television","fa fa-terminal","fa fa-thumb-tack","fa fa-thumbs-down","fa fa-thumbs-o-down","fa fa-thumbs-o-up","fa fa-thumbs-up","fa fa-ticket","fa fa-times","fa fa-times-circle","fa fa-times-circle-o","fa fa-tint","fa fa-toggle-down","fa fa-toggle-left","fa fa-toggle-off","fa fa-toggle-on","fa fa-toggle-right","fa fa-toggle-up","fa fa-trademark","fa fa-trash","fa fa-trash-o","fa fa-tree","fa fa-trophy","fa fa-truck","fa fa-tty","fa fa-tv","fa fa-umbrella","fa fa-university","fa fa-unlock","fa fa-unlock-alt","fa fa-unsorted","fa fa-upload","fa fa-user","fa fa-user-plus","fa fa-user-secret","fa fa-user-times","fa fa-users","fa fa-video-camera","fa fa-volume-down","fa fa-volume-off","fa fa-volume-up","fa fa-warning","fa fa-wheelchair","fa fa-wifi","fa fa-wrench","fa fa-hand-grab-o","fa fa-hand-lizard-o","fa fa-hand-o-down","fa fa-hand-o-left","fa fa-hand-o-right","fa fa-hand-o-up","fa fa-hand-paper-o","fa fa-hand-peace-o","fa fa-hand-pointer-o","fa fa-hand-rock-o","fa fa-hand-scissors-o","fa fa-hand-spock-o","fa fa-hand-stop-o","fa fa-thumbs-down","fa fa-thumbs-o-down","fa fa-thumbs-o-up","fa fa-thumbs-up","fa fa-ambulance","fa fa-automobile","fa fa-bicycle","fa fa-bus","fa fa-cab","fa fa-car","fa fa-fighter-jet","fa fa-motorcycle","fa fa-plane","fa fa-rocket","fa fa-ship","fa fa-space-shuttle","fa fa-subway","fa fa-taxi","fa fa-train","fa fa-truck","fa fa-wheelchair","fa fa-genderless","fa fa-intersex","fa fa-mars","fa fa-mars-double","fa fa-mars-stroke","fa fa-mars-stroke-h","fa fa-mars-stroke-v","fa fa-mercury","fa fa-neuter","fa fa-transgender","fa fa-transgender-alt","fa fa-venus","fa fa-venus-double","fa fa-venus-mars","fa fa-file","fa fa-file-archive-o","fa fa-file-audio-o","fa fa-file-code-o","fa fa-file-excel-o","fa fa-file-image-o","fa fa-file-movie-o","fa fa-file-o","fa fa-file-pdf-o","fa fa-file-photo-o","fa fa-file-picture-o","fa fa-file-powerpoint-o","fa fa-file-sound-o","fa fa-file-text","fa fa-file-text-o","fa fa-file-video-o","fa fa-file-word-o","fa fa-file-zip-o","fa fa-info-circle fa-lg fa-li","fa fa-circle-o-notch","fa fa-cog","fa fa-gear","fa fa-refresh","fa fa-spinner","fa fa-check-square","fa fa-check-square-o","fa fa-circle","fa fa-circle-o","fa fa-dot-circle-o","fa fa-minus-square","fa fa-minus-square-o","fa fa-plus-square","fa fa-plus-square-o","fa fa-square","fa fa-square-o","fa fa-cc-amex","fa fa-cc-diners-club","fa fa-cc-discover","fa fa-cc-jcb","fa fa-cc-mastercard","fa fa-cc-paypal","fa fa-cc-stripe","fa fa-cc-visa","fa fa-credit-card","fa fa-google-wallet","fa fa-paypal","fa fa-area-chart","fa fa-bar-chart","fa fa-bar-chart-o","fa fa-line-chart","fa fa-pie-chart","fa fa-bitcoin","fa fa-btc","fa fa-cny","fa fa-dollar","fa fa-eur","fa fa-euro","fa fa-gbp","fa fa-gg","fa fa-gg-circle","fa fa-ils","fa fa-inr","fa fa-jpy","fa fa-krw","fa fa-money","fa fa-rmb","fa fa-rouble","fa fa-rub","fa fa-ruble","fa fa-rupee","fa fa-shekel","fa fa-sheqel","fa fa-try","fa fa-turkish-lira","fa fa-usd","fa fa-won","fa fa-yen","fa fa-align-center","fa fa-align-justify","fa fa-align-left","fa fa-align-right","fa fa-bold","fa fa-chain","fa fa-chain-broken","fa fa-clipboard","fa fa-columns","fa fa-copy","fa fa-cut","fa fa-dedent","fa fa-eraser","fa fa-file","fa fa-file-o","fa fa-file-text","fa fa-file-text-o","fa fa-files-o","fa fa-floppy-o","fa fa-font","fa fa-header","fa fa-indent","fa fa-italic","fa fa-link","fa fa-list","fa fa-list-alt","fa fa-list-ol","fa fa-list-ul","fa fa-outdent","fa fa-paperclip","fa fa-paragraph","fa fa-paste","fa fa-repeat","fa fa-rotate-left","fa fa-rotate-right","fa fa-save","fa fa-scissors","fa fa-strikethrough","fa fa-subscript","fa fa-superscript","fa fa-table","fa fa-text-height","fa fa-text-width","fa fa-th","fa fa-th-large","fa fa-th-list","fa fa-underline","fa fa-undo","fa fa-unlink","fa fa-angle-double-down","fa fa-angle-double-left","fa fa-angle-double-right","fa fa-angle-double-up","fa fa-angle-down","fa fa-angle-left","fa fa-angle-right","fa fa-angle-up","fa fa-arrow-circle-down","fa fa-arrow-circle-left","fa fa-arrow-circle-o-down","fa fa-arrow-circle-o-left","fa fa-arrow-circle-o-right","fa fa-arrow-circle-o-up","fa fa-arrow-circle-right","fa fa-arrow-circle-up","fa fa-arrow-down","fa fa-arrow-left","fa fa-arrow-right","fa fa-arrow-up","fa fa-arrows","fa fa-arrows-alt","fa fa-arrows-h","fa fa-arrows-v","fa fa-caret-down","fa fa-caret-left","fa fa-caret-right","fa fa-caret-square-o-down","fa fa-caret-square-o-left","fa fa-caret-square-o-right","fa fa-caret-square-o-up","fa fa-caret-up","fa fa-chevron-circle-down","fa fa-chevron-circle-left","fa fa-chevron-circle-right","fa fa-chevron-circle-up","fa fa-chevron-down","fa fa-chevron-left","fa fa-chevron-right","fa fa-chevron-up","fa fa-exchange","fa fa-hand-o-down","fa fa-hand-o-left","fa fa-hand-o-right","fa fa-hand-o-up","fa fa-long-arrow-down","fa fa-long-arrow-left","fa fa-long-arrow-right","fa fa-long-arrow-up","fa fa-toggle-down","fa fa-toggle-left","fa fa-toggle-right","fa fa-toggle-up","fa fa-arrows-alt","fa fa-backward","fa fa-compress","fa fa-eject","fa fa-expand","fa fa-fast-backward","fa fa-fast-forward","fa fa-forward","fa fa-pause","fa fa-play","fa fa-play-circle","fa fa-play-circle-o","fa fa-random","fa fa-step-backward","fa fa-step-forward","fa fa-stop","fa fa-youtube-play","fa fa-500px","fa fa-adn","fa fa-amazon","fa fa-android","fa fa-angellist","fa fa-apple","fa fa-behance","fa fa-behance-square","fa fa-bitbucket","fa fa-bitbucket-square","fa fa-bitcoin","fa fa-black-tie","fa fa-btc","fa fa-buysellads","fa fa-cc-amex","fa fa-cc-diners-club","fa fa-cc-discover","fa fa-cc-jcb","fa fa-cc-mastercard","fa fa-cc-paypal","fa fa-cc-stripe","fa fa-cc-visa","fa fa-chrome","fa fa-codepen","fa fa-connectdevelop","fa fa-contao","fa fa-css3","fa fa-dashcube","fa fa-delicious","fa fa-deviantart","fa fa-digg","fa fa-dribbble","fa fa-dropbox","fa fa-drupal","fa fa-empire","fa fa-expeditedssl","fa fa-facebook","fa fa-facebook-f","fa fa-facebook-official","fa fa-facebook-square","fa fa-firefox","fa fa-flickr","fa fa-fonticons","fa fa-forumbee","fa fa-foursquare","fa fa-ge","fa fa-get-pocket","fa fa-gg","fa fa-gg-circle","fa fa-git","fa fa-git-square","fa fa-github","fa fa-github-alt","fa fa-github-square","fa fa-gittip","fa fa-google","fa fa-google-plus","fa fa-google-plus-square","fa fa-google-wallet","fa fa-gratipay","fa fa-hacker-news","fa fa-houzz","fa fa-html5","fa fa-instagram","fa fa-internet-explorer","fa fa-ioxhost","fa fa-joomla","fa fa-jsfiddle","fa fa-lastfm","fa fa-lastfm-square","fa fa-leanpub","fa fa-linkedin","fa fa-linkedin-square","fa fa-linux","fa fa-maxcdn","fa fa-meanpath","fa fa-medium","fa fa-odnoklassniki","fa fa-odnoklassniki-square","fa fa-opencart","fa fa-openid","fa fa-opera","fa fa-optin-monster","fa fa-pagelines","fa fa-paypal","fa fa-pied-piper","fa fa-pied-piper-alt","fa fa-pinterest","fa fa-pinterest-p","fa fa-pinterest-square","fa fa-qq","fa fa-ra","fa fa-rebel","fa fa-reddit","fa fa-reddit-square","fa fa-renren","fa fa-safari","fa fa-sellsy","fa fa-share-alt","fa fa-share-alt-square","fa fa-shirtsinbulk","fa fa-simplybuilt","fa fa-skyatlas","fa fa-skype","fa fa-slack","fa fa-slideshare","fa fa-soundcloud","fa fa-spotify","fa fa-stack-exchange","fa fa-stack-overflow","fa fa-steam","fa fa-steam-square","fa fa-stumbleupon","fa fa-stumbleupon-circle","fa fa-tencent-weibo","fa fa-trello","fa fa-tripadvisor","fa fa-tumblr","fa fa-tumblr-square","fa fa-twitch","fa fa-twitter","fa fa-twitter-square","fa fa-viacoin","fa fa-vimeo","fa fa-vimeo-square","fa fa-vine","fa fa-vk","fa fa-wechat","fa fa-weibo","fa fa-weixin","fa fa-whatsapp","fa fa-wikipedia-w","fa fa-windows","fa fa-wordpress","fa fa-xing","fa fa-xing-square","fa fa-y-combinator","fa fa-y-combinator-square","fa fa-yahoo","fa fa-yc","fa fa-yc-square","fa fa-yelp","fa fa-youtube","fa fa-youtube-play","fa fa-youtube-square","fa fa-ambulance","fa fa-h-square","fa fa-heart","fa fa-heart-o","fa fa-heartbeat","fa fa-hospital-o","fa fa-medkit","fa fa-plus-square","fa fa-stethoscope","fa fa-user-md","fa fa-wheelchair"]'
          var arr = JSON.parse(str)
          var content = ''
          for (var i = 0; i < arr.length; i++) {
            content += '<i class="' + arr[i] + '" title="' + arr[i].split('fa-')[1] + '"></i>'
          }
          this['field-' + opt.number].onclick = function () {
            var obj = this
            layer.open({
              title: '图标选择',
              offset: 76 + 'px', // top
              area: ['800px', '460px'], // width
              content: content,
              id: 'iconsWindow'
            })
            if (document.querySelector('#iconsWindow').querySelector('i[class="' + obj.value + '"]')) { getMethods.addClass(document.querySelector('#iconsWindow').querySelector('i[class="' + obj.value + '"]'), 'current') }
            document.querySelector('#iconsWindow').onmouseover = function (ev) {
              if (/^i$/i.test(ev.target.tagName)) {
                getMethods.addClass(ev.target, 'active')
              }
            }
            document.querySelector('#iconsWindow').onmouseout = function (ev) {
              if (/^i$/i.test(ev.target.tagName)) {
                getMethods.deleteClass(ev.target, 'active')
              }
            }
            document.querySelector('#iconsWindow').onclick = function (ev) {
              if (/^i$/i.test(ev.target.tagName)) {
                obj.value = ev.target.className.replace(/\s?active\s?|\s?current\s?/i, '')
                this.parentNode.querySelector('.layui-layer-btn0').click()
                this.onclick = null
                this.onmousemove = null
              }
            }
          }
        }
        this[fieldInputText].name = fields.name

        if (this.data && this.data[this[fieldInputText].name]) { this[fieldInputText].value = this.data[this[fieldInputText].name] }
        if (fields.value) { this[fieldInputText].value = fields.value }
      }
    }
    if (fields.type !== 'radio') {
      var names = {
        text: 'inputText'
      }
      var allField
      if (fields.type) {
        if (names[fields.type]) { allField = 'field-' + names[fields.type] + opt.number } else { allField = 'field-' + fields.type + opt.number }
      } else if (fields.tagName) {
        allField = 'field-' + fields.tagName + opt.number
      }
      if (!this[allField]) { allField = 'field-inputText' + opt.number }
      if (fields.readonly === true && this[allField]) {
        this[allField].setAttribute('readonly', true)
        this[allField].setAttribute('isreadonly', true)
      }
      if (fields.events) {
        this[allField].events = fields.events
        for (var events in fields.events) {
          getMethods.myAddEvent(this[allField], events, function (ev) {
            var oEvent = ev || event
            oEvent.cancelBubble = true
            this.events[oEvent.type]({
              input: this,
              parentId: $("[name='parentId']"),
              uid: $("[name='id']").val(),
              owner: _this,
              this: _this,
              component: _this.component
            })
          })
        }
      }
    }
    if (fields.allowEmpty && fields.type !== 'radio') {
      this['field-' + opt.number].setAttribute('allowEmpty', fields.allowEmpty)
    }

    if (fields.disabled === true) {
      this['field-' + opt.number].setAttribute('disabled', true)
    }
    if ((fields.validator && fields.validator.type) || fields.dropDown) {
      this.mustValidate.push(this['field-' + opt.number])
      this['field-' + opt.number].setAttribute('validator', false)
      this['field-' + opt.number].validator = fields.validator
      if (fields.placeholder) { this['field-' + opt.number].placeholder = fields.placeholder }
      this['field-' + opt.number].dropDown = fields.dropDown
      if (this['field-' + opt.number].validator && this['field-' + opt.number].validator.async) { this['field-' + opt.number].setAttribute('async', 'false') }
      this['field-' + opt.number].isOpen = true
      this['field-' + opt.number].onfocus = function () {
        layer.close(this.tips)
        this.setAttribute('validator', false)
        this.setAttribute('isReady', false)
      }
      if (this['field-' + opt.number].validator && this['field-' + opt.number].validator.async) {
        this['field-' + opt.number].onchange = function () {
          this.setAttribute('async', 'false')
        }
      }
      this['field-' + opt.number].onblur = function () {
        if (this.getAttribute('isreadonly')) {
          if (this.getAttribute('isready')) this.setAttribute('isready', true)
          if (this.getAttribute('validator')) this.setAttribute('validator', true)
          if (this.getAttribute('async')) this.setAttribute('async', true)
          return false
        }
        if (this.offsetTop === 0 && this.offsetLeft === 0) {
          if (this.parentNode.querySelector('iframe') || this.parentNode.querySelector('[name="' + this.name + 'Name' + '"]')) {
            if (this.parentNode.offsetTop === 0 && this.parentNode.offsetLeft === 0) { return false }
          } else {
            return false
          }
        }
        var that = this
        if (that.dropDown && that.dropDown === true) {
          setTimeout(function () {
            _this.validatorBlur({
              obj: that,
              validator: that.validator,
              dropDown: that.dropDown
            })
          }, 200)
        } else {
          _this.validatorBlur({
            obj: that,
            validator: that.validator,
            dropDown: that.dropDown
          })
        }
      }
      $(this['field-' + opt.number]).blur()
    }
    if (fields.dropdownList) {
      this.createPanel({
        name: fieldInputText + 'Name',
        tag: 'input',
        container: fieldDiv,
        noClass: true
      })
      this[fieldInputText + 'Name'].type = 'text'
      this[fieldInputText + 'Name'].setAttribute('readonly', true)
      this[fieldInputText + 'Name'].name = this[fieldInputText].name + 'Name'
      if (this.data && this.data[this[fieldInputText + 'Name'].name]) { this[fieldInputText + 'Name'].value = this.data[this[fieldInputText + 'Name'].name] }
      if (!this.noSubmit) { this.noSubmit = [this[fieldInputText + 'Name']] } else { this.noSubmit.push(this[fieldInputText + 'Name']) }
      this[fieldInputText + 'Name'].targetId = this['field-' + opt.number]
      if (fields.readonly !== true) {
        this[fieldInputText + 'Name'].style.background = 'white'
        this[fieldInputText + 'Name'].onfocus = function () {
          $(this.tagetId).focus()
        }
        getMethods.myAddEvent(this[fieldInputText + 'Name'], 'click', function (ev) {
          var oEvent = ev || window.event
          oEvent.cancelBubble = true
        })
        getMethods.myAddEvent(this[fieldInputText + 'Name'], 'focus', function () {
          var json = {
            owner: _this,
            ownerNameBtn: this,
            ownerBtn: this.targetId,
            container: this.parentNode,
            width: $(this).outerWidth(),
            absolute: {
              top: $(this).outerHeight()
            }
          }
          for (var i in json) {
            fields.dropdownList.config[i] = json[i]
          }
          eval(fields.dropdownList.xtype)(fields.dropdownList.config)
        })
      }
      this['field-' + opt.number].style.display = 'none'
    }
    if (this.fieldExtraClass) {
      for (var property in this.fieldExtraClass) {
        this.addExtraClass(this['field-' + property + opt.number], this.fieldExtraClass[property])
      }
    }
    if (fields.extraClass) {
      for (let property in fields.extraClass) {
        this.addExtraClass(this['field-' + property + opt.number], fields.extraClass[property])
      }
    }
    if (this[fieldInputText + 'Name']) {
      this[fieldInputText + 'Name'].className = this['field-' + opt.number].className
    }
  }
  selectAsync (opt) {
    var name = opt.obj.name
    var async = opt.obj.fields.async
    var _this = this
    var data = {}
    if (opt.origin) {
      data[opt.obj.fields.async.data.send] = opt.origin.value
    } else {
      data = opt.obj.fields.async.data.send
    }
    if (data.constructor !== Object || (opt.origin && !opt.origin.value)) {
      opt.obj.setAttribute('readonly', true)
      opt.obj.innerHTML = '<option value="">无选项</option>'
      if (opt.obj.fields.linkage) { $(opt.obj).change() }
      return false
    }
    if (opt.origin) {
      opt.origin.removeAttribute('readonly')
    }
    opt.obj.removeAttribute('readonly')
    opt.obj.innerHTML = '<option value="">读取中..</option>'
    $.ajax({
      url: async.url,
      data: data,
      type: async.type || 'post',
      success: function (response) {
        if (response.data && response.data.length) {
          if (opt.obj.querySelector('option')) { opt.obj.querySelector('option').innerHTML = '请选择' }
          for (var i = 0; i < response.data.length; i++) {
            _this.createPanel({
              name: name + 'option' + i,
              tag: 'option',
              container: opt.obj,
              noClass: true
            })
            if (opt.obj.fields.async.data.set.constructor === Object) {
              _this[name + 'option' + i].innerHTML = response.data[i][opt.obj.fields.async.data.set.html] || response.data[i].name
              _this[name + 'option' + i].value = response.data[i][opt.obj.fields.async.data.set.value]
            } else if (opt.obj.fields.async.data.set.constructor === String) {
              _this[name + 'option' + i].innerHTML = response.data[i][opt.obj.fields.async.data.html] || response.data[i].name
              _this[name + 'option' + i].value = response.data[i][opt.obj.fields.async.data.set]
            }
          }
          if (opt.obj.selectValue && opt.obj.querySelector('option[value="' + opt.obj.selectValue + '"]')) {
            opt.obj.value = opt.obj.selectValue
            $(opt.obj).blur()
// opt.obj.selectValue=null;
          }
          if (opt.obj.fields.linkage) { $(opt.obj).change() }
        } else {
          if (opt.obj.querySelector('option')) {
            opt.obj.setAttribute('readonly', true)
            opt.obj.querySelector('option').innerHTML = '无选项'
          }
          if (opt.obj.fields.linkage) { $(opt.obj).change() }
        }
      },
      error: function (response) {
        layer.alert('错误代码:' + response.status + '，请联系管理员', {title: '错误', shadeClose: true})
      }
    })
  }
  validatesFn (opt) {
    var _this = this
    var loading = layer.load(1, { shade: [0.1, '#fff'] })
    var testFailed = []
    var allReady = true
    var asyncReady = true
    var noReady = []
    for (var i = 0; i < _this.mustValidate.length; i++) {
      if (_this.mustValidate[i].getAttribute('async') && /false/.test(_this.mustValidate[i].getAttribute('async'))) {
        asyncReady = false
      }
    }
    for (let i = 0; i < _this.mustValidate.length; i++) {
      $(_this.mustValidate[i]).blur()
      if (_this.mustValidate[i].getAttribute('isReady') === 'false' || (_this.mustValidate[i].getAttribute('async') && _this.mustValidate[i].getAttribute('async') === 'false')) {
        noReady.push(_this.mustValidate[i])
        allReady = false
      }
    }
    if (!asyncReady) {
      if (opt && opt.windowBtn) {
        if (this.asyncFailure) {
          layer.close(loading)
        } else {
          layer.close(loading)
          setTimeout(function () { $(opt.windowBtn).click() }, 2000)
        }
      } else {
        layer.close(loading)
      }
      return false
    }
    if (allReady === true) {
      for (let i = 0; i < _this.mustValidate.length; i++) {
        if (_this.mustValidate[i].getAttribute('validator') === 'false') {
          if ((_this.mustValidate[i].getAttribute('allowEmpty') === 'true' && !_this.mustValidate[i].value)) {
            layer.close(_this.mustValidate[i].tip)
            _this.mustValidate[i].setAttribute('validator', true)
          } else {
            _this.mustValidate[i].setAttribute('validator', false)
            testFailed.push(_this.mustValidate[i])
          }
        }
      }
      if (testFailed.length) {
// clearInterval(timer);
        layer.close(loading)
        return false
      }
      if (_this.noSubmit) {
        for (let i = 0; i < _this.noSubmit.length; i++) {
          _this.noSubmit[i].disabled = true
        }
      }
      if (this.formContainer.querySelectorAll('[isreadonly=true]')) {
        for (let i = 0; i < this.formContainer.querySelectorAll('[isreadonly=true]').length; i++) {
          this.formContainer.querySelectorAll('[isreadonly=true]')[i].removeAttribute('disabled')
        }
      }
      if (_this.btns && _this.btns['edit']) {
        layer.close(loading)
        _this.btns['edit'].events.click({
          owner: _this.owner,
          component: _this.formContainer,
          panel: _this,
          btn: this
        })
      } else {
        layer.close(loading)
        return true
      }
      if (this.formContainer.querySelectorAll('[isreadonly=true]')) {
        for (let i = 0; i < this.formContainer.querySelectorAll('[isreadonly=true]').length; i++) {
          this.formContainer.querySelectorAll('[isreadonly=true]')[i].setAttribute('disabled', true)
        }
      }
      if (_this.noSubmit) {
        for (let i = 0; i < _this.noSubmit.length; i++) {
          _this.noSubmit[i].disabled = false
        }
      }
// clearInterval(timer);
      layer.close(loading)
    } else {
      for (let i = 0; i < noReady.length; i++) {
        if (noReady[i].getAttribute('isReady') !== 'false') {
          noReady.splice(i, 1)
        }
      }
      if (noReady.length === 0) {
        allReady = true
      }
      setTimeout(function () { _this.validatesFn() }, 200)
    }
  }
}

export default FormPanel
