import $ from 'jquery'
import layer from 'static/layer/layer'
import { btnEditClass, btnReloadClass, windowCenter, setConfig } from 'common/commonClass'
import FormPanel from 'component/formPanel'

export default function powerForm (opt) {
  var fields = [
    {
      label: '父ID',
      name: 'parentId',
      hidden: true,
      value: opt.parentId ? opt.parentId : 'root'
    }, {
      label: '编码',
      name: 'code',
      required: true,
      readonly: (function () {
        if (opt.data) { return true }
      })(),
      validator: {
        type: ['validateLength'],
        validateRule: {
          min: 1,
          max: 50
        },
        async: {
          url: '/authority/checkCode',
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
      label: '名称',
      name: 'name'
    }, {
      label: '类型 ',
      name: 'type',
      required: true,
      type: 'radio',
      option: [
        {
          html: '菜单',
          value: '1'
        },
        {
          html: '按钮',
          value: '2'
        }
      ]
    }, {
      label: 'url',
      required: true,
      name: 'url'
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
      name: 'description',
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
  var btns = {
    edit: {
      extraClass: btnEditClass,
      events: {
        click: function (opt) {
          var loading = layer.load(1, { shade: [0.1, '#fff'] })
          $.ajax({
            url: $(opt.component).find('input[name=id]').length ? '/authority/update' : '/authority/save',
            type: 'post',
            data: $('.' + opt.component.className.split(' ')[0]).serialize(),
            success: function (response) {
              if (response.isSuccess) {
                layer.closeAll()
                opt.panel.owner.partialRefresh({
                  uid: opt.panel.formContainer.querySelector('[name="parentId"]').value
                })
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
        }
      }
    }
  }
  return new FormPanel(setConfig({
    opt: opt,
    fnName: 'powerForm',
    classGroup: 'FormPanel',
    btns: btns,
    config: {
      container: windowCenter,
      data: opt.data,
      fields: fields,
      queryUrl: '/authority/queryById',
      queryType: 'get',
      extraClass: ['ibox'],
      defineBtnsExtraClass: ['ibox-tools'],
      boxExtraClass: ['col-sm-7'],
      formExtraClass: ['ibox-content', 'form-horizontal']
    }
  }))
}
