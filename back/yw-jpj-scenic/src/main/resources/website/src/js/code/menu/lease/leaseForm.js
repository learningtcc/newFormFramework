import layer from 'static/layer/layer'
import { setConfig } from 'common/commonClass'
import WindowPanel from 'component/windowPanel'

export default function leaseForm (opt) {
  var fields = [
    {
      column: [
        {
          label: '标题',
          readonly: true,
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
          name: '',
          readonly: true,
          tagName: 'select',
          type: 'enumeration'
          // async: {
          //   url: 'dict/queryByCode',
          //   data: {
          //     send: {
          //       code: 'business_type'
          //     },
          //     set: 'id'
          //   }
          // }
        }, {
          label: '联系方式',
          name: 'contactTel',
          readonly: true
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
          readonly: true
        }, {
          label: '价格(万元/年)',
          name: 'price',
          readonly: true
        }, {
          label: '价格(元/天)',
          name: 'otherPrice',
          readonly: true
        }, {
          label: '地址',
          name: 'address',
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
          readonly: true
        },
        {
          label: '经度',
          name: 'longitude',
          readonly: true
        }, {
          label: '纬度',
          name: 'latitude',
          readonly: true
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
          readonly: true,
          type: 'map',
          location: {
            longitude: 'longitude',
            latitude: 'latitude'// 地图中心点取得是同一接口中的经纬度字段，此处设置接口经纬度字段至地图
          },
          mapArea: {
            width: '100%', // 字符串类型
            height: '360px'
          },
          myEvent (opt) {
            // alert(opt.e.point.lng + "," + opt.e.point.lat);
          },
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
          label: '宣传图',
          name: 'pics',
          type: 'multigraph',
          imgListWidth: '78%',
          readonly: true,
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
          readonly: true,
          area: {
            width: '150%'
          },
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
      title: '编辑招租信息'
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
          queryUrl: '/lease/detail',
          queryType: 'get'
        }
      })
    ]
  })
}
