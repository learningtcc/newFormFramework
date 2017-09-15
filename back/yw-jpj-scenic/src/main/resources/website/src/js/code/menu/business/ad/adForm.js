import { setConfig } from 'common/commonClass';
import WindowPanel from 'component/windowPanel';

export default function adForm(opt){
    var fields=[
        {
            column:[
                {
                    label:'广告图名称',
                    required:true,
                    name:'name',
                    validator:{
                        type:['validateLength'],
                        validateRule:{
                            min:1,
                            max:50
                        }
                    }
                },{
                    label:'广告图',
                    name:'imageUrl',
                    required:true,
                    type:'img'
                }
            ],
            extraClass:{
                boxDiv:['col-sm-6']
            }
        },{
            column:[
                {
                    label:'广告类别',
                    name:'tableName',
                    required:true,
                    tagName:'select',
                    async:{
                        url:'/advertising/findTypeList',
                        type:'get',
                        data:{
                            send:{
                                code:'goods_category'
                            },
                            set:'name'
                        }
                    }
                },{
                    label:'序号',
                    name:'sort',
                    required:true,
                    validator:{
                        type:['validateNumber','validateLength'],
                        validateRule:{
                            min:1,
                            max:10,
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
                    if(opt.panel.items['adForm'].validatesFn({windowBtn:opt.btn})){
                        var loading = layer.load(1,{shade: [0.1,'#fff'] });
                        $.ajax({
                            url:$(opt.panel.component).find('input[name=id]').length?'/advertising/modify':'/advertising/save',
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
            title:opt.data?'编辑广告信息':'新建广告信息'
        },
        btns:btns,
        items:[
            setConfig({
                opt:opt,
                fnName:'adForm',
                config:{
                    xtype:'FormPanel',
                    data:opt.data,
                    fields:fields,
                    queryUrl:'/advertising/queryById',
                    queryType:'get'
                }
            })
        ]
    });
}
