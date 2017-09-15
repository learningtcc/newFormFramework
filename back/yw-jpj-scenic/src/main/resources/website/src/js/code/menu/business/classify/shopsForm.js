import { setConfig } from 'common/commonClass';
import WindowPanel from 'component/windowPanel';

export default function shopsForm(opt){
    var fields=[
        {
            column:[
               {
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
                },{
                    label: '父ID',
                    name: 'parentId',
                    hidden: true,
                    value: opt.parentId
                }
            ],
            extraClass:{
                boxDiv:['col-sm-6']
            }
        },{
            column:[
                {
                    label: '商铺类型名称',
                    name: 'name',
                    required:true,
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
                }
            ],
            extraClass:{
                boxDiv:['col-sm-6']
            }
        },{
            column:[
                {
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
            ],
            extraClass:{
                boxDiv:['col-sm-6']
            }
        }
    ];
    var btns={
        edit:{
            html:'确认',
            extraClass:['layui-layer-btn0'],
            events:{
                click:function(opt){
                    if(opt.panel.items['shopsForm'].validatesFn({windowBtn:opt.btn})){
                        var loading = layer.load(1,{shade: [0.1,'#fff'] });
                        $.ajax({
                            url:$(opt.panel.component).find('input[name=id]').length?'/dict/update':'/dict/save',
                            type:'post',
                            data:$('.'+opt.panel.component.className.split(' ')[0]).find('form').serialize(),//+userType,
                            success:function(response){
                                if(response.isSuccess){
                                    layer.closeAll();
                                    opt.panel.owner.refresh();
                                }else{
                                    layer.close(loading);
                                    layer.msg(response.errorMessage,function(){});
                                }
                            },
                            error:function(response){
                                layer.close(loading);
                                layer.alert('错误代码:'+response.status+'，请联系管理员',{title:'错误提示',shadeClose: true});
                            }
                        });
                    }
                }
            }
        },
        closeBtn:{
            html:'关闭',
            extraClass:['layui-layer-btn0'],
            events:{
                click:function(opt){
                    layer.closeAll();
                }
            }
        },
        reload:{
            html:'重置',
            extraClass:['layui-layer-btn0'],
            events:{
                click:function(opt){
                    for(var i in opt.panel.items){
                        if(opt.panel.items[i].refresh)
                            opt.panel.items[i].refresh();
                    }
                }
            }
        }
    }
    return new WindowPanel({
        owner:opt.owner,
        layerConfig:{
            area:'1000px',
            title:opt.data?'编辑商铺类型信息':'新建商铺类型信息'
        },
        btns:btns,
        items:[
            setConfig({
                opt:opt,
                fnName:'shopsForm',
                config:{
                    xtype:'FormPanel',
                    data:opt.data,
                    fields:fields,
                    queryUrl:'/dict/queryById',
                    queryType:'get'
                }
            })
        ]
    });
}
