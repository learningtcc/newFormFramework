import $ from 'jquery'
import layer from 'static/layer/layer'
import { btnEditClass, btnReloadClass, windowCenter, setConfig } from 'common/commonClass'
import FormPanel from 'component/formPanel'

export default function dictForm (opt) {
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
          url: '/dict/checkUniqueCode',
          type: 'post',
          data: {
            code: 'value'
          }
        }
      }
    }, {
      label: '名称',
      name: 'name',
      validator: {
        type: ['validateLength'],
        validateRule: {
          min: 1,
          max: 50
        }
      }
    }, {
      label: '字典值 ',
      name: 'value',
      validator: {
        type: ['validateLength'],
        validateRule: {
          min: 1,
          max: 50
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
            url: $(opt.component).find('input[name=id]').length ? '/dict/update' : '/dict/save ',
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
        },
        mouseenter: function (opt) {
          $(opt.btn).addClass('fa-spin')
        },
        mouseleave: function (opt) {
          $(opt.btn).removeClass('fa-spin')
        }
      }
    }
  }
  return new FormPanel(setConfig({
    opt: opt,
    fnName: 'dictForm',
    classGroup: 'FormPanel',
    btns: btns,
    config: {
      data: opt.data,
      fields: fields,
      queryUrl: '/dict/queryById',
      queryType: 'get',
      extraClass: ['ibox'],
      defineBtnsExtraClass: ['ibox-tools'],
      boxExtraClass: ['col-sm-7'],
      formExtraClass: ['ibox-content', 'form-horizontal'],
      container: windowCenter
    }
  }))
}
