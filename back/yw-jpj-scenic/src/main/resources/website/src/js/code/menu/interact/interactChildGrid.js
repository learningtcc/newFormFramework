import $ from 'jquery'
import layer from 'static/layer/layer'
import { btnReloadClass, windowCenter, setConfig } from 'common/commonClass'
import GridPanel from 'component/gridPanel'
import { checkActive, getFormatDate } from 'common/method'
import interactChildForm from 'code/menu/interact/interactChildForm'
import interactCommentGrid from 'code/menu/interact/interactCommentGrid'

export default function interactChildGrid (opt) {
  var btns = {
    reload: {
      extraClass: btnReloadClass,
      events: {
        click: function (opt) {
          opt.panel.refresh()
          opt.panel.clearAllItem()
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
  var operationBtns = {
    comments: {
      extraClass: ['fa', 'fa-eye', 'btn', 'btn-sm', 'btn-success', 'btn-space'],
      events: {
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('该互动内容下的评论列表', opt.btn, {
            tips: [1, '#5cb85c']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          checkActive({
            table: opt.panel.table,
            tr: opt.tr,
            backgroundColor: '#286090'
          })
          opt.panel.clearAllItem()
          opt.panel.hideOtherRows({
            tr: opt.tr
          })
          opt.panel.items['interactCommentGrid'] = interactCommentGrid({
            owner: opt.panel,
            ownerBtn: opt.panel,
            data: {
              id: opt.tr.getAttribute('uid'),
              title: $(opt.tr).children().eq($(opt.tr).parent().prev().find('[name="interactionTitle"]').attr('index')).text()
            }
          })
        }
      }
    },
    edit: {
      extraClass: ['fa', 'fa-edit', 'btn', 'btn-sm', 'btn-warning', 'btn-space'],
      events: {
        mouseenter: function (opt) {
          opt.btn.tips = layer.tips('查看内容详情', opt.btn, {
            tips: [1, '#ec971f']
          })
        },
        mouseleave: function (opt) {
          layer.close(opt.btn.tips)
        },
        click: function (opt) {
          opt.panel.items['interactChildForm'] = interactChildForm({
            owner: opt.panel,
            ownerBtn: opt.panel,
            data: {
              id: opt.tr.getAttribute('uid')
            }
          })
        }
      }
    }
  }
  var fields = [
    {
      text: '序号',
      type: 'increment',
      style: 'width:45px;'
    }, {
      text: '互动标题',
      name: 'interactionTitle'
    }, {
      text: '评论数量',
      name: 'commentNum'
    }, {
      text: '发布人',
      name: 'publisherNickname'
    }, {
      text: '发布时间',
      name: 'publisherTime'
    }, {
      type: 'operations',
      text: '操作',
      style: 'width:210px;'
    }
  ]

  return new GridPanel(setConfig({
    opt: opt,
    fnName: 'interactChildGrid',
    btns: btns,
    operationBtns: operationBtns,
    classGroup: 'GridPanel',
    config: {
      container: windowCenter,
      url: '/interactive/getInteractiveContentList',
      type: 'post',
      pageSize: 10,
      pageNo: 1,
      fields: fields,
      title: {
        innerHTML: '"' + opt.data.title + '"主题下的互动内容'
      },
      boxExtraClass: ['col-sm-12'],
      render: (opt) => {
        var $tr = $(opt.table).find('tbody tr')
        if (opt.response.data && opt.response.data.root && opt.response.data.root.length) {
          // opt.response.data.root.map((item, index) => {
          //   $tr.eq(index).children().eq($tr.parent().prev().find('tr').find('[name="createTime"]').index()).text(getFormatDate({
          //     timeStamp: item.createTime,
          //     format: 'yyyy-MM-dd HH:mm:ss'
          //   }))
          // })
        }
      },
      search: {
        fields: [
          {
            name: 'Interaction_title',
            text: '互动标题'// placeholder
          },
          {
            name: 'startTime',
            text: '发布时间起始日期',
            type: 'date'
          },
          {
            name: 'endTime',
            text: '结束日期',
            type: 'date'
          },
          {
            name: 'interactivThemeFk',
            text: '父id',
            hidden: true,
            value: opt.data.id
          }
        ]
      }
    }
  }))
}
