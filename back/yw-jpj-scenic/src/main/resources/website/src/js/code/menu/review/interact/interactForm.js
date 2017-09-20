import $ from 'jquery'
import layer from 'static/layer/layer'
import { setConfig } from 'common/commonClass'
import WindowPanel from 'component/windowPanel'

export default function interactForm(opt) {
	var fields = [{
		column: [{
			label: '标题',
			readonly: true,
			name: 'interactionTitle'
		}]
	},{
		column: [{
			label: '内容',
			readonly: true,
			name: 'interactiveContent'
		}]
	},{
		column: [{
			label: '图片',
			readonly: true,
			name: 'picList',
		    type: 'multigraph', // 图集
		    imgListWidth: '50%' // 图集宽度占整个弹出层的百分比
		}]
	},{
		column: [{
			label: '发布时间',
			readonly: true,
			name: 'publisherTime'
		}]
	},{
		column: [{
			label: '审核结果',
			name: 'auditStatus',
			tagName: 'select',
			required: true,
			events: {
				change: (opt) => {
					if($(opt.component).find('select[name="auditStatus"]').val() == 'AuditFail'){
						$(opt.component).find('input[name="auditExplain"]').parents('.form-group').show()
					}else{
						$(opt.component).find('input[name="auditExplain"]').parents('.form-group').hide()
					}
				}
			},
			async: {
              	url: '/auditManagement/getAuditStatusList',
              	type: 'post',
              	data: {
                	send: {},
                	set: 'value'
              	}
            }
		}]
	},{
		column: [{
			label: '审核不通过原因',
			required: true,
			name: 'auditExplain'
		}]
	},{
		column: [{
			label: '审核时间',
			readonly: true,
			name: 'auditTime'
		}]
	},{
		column: [{
			label: '审核人',
			readonly: true,
			name: 'auditName'
		}]
	}]
	var btns = {
		edit: {
			html: '确认',
			extraClass: ['layui-layer-btn0'],
			events: {
				click: function(opt) {
					if ($('select[name="auditStatus"]').val() !== 'AuditFail') {
						$('input[name="auditExplain"]').attr('validator', true)
					}
					if (opt.panel.items['interactForm'].validatesFn({windowBtn: opt.btn})) {
            			var loading = layer.load(1, { shade: [0.1, '#fff'] })
            			// console.log($('.' + opt.panel.component.className.split(' ')[0]).find('form').serialize())
            			$.ajax({
				            url: '/auditManagement/updateInteractiveContentAudit',
				            type: 'post',
				            data: $('.' + opt.panel.component.className.split(' ')[0]).find('form').serialize(),
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
				click: function(opt) {
					layer.closeAll()
				}
			}
		},
		reload: {
			html: '重置',
			extraClass: ['layui-layer-btn0'],
			events: {
				click: function(opt) {
					for(var i in opt.panel.items) {
						if(opt.panel.items[i].refresh) {
							opt.panel.items[i].refresh()
						}
					}
				}
			}
		}
	}
	return new WindowPanel({
		owner: opt.owner,
		layerConfig: {
			area: '800px',
			title: '审核管理-互动-详情'
		},
		btns: btns,
		items: [
			setConfig({
				opt: opt,
				fnName: 'interactForm',
				config: {
					xtype: 'FormPanel',
					data: opt.data,
					fields: fields,
					queryUrl: '/interactive/getInteractiveContent',
					queryType: 'post',
					render: (opt) => {
						if(opt.response.data.auditStatus == 'Audited' || opt.response.data.auditStatus == 'AuditFail'){
							$(opt.component).find('select[name="auditStatus"]').attr('disabled', 'disabled')
							$(opt.component).find('input[name="auditExplain"]').attr('disabled', 'disabled')
						}
						if(opt.response.data.auditStatus == 'AuditFail'){
							$(opt.component).find('input[name="auditExplain"]').parents('.form-group').show()
						}else{
							$(opt.component).find('input[name="auditExplain"]').parents('.form-group').hide()
						}
					}
				}
			})
		]
	})
}
