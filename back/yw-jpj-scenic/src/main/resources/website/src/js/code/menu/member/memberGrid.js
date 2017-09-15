import { windowCenter, setConfig } from 'common/commonClass'
import GridPanel from 'component/gridPanel'
import memberForm from 'code/menu/member/memberForm'
import layer from 'static/layer/layer'
import { checkActive } from 'common/method'
import memberAddressGrid from 'code/menu/member/memberAddressGrid'
import $ from 'jquery'
export default function userGrid (opt) {
  var operationBtns = {
    edit: {
      extraClass: ['fa', 'fa-edit', 'btn', 'btn-sm', 'btn-warning', 'btn-space'],
      events: {
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('会员详情', opt.btn, {
            tips: [1, '#286090']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          opt.panel.items['memberForm'] = memberForm({
            owner: opt.panel,
            ownerBtn: opt.panel,
            data: {
              memberId: opt.tr.getAttribute('uid')
            }
          })
        }
      }
    },
    setting: {
      extraClass: ['fa', 'fa-truck', 'btn', 'btn-sm', 'btn-success', 'btn-space'],
      events: {
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('收货地址', opt.btn, {
            tips: [1, '#449d44']
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
          opt.panel.items['memberAddressGrid'] = memberAddressGrid({
            owner: opt.panel,
            ownerBtn: opt.panel,
            data: {
              memberId: opt.tr.getAttribute('uid'),
              title: $(opt.tr).children().eq($(opt.tr).parent().prev().find('[name="nickName"]').attr('index')).text()
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
      text: '会员昵称',
      name: 'nickName'
    },
    //{
    //   text: '头像',
    //   type: 'img',
    //   name: 'headImageUrl'
    // },
     {
      text: '地区',
      name: 'city'
    }, {
      text: '关注时间',
      name: 'subscribeTime'
    }, {
      type: 'operations',
      text: '操作',
      style: 'width:160px;'
    }
  ]
  return new GridPanel(setConfig({
    opt: opt,
    fnName: 'userGrid',
    operationBtns: operationBtns,
    classGroup: 'GridPanel',
    config: {
      container: windowCenter,
      url: '/cms/member/getMemberList',
      type: 'post',
      fields: fields,
      pageSize: 10,
      pageNo: 1,
      title: {
        innerHTML: '会员管理'
      },
      boxExtraClass: ['col-sm-12'],
      clearOperationBtns: {
        isAdmin: 'Y'
      },
      search: {
        fields: [
          {
            name: 'nick_name',
            text: '昵称'// placeholder
          },
          {
            name: 'city',
            text: '城市'// placeholder
          },
          {
            name: 'startTime',
            text: '开始时间', // placeholder
            type: 'time'
          }, {
            name: 'endTime',
            text: '结束时间', // placeholder
            type: 'time'
          }
        ]
      }
    }
  }))
}
