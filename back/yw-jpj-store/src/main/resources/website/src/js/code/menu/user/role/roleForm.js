import $ from 'jquery'
import layer from 'static/layer/layer'
import { setConfig } from 'common/commonClass'
import WindowPanel from 'component/windowPanel'

export default function roleForm (opt) {
  var fields = [{
    label: '名称',
    required: true,
    name: 'name',
    validator: {
      type: ['validateLength'],
      validateRule: {
        min: 1,
        max: 50
      }
    }
  }, {
    label: '角色编码',
    name: 'code',
    required: true,
    validator: {
      type: ['validateLength'],
      validateRule: {
        min: 1,
        max: 50
      },
      async: {
        url: '/role/checkCode',
        type: 'get',
        data: {
          code: 'value',
          id: (function () {
            if (opt.data && opt.data.id) { return opt.data.id }
          })()
        }
      }
    }
  }, {
    label: '禁启用',
    type: 'radio',
    name: 'isEnable',
    required: true,
    option: [
      {
        html: '启用',
        value: 'Y'
      },
      {
        html: '禁用',
        value: 'N'
      }
    ]
  }, {
    label: '排序',
    required: true,
    name: 'sort',
    validator: {
      type: ['validateNumLength'],
      validateRule: {
        min: 1,
        max: 5
      }
    }
  }, {
    label: '描述',
    name: 'remark',
    tagName: 'textarea',
    cols: 40,
    rows: 5,
    validator: {
      type: ['validateLength'],
      validateRule: {
        min: 1,
        max: 500
      }
    }
  }
  ]
  return new WindowPanel({
    owner: opt.owner,
    layerConfig: {
      area: '500px',
      title: opt.data ? '编辑角色信息' : '新建角色信息'
    },
    btns: {
      edit: {
        html: '确认',
        extraClass: ['layui-layer-btn0'],
        events: {
          click: function (opt) {
            if (opt.panel.items['roleForm'].validatesFn({windowBtn: opt.btn})) {
              var loading = layer.load(1, { shade: [0.1, '#fff'] })
              $.ajax({
                url: $(opt.panel.component).find('input[name=id]').length ? '/role/update' : '/role/save',
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
    },
    items: [
      setConfig({
        opt: opt,
        fnName: 'roleForm',
        config: {
          xtype: 'FormPanel',
          data: opt.data,
          fields: fields,
          queryUrl: '/role/queryById',
          queryType: 'get'
        }
      })
    ]
  })
}
