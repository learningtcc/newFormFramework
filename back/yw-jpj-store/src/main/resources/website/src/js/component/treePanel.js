import $ from 'jquery'
import layer from 'static/layer/layer'
import Panel from 'component/panel'
import { WINDOW_CENTER } from 'common/commonClass'
import { addClass, myAddEvent, Loading, startMove, deleteClass } from 'common/method'

class TreePanel extends Panel {
  constructor (opt) {
    super(opt)

    this.queryData = this.data
    delete this.data

    this.extraInit()
  }
  extraInit () {
    this.createComponent({
      setBox: true,
      container: this.container
    })
    var _this = this
    if (this.layerConfig) {
      this.title = null
      this.createWindow()
      this.createComponent()
      this.createPanel({
        name: 'defineBtns',
        tag: 'div',
        container: $(this.win).find('.layui-layer-btn')[0]
      })
      for (var btns in this.btns) {
        this.setBtns(btns)
      }
    }
    if (!this.isSelect) {
      this.createTitle()
      if (this.title) {
        if (!this['defineBtns']) {
          this.createPanel({
            name: 'defineBtns',
            tag: 'div',
            container: this.title.className + 'Panel'
          })
        }
        this.addExtraClass(this.defineBtns, this.defineBtnsExtraClass)
        for (var btn in this.btns) {
          if (this.btns[btn] && this.btns[btn].constructor === Object) { this.setBtns(btn) }
        }
      }
    } else {
      if (this.isSelect.title) {
        this.createTitle()
        if (this.title) {
          if (!this['defineBtns']) {
            this.createPanel({
              name: 'defineBtns',
              tag: 'div',
              container: this.title.className + 'Panel'
            })
          }
          this.addExtraClass(this.defineBtns, this.defineBtnsExtraClass)
          for (let btns in this.btns) {
            this.setBtns(btns)
          }
        }
      }
      document.onclick = function () {
        if (_this[_this.className].parentNode) {
          _this[_this.className].parentNode.removeChild(_this[_this.className])
        }
      }
      this.component.onclick = function (ev) {
        var oEvent = ev || event
        oEvent.cancelBubble = true
      }
      if (this.isSelect.time !== false) {
        this.component.onmouseleave = function () {
          this.timer = setTimeout(function () {
            if (_this[_this.className].parentNode) {
              _this[_this.className].parentNode.removeChild(_this[_this.className])
            }
          }, 1000)
        }
        this.component.onmouseenter = function () {
          clearTimeout(this.timer)
        }
      }
    }
    this.refresh()
    this.createItems()
  }
  createComponent (opt) {
    if (opt && opt.setBox && !this.isSelect) {
      if (opt.container && typeof opt.container === 'string') {
        opt.container = document.getElementById(opt.container)
        if (!opt.container) {
          opt.container = this.defaultContainer
        }
      } else {
        opt.container = this.defaultContainer
      }
      this.createPanel({
        name: this.className + 'box',
        tag: 'div',
        container: opt.container || 'container',
        noClass: true
      })
      this[this.className + 'box'].id = this.id
      if (this.boxExtraClass) this.addExtraClass(this[this.className + 'box'], this.boxExtraClass)
      this.createPanel({
        name: this.className,
        tag: 'div',
        container: this.className + 'box'
      })
    } else {
      if (this.id && document.getElementById(this.id)) {
        document.getElementById(this.id).parentNode.removeChild(document.getElementById(this.id))
      }

      this.createPanel({
        name: this.className,
        tag: 'div',
        container: 'container'
      })
      this[this.className].id = this.id
    }
    // for (var attr in this.style) {
    //   style[attr] = this.style[attr]
    // }
    if (this.isSelect) {
      if (this.absolute) {
        if (this.absolute.top) this[this.className].style.top = this.absolute.top + 'px'
        if (this.absolute.left) this[this.className].style.left = this.absolute.left + 'px'
      }
      this[this.className].style.width = this.width + 'px'
      this[this.className].style.zIndex = 999
      this[this.className].style.position = 'absolute'
      addClass(this[this.className], 'dropdownList')
    } else {
      addClass(this[this.className], 'treeComponent')
    }
    this.addExtraClass(this[this.className], this.extraClass)
    if (!this[this.className + 'box']) { this[this.className].id = this.id }
    if (!this.noInsert) { this[this.className].parentNode.insertBefore(this[this.className], this[this.className].parentNode.children[0]) }
    this.component = this[this.className]
  }
  createTree () {
    var _this = this
    if (this.isSelect && this.isSelect.root !== false) {
      this.createPanel({
        name: 'topLevel',
        tag: 'div'
      })
      this['topLevel'].innerHTML = '移动到顶级'
      this['topLevel'].setAttribute('uid', 'root')
      this['topLevel'].onclick = function () {
        _this.treeEvents.click({
          btn: this,
          panel: _this,
          owner: _this.owner,
          ownerBtn: _this.ownerBtn,
          ownerNameBtn: _this.ownerNameBtn
        })
      }
    }
    this.createPanel({
      name: 'root',
      tag: 'ul'
    })
    if (this.treeClass) this.addExtraClass(this.root, this.treeClass.root)
    this.forTreeFn({
      data: this.queryData || this.data,
      root: 'root',
      zIndex: 0
    })
  }
  forTreeFn (opt) {
    var _this = this
    if (opt.root.constructor === String && opt.root !== 'root') {
      this.createPanel({
        name: 'ul-' + opt.zIndex,
        tag: 'ul',
        container: 'li-' + (opt.zIndex - 1),
        noClass: true
      })
      opt.root = 'ul-' + opt.zIndex
    } else if (opt.root.constructor === HTMLLIElement) {
      this.createPanel({
        name: 'ul-' + opt.zIndex,
        tag: 'ul',
        container: opt.root,
        noClass: true
      })
      opt.root = 'ul-' + opt.zIndex
    }
    if (opt.data.root) {
      opt.data = opt.data.root
    }
    for (let i = 0; i < opt.data.length; i++) {
      if (opt.root === 'root') { opt.zIndex = 0 }
      this.createPanel({
        name: 'li-' + opt.zIndex,
        tag: 'li',
        container: opt.root,
        noClass: true
      })
      if (opt.root === 'root') { this['li-' + opt.zIndex].setAttribute('group', i) }
      this[opt.root].setAttribute('extend', true)
      if (this.isMenu) { this[opt.root].setAttribute('extend', false) }

      var tagArr = ['li', 'a', 'i']

      for (let i = 1; i < tagArr.length; i++) {
        this.createPanel({
          name: tagArr[i] + '-' + opt.zIndex,
          tag: tagArr[i],
          container: tagArr[i - 1] + '-' + opt.zIndex,
          noClass: true
        })
      }

      if (opt.data[i].children || (!opt.data[i].children && this.notLeaf)) {
        this.createPanel({
          name: 'b-' + opt.zIndex,
          tag: 'b',
          container: 'a-' + opt.zIndex,
          noClass: true
        })
        if (_this.extend) {
          ['i', 'b'].map(function (item) {
            if (opt.data[i].children) {
              addClass(_this[item + '-' + opt.zIndex], _this.extend[_this[opt.root].getAttribute('extend')][item])
            } else {
              addClass(_this[item + '-' + opt.zIndex], _this.extend[!_this[opt.root].getAttribute('extend')][item])
            }
          })
        }
        if (opt.data[i].children) {
          myAddEvent(this['a-' + opt.zIndex], 'click', function (ev) {
            var oEvent = ev || event
            oEvent.cancelBubble = true
            this.parentNode.getElementsByTagName('ul')[0].setAttribute('extend', !eval(this.parentNode.getElementsByTagName('ul')[0].getAttribute('extend')))
            var b = this.getElementsByTagName('b')[0]
            var i = this.getElementsByTagName('i')[0]
            if (_this.extend) {
              $(b).toggleClass(_this.extend.false.b).toggleClass(_this.extend.true.b)
              $(i).toggleClass(_this.extend.false.i).toggleClass(_this.extend.true.i)
            }
            if (_this.isMenu) { return false }
            $(this.parentNode.getElementsByTagName('ul')[0]).slideToggle(200)
          })
        } else if (this.notLeaf) {
          myAddEvent(this['a-' + opt.zIndex], 'click', function (ev) {
            var that = this
            var oEvent = ev || event
            oEvent.cancelBubble = true
            var childUl = this.parentNode.getElementsByTagName('ul')[0]
            var b = this.getElementsByTagName('b')[0]
            var i = this.getElementsByTagName('i')[0]
            if (!b || $(that).find('del').length > 0) { return false }
            if (childUl) {
              childUl.setAttribute('extend', !eval(childUl.getAttribute('extend')))
              $(b).toggleClass(_this.extend.false.b).toggleClass(_this.extend.true.b)
              $(i).toggleClass(_this.extend.false.i).toggleClass(_this.extend.true.i)
              $(childUl).slideToggle(200)
            } else if (!childUl && b) {
              that.data = {}
              for (var attr in _this.notLeaf.data) {
                that.data[attr] = that.querySelector('span').getAttribute(_this.notLeaf.data[attr])
                if (_this.notLeaf.data[attr] && !that.data[attr]) {
                  that.data[attr] = _this.notLeaf.data[attr]
                }
                if (!that.data[attr] && that.data[attr] !== 0) {
                  b.parentNode.removeChild(b)
                  return false
                }
              }
              $(that).append('<del style="background-color:transparent;text-decoration:none" class="fa fa-spinner fa-pulse fa-fw"></del>')
              $.ajax({
                url: _this.notLeaf.url,
                data: that.data,
                type: _this.notLeaf.type || 'post',
                success: function (response) {
                  if (response.data && response.data.length) {
                    _this.forTreeFn({
                      data: response.data,
                      root: that.parentNode,
                      zIndex: parseInt(that.querySelector('span').getAttribute('index')) + 1
                    })
                    $(b).toggleClass(_this.extend.false.b).toggleClass(_this.extend.true.b)
                    $(i).toggleClass(_this.extend.false.i).toggleClass(_this.extend.true.i)
                    $(childUl).slideToggle(200)
                  } else {
                    b.parentNode.removeChild(b)
                  }
                  $(that).find('del').remove()
                },
                error: function (response) {
                  layer.alert('错误代码:' + response.status + '，请联系管理员', {title: '错误', shadeClose: true})
                }
              })
            }
          })
        }
      } else {
        this['li-' + opt.zIndex].setAttribute('leaf', true)
        if (_this.extend) { addClass(this['i-' + opt.zIndex], _this.extend[!this[opt.root].getAttribute('extend')]['i']) }
      }

      var container = 'a-' + opt.zIndex
      if (this.configCheck) {
        this.createPanel({
          name: 'label-' + opt.zIndex,
          tag: 'label',
          container: 'a-' + opt.zIndex,
          noClass: true
        })
        container = 'label-' + opt.zIndex
        this.createPanel({
          name: 'checkbox-' + opt.zIndex,
          tag: 'input',
          container: container,
          noClass: true
        })
        if (_this.configCheck.disabled) {
          this['checkbox-' + opt.zIndex].disabled = true
        }
        this['checkbox-' + opt.zIndex].type = 'checkbox'
        _this.setAttributes({
          target: this['checkbox-' + opt.zIndex],
          origin: opt.data[i],
          attrs: {
            uid: opt.data[i].id || ''
          }
        })
        if (!opt.data[i].children) { this['checkbox-' + opt.zIndex].setAttribute('leaf', true) }
        var that = this
        myAddEvent(this['checkbox-' + opt.zIndex], 'click', function (ev) {
          var oEvent = ev || event
          oEvent.cancelBubble = true
          if (that.configCheck.recursion) {
            var ul = this.parentNode.parentNode.parentNode.getElementsByTagName('ul')
            if (ul.length) {
              for (var i = 0; i < ul[0].getElementsByTagName('input').length; i++) {
                ul[0].getElementsByTagName('input')[i].indeterminate = false
                ul[0].getElementsByTagName('input')[i].checked = this.checked
              }
            }
            var self = this;
            (function setOwnerCheck (obj) {
              if (/\broot\b/i.test(obj.parentNode.parentNode.parentNode.parentNode.className)) {
                return false
              } else {
                var li = obj.parentNode.parentNode.parentNode.parentNode.getElementsByTagName('li')
                var input = obj.parentNode.parentNode.parentNode.parentNode.parentNode.getElementsByTagName('input')[0]
                if (li.length > 1) {
                  var allChecked = {
                    checked: 0,
                    unchecked: 0
                  }
                  for (var i = 0; i < li.length; i++) {
                    if (li[i].getElementsByTagName('input')[0].type === 'checkbox') {
                      if (li[i].getElementsByTagName('input')[0].checked) {
                        allChecked.checked++
                      } else {
                        allChecked.unchecked++
                      }
                    }
                  }
                  if (allChecked.checked && allChecked.unchecked) {
                    input.indeterminate = true
                    input.checked = false
                  } else {
                    input.indeterminate = false
                    input.checked = self.checked
                  }
                } else {
                  input.checked = self.checked
                }
                setOwnerCheck(input)
              }
            })(this)
          }
        })
        if (!this.allCheckbox) { this.allCheckbox = [] }
        if (opt.data[i].checked && this['checkbox-' + opt.zIndex].getAttribute('leaf')) { this.allCheckbox.push(this['checkbox-' + opt.zIndex]) }
      }
      this.createPanel({
        name: 'span-' + opt.zIndex,
        tag: 'span',
        container: container,
        noClass: true
      })
      this['span-' + opt.zIndex].innerHTML = opt.data[i].name || opt.data[i].cnName
      if (opt.data[i].icon) { this.addExtraClass(this['i-' + opt.zIndex], opt.data[i].icon) }
      if (opt.data[i].href) { this['a-' + opt.zIndex].href = opt.data[i].href }
      var arr = ['ul', 'li', 'a', 'i', 'b', 'span', 'checkbox']
      _this.setAttributes({
        target: this['span-' + opt.zIndex],
        origin: opt.data[i],
        attrs: {
          uid: 'id'
        }
      })
      if (opt.root !== 'root') {
        this[opt.root].setAttribute('group', this[opt.root].parentNode.getAttribute('group'))
        this['li-' + opt.zIndex].setAttribute('group', this[opt.root].parentNode.getAttribute('group'))
      }
      for (var j = 0; j < this['li-' + opt.zIndex].getElementsByTagName('*').length; j++) {
        this['li-' + opt.zIndex].getElementsByTagName('*')[j].setAttribute('group', this['li-' + opt.zIndex].getAttribute('group'))
      }
      var index = 0
      if (opt.root === 'root') {
        index = 0
      } else {
        if (this.configCheck) {
          index = parseInt(this['span-' + opt.zIndex].parentNode.parentNode.parentNode.parentNode.parentNode.getElementsByTagName('span')[0].getAttribute('index')) + 1
        } else {
          index = parseInt(this['span-' + opt.zIndex].parentNode.parentNode.parentNode.parentNode.getElementsByTagName('span')[0].getAttribute('index')) + 1
        }
      }
      if (this.configCheck) { this['checkbox-' + opt.zIndex].setAttribute('index', index) }
      this['span-' + opt.zIndex].setAttribute('index', index)
      if (opt.data[i].id) {
        this['span-' + opt.zIndex].setAttribute('uid', opt.data[i].id)
      }
// this['span-'+opt.zIndex].uid=opt.data[i].id;
      if (this['span-' + (opt.zIndex - 1)]) {
        _this.setAttributes({
          target: this['span-' + opt.zIndex],
          origin: this['span-' + opt.zIndex].parentNode.parentNode.parentNode.parentNode.getElementsByTagName('span')[0],
          attrs: {
            parentId: opt.data[i].parentId,
            parantIdName: 'innerHTML'
          }
        })
      }
      if (_this.treeClass && _this.treeClass[index]) {
        for (let j = 0; j < arr.length; j++) {
          if (this[arr[j] + '-' + index]) { _this.addExtraClass(this[arr[j] + '-' + opt.zIndex], _this.treeClass[index][arr[j]]) }
        }
      }
      if (_this.treeClass && _this.treeClass.all) {
        for (let j = 0; j < arr.length; j++) {
          if (this[arr[j] + '-' + index]) { _this.addExtraClass(this[arr[j] + '-' + opt.zIndex], _this.treeClass.all[arr[j]]) }
        }
      }
      if (_this.configCheck && _this.configCheck.click) {
        myAddEvent(this['checkbox-' + opt.zIndex], 'click', function () {
          _this.configCheck.click({
            label: this.parentNode,
            btn: this.parentNode.getElementsByTagName('span')[0],
            panel: _this,
            input: this
          })
        })
      }
      if (_this.configCheck) {
        myAddEvent(this['label-' + opt.zIndex], 'click', function (ev) {
          var oEvent = ev || event
          oEvent.cancelBubble = true
        })
        if (_this.treeEvents && _this.treeEvents.checkClick) {
          myAddEvent(this['checkbox-' + opt.zIndex], 'click', function () {
            _this.treeEvents.checkClick({
              btn: this,
              panel: _this,
              owner: _this.owner,
              ownerBtn: _this.ownerBtn,
              ownerNameBtn: _this.ownerNameBtn
            })
            $(_this.ownerBtn).blur()
          })
        }
      } else {
        if (_this.isMenu) {
          if (opt.data[i] && opt.data[i].events) {
            this['a-' + opt.zIndex].events = opt.data[i].events
            for (var events in opt.data[i].events) {
              myAddEvent(this['a-' + opt.zIndex], events, function (ev) {
// layer.closeAll();
                var oEvent = ev || event
                oEvent.cancelBubble = true
                this.events[oEvent.type]({
                  btn: this,
                  panel: _this,
                  owner: _this.owner
                })
              })
            }
          }
        }
        myAddEvent(this['span-' + opt.zIndex], 'click', function (ev) {
          if (_this.isMenu) {
            return false
          }
          if (_this.ownerBtn && _this.ownerBtn.dropDown) { $(_this.ownerBtn).blur() }
          var oEvent = ev || event
          oEvent.cancelBubble = true
          if (/active/.test(this.className)) {
            deleteClass(this, 'active')
            deleteClass(this, 'current')
            _this.setAttributes({
              target: _this[_this.className],
              attrs: {
                uid: 'uid'
              }
            })
            _this[_this.className].setAttribute('index', '')
          } else {
            for (var i = 0; i < _this.root.getElementsByTagName('*').length; i++) {
              deleteClass(_this.root.getElementsByTagName('*')[i], 'active')
              deleteClass(_this.root.getElementsByTagName('*')[i], 'current')
            }
            addClass(this, 'active')
            _this.setAttributes({
              target: _this[_this.className],
              origin: this,
              attrs: {
                uid: this.getAttribute('uid')
              }
            })
            _this[_this.className].setAttribute('index', this.getAttribute('index'))
            if (_this.owner && $(_this.owner.formContainer).find($("[name='parentId']"))[0]) { $(_this.owner.formContainer).find($("[name='parentId']"))[0].value = this.getAttribute('uid') }

            _this.treeEvents.click({
              btn: this,
              panel: _this,
              owner: _this.owner,
              ownerBtn: _this.ownerBtn,
              ownerNameBtn: _this.ownerNameBtn
            })
          }
        })
      }
      if (this.treeEvents) {
        for (let events in this.treeEvents) {
          if (events !== 'click') {
            myAddEvent(this['a-' + opt.zIndex], events, function (ev) {
              var oEvent = ev || event
              oEvent.cancelBubble = true
              _this.treeEvents[oEvent.type]({
                btn: this,
                panel: _this,
                owner: _this.owner
              })
            })
          }
        }
      }
      if (this.extraAttr) {
        if (this.extraAttr.constructor === Array) {
          for (let j = 0; j < this.extraAttr.length; j++) {
            if (opt.data[i][this.extraAttr[j]] || opt.data[i][this.extraAttr[j]] === 0) { this['span-' + opt.zIndex].setAttribute(this.extraAttr[j], opt.data[i][this.extraAttr[j]]) }
          }
        } else if (this.extraAttr.constructor === Object) {
          for (let attr in this.extraAttr) {
            if (opt.data[i][this.extraAttr[attr].name] || opt.data[i][this.extraAttr[attr].name] === 0) {
              if (this.extraAttr[attr].value && this.extraAttr[attr].value[opt.data[i][this.extraAttr[attr].name]]) {
                this['span-' + opt.zIndex].setAttribute(attr, this.extraAttr[attr].value[opt.data[i][this.extraAttr[attr].name]])
              } else {
                this['span-' + opt.zIndex].setAttribute(attr, opt.data[i][this.extraAttr[attr].name])
              }
            }
          }
        }
      }
      if (opt.data[i].children) {
        opt.zIndex++
        this.forTreeFn({
          data: opt.data[i].children,
          root: 'li-' + (opt.zIndex - 1),
          zIndex: opt.zIndex
        })
      }
    }
  }
  partialRefresh (opt) {
    if (this.root.querySelector('[uid="' + opt.uid + '"]')) {
      var target = this.root.querySelector('[uid="' + opt.uid + '"]')
      if (target.parentNode.parentNode.querySelector('ul')) {
        if (target.parentNode.parentNode.querySelector('ul').getAttribute('extend') === true) { target.parentNode.click() }
        target.parentNode.removeChild(target.parentNode.querySelector('b'))
        target.parentNode.parentNode.removeChild(target.parentNode.parentNode.querySelector('ul'))
      }
      if (!target.parentNode.querySelector('b')) {
        var b = document.createElement('b')
        target.parentNode.insertBefore(b, target)
        b.className = 'fa-caret-right fa'
      }
      target.parentNode.click()
    } else {
      this.refresh()
    }
  }
  refresh () {
    if (!this.url) {
      this.renderData({
        data: this.data
      })
      if (this.render) {
        this.afterRender()
      }
      return false
    }
    if (this.loading) { return false }
    if (this.root) {
      this.root.parentNode.removeChild(this.root)
    }
    if (this['errorMessage']) {
      this['errorMessage'].parentNode.removeChild(this['errorMessage'])
    }
    var _this = this
    Loading.on(this[this.className])
    this.loading = true
    if (this.ownerBtn) {
      this.queryData = this.queryData
      if (!this.queryData && this.data && this.data.constructor === Object) {
        this.queryData = this.data
      }
    }
    $.ajax({
      url: this.ajaxUrl || this.url,
      data: this.queryData,
      type: this.ajaxType || this.type || 'post',
      success: function (response) {
        _this.response = response
        if (response.isSuccess) {
          if (!response.data || response.data.length === 0) {
            _this.createPanel({
              name: 'errorMessage',
              tag: 'div',
              container: _this.component
            })
            _this['errorMessage'].innerHTML = '数据为空'
            addClass(_this['errorMessage'], 'ibox-content')
// layer.msg(_this['errorMessage'].innerHTML,function(){});
          } else {
            _this.renderData({
              data: response.data
            })
          }
          Loading.off(_this[_this.className])
          _this.loading = false
          if (_this.render) {
            _this.afterRender()
          }
        } else if (response.errorMessage) {
          Loading.off(_this[_this.className])
          _this.loading = false
          layer.msg(response.errorMessage, function () {})
        } else {
          Loading.off(_this[_this.className])
          _this.loading = false
          layer.msg('登录失效，请重新登录', function () {})
          setTimeout(function () {
            window.location.href = '/login.htm'
          }, 2000)
        }
      },
      error: function (response) {
        Loading.off()
        _this.loading = false
        layer.alert('错误代码:' + response.status + '，请联系管理员', {title: '错误', shadeClose: true})
      }
    })
  }
  renderData (opt) {
    this.setAttributes({
      target: this[this.className],
      attrs: {
        uid: 'uid'
      }
    })
    this.data = opt.data

    var self = this
    Object.getOwnPropertyNames(Object.getPrototypeOf(this)).map(function (item, i, list) {
      if (!/init|extraInit|createPanel|createComponent|createTitle|createWindow|refresh|createItems/i.test(item)) {
        if (/create/i.test(item)) {
          self[item]()
        }
      }
    })

    if (this.configCheck && this.allCheckbox.length) {
      for (var i = 0; i < this.allCheckbox.length; i++) {
        this.allCheckbox[i].click()
      }
    }
    if (this.noRoot) {
      if (this.root.querySelector('div')) this.root.querySelector('div').style.display = 'none'
      if (this.root.querySelector('ul')) this.root.querySelector('ul').style.display = 'block'
    }
    if (this.isMenu) {
      this.menuSelect()
    }
  }
  afterRender (opt) {
    this.render({
      owner: this.owner,
      component: this.component,
      root: this.root,
      data: (() => {
        if (opt && opt.data) {
          return opt.data
        }
      })(),
      response: this.response,
      ownerBtn: this.ownerBtn,
      ownerNameBtn: this.ownerNameBtn,
      panel: this
    })
    if (this.configCheck && this.configCheck.allDisabled) {
      for (let i = 0; i < this.root.getElementsByTagName('input').length; i++) {
        this.root.getElementsByTagName('input')[i].disabled = true
      }
    } else if (this.configCheck && this.configCheck.topDisabled) {
      for (let i = 0; i < this.root.getElementsByTagName('input').length; i++) {
        if (this.root.getElementsByTagName('input')[i].getAttribute('index') === 0) { this.root.getElementsByTagName('input')[i].disabled = true }
      }
    }
  }
  menuSelect () {
    var btns = this.root.querySelectorAll('a')
    for (var i = 0; i < btns.length; i++) {
      if (btns[i].parentNode.children[1]) {
        btns[i].parentNode.children[1].style.height = 'auto'
        btns[i].parentNode.children[1].setAttribute('originHeight', btns[i].parentNode.children[1].offsetHeight)
        btns[i].parentNode.children[1].style.height = '0'
      }
      btns.index = i
      myAddEvent(btns[i], 'click', function () {
        if (/active/.test(this.parentNode.className)) {
          if (this.parentNode.children[1]) { startMove(this.parentNode.children[1], {height: 0}) }
          deleteClass(this.parentNode, 'active')
        } else {
          for (var i = 0; i < this.parentNode.parentNode.getElementsByTagName('a').length; i++) {
            if (this.parentNode.parentNode.getElementsByTagName('a')[i].parentNode.className && /active/.test(this.parentNode.parentNode.getElementsByTagName('a')[i].parentNode.className)) {
              if (this.parentNode.parentNode.getElementsByTagName('a')[i].parentNode.children[1]) { startMove(this.parentNode.parentNode.getElementsByTagName('a')[i].parentNode.children[1], {height: 0}) }
              deleteClass(this.parentNode.parentNode.getElementsByTagName('a')[i].parentNode, 'active')
            }
          }
          if (this.parentNode.children[1]) {
            var _this = this
            startMove(this.parentNode.children[1], {height: _this.parentNode.children[1].getAttribute('originHeight')}, function () { _this.parentNode.children[1].style.height = 'auto' })
          }
          addClass(this.parentNode, 'active')
        }
      })
    }
// 需要默认点击，所以在点击事件之后生成
    this.creatCenter()
  }
  creatCenter () {
    window.center = document.createElement('div')
    if (this.container) {
      this.container = document.getElementById(this.container)
      if (!this.container) {
        this.container = document.body
      }
    } else {
      this.container = document.body
    }

    this.container.appendChild(window.center)
    window.center.className = 'center drore-main'
    window.center.id = WINDOW_CENTER
    var firstHash = '';
    (function getFirstA (obj) {
      if (obj.querySelector('li')) {
        if (obj.querySelector('li').querySelector('ul')) { getFirstA(obj.querySelector('ul')) } else { firstHash = obj.querySelector('a').getAttribute('href').substring(1) }
      }
    })(document.querySelector('#menu'))
    if (!window.location.hash) { window.location.hash = firstHash }
    var li = $(document.querySelector('[href="' + window.location.hash + '"]')).parents('li')
    for (var i = li.length - 1; i > -1; i--) {
      li[i].querySelector('a').click()
    }
  }
}

export default TreePanel
