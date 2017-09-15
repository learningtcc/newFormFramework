import { btnPlusClass, btnReloadClass, windowCenter, setConfig } from 'common/commonClass'
import GridPanel from 'component/gridPanel'
import teaForm from 'code/menu/tea/teaForm'
import layer from 'static/layer/layer'
import $ from 'jquery'
export default function teaGrid (opt) {
  var btns = {
    plus: {
      extraClass: btnPlusClass,
      events: {
        click: function (opt) {
          opt.panel.items['teaForm'] = teaForm({
            owner: opt.panel,
            ownerBtn: opt.panel
          })
        },
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('新增活动', opt.btn, {
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
          opt.panel.items['teaForm'] = teaForm({
            owner: opt.panel,
            ownerBtn: opt.panel,
            data: {
              id: opt.tr.getAttribute('uid')
            }
          })
        }
      }
    },
    isEnable: {
      extraClass: ['fa', 'fa-eye', 'btn', 'btn-sm', 'btn-info', 'btn-space'],
      events: {
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('活动禁启用设置', opt.btn, {
            tips: [1, '#5bc0de']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          let enableIndex = $(opt.tr).parent().prev().find('[name="isUsing"]').attr('index')
          let isEnable = $(opt.tr).children().eq(enableIndex).text().indexOf('正') > -1
          $.ajax({
            url: '/cms/teaCulture/setIsUsing',
            type: 'post',
            data: {
              id: opt.tr.getAttribute('uid'),
              status: isEnable ? 'N' : 'Y'
            },
            success: function (response) {
              if (response.isSuccess) {
                layer.msg((isEnable ? '禁用' : '启用') + '成功!')
                opt.panel.refresh()
              } else {
                layer.alert(response.errorMessage, function () {})
              }
            },
            error: function (response) {
              layer.alert(response.errorMessage, function () {})
            }
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
      text: '主题',
      name: 'title'
    }, {
      text: '状态',
      name: 'isUsing',
      data: {
        Y: '正常',
        N: '禁用'
      }
    }, {
      type: 'operations',
      text: '操作',
      style: 'width:210px;'
    }
  ]

  return new GridPanel(setConfig({
    opt: opt,
    fnName: 'culturalGrid',
    btns: btns,
    operationBtns: operationBtns,
    classGroup: 'GridPanel',
    config: {
      container: windowCenter,
      url: '/cms/teaCulture/queryPage',
      type: 'post',
      pageSize: 10,
      pageNo: 1,
      fields: fields,
      title: {
        innerHTML: '茶文化活动管理'
      },
      boxExtraClass: ['col-sm-12'],
      render: (opt) => {
        let datas = opt.response.data.root
        if (datas && datas.length) {
          $(opt.table).find('tbody').children().map((index, item) => {
            if (datas[index].isUsing !== 'Y') {
              $(item).find('[methodname="isEnable"]').removeClass('fa-eye').addClass('fa-eye-slash')
            }
          })
        }
      },
      search: {
        fields: [
          {
            name: 'title',
            text: '活动标题'// placeholder
          },
          {
            name: 'startTime',
            text: '发布时间起始日期',
            type: 'date'
          },
          {
            name: 'endTime',
            text: '结束日期',
            type: 'date'
          }
        ]
      }
    }
  }))
}
