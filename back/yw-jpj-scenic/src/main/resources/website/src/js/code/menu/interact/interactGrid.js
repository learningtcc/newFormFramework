import $ from 'jquery'
import layer from 'static/layer/layer'
import { btnPlusClass, btnReloadClass, windowCenter, setConfig } from 'common/commonClass'
import { checkActive, getFormatDate } from 'common/method'
import GridPanel from 'component/gridPanel'
import interactForm from 'code/menu/interact/interactForm'
import interactChildGrid from 'code/menu/interact/interactChildGrid'

export default function interactGrid (opt) {
  var btns = {
    plus: {
      extraClass: btnPlusClass,
      events: {
        click: function (opt) {
          opt.panel.items['interactForm'] = interactForm({
            owner: opt.panel,
            ownerBtn: opt.panel
          })
        },
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('新增互动主题', opt.btn, {
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
          opt.panel.clearAllItem()
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
    detail: {
      extraClass: ['fa', 'fa-eye', 'btn', 'btn-sm', 'btn-success', 'btn-space'],
      events: {
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('该互动主题下的互动内容', opt.btn, {
            tips: [1, '#5cb85c']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          checkActive({
            table: opt.panel.table,
            tr: opt.tr,
            backgroundColor: '#286090'
          })
          opt.panel.clearAllItem()
          opt.panel.hideOtherRows({
            tr: opt.tr
          })
          opt.panel.items['interactChildGrid'] = interactChildGrid({
            owner: opt.panel,
            ownerBtn: opt.panel,
            data: {
              id: opt.tr.getAttribute('uid'),
              title: $(opt.tr).children().eq($(opt.tr).parent().prev().find('[name="themeName"]').attr('index')).text()
            }
          })
        }
      }
    },
    edit: {
      extraClass: ['fa', 'fa-edit', 'btn', 'btn-sm', 'btn-warning', 'btn-space'],
      events: {
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('详情编辑', opt.btn, {
            tips: [1, '#ec971f']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          opt.panel.items['interactForm'] = interactForm({
            owner: opt.panel,
            ownerBtn: opt.panel,
            data: {
              id: opt.tr.getAttribute('uid')
            }
          })
        }
      }
    },
    delete: {
      extraClass: ['fa', 'fa-trash', 'btn', 'btn-sm', 'btn-danger', 'btn-space'],
      events: {
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('删除互动主题', opt.btn, {
            tips: [1, '#c9302c']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          layer.confirm('确定删除此条互动主题？', {
            btn: ['删除', '取消'] // 按钮
          }, function () {
            $.ajax({
              url: '/interactive/deleteInteractiveTheme',
              type: 'post',
              data: {
                id: opt.tr.getAttribute('uid')
              },
              success: function (response) {
                if (response.isSuccess) {
                  layer.msg('删除成功')
                  opt.panel.refresh({
                    delete: true
                  })
                } else {
                  layer.alert(response.errorMessage, function () {})
                }
              },
              error: function (response) {
                layer.alert(response.errorMessage, function () {})
              }
            })
          }, function () {})
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
      text: '互动主题',
      name: 'themeName'
    }, {
      text: '发布时间',
      name: 'createTime'
    }, {
      text: '序列号',
      name: 'number'
    }, {
      type: 'operations',
      text: '操作',
      style: 'width:210px;'
    }
  ]

  return new GridPanel(setConfig({
    opt: opt,
    fnName: 'interactGrid',
    btns: btns,
    operationBtns: operationBtns,
    classGroup: 'GridPanel',
    config: {
      container: windowCenter,
      url: '/interactive/getInteractiveThemeList',
      type: 'post',
      pageSize: 10,
      pageNo: 1,
      fields: fields,
      title: {
        innerHTML: '互动主题管理'
      },
      boxExtraClass: ['col-sm-12'],
      render: (opt) => {
        var $tr = $(opt.table).find('tbody tr')
        if (opt.response.data && opt.response.data.root && opt.response.data.root.length) {
          opt.response.data.root.map((item, index) => {
            $tr.eq(index).children().eq($tr.parent().prev().find('tr').find('[name="createTime"]').index()).text(getFormatDate({
              timeStamp: item.createTime,
              format: 'yyyy-MM-dd HH:mm:ss'
            }))
          })
        }
      }
    }
  }))
}
