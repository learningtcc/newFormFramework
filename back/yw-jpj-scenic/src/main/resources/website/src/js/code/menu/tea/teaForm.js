import $ from 'jquery'
import layer from 'static/layer/layer'
import { setConfig } from 'common/commonClass'
import WindowPanel from 'component/windowPanel'

export default function teaForm (opt) {
  var fields = [
    {
      column: [
        {
          label: '主题',
          required: true,
          name: 'title',
          extraClass: {
            boxDiv: ['new-col-width'],
            label: ['new-label-width'],
            inputText: ['col-sm-6']
          }
        }, {
          label: '排序',
          name: 'serial',
          required: true,
          extraClass: {
            boxDiv: ['new-col-width'],
            label: ['new-label-width'],
            inputText: ['col-sm-6']
          }
        }
      ]
    }, {
      column: [
        {
          label: '简介',
          name: 'introduction',
          required: true,
          tagName: 'textarea',
          rows: 2,
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
          label: '详情',
          required: true,
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
          if (opt.panel.items['teaForm'].validatesFn({windowBtn: opt.btn})) {
            var loading = layer.load(1, { shade: [0.1, '#fff'] })
            let richText = ''
            Object.keys(opt.panel.items['teaForm']).map((item, index) => {
              if (item.indexOf('field-inputText') > -1 && $('#' + item).attr('type') === 'richTextEditor') {
                richText = '&' + $('#' + item).attr('name') + '=' + $('#' + item).find('[contenteditable="true"]').html()
              }
            })
            $.ajax({
              url: $(opt.panel.component).find('input[name=id]').length ? '/cms/teaCulture/addTeaCulture' : '/cms/teaCulture/addTeaCulture',
              type: 'post',
              data: $('.' + opt.panel.component.className.split(' ')[0]).find('form').serialize() + richText, // +userType,
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
      title: opt.data ? '编辑茶文化活动' : '新建茶文化活动'
    },
    btns: btns,
    items: [
      setConfig({
        opt: opt,
        fnName: 'teaForm',
        config: {
          xtype: 'FormPanel',
          data: opt.data,
          fields: fields,
          queryUrl: '/cms/teaCulture/queryById',
          queryType: 'post'
        }
      })
    ]
  })
}
