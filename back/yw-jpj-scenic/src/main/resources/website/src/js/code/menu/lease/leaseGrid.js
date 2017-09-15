import $ from 'jquery'
import layer from 'static/layer/layer'
import { btnReloadClass, windowCenter, setConfig } from 'common/commonClass'
import GridPanel from 'component/gridPanel'
import leaseForm from 'code/menu/lease/leaseForm'

export default function leaseGrid (opt) {
  var btns = {
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
          opt.panel.items['leaseForm'] = leaseForm({
            owner: opt.panel,
            ownerBtn: opt.panel,
            data: {
              id: opt.tr.getAttribute('uid')
            }
          })
        }
      }
    },
    top: {
      extraClass: ['fa', 'fa-hand-pointer-o', 'btn', 'btn-sm', 'btn-success', 'btn-space'],
      events: {
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('置顶热门,只能置顶一个招租上热门!', opt.btn, {
            tips: [1, '#ec971f']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          $.ajax({
            url: '/lease/setTop',
            type: 'post',
            data: {
              id: opt.tr.getAttribute('uid')
            },
            success: function (response) {
              if (response.isSuccess) {
                layer.msg('置顶成功!')
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
    },
    isEnable: {
      extraClass: ['fa', 'fa-eye', 'btn', 'btn-sm', 'btn-info', 'btn-space'],
      events: {
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('活动禁启用设置', opt.btn, {
            tips: [1, '#ec971f']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          let enableIndex = $(opt.tr).parent().prev().find('[name="is_using"]').attr('index')
          let isEnable = $(opt.tr).children().eq(enableIndex).text().indexOf('启') > -1
          $.ajax({
            url: '/lease/setIsUsing',
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
      text: '招租标题',
      name: 'title'
    }, {
      text: '招租商铺',
      name: 'store_name'
    }, {
      text: '商铺类型',
      name: 'type',
      type: 'enumeration'
    }, {
      text: '联系方式',
      name: 'contact_tel'
    }, {
      text: '是否启用',
      name: 'is_using',
      data: {
        Y: '启用',
        N: '禁用'
      }
    }, {
      text: '发布时间',
      name: 'release_time'
    }, {
      type: 'operations',
      text: '操作',
      style: 'width:210px;'
    }
  ]

  return new GridPanel(setConfig({
    opt: opt,
    fnName: 'leaseGrid',
    btns: btns,
    operationBtns: operationBtns,
    classGroup: 'GridPanel',
    config: {
      container: windowCenter,
      url: '/lease/list',
      type: 'get',
      pageSize: 10,
      pageNo: 1,
      fields: fields,
      title: {
        innerHTML: '旺铺招租管理'
      },
      boxExtraClass: ['col-sm-12'],
      render: (opt) => {
        let datas = opt.response.data.root
        if (datas && datas.length) {
          $(opt.table).find('tbody').children().map((index, item) => {
            if (datas[index].is_top === 'Y') {
              $(item).find('[methodname="top"]').attr('disabled', 'disabled')
            }
            if (datas[index].is_using !== 'Y') {
              $(item).find('[methodname="isEnable"]').removeClass('fa-eye').addClass('fa-eye-slash')
            }
          })
        }
      },
      search: {
        fields: [
          {
            name: 'title',
            text: '招租标题'// placeholder
          },
          {
            name: 'store_name',
            text: '招租商铺'
          },
          {
            name: 'type',
            text: '商铺类型',
            type: 'select',
            async: {
              url: '/dict/queryByCode',
              type: 'post',
              data: {
                send: {
                  code: 'business_type'
                },
                set: 'value'
              }
            }
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
