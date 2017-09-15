import { setConfig } from 'common/commonClass';
import WindowPanel from 'component/windowPanel';

export default function majorForm(opt){
    var fields=[
        {
            column:[
                {
                    label: '商品名称',
                    name: 'name',
                    required: true,
                    validator: {
                        type: ['validateLength'],
                        validateRule: {
                            min: 1,
                            max: 50
                        }
                    }
                },{
                    label: '图片',
                    name: 'themePic',
                    required: true,
                    type:'img'
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label: '最低价',
                    name: 'priceLow',
                    required:true,
                    validator: {
                        type: ['validateLength'],
                        validateRule: {
                            min: 1,
                            max: 50
                        }
                    }
                }, {
                    label: '最高价 ',
                    name: 'priceHigh',
                    validator: {
                        type: ['validateLength'],
                        validateRule: {
                            min: 1,
                            max: 50
                        }
                    }
                },{
                    label: '规格单位 ',
                    name: 'priceUnit',
                    validator: {
                        type: ['validateLength'],
                        validateRule: {
                            min: 1,
                            max: 50
                        }
                    }
                },{
                    label: '父键ID',
                    name: 'typeId',
                    hidden:true,
                    value: opt.typeId
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label: '图集',
                    name: 'pics',
                    type: 'multigraph',
                    imgListWidth: '78%',
                    required: true,
                    extraClass: {
                        boxDiv: ['new-col-width'],
                        label: ['new-label-width'],
                        inputText: ['bigest-input-width']
                    }
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        }
    ];
    var btns={
        edit:{
            html:'确认',
            extraClass:['layui-layer-btn0'],
            events:{
                click:function(opt){
                    if(opt.panel.items['majorForm'].validatesFn({windowBtn:opt.btn})){
                        var loading = layer.load(1,{shade: [0.1,'#fff'] });
                        $.ajax({
                            url:$(opt.panel.component).find('input[name=id]').length?'/teaSpecies/modify':'/teaSpecies/save',
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
            title:opt.data?'编辑商品种类信息':'新建商品种类信息'
        },
        btns:btns,
        items:[
            setConfig({
                opt:opt,
                fnName:'majorForm',
                config:{
                    xtype:'FormPanel',
                    data:opt.data,
                    fields:fields,
                    queryUrl:'/teaSpecies/queryById',
                    queryType:'get'
                }
            })
        ]
    });
}
