import { btnPlusClass, btnReloadClass,windowCenter, setConfig } from 'common/commonClass'
import GridPanel from 'component/gridPanel'
import salesForm from 'code/menu/sales/salesForm'
import salesNew from 'code/menu/sales/salesNew'
import layer from 'static/layer/layer'
// import salesNew from 'code/menu/sales/salesNew'
export default function culturalGrid (opt) {
  var btns = {
  plus: {
    extraClass: btnPlusClass,
    events: {
      click: function (opt) {
        opt.panel.items['salesNew'] = salesNew({
          owner: opt.panel,
          ownerBtn: opt.panel,
          data:{
            user:''
          }
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
          opt.btn.tips = layer.tips('编辑', opt.btn, {
            tips: [1, '#ec971f']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          opt.panel.items['salesNew'] = salesNew({
            owner: opt.panel,
            ownerBtn: opt.panel,
            data: {
              id: opt.tr.getAttribute('uid'),
              user: opt.panel.data[$(opt.tr).find('td')[0].innerHTML-1].type
            }
          })
        }
      }
    },
    details: {
      extraClass: ['fa', 'fa-file-text-o', 'btn', 'btn-sm', 'btn-success', 'btn-space'],
      events: {
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('详情', opt.btn, {
            tips: [1, '#5cb85c']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          opt.panel.items['salesForm'] = salesForm({
            owner: opt.panel,
            ownerBtn: opt.panel,
            data: {
              id: opt.tr.getAttribute('uid')
            }
          })
        }
      }
    },
    end: {
      extraClass: ['fa', 'fa-circle-o-notch', 'btn', 'btn-sm', 'btn-info', 'btn-space'],
      events: {
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('结束', opt.btn, {
            tips: [1, '#5bc0de']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          layer.confirm('确定结束此条优惠活动？', {
            btn: ['确认', '取消'] // 按钮
          }, function () {
            $.ajax({
              url: '/offerVoucher/over',
              type: 'post',
              data: {
                id: opt.tr.getAttribute('uid')
              },
              success: function (response) {
                if (response.isSuccess) {
                  layer.msg('已经结束')
                  opt.panel.refresh()
                } else {
                  layer.alert(response.message, function () {})
                }
              },
              error: function (response) {
                layer.alert(response.message, function () {})
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
      type: 'increment'
    }, {
      text: '优惠商家',
      name: 'storeName'
    }, {
      text: '优惠',
      name: 'type'
    }, {
      text: '优惠开始时间',
      name: 'startTime'
    }, {
      text: '优惠结束时间',
      name: 'endTime'
    }, {
      text: '优惠状态',
      name: 'offerStatus',
      data: {
        NotBegin: '未开始',
        InHand: '进行中',
        HasEnd: '已结束'
      }
    }, {
      text: '发布时间',
      name: 'publishTime'
    }, {
      type: 'operations',
      text: '操作',
      style: 'width:210px;'
    }
  ]
  return new GridPanel(setConfig({
    opt: opt,
    fnName: 'salesGrid',
    btns: btns,
    operationBtns: operationBtns,
    classGroup: 'GridPanel',
    config: {
      container: windowCenter,
      url: '/offerVoucher/getOfferVoucherList',
      type: 'post',
      pageSize: 10,
      pageNo: 1,
      fields: fields,
      title: {
        innerHTML: '优惠活动管理'
      },
      boxExtraClass: ['col-sm-12'],
			render: (opt) => {
        let datas = opt.response.data.root
        if (datas && datas.length) {
          $(opt.table).find('tbody').children().map((index, item) => {
            if (datas[index].offerStatus === 'InHand') {
              $(item).find('[methodname="edit"]').attr('disabled', 'disabled')
            }
            if (datas[index].offerStatus !== 'InHand') {
              $(item).find('[methodname="end"]').hide()
            }
          })
        }
				var $tr = $(opt.table).find('tbody tr')
				if (opt.response.data && opt.response.data.root && opt.response.data.root.length) {
					opt.response.data.root.map((item, index) => {
						switch (item.type) {
							case 'full_cut':
								$tr.eq(index).children().eq($tr.parent().prev().find('tr').find('[name="type"]').index()).text('满' + item.full + '元减'+item.less+'元')
								break;
							case 'full_discount':
								$tr.eq(index).children().eq($tr.parent().prev().find('tr').find('[name="type"]').index()).text('满' + item.full + '元打'+item.discount+'折')
								break;
							case 'discount':
								$tr.eq(index).children().eq($tr.parent().prev().find('tr').find('[name="type"]').index()).text(item.discount+'折')
								break;
							default:
						}
					})
				}
			},
      search: {
        fields: [
          {
            name: 'storeName',
            text: '优惠商家'// placeholder
          },
          {
            name: 'type',
            type: 'select',
            text: '优惠类型',
            async: {
              url: '/offerVoucher/getCouponType',
              type: 'get',
              data: {
                send: {},
                set: 'value'
              }
            }
          },
          {
            name: 'offerStatus',
            text: '优惠状态', // placeholder
            type: 'select',
            value: {
              '优惠状态': '',
              '未开始': 'NotBegin',
              '进行中': 'InHand',
              '已结束': 'HasEnd'
            }
          },
          {
            name: 'startTime',
            type: 'time',
            text: '发布时间起始日期'

          },
          {
            name: 'endTime',
            type: 'time',
            text: '发布时间结束日期'
          }
        ]
      }
    }
  }))
}
