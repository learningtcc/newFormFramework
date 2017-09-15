import $ from 'jquery'
import layer from 'static/layer/layer'
import { btnEditClass, btnReloadClass, windowCenter, setConfig } from 'common/commonClass'
import FormPanel from 'component/formPanel'

export default function businessForm (opt) {
  var fields = [
    {
      column: [
        {
          label: '店铺状态',
          name: 'isRelease',
          type: 'radio',
          readonly: true,
          option: [
            {
              html: '发布',
              value: 'Y'
            },
            {
              html: '暂不发布',
              value: 'N'
            }
          ]
        },
        {
          label: '店铺名称',
          name: 'name'
        },
        {
          label: '店铺类型',
          name: 'type'
        }
      ],
      extraClass: {
        boxDiv: ['col-sm-4']
      }
    },
    {
      column: [
        {
          label: '店铺图片/头像',
          name: 'themePic',
          type: 'img'
        },
        {
          label: '面积',
          name: 'area'
        },
        {
          label: '运费',
          name: 'fee'
        }
      ],
      extraClass: {
        boxDiv: ['col-sm-4']
      }
    },
    {
      column: [
        {
          label: '客服电话',
          name: 'serviceTel'
        },
        {
          label: '联系人姓名',
          name: 'contactPerson'
        },
        {
          label: '联系人电话',
          name: 'contactPhone',
        }
      ],
      extraClass: {
        boxDiv: ['col-sm-4']
      }
    },
    {
      column: [
        {
          label: '地址',
          name: 'address',
        },
        {
          label: '地址经度',
          name: 'longitude',
          events: {
            blur: function (opt) {
              let longitude = $(opt.component).find('input[name="longitude"]').val() ? $(opt.component).find('input[name="longitude"]').val() : 116.404
              let latitude = $(opt.component).find('input[name="dimension"]').val() ? $(opt.component).find('input[name="dimension"]').val() : 39.915
              opt.this.MAP.clearOverlays()
            	let point = new opt.this.BMAP.Point(longitude, latitude);
              opt.this.MAP.centerAndZoom(point, 16);
              let marker = new opt.this.BMAP.Marker(point);
              opt.this.MAP.addOverlay(marker);
            }
          }
        },
        {
          label: '地址纬度',
          name: 'dimension',
          events: {
            blur: function (opt) {
              let longitude = $(opt.component).find('input[name="longitude"]').val() ? $(opt.component).find('input[name="longitude"]').val() : 116.404
              let latitude = $(opt.component).find('input[name="dimension"]').val() ? $(opt.component).find('input[name="dimension"]').val() : 39.915
              opt.this.MAP.clearOverlays()

            	let point = new opt.this.BMAP.Point(longitude, latitude);
              opt.this.MAP.centerAndZoom(point, 16);
              let marker = new opt.this.BMAP.Marker(point);
              opt.this.MAP.addOverlay(marker);
            }
          }
        }
      ],
      extraClass: {
        boxDiv: ['col-sm-4']
      }
    },
    {
      column: [
        {
          label: '地图',
          name: '',
          type: 'map',
          location: {
            longitude: 'longitude',
            latitude: 'dimension'// 地图中心点取得是同一接口中的经纬度字段，此处设置接口经纬度字段至地图
          },
          area: {
            width: '120%', // 字符串类型
            height: '360px'
          },
          myEvent (opt) {
            $(opt.component).find('input[name="longitude"]').val(opt.e.point.lng)
            $(opt.component).find('input[name="dimension"]').val(opt.e.point.lat)
            // alert(opt.e.point.lng + "," + opt.e.point.lat);
          },
          extraClass: {
            boxDiv: ['form-group'],
            label: ['col-sm-1', 'control-label'],
            inputText: ['col-sm-11']
          }
        }
      ]
    },
    {
      column: [
        {
          label: '微信/公众号二维码',
          name: 'wxUrl',
          type: 'img'
        },
        {
          label: '收款二维码(微信)',
          name: 'weixinUrl',
          type: 'img'
        },
        {
          label: '收款二维码(支付宝)',
          name: 'alipayUrl',
          type: 'img'
        }
      ],
      extraClass: {
        boxDiv: ['col-sm-4']
      }
    },
    {
      column: [
        {
          label: '银行收款',
          name: 'bankAddress'
        },
        {
          label: '银行卡号',
          name: 'bankCard'
        },
        {
          label: '持卡人',
          name: 'payee'
        }
      ],
      extraClass: {
        boxDiv: ['col-sm-4']
      }
    },
    {
      column: [
        {
          label: '店铺图集',
          name: 'pics',
          type: 'multigraph',
          extraClass: {
            boxDiv: ['form-group'],
            label: ['col-sm-1', 'control-label'],
            inputText: ['col-sm-11']
          }
        }
      ]
    },
    {
      column: [
        {
          label: '店铺介绍',
          name: 'description',
          type: 'richTextEditor',
          area: {
            width: '122%', // 字符串类型
            height: '360px'
          },
          extraClass: {
            boxDiv: ['form-group'],
            label: ['col-sm-1', 'control-label'],
            inputText: ['col-sm-11']
          }
        }
      ]
    }
  ]
  var btns = {
    edit: {
      extraClass: btnEditClass,
      events: {
        click: function (opt) {
          var loading = layer.load(1, { shade: [0.1, '#fff'] })
          $.ajax({
            url: '/store/update',
            type: 'post',
            data: $('.' + opt.component.className.split(' ')[0]).serialize(),
            success: function (response) {
              if (response.isSuccess) {
                layer.closeAll()
                opt.panel.refresh()
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
    },
    reload: {
      extraClass: btnReloadClass,
      events: {
        click: function (opt) {
          opt.panel.refresh()
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
  return new FormPanel(setConfig({
    opt: opt,
    fnName: 'businessForm',
    classGroup: 'FormPanel',
    btns: btns,
    config: {
    	title: {
    		innerHTML: '商家信息'
    	},
      data: opt.data,
      fields: fields,
      queryUrl: '/store/queryDetail',
      queryType: 'get',
      mustQuery: true,
      extraClass: ['ibox'],
      defineBtnsExtraClass: ['ibox-tools'],
      boxExtraClass: ['col-sm-12'],
      formExtraClass: ['ibox-content', 'form-horizontal'],
      container: windowCenter
    }
  }))
}
