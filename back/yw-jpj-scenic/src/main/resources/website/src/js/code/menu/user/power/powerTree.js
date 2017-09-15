import $ from 'jquery'
import layer from 'static/layer/layer'
import { btnPlusClass, btnMinusClass, btnReloadClass, windowCenter, setConfig } from 'common/commonClass'
import TreePanel from 'component/treePanel'
import powerForm from 'code/menu/user/power/powerForm'

export default function powerTree (opt) {
  var btns = {
    plus: {
      extraClass: btnPlusClass,
      events: {
        click: function (opt) {
          opt.panel.items['powerForm'] = powerForm({
            owner: opt.panel,
            ownerBtn: opt.panel,
            parentId: (function () {
              if (opt.panel.component.getAttribute('uid') && opt.panel.component.getAttribute('uid') !== 'uid') { return opt.panel.component.getAttribute('uid') }
            })(),
            title: {
              innerHTML: (function () {
                if (opt.panel.component.getAttribute('uid') && opt.panel.component.getAttribute('uid') !== 'uid') { return '在 [ ' + opt.panel.component.querySelector('[uid="' + opt.panel.component.getAttribute('uid') + '"]').innerHTML + ' ] 下新增权限' } else { return '新增权限' }
              })()
            }
          })
        },
        mouseenter: function (opt) {
          var target = ''
          if (opt.panel.component.getAttribute('uid') && opt.panel.component.getAttribute('uid') !== 'uid') {
            target = '在  [ ' + opt.panel.root.querySelector('[uid="' + opt.panel.component.getAttribute('uid') + '"]').innerHTML + ' ] 下'
          }
          opt.btn.tips = layer.tips(target + '新增权限', opt.btn, {
            tips: [1, '#286090']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        }
      }
    },
    minus: {
      extraClass: btnMinusClass,
      events: {
        click: function (opt) {
          if (opt.panel.component.getAttribute('uid') && opt.panel.component.getAttribute('uid') !== 'uid') {
// 询问框
            layer.confirm('你真的要删除“' + opt.panel.root.querySelector('[uid="' + opt.panel.component.getAttribute('uid') + '"]').innerHTML + '”', {
              btn: ['确定', '取消'] // 按钮
            }, function () {
              $.ajax({
                url: '/authority/removeById',
                data: {
                  id: opt.panel.component.getAttribute('uid')
                },
                success: function (response) {
                  if (response.isSuccess) {
                    layer.msg('删除成功')
                    setTimeout(function () { window.location.reload() }, 1000)
                  } else {
                    layer.msg(response.errorMessage, function () {})
                  }
                },
                error: function (response) {
                  layer.alert('错误代码:' + response.status + '，请联系管理员', {title: '错误提示', shadeClose: true})
                }
              })
            }, function () {
              layer.closeAll() // 关闭所有层
            })
          } else {
            layer.msg('请先选择列表中的一项', function () {})
          }
        },
        mouseenter: function (opt) {
          var target = ''
          if (opt.panel.component.getAttribute('uid') && opt.panel.component.getAttribute('uid') !== 'uid') {
            target = '删除  [ ' + opt.panel.root.querySelector('[uid="' + opt.panel.component.getAttribute('uid') + '"]').innerHTML + ' ] 及下级的权限'
          }
          opt.btn.tips = layer.tips(target + '删除权限', opt.btn, {
            tips: [1, '#5bc0de']
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
  return new TreePanel(setConfig({
    opt: opt,
    fnName: 'powerTree',
    btns: btns,
    classGroup: 'TreePanel',
    config: {
      container: windowCenter,
      treeEvents: {
        click: function (opt) {
          opt.panel.items['powerForm'] = powerForm({
            owner: opt.panel,
            ownerBtn: opt.panel,
            parentId: opt.btn.getAttribute('parentId'),
            data: {
              id: opt.btn.getAttribute('uid')
            },
            title: {
              innerHTML: '编辑 [' + opt.panel.component.querySelector('[uid="' + opt.btn.getAttribute('uid') + '"]').innerHTML + ' ] 内容'
            }
          })
        }
      },
      btns: {
        edit: true,
        reload: true
      },
      url: '/authority/queryByParentId',
      type: 'get',
      title: {
        innerHTML: '权限管理'
      },
      notLeaf: {
        url: '/authority/queryByParentId',
        type: 'get',
        data: {
          parentId: 'uid'
        }
      }
    }
  }))
}
