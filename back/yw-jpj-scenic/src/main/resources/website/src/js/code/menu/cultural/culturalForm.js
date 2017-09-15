import $ from 'jquery'
import layer from 'static/layer/layer'
import { setConfig } from 'common/commonClass'
import WindowPanel from 'component/windowPanel'

export default function culturalForm (opt) {
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
          label: '发布者',
          name: 'announcerId',
          required: true,
          validator: {
            type: ['validateLength'],
            validateRule: {
              min: 1,
              max: 50
            }
          }
        }, {
          label: '主图',
          name: 'imageUrl',
          required: true,
          type: 'img',
          extraClass: {
            boxDiv: ['form-group'],
            label: ['col-sm-4', 'control-label'],
            inputText: ['col-sm-8']
          }
        }
      ],
      extraClass: {
        boxDiv: ['col-sm-4']
      }
    }, {
      column: [
        {
          label: '视频',
          name: 'videoUrl',
          type: 'video'
        }, {
          label: '是否发布',
          name: 'isRelease',
          type: 'radio',
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
        }
      ],
      extraClass: {
        boxDiv: ['col-sm-4']
      }
    }, {
      column: [
        {
          label: '活动编辑',
          name: 'description',
          type: 'richTextEditor',
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
    edit: {
      html: '确认',
      extraClass: ['layui-layer-btn0'],
      events: {
        click: function (opt) {
          if (opt.panel.items['culturalForm'].validatesFn({windowBtn: opt.btn})) {
            var loading = layer.load(1, { shade: [0.1, '#fff'] })
            $.ajax({
              url: $(opt.panel.component).find('input[name=id]').length ? '/cms/street/addCulture' : '/cms/street/addCulture',
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
      title: opt.data ? '编辑文化活动' : '新建文化活动'
    },
    btns: btns,
    items: [
      setConfig({
        opt: opt,
        fnName: 'culturalForm',
        config: {
          xtype: 'FormPanel',
          data: opt.data,
          fields: fields,
          queryUrl: '/cms/street/detail',
          queryType: 'get'
        }
      })
    ]
  })
}
