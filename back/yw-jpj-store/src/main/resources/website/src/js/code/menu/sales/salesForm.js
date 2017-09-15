import { setConfig } from 'common/commonClass'
import WindowPanel from 'component/windowPanel'
import layer from 'static/layer/layer'
import $ from 'jquery'
export default function salesForm (opt) {
  var fields = [
    {
      column: [
        {
          label: '优惠类型',
          required: true,
          name: 'type',
          readonly: true,
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
          label: '使用条件',
          name: 'full',
          readonly: true
        },
        {
          label: '满减金额',
          name: 'less',
          readonly: true
        },
        {
          label: '折扣力度',
          name: 'discount',
          readonly: true
        },
        {
          label: '优惠图片（360*540）',
          name: 'imageUrl',
          required: true,
          readonly: true,
          type: 'img',
          extraClass: {
            boxDiv: ['form-group'],
            label: ['col-sm-4', 'control-label'],
            inputText: ['col-sm-8']
          }
        }
      ]
    }, {
      column: [
        {
          label: '优惠开始时间',
          name: 'startTime',
          readonly: true,
          type: ''
        },
        {
          label: '优惠结束时间',
          name: 'endTime',
          readonly: true,
          type: ''
        }
      ]
    }
  ]
  var btns = {
    closeBtn: {
      html: '关闭',
      extraClass: ['layui-layer-btn0'],
      events: {
        click: function (opt) {
          layer.closeAll()
        }
      }
    }
  }
  return new WindowPanel({
    owner: opt.owner,
    layerConfig: {
      title: '优惠活动详情'
    },
    btns: btns,
    items: [
      setConfig({
        opt: opt,
        fnName: 'salesForm',
        config: {
          xtype: 'FormPanel',
          data: opt.data,
          fields: fields,
          queryUrl: '/offerVoucher/getOfferVoucher',
          queryType: 'post',
          render: (opt) => {
            let datas = opt.response.data
            if (datas) {
              switch (datas.type) {
                case 'full_cut':
                  $(opt.component).find('input[name="full"]').val('订单满' + datas.full + '元可用')
                  $(opt.component).find('input[name="less"]').val(datas.less + '元')
                  $(opt.component).find('input[name="discount"]').parents('.form-group').hide()
                  break
                case 'full_discount':
                  $(opt.component).find('input[name="full"]').val('订单满' + datas.full + '元可用')
                  $(opt.component).find('input[name="discount"]').val(datas.discount + '折')
                  $(opt.component).find('input[name="less"]').parents('.form-group').hide()
                  break
                case 'discount':
                  $(opt.component).find('input[name="discount"]').val(datas.discount + '折')
                  $(opt.component).find('input[name="full"]').parents('.form-group').hide()
                  $(opt.component).find('input[name="less"]').parents('.form-group').hide()
                  break
                default:
              }
            }
          }
        }
      })
    ]
  })
}
