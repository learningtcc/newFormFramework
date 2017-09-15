import $ from 'jquery'
import layer from 'static/layer/layer'
import { setConfig } from 'common/commonClass'
import WindowPanel from 'component/windowPanel'

export default function commentForm(opt) {
	var fields = [{
		column: [{
			label: '订单编号',
			readonly: true,
			name: 'orderNo'
		}]
	},{
		column: [{
			label: '商品编号',
			readonly: true,
			name: 'commodityId'
		}]
	},{
		column: [{
			label: '商品名称',
			readonly: true,
			name: 'commodityName'
		}]
	},{
		column: [{
			label: '用户ID',
			readonly: true,
			name: 'userId'
		}]
	},{
		column: [{
			label: '评论时间',
			readonly: true,
			name: 'evaluationTime'
		}]
	},{
		column: [{
			label: '评论内容',
			readonly: true,
			name: 'content'
		}]
	}]
	var btns = {
		closeBtn: {
			html: '关闭',
			extraClass: ['layui-layer-btn0'],
			events: {
				click: function(opt) {
					layer.closeAll()
				}
			}
		}
	}
	return new WindowPanel({
		owner: opt.owner,
		layerConfig: {
			area: '800px',
			title: '评价管理-详情'
		},
		btns: btns,
		items: [
			setConfig({
				opt: opt,
				fnName: 'commentForm',
				config: {
					xtype: 'FormPanel',
					data: opt.data,
					fields: fields,
					queryUrl: '/commodityEvaluation/queryById',
					queryType: 'post',
					render: (opt) => {}
				}
			})
		]
	})
}