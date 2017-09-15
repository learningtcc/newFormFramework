import $ from 'jquery'
import layer from 'static/layer/layer'
import { setConfig } from 'common/commonClass'
import WindowPanel from 'component/windowPanel'

export default function interactForm (opt) {
  var fields = [
    {
      label: '主题名称',
      required: true,
      name: 'themeName',
      validator: {
        type: ['validateLength'],
        validateRule: {
          min: 1,
          max: 50
        }
      }
    }, {
      label: '主题图片',
      name: 'themePic',
      required: true,
      type: 'img'
    }, {
      label: '序号',
      name: 'number',
      required: true
    }
  ]
  var btns = {
    edit: {
      html: '确认',
      extraClass: ['layui-layer-btn0'],
      events: {
        click: function (opt) {
          if (opt.panel.items['interactForm'].validatesFn({windowBtn: opt.btn})) {
            var loading = layer.load(1, { shade: [0.1, '#fff'] })
            $.ajax({
              url: $(opt.panel.component).find('input[name=id]').length ? '/interactive/updateInteractiveTheme' : '/interactive/addInteractiveTheme',
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
      title: opt.data ? '编辑互动主题' : '新建互动主题'
    },
    btns: btns,
    items: [
      setConfig({
        opt: opt,
        fnName: 'interactForm',
        config: {
          xtype: 'FormPanel',
          data: opt.data,
          fields: fields,
          queryUrl: '/interactive/getInteractiveTheme',
          queryType: 'post'
        }
      })
    ]
  })
}
