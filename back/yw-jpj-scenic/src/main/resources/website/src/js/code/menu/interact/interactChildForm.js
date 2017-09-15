import $ from 'jquery'
import layer from 'static/layer/layer'
import { getFormatDate } from 'common/method'
import { setConfig } from 'common/commonClass'
import WindowPanel from 'component/windowPanel'

export default function interactChildForm (opt) {
  var fields = [
    {
      label: '标题',
      readonly: true,
      name: 'interactionTitle',
      validator: {
        type: ['validateLength'],
        validateRule: {
          min: 1,
          max: 50
        }
      }
    }, {
      label: '发布时间',
      name: 'createTime',
      type: 'date',
      readonly: true
    }, {
      label: '内容',
      name: 'interactiveContent',
      readonly: true,
      type: 'richTextEditor'
    }, {
      label: '图片',
      name: 'picList',
      readonly: true,
      type: 'multigraph', // 图集
      imgListWidth: '50%' // 图集宽度占整个弹出层的百分比
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
      title: '查看互动详情'
    },
    btns: btns,
    items: [
      setConfig({
        opt: opt,
        fnName: 'interactChildForm',
        config: {
          xtype: 'FormPanel',
          data: opt.data,
          fields: fields,
          queryUrl: '/interactive/getInteractiveContent',
          queryType: 'post',
          render: opt => {
            $(opt.component).find('input[name="createTime"]').val(getFormatDate({
              timeStamp: parseInt($(opt.component).find('input[name="createTime"]').val()),
              format: 'yyyy-MM-dd HH:mm:ss'
            }))
          }
        }
      })
    ]
  })
}
