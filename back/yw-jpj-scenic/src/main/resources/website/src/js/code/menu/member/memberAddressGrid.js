import $ from 'jquery'
import { btnReloadClass, windowCenter, setConfig } from 'common/commonClass'
import GridPanel from 'component/gridPanel'

export default function memberAddressGrid (opt) {
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
  var fields = [
    {
      text: '序号',
      type: 'increment',
      style: 'width:45px;'
    }, {
      text: '收货人',
      name: 'receiver'
    }, {
      text: '联系方式',
      name: 'phone'
    }, {
      text: '收货地址',
      name: 'receipt_add'
    }
  ]

  return new GridPanel(setConfig({
    opt: opt,
    fnName: 'memberAddressGrid',
    btns: btns,
    // operationBtns: operationBtns,
    classGroup: 'GridPanel',
    config: {
      container: windowCenter,
      url: '/cms/member/getMemberAddress',
      type: 'post',
      data: opt.data,
      pageSize: 10,
      pageNo: 1,
      fields: fields,
      title: {
        innerHTML: '会员"' + opt.data.title + '"收货信息'
      },
      boxExtraClass: ['col-sm-12'],
      search: {
        fields: [
          {
            name: 'memberId',
            text: '内容ID', // placeholder
            hidden: true,
            value: opt.data.memberId
          }
        ]
      }
    }
  }))
}
