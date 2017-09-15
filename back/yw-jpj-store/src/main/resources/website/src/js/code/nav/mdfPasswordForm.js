import $ from 'jquery'
import layer from 'static/layer/layer'
import { setConfig } from 'common/commonClass'
import WindowPanel from 'component/windowPanel'

export default function mdfPasswordForm (opt) {
  var btns = {
    edit: {
      html: '确认',
      extraClass: ['layui-layer-btn0'],
      events: {
        click: function (opt) {
          var oldPassWord = $(opt.panel.items.mdfPasswordForm.formContainer).find($('input[name=oldPassWord]'))
          var newPassWord = $(opt.panel.items.mdfPasswordForm.formContainer).find($('input[name=newPassWord]'))
          var newPassWordTwo = $(opt.panel.items.mdfPasswordForm.formContainer).find($('input[name=newPassWordTwo]'))
          $(opt.panel.items.mdfPasswordForm.formContainer).find($('input')).click(function () {
            $(this).css('border', '')
          })
          if (!$(opt.panel.items.mdfPasswordForm.formContainer).find($('input')).val()) {
            $(opt.panel.items.mdfPasswordForm.formContainer).find($('input')).css('border', '1px solid red')
            layer.msg('请填写必填字段', function () {})
            return false
          }
          if (newPassWord.val() !== newPassWordTwo.val()) {
            newPassWord.css('border', '1px solid red')
            newPassWordTwo.css('border', '1px solid red')
            layer.msg('两次输入的密码不一致', function () {})
            return false
          }
          $.ajax({
            type: 'post',
            url: '/user/updatePassWord',
            data: {
              oldPassWord: oldPassWord.val(),
              newPassWord: newPassWord.val()
            },
            success: function (response) {
              if (response.isSuccess) {
                window.location.href = 'login.htm'
              } else {
                layer.msg(response.errorMessage, function () {})
              }
            }
          })
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
    }
  }
  var fields = [
    {
      label: '旧密码',
      name: 'oldPassWord',
      type: 'password',
      required: true,
      validator: {
        type: ['validateLength'],
        validateRule: {
          min: 1,
          max: 20
        }
      }
    }, {
      label: '新密码',
      name: 'newPassWord',
      type: 'password',
      required: true,
      validator: {
        type: ['validateLength'],
        validateRule: {
          min: 1,
          max: 20
        }
      }
    }, {
      label: '密码确认',
      name: 'newPassWordTwo',
      type: 'password',
      required: true,
      validator: {
        type: ['validateLength'],
        validateRule: {
          min: 1,
          max: 20
        }
      }
    }
  ]
/* eslint-disable no-new */
  new WindowPanel({
    owner: opt.owner,
    btns: btns,
    layerConfig: {
      area: '600px',
      title: '修改密码'
    },
    defineBtnsExtraClass: ['layui-layer-btn'],
    items: [
      setConfig({
        opt: opt,
        fnName: 'mdfPasswordForm',
        config: {
          xtype: 'FormPanel',
          fields: fields
        }
      })
    ]
  })
}
