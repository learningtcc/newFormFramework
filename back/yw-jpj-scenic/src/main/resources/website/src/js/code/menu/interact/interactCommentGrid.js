import $ from 'jquery'
// import layer from 'static/layer/layer'
import { btnReloadClass, windowCenter, setConfig } from 'common/commonClass'
import { getFormatDate } from 'common/method'
import GridPanel from 'component/gridPanel'

export default function interactCommentGrid (opt) {
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
  // var operationBtns = {
  //   delete: {
  //     extraClass: ['fa', 'fa-trash', 'btn', 'btn-sm', 'btn-danger', 'btn-space'],
  //     events: {
  //       mouseenter: function (opt) {
  //         opt.btn.tips = layer.tips('删除评论', opt.btn, {
  //           tips: [1, '#d9534f']
  //         })
  //       },
  //       mouseleave: function (opt) {
  //         layer.close(opt.btn.tips)
  //       },
  //       click: function (opt) {
  //         layer.confirm('确定删除此条评论？', {
  //           btn: ['删除', '取消'] // 按钮
  //         }, function () {
  //           $.ajax({
  //             url: '/thematicActivities/deleteById',
  //             type: 'post',
  //             data: {
  //               id: opt.tr.getAttribute('uid')
  //             },
  //             success: function (response) {
  //               if (response.isSuccess) {
  //                 layer.msg('删除成功')
  //                 opt.panel.refresh()
  //               } else {
  //                 layer.alert(response.errorMessage, function () {})
  //               }
  //             },
  //             error: function (response) {
  //               layer.alert(response.errorMessage, function () {})
  //             }
  //           })
  //         }, function () {})
  //       }
  //     }
  //   }
  // }
  var fields = [
    {
      text: '序号',
      type: 'increment',
      style: 'width:45px;'
    }, {
      text: '评论人',
      name: 'reviewerNickname'
    }, {
      text: '评论时间',
      name: 'createTime'
    }, {
      text: '评论内容',
      name: 'evaluationContent'
    }
    // , {
    //   type: 'operations',
    //   text: '操作',
    //   style: 'width:210px;'
    // }
  ]

  return new GridPanel(setConfig({
    opt: opt,
    fnName: 'interactCommentGrid',
    btns: btns,
    // operationBtns: operationBtns,
    classGroup: 'GridPanel',
    config: {
      container: windowCenter,
      url: '/interactive/getInteractiveEvaluationList',
      type: 'post',
      pageSize: 10,
      pageNo: 1,
      fields: fields,
      title: {
        innerHTML: '"' + opt.data.title + '"互动内容下的评论列表'
      },
      boxExtraClass: ['col-sm-12'],
      render: (opt) => {
        var $tr = $(opt.table).find('tbody tr')
        if (opt.response.data && opt.response.data.root && opt.response.data.root.length) {
          opt.response.data.root.map((item, index) => {
            $tr.eq(index).children().eq($tr.parent().prev().find('tr').find('[name="createTime"]').index()).text(getFormatDate({
              timeStamp: item.createTime,
              format: 'yyyy-MM-dd HH:mm:ss'
            }))
          })
        }
      },
      search: {
        fields: [
          {
            name: 'contentId',
            text: '内容ID', // placeholder
            hidden: true,
            value: opt.data.id
          }
        ]
      }
    }
  }))
}
