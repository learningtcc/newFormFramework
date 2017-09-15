import $ from 'jquery'
import layer from 'static/layer/layer'
import { setConfig } from 'common/commonClass'
import WindowPanel from 'component/windowPanel'

export default function memberForm (opt) {
  var fields = [
    {
      column: [
        {
          label: '头像',
          readonly: true,
          name: 'headImageUrl',
          type: 'img',
          value: 'headImageUrl'
        }, {
          label: '昵称',
          name: 'nickName',
          readonly: true
        }, {
          label: '性别',
          name: 'sex',
          readonly: true
        }
      ]
    },
    {
      column: [
        // {
        //   label: '联系方式',
        //   readonly:true,
        //   type:'',
        //   name: '',
        // },
        {
          label: '所在地',
          name: 'city',
          readonly: true
        }, {
          label: '关注时间',
          name: 'subscribeTime',
          readonly: true
        }
      ]
    }
  ]
  return new WindowPanel({
    owner: opt.owner,
    layerConfig: {
      area: '600px',
      title: '会员详情'
    },
    btns: {
      closeBtn: {
        html: '返回',
        extraClass: ['layui-layer-btn0'],
        events: {
          click: function (opt) {
            layer.closeAll()
          }
        }
      }
    },
    items: [
      setConfig({
        opt: opt,
        fnName: 'memberForm',
        config: {
          xtype: 'FormPanel',
          data: opt.data,
          fields: fields,
          queryUrl: '/cms/member/getMember',
          queryType: 'post',
          mustQuery: true,
          render: (opt) => {
            let datas = opt.response.data
            switch (datas.sex) {
              case '0':$(opt.component).find('input[name="sex"]').val('未知')
                break
              case '1':$(opt.component).find('input[name="sex"]').val('男')
                break
              case '2':$(opt.component).find('input[name="sex"]').val('女')
                break
              default:
            }
          }
        }
      })
    ]
  })
}
