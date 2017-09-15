import $ from 'jquery'
import layer from 'static/layer/layer'
import { btnPlusClass, btnReloadClass, treeClass, windowCenter, setConfig } from 'common/commonClass'
import { Loading as commonLoading } from 'common/method'
import GridPanel from 'component/gridPanel'
import TreePanel from 'component/treePanel'
import userForm from 'code/menu/user/user/userForm'

export default function userGrid (opt) {
  var btns = {
    plus: {
      extraClass: btnPlusClass,
      events: {
        click: function (opt) {
          opt.panel.items['userForm'] = userForm({
            owner: opt.panel,
            ownerBtn: opt.panel
          })
        },
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('新增用户', opt.btn, {
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
    resetPassword: {
      extraClass: ['fa', 'fa-key', 'btn', 'btn-sm', 'btn-primary', 'btn-space'],
      events: {
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('重置密码', opt.btn, {
            tips: [1, '#286090']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          layer.confirm('确认重置密码?', {
            btn: ['确定', '取消'] // 按钮
          }, function () {
            var thisId = $(opt.tr).attr('uid')
            // var thisName = $(opt.tr).attr('name')
            var loading = layer.load(1, { shade: [0.1, '#fff'] })
            $.ajax({
              type: 'get',
              url: '/user/resetPassWord',
              data: {
                id: thisId
              },
              success: function (response) {
                if (response.isSuccess) {
                  layer.alert('新密码已发送至邮箱', {title: '密码重置成功', shadeClose: true})
                } else {
                  layer.msg(response.errorMessage, function () {})
                }
                layer.close(loading)
              },
              error: function (response) {
                layer.close(loading)
                layer.alert('错误代码:' + response.status + '，请联系管理员', {title: '错误提示', shadeClose: true})
              }
            })
            layer.closeAll() // 关闭所有层
          }, function () {
            layer.closeAll() // 关闭所有层
          })
        }
      }
    },
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
          opt.panel.items['userForm'] = userForm({
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
          opt.btn.tips = layer.tips('设置角色', opt.btn, {
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
              innerHTML: '设置角色'
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
            url: '/role/queryAllRoleByEnable',
            noBtns: true,
            render: function (opt) {
              opt.panel.defineBtns.style.display = opt.panel.root.style.display = 'none'
              commonLoading.on(opt.component)
              $.ajax({
                url: '/user/queryRole',
                data: {
                  userId: opt.owner.component.getAttribute('uid'),
                  isEnable: 'Y'
                },
                success: function (response) {
                  if (response.data && response.data.length) {
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
                extraClass: ['fa', 'fa-check-square-o', 'font-info'],
                events: {
                  mouseenter: function (opt) {
                    opt.btn.tips = layer.tips('提交配置角色', opt.btn, {
                      tips: [1, '#449d44'],
                      time: 0
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
                      url: '/user/saveRole',
                      data: {
                        userId: opt.owner.component.getAttribute('uid'),
                        roleIds: str
                      },
                      success: function (response) {
                        layer.close(loading)
                        if (response.isSuccess) {
                          layer.msg('配置角色成功')
                          var dataStr = this.data.split('&')
                          dataStr.map(function (item, i, list) {
                            if (/userId/.test(item)) {
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
                    opt.btn.tips = layer.tips('刷新配置角色', opt.btn, {
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
                    opt.btn.tips = layer.tips('关闭配置角色', opt.btn, {
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
              url: '/user/removeById',
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
      text: '用户名',
      name: 'userName'
    }, {
      text: '姓名',
      name: 'name'
    }, {
      text: '邮箱',
      name: 'mail'
    }, {
      text: '性别',
      name: 'sex',
      data: {
        1: '男',
        2: '女'
      }
    }, {
      text: '状态',
      name: 'isLock',
      data: {
        N: '正常',
        Y: '锁定'
      }
    }, {
      type: 'operations',
      text: '操作',
      style: 'width:160px;'
    }
  ]
  return new GridPanel(setConfig({
    opt: opt,
    fnName: 'userGrid',
    btns: btns,
    operationBtns: operationBtns,
    classGroup: 'GridPanel',
    config: {
      container: windowCenter,
      url: '/user/queryUserList',
      fields: fields,
      pageSize: 10,
      pageNo: 1,
      title: {
        innerHTML: '用户管理'
      },
      boxExtraClass: ['col-sm-12'],
      clearOperationBtns: {
        isAdmin: 'Y'
      },
      search: {
        fields: [
          {
            name: 'name',
            text: '姓名'// placeholder
          },
          {
            name: 'isLock',
            text: '状态', // placeholder
            type: 'select',
            value: {
              '全部': '',
              '锁定': 'Y',
              '正常': 'N'
            }
          }
        ]
      }
    }
  }))
}
