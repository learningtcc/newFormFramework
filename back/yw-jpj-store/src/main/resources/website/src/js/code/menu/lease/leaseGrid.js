import $ from 'jquery'
import layer from 'static/layer/layer'
import { btnPlusClass, btnReloadClass, windowCenter, setConfig } from 'common/commonClass'
import { getFormatDate } from 'common/method'
import GridPanel from 'component/gridPanel'
import leaseForm from 'code/menu/lease/leaseForm'

export default function leaseGrid (opt) {
  var btns = {
  	plus: {
      extraClass: btnPlusClass,
      events: {
        click: function (opt) {
          opt.panel.items['leaseForm'] = leaseForm({
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
    isRelease: {
      extraClass: ['fa', 'fa-eye', 'btn', 'btn-sm', 'btn-info', 'btn-space'],
      events: {
        mouseenter: function (opt) {
        	let releaseIndex = $(opt.tr).parent().prev().find('[name="isRelease"]').attr('index')
          let isRelease = $(opt.tr).children().eq(releaseIndex).text().indexOf('成功') > -1
          opt.btn.tips = layer.tips(isRelease ? '撤下' : '发布', opt.btn, {
            tips: [1, '#ec971f']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          let releaseIndex = $(opt.tr).parent().prev().find('[name="isRelease"]').attr('index')
          let isRelease = $(opt.tr).children().eq(releaseIndex).text().indexOf('成功') > -1
          $.ajax({
            url: '/cms/lease/setIsPublish',
            type: 'post',
            data: {
              id: opt.tr.getAttribute('uid'),
              status: isRelease ? 'N' : 'Y'
            },
            success: function (response) {
              if (response.isSuccess) {
                layer.msg((isRelease ? '发布' : '撤下') + '成功!')
                opt.panel.refresh()
              } else {
                layer.alert('出错：'+response.errorMessage, function () {})
              }
            },
            error: function (response) {
              layer.alert('出错：'+response.errorMessage, function () {})
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
    }, 
//  {
//    text: '招租商铺',
//    name: 'storeName'
//  }, 
    {
      text: '商铺类型',
      name: 'type',
      type: 'enumeration'
    }, {
      text: '联系方式',
      name: 'contactTel'
    }, {
      text: '发布状态',
      name: 'isRelease',
      data: {
        Y: '发布成功',
        N: '待发布'
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
    fnName: 'leaseGrid',
    btns: btns,
    operationBtns: operationBtns,
    classGroup: 'GridPanel',
    config: {
      container: windowCenter,
      url: '/cms/lease/leasingList',
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
            if (datas[index].isRelease == 'Y') {
              $(item).find('[methodname="isRelease"]').removeClass('fa-eye').addClass('fa-eye-slash')
              $(item).find('[methodname="edit"]').attr('disabled', 'disabled')
            }
            $(item).children().eq($(item).parent().prev().find('[name="releaseTime"]').index()).text(getFormatDate({
              timeStamp: datas[index].releaseTime,
              format: 'yyyy-MM-dd HH:mm:ss'
            }))
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
           },
           hidden: true
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
