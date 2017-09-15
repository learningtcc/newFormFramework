import $ from 'jquery'
import layer from 'static/layer/layer'
import { setConfig } from 'common/commonClass'
import WindowPanel from 'component/windowPanel'

export default function userForm (opt) {
  var fields = [
    {
      name: 'isAdmin',
      hidden: true,
      value: '0'
    }, {
      column: [
        {
          label: '用户名',
          name: 'userName',
          required: true,
          validator: {
            type: ['validateLength', 'validateSpecialRule'],
            validateRule: {
              min: 6,
              max: 50
            },
            async: {
              url: '/user/checkedUserName',
              type: 'get',
              data: {
                userName: 'value'
              }
            }
          }
        }, {
          label: '昵称',
          name: 'nickName'
        }, {
          label: '姓名',
          name: 'name',
          required: true,
          validator: {
            type: ['validateLength'],
            validateRule: {
              min: 1,
              max: 50
            }
          }
        }
      ],
      extraClass: {
        boxDiv: ['col-sm-4']
      }
    }, {
      column: [
        {
          label: '邮箱',
          name: 'mail',
          required: true,
          extraClass: {
            boxDiv: ['col-sm-8', 'new-col-width'],
            label: ['new-label-width'],
            inputText: ['new-input-width']
          },
          validator: {
            type: ['validateEmail']
          }
        }, {
          label: '性别',
          name: 'sex',
          type: 'radio',
          required: true,
          option: [
            {
              html: '男',
              value: '1'
            },
            {
              html: '女',
              value: '2'
            }
          ],
          extraClass: {
            boxDiv: ['col-sm-4']
          }
        }
      ]
    }, {
      column: [
        {
          label: '状态',
          name: 'isLock',
          type: 'radio',
          required: true,
          option: [
            {
              html: '正常',
              value: 'N'
            },
            {
              html: '锁定',
              value: 'Y'
            }
          ],
          extraClass: {
            boxDiv: ['col-sm-4']
          }
        },
        {
          label: '景点',
          name: 'spotId',
          tagName: 'select',
          async: {
            url: 'dict/queryByCode',
            data: {
              send: {
                code: 'spot'
              },
              set: 'id'
            }
          }
        }
      ],
      extraClass: {
        boxDiv: ['col-sm-4']
      }
    }
  ]
  return new WindowPanel({
    owner: opt.owner,
    layerConfig: {
      area: '1000px',
      title: opt.data ? '编辑用户信息' : '新建用户信息'
    },
    btns: {
      edit: {
        html: '确认',
        extraClass: ['layui-layer-btn0'],
        events: {
          click: function (opt) {
            if (opt.panel.items['userForm'].validatesFn({windowBtn: opt.btn})) {
              var loading = layer.load(1, { shade: [0.1, '#fff'] })
              $.ajax({
                url: $(opt.panel.component).find('input[name=id]').length ? '/user/update' : '/user/save',
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
        fnName: 'userForm',
        config: {
          xtype: 'FormPanel',
          data: opt.data,
          fields: fields,
          queryUrl: '/user/queryById',
          queryType: 'get'
        }
      })
    ]
  })
}
