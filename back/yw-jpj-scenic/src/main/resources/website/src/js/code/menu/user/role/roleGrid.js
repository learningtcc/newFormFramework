import $ from 'jquery'
import layer from 'static/layer/layer'
import { btnPlusClass, btnReloadClass, treeClass, windowCenter, setConfig } from 'common/commonClass'
import { Loading as commonLoading } from 'common/method'
import GridPanel from 'component/gridPanel'
import TreePanel from 'component/treePanel'
import roleForm from 'code/menu/user/role/roleForm'

export default function roleGrid (opt) {
  var btns = {
    plus: {
      extraClass: btnPlusClass,
      events: {
        click: function (opt) {
          opt.panel.items['roleForm'] = roleForm({
            owner: opt.panel,
            ownerBtn: opt.panel
          })
        },
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('新增角色', opt.btn, {
            tips: [1, '#286090']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        }
      }
    },
    reload: {
      extraClass: btnReloadClass,
      events: {
        click: function (opt) {
          opt.panel.refresh()
        },
        mouseenter: function (opt) {
          $(opt.btn).addClass('fa-spin')
        },
        mouseleave: function (opt) {
          $(opt.btn).removeClass('fa-spin')
        }
      }
    }
  }
  var operationBtns = {
    edit: {
      extraClass: ['fa', 'fa-edit', 'btn', 'btn-sm', 'btn-warning', 'btn-space'],
      events: {
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('编辑', opt.btn, {
            tips: [1, '#ec971f']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          opt.panel.items['roleForm'] = roleForm({
            owner: opt.panel,
            ownerBtn: opt.panel,
            data: {
              id: opt.tr.getAttribute('uid')
            }
          })
        }
      }
    },
    setting: {
      extraClass: ['fa', 'fa-cog', 'btn', 'btn-sm', 'btn-success', 'btn-space'],
      events: {
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('设置权限', opt.btn, {
            tips: [1, '#449d44']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          // var thisId = $(opt.tr).attr('uid')
          var top = $(opt.btn).position().top + $(opt.btn).outerHeight()
          var left
          // var container = $('.treeComponent')[0]
          var setTreeLeft = 270

          var restWidth = $(opt.tr).parents('.ibox').outerWidth() - $(opt.btn).position().left - $(opt.btn).outerWidth()
          if (restWidth < setTreeLeft) {
            left = $(opt.btn).position().left - (setTreeLeft - restWidth)
          } else {
            left = $(opt.btn).position().left
          }
/* eslint-disable no-new */
          new TreePanel({
            container: opt.btn.parentNode,
            isSelect: {
              root: false,
              title: true,
              time: false
            },
            title: {
              extraClass: ['ibox-title'],
              innerHTML: '设置权限'
            },
            id: 'setting',
            className: 'setting',
            extraClass: ['dropdown-menu', 'show'],
            defineBtnsExtraClass: ['ibox-tools'],
            treeClass: treeClass,
            extend: {
              true: {
                b: 'fa-caret-down',
                i: 'fa-folder-open'
              },
              false: {
                b: 'fa-caret-right',
                i: 'fa-folder'
              }
            },
            owner: opt.panel, // 上级
            absolute: {
              top: top,
              left: left
            },
            configCheck: true,
            width: 270,
            url: '/authority/queryTree',
            type: 'get',
            noBtns: true,
            render: function (opt) {
              opt.panel.defineBtns.style.display = opt.panel.root.style.display = 'none'
              commonLoading.on(opt.component)
              $.ajax({
                url: '/role/queryAuthorityByRoleId',
                data: {
                  roleId: opt.owner.component.getAttribute('uid'),
                  isEnable: 'Y'
                },
                success: function (response) {
                  if (response.data) {
                    for (var i = 0; i < response.data.length; i++) {
                      if (opt.panel.root.querySelector('[uid="' + response.data[i].id + '"]')) { opt.panel.root.querySelector('[uid="' + response.data[i].id + '"]').click() }
                    }
                  }
                  commonLoading.off(opt.component)
                  opt.panel.defineBtns.style.display = opt.panel.root.style.display = 'block'
                },
                error: function (response) {
                  commonLoading.off(opt.component)
                  layer.alert('错误代码:' + response.status + '，请联系管理员', {title: '错误', shadeClose: true})
                }
              })
            },
            btns: {
              confirm: {
                extraClass: ['confirm', 'fa', 'fa-check-square-o', 'font-info'],
                events: {
                  mouseenter: function (opt) {
                    opt.btn.tips = layer.tips('提交权限设置', opt.btn, {
                      tips: [1, '#449d44']
                    })
                  },
                  mouseleave: function (opt) {
                    layer.close(opt.btn.tips)
                  },
                  click: function (opt) {
                    layer.closeAll()
                    var aInput = opt.panel.root.getElementsByTagName('input')
                    var str = ''
                    for (var i = 0; i < aInput.length; i++) {
                      if (aInput[i].checked) {
                        str += aInput[i].getAttribute('uid') + ','
                      }
                    }
                    str = str.slice(0, -1)
                    var loading = layer.load(1, { shade: [0.1, '#fff'] })
                    $.ajax({
                      type: 'post',
                      url: '/role/saveAuthority',
                      data: {
                        roleId: opt.owner.component.getAttribute('uid'),
                        authoritys: str
                      },
                      success: function (response) {
                        layer.close(loading)
                        if (response.isSuccess) {
                          layer.msg('配置权限成功')
                          var dataStr = this.data.split('&')
                          dataStr.map(function (item, i, list) {
                            if (/roleId/.test(item)) {
                              $('tr[uid=' + item.split('=')[1] + ']').find('.dropdownList.setting').remove()
                            }
                          })
                        } else {
                          layer.alert(response.errorMessage, {title: '错误', shadeClose: true})
                        }
                      },
                      error: function (response) {
                        layer.close(loading)
                        layer.alert('错误代码:' + response.status + '，请联系管理员', {title: '错误', shadeClose: true})
                      }
                    })
                  }
                }
              },
              reload: {
                extraClass: window.btnReloadClass,
                events: {
                  mouseenter: function (opt) {
                    opt.btn.tips = layer.tips('刷新权限设置', opt.btn, {
                      tips: [1, '#449d44']
                    })
                  },
                  mouseleave: function (opt) {
                    layer.close(opt.btn.tips)
                  },
                  click: function (opt) {
                    layer.closeAll()
                    opt.panel.refresh()
                  }
                }
              },
              closeBtn: {
                extraClass: ['fa', 'fa-close', 'font-info'],
                events: {
                  mouseenter: function (opt) {
                    opt.btn.tips = layer.tips('关闭权限设置', opt.btn, {
                      tips: [1, '#449d44']
                    })
                  },
                  mouseleave: function (opt) {
                    layer.close(opt.btn.tips)
                  },
                  click: function (opt) {
                    layer.closeAll()
                    $('#' + opt.panel.id).remove()
                  }
                }
              }
            }
          })
        }
      }
    },
    delete: {
      extraClass: ['fa', 'fa-trash', 'btn', 'btn-sm', 'btn-danger', 'btn-space'],
      events: {
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('删除', opt.btn, {
            tips: [1, '#c9302c']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          layer.confirm('你真的要删除“' + opt.tr.cells[opt.panel.table.tHead.querySelector('[name="name"]').getAttribute('index')].innerHTML + '”', {
            btn: ['确定', '取消'] // 按钮
          }, function () {
            $.ajax({
              url: '/role/removeById',
              type: 'get',
              data: {
                id: opt.tr.getAttribute('uid')
              },
              success: function (response) {
                if (response.isSuccess) {
                  layer.msg('删除成功')
                  opt.panel.refresh()
                } else {
                  layer.alert(response.errorMessage, {title: '错误提示', shadeClose: true})
                }
              },
              error: function (response) {
                layer.alert('错误代码:' + response.status + '，请联系管理员', {title: '错误', shadeClose: true})
              }
            })
            layer.closeAll() // 关闭所有层
          }, function () {
            layer.closeAll() // 关闭所有层
          })
        }
      }
    }
  }
  var fields = [
    {
      text: '序号',
      type: 'increment',
      style: 'width:45px;'
    }, {
      text: '名称',
      name: 'name'
    }, {
      text: '角色编码',
      name: 'code'
    }, {
      text: '禁启用',
      name: 'isEnable',
      data: {
        Y: '启用',
        N: '禁用'
      },
      style: 'width:100px;'
    }, {
      text: '排序',
      name: 'sort',
      style: 'width:50px;'
    }, {
      type: 'operations',
      text: '操作',
      style: 'width:160px;'
    }
  ]
  return new GridPanel(setConfig({
    opt: opt,
    fnName: 'roleGrid',
    btns: btns,
    operationBtns: operationBtns,
    classGroup: 'GridPanel',
    config: {
      container: windowCenter,
      url: '/role/queryAllRoleByEnable',
      fields: fields,
      title: {
        innerHTML: '角色管理'
      },
      boxExtraClass: ['col-sm-12'],
      search: {
        fields: [
          {
            name: 'name',
            text: '名称'// placeholder
          },
          {
            name: 'isEnable',
            text: '是否禁用', // placeholder
            type: 'select',
            value: {
              '全部': '',
              '启用': 'Y',
              '禁用': 'N'
            }
          }
        ]
      }
    }
  }))
}
