import $ from 'jquery'
import layer from 'static/layer/layer'
import { btnReloadClass, windowCenter, setConfig } from 'common/commonClass'
import GridPanel from 'component/gridPanel'
import commentForm from 'code/menu/comment/commentForm'

export default function commentGrid(opt) {
	var btns = {
		reload: {
			extraClass: btnReloadClass,
			events: {
				click: function(opt) {
					opt.panel.refresh()
					opt.panel.clearAllItem()
				},
				mouseenter: function(opt) {
					$(opt.btn).addClass('fa-spin')
				},
				mouseleave: function(opt) {
					$(opt.btn).removeClass('fa-spin')
				}
			}
		}
	}
	var operationBtns = {
		edit: {
			extraClass: ['fa', 'fa-eye', 'btn', 'btn-sm', 'btn-info', 'btn-space'],
			events: {
				mouseenter: function(opt) {
					opt.btn.tips = layer.tips('查看详情', opt.btn, {
						tips: [1, '#5bc0de']
					})
				},
				mouseleave: function(opt) {
					layer.close(opt.btn.tips)
				},
				click: function(opt) {
					opt.panel.items['commentForm'] = commentForm({
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
	var fields = [{
		text: '序号',
		type: 'increment',
		style: 'width:45px;'
	}, {
		text: '订单编号',
		name: 'orderNo'
	}, {
		text: '商品编号',
		name: 'commodityId'
	}, {
		text: '商品名称',
		name: 'commodityName'
	}, {
		text: '用户ID',
		name: 'userId'
	}, {
		text: '评论时间',
		name: 'evaluationTime'
	}, {
		text: '评论内容',
		name: 'content'
	}, {
		type: 'operations',
		text: '操作'
	}]
	return new GridPanel(setConfig({
		opt: opt,
		fnName: 'commentGrid',
		btns: btns,
		operationBtns: operationBtns,
		classGroup: 'GridPanel',
		config: {
			container: windowCenter,
			url: '/commodityEvaluation/queryPage',
			type: 'post',
			pageSize: 10,
			pageNo: 1,
			fields: fields,
			title: {
				innerHTML: '评价管理'
			},
			boxExtraClass: ['col-sm-12'],
			render: (opt) => {},
			search: {
				fields: [
					{
						name: 'orderNo',
						text: '订单编号'
					}
				]
			}
		}
	}))
}