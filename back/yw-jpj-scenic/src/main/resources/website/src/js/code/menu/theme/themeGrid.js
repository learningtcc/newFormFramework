import $ from 'jquery'
import layer from 'static/layer/layer'
import { btnPlusClass, btnReloadClass, windowCenter, setConfig } from 'common/commonClass'
import GridPanel from 'component/gridPanel'
import themeForm from 'code/menu/theme/themeForm'

export default function themeGrid (opt) {
  var btns = {
    plus: {
      extraClass: btnPlusClass,
      events: {
        click: function (opt) {
          opt.panel.items['themeForm'] = themeForm({
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
          opt.panel.items['themeForm'] = themeForm({
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
          opt.btn.tips = layer.tips('置顶热门,只能置顶一个活动上热门!', opt.btn, {
            tips: [1, '#5cb85c']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          $.ajax({
            url: '/thematicActivities/setTop',
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
            tips: [1, '#5bc0de']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          let enableIndex = $(opt.tr).parent().prev().find('[name="isUsing"]').attr('index')
          let isEnable = $(opt.tr).children().eq(enableIndex).text().indexOf('启') > -1
          $.ajax({
            url: '/thematicActivities/setIsUsing',
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
    },
    delete: {
      extraClass: ['fa', 'fa-trash', 'btn', 'btn-sm', 'btn-danger', 'btn-space'],
      events: {
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('删除活动', opt.btn, {
            tips: [1, '#d9534f']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          layer.confirm('确定删除此条主题活动？', {
            btn: ['删除', '取消'] // 按钮
          }, function () {
            $.ajax({
              url: '/thematicActivities/deleteById',
              type: 'post',
              data: {
                id: opt.tr.getAttribute('uid')
              },
              success: function (response) {
                if (response.isSuccess) {
                  layer.msg('删除成功')
                  opt.panel.refresh()
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
      text: '活动标题',
      name: 'title'
    }, {
      text: '活动地址',
      name: 'address'
    }, {
      text: '活动类型',
      name: 'type',
      type: 'enumeration' //使用搜索时调用的枚举接口根据code显示中文
    }, {
      text: '发布状态',
      name: 'isRelease',
      data: {
        Y: '发布成功',
        N: '待发布'
      }
    }, {
      text: '查看人数',
      name: 'clicks'
    }, {
      text: '是否最新',
      name: 'isNewest',
      data: {
        Y: '最新',
        N: '否'
      }
    }, {
      text: '是否启用',
      name: 'isUsing',
      data: {
        Y: '启用',
        N: '禁用'
      }
    }, {
      text: '发布时间',
      name: 'releaseTime'
    }, {
      type: 'operations',
      text: '操作',
      style: 'width:210px;'
    }
  ]

  return new GridPanel(setConfig({
    opt: opt,
    fnName: 'themeGrid',
    btns: btns,
    operationBtns: operationBtns,
    classGroup: 'GridPanel',
    config: {
      container: windowCenter,
      url: '/thematicActivities/queryList',
      type: 'post',
      pageSize: 10,
      pageNo: 1,
      fields: fields,
      title: {
        innerHTML: '主题活动管理'
      },
      boxExtraClass: ['col-sm-12'],
      render: (opt) => {
        let datas = opt.response.data.root
        if (datas && datas.length) {
          $(opt.table).find('tbody').children().map((index, item) => {
            if (datas[index].isTop === 'Y') {
              $(item).find('[methodname="top"]').attr('disabled', 'disabled')
            }
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
            name: 'type',
            text: '活动类型',
            type: 'select',
            async: {
              url: '/dict/queryByCode',
              type: 'post',
              data: {
                send: {
                  code: 'activity_type'
                },
                set: 'value'
              }
            }
            // hidden: true   // 如果有表格表单使用到枚举类型，但是该枚举类型在搜索条件中没有，隐藏掉使用
          },
          {
            name: 'isRelease',
            text: '发布状态',
            type: 'select',
            value: {
              '全部': '',
              '发布成功': 'Y',
              '待发布': 'N'
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
