import layer from 'static/layer/layer'
import { setConfig } from 'common/commonClass'
import WindowPanel from 'component/windowPanel'

export default function leaseForm (opt) {
  var fields = [
    {
      column: [
        {
          label: '标题',
          required: true,
          name: 'title',
          validator: {
            type: ['validateLength'],
            validateRule: {
              min: 1,
              max: 50
            }
          }
        }, {
          label: '商铺类型',
          name: 'type',
          required: true,
          tagName: 'select',
          async: {
            url: 'dict/queryByCode',
            data: {
              send: {
                code: 'business_type'
              },
              set: 'code'
            }
          }
        }, {
          label: '联系方式',
          name: 'contactTel',
          required: true
        }
      ],
      extraClass: {
        boxDiv: ['col-sm-4']
      }
    }, {
      column: [
        {
          label: '面积(㎡)',
          name: 'area',
          required: true
        }, {
          label: '价格(万元/年)',
          name: 'price',
          required: true,
          events:{
          	blur: (opt) => {
          		let price = $(opt.component).find('input[name="price"]').val()
          		if(!isNaN(price)){
          			$(opt.component).find('input[name="dayprice"]').val((price/265).toFixed(2))
          		}else{
          			$(opt.component).find('input[name="dayprice"]').val('')
          		}
          	}
          }
        }, {
          label: '价格(元/天)',
          name: 'dayprice',
          readonly: true
        }
      ],
      extraClass: {
        boxDiv: ['col-sm-4']
      }
    }, {
      column: [
        {
          label: '地址',
          name: 'address',
          required: true
        },
        {
          label: '经度',
          name: 'longitude',
          events: {
            blur: function (opt) {
              let longitude = $(opt.component).find('input[name="longitude"]').val() ? $(opt.component).find('input[name="longitude"]').val() : 116.404
              let latitude = $(opt.component).find('input[name="latitude"]').val() ? $(opt.component).find('input[name="latitude"]').val() : 39.915
              opt.this.MAP.clearOverlays()
            	let point = new opt.this.BMAP.Point(longitude, latitude);
              opt.this.MAP.centerAndZoom(point, 16);
              let marker = new opt.this.BMAP.Marker(point);
              opt.this.MAP.addOverlay(marker);
            }
          }
        }, {
          label: '纬度',
          name: 'latitude',
          events: {
            blur: function (opt) {
              let longitude = $(opt.component).find('input[name="longitude"]').val() ? $(opt.component).find('input[name="longitude"]').val() : 116.404
              let latitude = $(opt.component).find('input[name="latitude"]').val() ? $(opt.component).find('input[name="latitude"]').val() : 39.915
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
    }, {
      column: [
        {
          label: '地图',
          name: '',
          type: 'map',
          location: {
            longitude: 'longitude',
            latitude: 'latitude'// 地图中心点取得是同一接口中的经纬度字段，此处设置接口经纬度字段至地图
          },
          mapArea: {
            width: '120%', // 字符串类型
            height: '360px'
          },
          myEvent (opt) {
            $(opt.component).find('input[name="longitude"]').val(opt.e.point.lng)
            $(opt.component).find('input[name="latitude"]').val(opt.e.point.lat)
            // alert(opt.e.point.lng + "," + opt.e.point.lat);
          },
          extraClass: {
            boxDiv: ['form-group'],
            label: ['col-sm-1', 'control-label'],
            inputText: ['col-sm-11']
          }
        }
      ]
    }, {
      column: [
        {
          label: '宣传图',
          name: 'pics',
          type: 'multigraph',
          imgListWidth: '78%',
          extraClass: {
            boxDiv: ['new-col-width'],
            label: ['new-label-width'],
            inputText: ['bigest-input-width']
          }
        }
      ]
    }, {
      column: [
        {
          label: '商铺介绍',
          name: 'describes',
          type: 'richTextEditor',
          extraClass: {
            boxDiv: ['new-col-width'],
            label: ['new-label-width'],
            inputText: ['bigest-input-width']
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
				click: function(opt) {
					if (opt.panel.items['leaseForm'].validatesFn({windowBtn: opt.btn})) {
            			var loading = layer.load(1, { shade: [0.1, '#fff'] })
//          			let richText = ''
//			            Object.keys(opt.panel.items['leaseForm']).map((item, index) => {
//			              if (item.indexOf('field-inputText') > -1 && $('#' + item).attr('type') === 'richTextEditor') {
//			                richText = '&' + $('#' + item).attr('name') + '=' + $('#' + item).find('[contenteditable="true"]').html()
//			              }
//			            })	
            			$.ajax({
				            url: '/cms/lease/addOrUpdate',
				            type: 'post',
				            data: $('.' + opt.panel.component.className.split(' ')[0]).find('form').serialize(),
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
      area: '1200px',
      title: opt.data ? '编辑招租信息' : '新增招租信息'
    },
    btns: btns,
    items: [
      setConfig({
        opt: opt,
        fnName: 'leaseForm',
        config: {
          xtype: 'FormPanel',
          data: opt.data,
          fields: fields,
          queryUrl: '/cms/lease/detail',
          queryType: 'get'
        }
      })
    ]
  })
}
