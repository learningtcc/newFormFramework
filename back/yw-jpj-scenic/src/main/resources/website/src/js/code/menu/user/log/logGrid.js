import { setConfig, windowCenter } from 'common/commonClass'
import GridPanel from 'component/gridPanel'

export default function logGrid (opt) {
  var fields = [
    {
      text: '序号',
      type: 'increment',
      style: 'width:45px;'
    }, {
      text: '用户名',
      name: 'userIdName'
    }, {
      text: 'IP地址',
      name: 'ip'
    }, {
      name: 'recordDate'
    }
  ]
  return new GridPanel(setConfig({
    opt: opt,
    fnName: 'logGrid',
    classGroup: 'GridPanel',
    config: {
      container: windowCenter,
      url: '/userLoginInOut/queryListByPage',
      fields: fields,
      pageSize: 10,
      pageNo: 1,
      title: {
        innerHTML: '日志管理'
      },
      render: function (opt) {
        for (let i = 0; i < opt.panel.table.tBodies[0].rows.length; i++) {
          var json = {}
          var nowDate = new Date(parseInt(opt.panel.table.tBodies[0].rows[i].cells[opt.panel.table.tHead.querySelector('[name="recordDate"]').getAttribute('index')].innerHTML))
          json.year = nowDate.getFullYear()
          json.month = nowDate.getMonth() + 1
          json.date = nowDate.getDate()
          json.hours = nowDate.getHours()
          json.minutes = nowDate.getMinutes()
          json.seconds = nowDate.getSeconds()
          for (let j in json) {
            if (parseInt(json[j]) < 10) {
              json[j] = '0' + json[j]
            }
          }
          opt.panel.table.tBodies[0].rows[i].cells[opt.panel.table.tHead.querySelector('[name="recordDate"]').getAttribute('index')].innerHTML = json.year + '-' + json.month + '-' + json.date + ' ' + json.hours + ':' + json.minutes + ':' + json.seconds
        }
        var type = opt.panel.searchBox.querySelector('[name="inoutType"]').value
        if (type === 0) {
          let json = {
            1: '登入时间',
            2: '登出时间'
          }
          for (let i = 0; i < opt.panel.table.tBodies[0].rows.length; i++) {
            if (opt.panel.table.tBodies[0].rows[i].getAttribute('inouttype') === 1) {
              opt.panel.table.tBodies[0].rows[i].style.background = 'honeydew'
            } else if (opt.panel.table.tBodies[0].rows[i].getAttribute('inouttype') === 2) {
              opt.panel.table.tBodies[0].rows[i].style.background = 'aliceblue'
            }
            opt.panel.table.tBodies[0].rows[i].cells[opt.panel.table.tHead.querySelector('[name="recordDate"]').getAttribute('index')].innerHTML = json[opt.panel.table.tBodies[0].rows[i].getAttribute('inouttype')] + ':' + opt.panel.table.tBodies[0].rows[i].cells[opt.panel.table.tHead.querySelector('[name="recordDate"]').getAttribute('index')].innerHTML
            opt.panel.table.tBodies[0].rows[i].oldStyle = opt.panel.table.tBodies[0].rows[i].getAttribute('style')
            opt.panel.table.tBodies[0].rows[i].onmouseenter = function () {
              this.removeAttribute('style')
            }
            opt.panel.table.tBodies[0].rows[i].onmouseleave = function () {
              this.style = this.oldStyle
            }
          }
        }
      },
      extraAttr: {
        inoutType: 'inoutType'
      },
      search: {
        fields: [
          {
            name: 'userIdName',
            text: '用户名称'// placeholder
          }, {
            name: 'ip',
            text: 'IP地址'// placeholder
          }, {
            name: 'startDate',
            text: '开始时间', // placeholder
            type: 'time'
          }, {
            name: 'endDate',
            text: '结束时间', // placeholder
            type: 'time'
          }, {
            name: 'inoutType',
            text: '状态', // placeholder
            type: 'select',
            value: {
              '全部': '0',
              '登入时间': '1',
              '登出时间': '2'
            },
            change: function (opt) {
              for (var i = 0; i < opt.panel.fields.length; i++) {
                if (opt.panel.fields[i].name === 'recordDate') {
                  opt.panel.fields[i].text = opt.input.selectedOptions[0].innerHTML
                  break
                }
              }
            }
          }
        ]
      }
    }
  }))
}
