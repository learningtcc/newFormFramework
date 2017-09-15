import { setConfig } from 'common/commonClass';
import WindowPanel from 'component/windowPanel';

export default function adForm(opt){
    var temp_json = opt;
    var fields=[
        {
            column:[
                {
                    label:'商品名称',
                    readonly:true,
                    name:'name'
                },{
                    label:'状态',
                    name:'status',
                    readonly:true,
                },
                // ,{
                //     label:'广告图',
                //     name:'pic',
                //     required:true,
                //     type:'img'
                // }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label:'商品类别',
                    name:'typeIdName',
                    readonly:true
                },{
                    label:'商品种类',
                    name:'speciesIdName',
                    readonly:true
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label:'商品价格',
                    name:'price',
                    readonly:true
                },{
                    label:'商品规格',
                    name:'standard',
                    readonly:true,
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label:'商品主图',
                    name:'theme_pic',
                    readonly:true,
                    type:'img'
                },
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label:'商品详情',
                    readonly:true,
                    name:'details',
                    type:'richTextEditor',
                    extraClass:{
                        boxDiv:['new-col-width'],
                        label:['new-label-width'],
                        inputText:['bigest-input-width']
                    }
                }
            ]
        }
    ];
    var btns={
        edit:{
            html:'上下架',
            extraClass:['layui-layer-btn0'],
            events:{
                click:function(opt){
                    var loading = layer.load(1,{shade: [0.1,'#fff'] });
                    $.ajax({
                        url:'/commodity/onOffShelves',
                        type:'post',
                        data:{
                            id:temp_json.data.id
                        },
                        success:function(response){
                            if(response.isSuccess){
                                layer.closeAll();
                                layer.msg('设置成功!');
                                opt.panel.owner.refresh();
                            }else{
                                layer.close(loading);
                                layer.alert(response.errorMessage,function(){});
                            }
                        },
                        error:function(response){
                            layer.close(loading);
                            layer.alert('错误代码:'+response.status+'，请联系管理员',{title:'错误提示',shadeClose: true});
                        }
                    })
                    // $.ajax({
                    //     url:$(opt.panel.component).find('input[name=id]').length?'/advertising/modify':'/advertising/save',
                    //     type:'post',
                    //     data:$('.'+opt.panel.component.className.split(' ')[0]).find('form').serialize(),//+userType,
                    //     success:function(response){
                    //         if(response.isSuccess){
                    //             layer.closeAll();
                    //             opt.panel.owner.refresh();
                    //         }else{
                    //             layer.close(loading);
                    //             layer.msg(response.errorMessage,function(){});
                    //         }
                    //     },
                    //     error:function(response){
                    //         layer.close(loading);
                    //         layer.alert('错误代码:'+response.status+'，请联系管理员',{title:'错误提示',shadeClose: true});
                    //     }
                    // });
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
            title:opt.data?'编辑商品信息':'新建商品信息'
        },
        btns:btns,
        items:[
            setConfig({
                opt:opt,
                fnName:'goodsForm',
                config:{
                    xtype:'FormPanel',
                    data:opt.data,
                    fields:fields,
                    queryUrl:'/commodity/queryById',
                    queryType:'get'
                }
            })
        ]
    });
}
