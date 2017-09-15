import $ from 'jquery'
import layer from 'static/layer/layer'
import { btnPlusClass, btnReloadClass, windowCenter, setConfig } from 'common/commonClass'
import TreePanel from 'component/treePanel'
import dictForm from 'code/menu/dict/dictForm'

export default function dictTree (opt) {
  var btns = {
    plus: {
      extraClass: btnPlusClass,
      events: {
        click: function (opt) {
          opt.panel.items['dictForm'] = dictForm({
            owner: opt.panel,
            ownerBtn: opt.panel,
            parentId: (function () {
              if (opt.panel.component.getAttribute('uid') && opt.panel.component.getAttribute('uid') !== 'uid') {
                return opt.panel.component.getAttribute('uid')
              }
            })(),
            title: {
              innerHTML: (function () {
                if (opt.panel.component.getAttribute('uid') && opt.panel.component.getAttribute('uid') !== 'uid') { return '在 [ ' + opt.panel.component.querySelector('[uid="' + opt.panel.component.getAttribute('uid') + '"]').innerHTML + ' ] 下新增字典' } else { return '新增字典' }
              })()
            }
          })
        },
        mouseenter: function (opt) {
          var target = ''
          if (opt.panel.component.getAttribute('uid') && opt.panel.component.getAttribute('uid') !== 'uid') {
            target = '在  [ ' + opt.panel.root.querySelector('[uid="' + opt.panel.component.getAttribute('uid') + '"]').innerHTML + ' ] 下'
          }
          opt.btn.tips = layer.tips(target + '新增字典', opt.btn, {
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
  return new TreePanel(setConfig({
    opt: opt,
    fnName: 'dictTree',
    btns: btns,
    classGroup: 'TreePanel',
    config: {
      url: '/dict/queryByParentId',
      type: 'post',
      title: {
        innerHTML: '字典结构树'
      },
      container: windowCenter,
      notLeaf: {
        url: '/dict/queryByParentId',
        type: 'post',
        data: {
          parentId: 'uid'
        }
      },
      treeEvents: {
        click: function (opt) {
          opt.panel.items['dictForm'] = dictForm({
            owner: opt.panel,
            ownerBtn: opt.panel,
            title: {
              innerHTML: opt.btn.innerHTML
            },
            parentId: opt.btn.getAttribute('parentId'),
            data: {
              id: opt.btn.getAttribute('uid')
            }
          })
        }
      }
    }
  }))
}
