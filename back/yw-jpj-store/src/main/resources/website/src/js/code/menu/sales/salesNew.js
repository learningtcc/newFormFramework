import { setConfig } from 'common/commonClass'
import WindowPanel from 'component/windowPanel'
import $ from 'jquery'
import layer from 'static/layer/layer'
export default function salesNew (opt) {
  let json = opt
  var fields = [
    {
      column: [
        {
          label: '选择优惠类型',
          name: 'type',
          required: true,
          tagName: 'select',
          async: {
            url: '/offerVoucher/getCouponType',
            type: 'get',
            data: {
              send: {},
              set: 'value'
            }
          }
        }, {
          label: '优惠条件：订单需满金额（单位：元）',
          name: 'full',
          required: true
        },
        {
          label: '满减金额（单位：元）',
          name: 'less',
          required: true
        },
        {
          label: '折扣力度（单位：折）',
          name: 'discount',
          required: true
        }
      ]
    }, {
      column: [
        {
          label: '优惠开始时间',
          name: 'startTime',
          required: true,
          type: 'time'
        }, {
          label: '结束时间',
          name: 'endTime',
          required: true,
          type: 'time'
        }
      ]
    },
    {
      column: [
        {
          label: '优惠图片（360*540）',
          name: 'imageUrl',
          required: true,
          type: 'img',
          extraClass: {
            boxDiv: ['form-group'],
            label: ['col-sm-4', 'control-label'],
            inputText: ['col-sm-8']
          }
        }
      ]
    }
  ]
  var btns = {
    edit: {
      html: '确认',
      extraClass: ['layui-layer-btn0'],
      events: {
        click: function (opt) {
          console.log(opt.panel.items['salesNew'],'opt.panel')
          if (opt.panel.items['salesNew'].validatesFn({windowBtn: opt.btn})) {
            var loading = layer.load(1, { shade: [0.1, '#fff'] })
            $.ajax({
              url: $(opt.panel.component).find('input[name=id]').length ? '/offerVoucher/addOrUpdate' : '/offerVoucher/addOrUpdate',
              type: 'post',
              data: $('.' + opt.panel.component.className.split(' ')[0]).find('form').serialize(), // +userType,
              success: function (response) {
                if (response.isSuccess) {
                  layer.closeAll()
                  opt.panel.owner.refresh()
                } else {
                  layer.close(loading)
                  layer.msg(response.errorMessage, function () {})
                }
              },
              error: function (response) {
                layer.close(loading)
                layer.alert('错误代码:' + response.status + '，请联系管理员', {title: '错误提示', shadeClose: true})
              }
            })
          }
        }
      }
    },
    closeBtn: {
      html: '关闭',
      extraClass: ['layui-layer-btn0'],
      events: {
        click: function (opt) {
          layer.closeAll()
        }
      }
    },
    reload: {
      html: '重置',
      extraClass: ['layui-layer-btn0'],
      events: {
        click: function (opt) {
          for (var i in opt.panel.items) {
            if (opt.panel.items[i].refresh) { opt.panel.items[i].refresh() }
          }
        }
      }
    }
  }
  return new WindowPanel({
    owner: opt.owner,
    layerConfig: {
      area: '800px',
      title: opt.data.id ? '编辑优惠活动' : '新建优惠活动'
    },
    btns: btns,
    items: [
      setConfig({
        opt: opt,
        fnName: 'salesNew',
        config: {
          xtype: 'FormPanel',
          data: opt.data,
          fields: fields,
          queryUrl: '/offerVoucher/getOfferVoucher',
          queryType: 'post',
          render: (opt) => {
            let typeVal = json.data.user
            switch (typeVal) {
              case 'full_cut':
                $(opt.component).find('input[name="discount"]').attr('validator',true)
                $(opt.component).find('input[name="discount"]').parents('.form-group').hide()
                $(opt.component).find('input[name="full"]').parents('.form-group').show()
                $(opt.component).find('input[name="less"]').parents('.form-group').show()
                break
              case 'full_discount':
                $(opt.component).find('input[name="less"]').attr('validator',true)
                $(opt.component).find('input[name="less"]').parents('.form-group').hide()
                $(opt.component).find('input[name="discount"]').parents('.form-group').show()
                $(opt.component).find('input[name="full"]').parents('.form-group').show()
                break
              case 'discount':
                $(opt.component).find('input[name="less"]').attr('validator',true)
                $(opt.component).find('input[name="full"]').attr('validator',true)
                $(opt.component).find('input[name="full"]').parents('.form-group').hide()
                $(opt.component).find('input[name="less"]').parents('.form-group').hide()
                $(opt.component).find('input[name="discount"]').parents('.form-group').show()
                break
              default:
                $(opt.component).find('input[name="less"]').attr('validator',true)
                $(opt.component).find('input[name="full"]').attr('validator',true)
                $(opt.component).find('input[name="full"]').parents('.form-group').hide()
                $(opt.component).find('input[name="less"]').parents('.form-group').hide()
                $(opt.component).find('input[name="discount"]').parents('.form-group').show()
                break
            }
            $(opt.component).find('select[name="type"]').change(function () {
              console.log($(opt.component).find('input[name="discount"]'), 'datatatata',$(opt.component).find('input[name="discount"]').attr('validator'))
              switch ($(opt.component).find('select[name="type"]').val()) {
                case 'full_cut':
                  $(opt.component).find('input[name="discount"]').attr('validator',true)
                  $(opt.component).find('input[name="discount"]').parents('.form-group').hide()
                  $(opt.component).find('input[name="full"]').parents('.form-group').show()
                  $(opt.component).find('input[name="less"]').parents('.form-group').show()
                  $(opt.component).find('input[name="discount"]').val(' ')
                  break
                case 'full_discount':
                  $(opt.component).find('input[name="less"]').attr('validator',true)
                  $(opt.component).find('input[name="less"]').val(' ')
                  $(opt.component).find('input[name="less"]').parents('.form-group').hide()
                  $(opt.component).find('input[name="discount"]').parents('.form-group').show()
                  $(opt.component).find('input[name="full"]').parents('.form-group').show()
                  break
                case 'discount':
                  $(opt.component).find('input[name="less"]').attr('validator',true)
                  $(opt.component).find('input[name="full"]').attr('validator',true)
                  $(opt.component).find('input[name="full"]').val(' ')
                  $(opt.component).find('input[name="less"]').val(' ')
                  $(opt.component).find('input[name="full"]').parents('.form-group').hide()
                  $(opt.component).find('input[name="less"]').parents('.form-group').hide()
                  $(opt.component).find('input[name="discount"]').parents('.form-group').show()
                  break
                default:
                  $(opt.component).find('input[name="less"]').attr('validator',true)
                  $(opt.component).find('input[name="full"]').attr('validator',true)
                  $(opt.component).find('input[name="full"]').val(' ')
                  $(opt.component).find('input[name="less"]').val(' ')
                  $(opt.component).find('input[name="full"]').parents('.form-group').hide()
                  $(opt.component).find('input[name="less"]').parents('.form-group').hide()
                  $(opt.component).find('input[name="discount"]').parents('.form-group').show()
                  break
              }
            })
          }
        }
      })
    ]
  })
}
