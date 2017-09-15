import { setConfig } from 'common/commonClass'
import WindowPanel from 'component/windowPanel'
import $ from 'jquery'
import layer from 'static/layer/layer'

export default function themeForm (opt) {
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
          label: '活动类型',
          name: 'type',
          required: true,
          tagName: 'select',
          type: 'enumeration' // 使用已有的枚举类型,不再异步获取
          // async: {
          //   url: 'dict/queryByCode',
          //   data: {
          //     send: {
          //       code: 'activity_type'
          //     },
          //     set: 'code'
          //   }
          // }
        }, {
          label: '活动地址',
          name: 'address',
          required: true
        }
      ],
      extraClass: {
        boxDiv: ['col-sm-4']
      }
    }, {
      column: [
        {
          label: '主图(450*360)',
          name: 'themePic',
          required: true,
          type: 'img'
        }, {
          label: '活动开始时间',
          name: 'startTime',
          required: true,
          type: 'date'
        }, {
          label: '结束时间',
          name: 'endTime',
          required: true,
          type: 'date'
        }
      ],
      extraClass: {
        boxDiv: ['col-sm-4']
      }
    }, {
      column: [
        {
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
          label: '活动详情',
          required: true,
          name: 'explains',
          type: 'richTextEditor',
          area: {
            width: '150%',
            height: '350px'
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
          if (opt.panel.items['themeForm'].validatesFn({windowBtn: opt.btn})) {
            var loading = layer.load(1, { shade: [0.1, '#fff'] })
            // let richText = ''
            // Object.keys(opt.panel.items['themeForm']).map((item, index) => {
            //   if (item.indexOf('field-inputText') > -1 && $('#' + item).attr('type') === 'richTextEditor') {
            //     richText = '&' + $('#' + item).attr('name') + '=' + $('#' + item).find('[contenteditable="true"]').html()
            //   }
            // })
            $.ajax({
              url: $(opt.panel.component).find('input[name=id]').length ? '/thematicActivities/update' : '/thematicActivities/save',
              type: 'post',
              data: $('.' + opt.panel.component.className.split(' ')[0]).find('form').serialize(), // +userType,
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
      title: opt.data ? '编辑主题活动' : '新建主题活动'
    },
    btns: btns,
    items: [
      setConfig({
        opt: opt,
        fnName: 'themeForm',
        config: {
          xtype: 'FormPanel',
          data: opt.data,
          fields: fields,
          queryUrl: '/thematicActivities/queryById',
          queryType: 'post'
        }
      })
    ]
  })
}
