import { setConfig } from 'common/commonClass';
import WindowPanel from 'component/windowPanel';

export default function goodsForm(opt){
    var temp_json = opt;
    var fields=[
        {
            column:[
                {
                    label:'商品名称',
                    readonly:true,
                    name:'name'
                },{
                    label:'商品价格',
                    readonly:true,
                    name:'price'
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label:'商品类别',
                    name:'typeId',
                    readonly:true,
                    tagName: 'select',
                    async: {
                        url: '/dict/queryByCode',
                        type: 'post',
                        data: {
                            send: {
                                code: 'goods_category'
                            },
                            set: 'id'
                        }
                    },
                    linkage:{
                        speciesId:true
                    }
                },{
                    label:'商品种类',
                    name:'speciesId',
                    readonly:true,
                    tagName: 'select',
                    async: {
                        url: '/commodity/findListByTypeId',
                        type: 'get',
                        data: {
                            // send: {
                            //     typeId:
                            // },
                            set: 'id',
                            send:'typeId'
                        }
                    }
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label:'价格单位',
                    name:'priceId',
                    readonly:true
                },{
                    label:'商品主图',
                    name:'theme_pic',
                    // readonly:true,
                    type:'img'
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
        },{
            column:[
                {
                    label: '特色商品',
                    name: 'isFeatures',
                    type: 'radio',
                    readonly: true,
                    option: [
                        {
                            html: '是',
                            value: 'Y'
                        },
                        {
                            html: '否',
                            value: 'N'
                        }
                    ]
                }, {
                    label: '上下架',
                    name: 'isShelves',
                    type: 'radio',
                    readonly: true,
                    option: [
                        {
                            html: '上架',
                            value: 'Y'
                        },
                        {
                            html: '下架',
                            value: 'N'
                        }
                    ]
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label: '商品规格',
                    name: 'standard',
                    tagName: 'textarea',
                    readonly:true,
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
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label:'商品详情',
                    name:'details',
                    readonly:true,
                    type:'richTextEditor',
                    area: {
                        width: '122%', // 字符串类型
                        height: '360px'
                    },
                    extraClass:{
                        boxDiv: ['form-group'],
                        label: ['col-sm-1', 'control-label'],
                        inputText: ['col-sm-11']
                    }
                }
            ]
        }
    ];
    var btns={
        edit:{
            html:'确定',
            extraClass:['layui-layer-btn0'],
            events:{
                click:function(opt){
                    let richText = ''
                    Object.keys(opt.panel).map((item, index) => {
                        if (item.indexOf('field-inputText') > -1 && $('#' + item).attr('type') === 'richTextEditor') {
                            richText = '&' + $('#' + item).attr('name') + '=' + $('#' + item).find('[contenteditable="true"]').html()
                        }
                    })
                    var loading = layer.load(1,{shade: [0.1,'#fff'] });
                    $.ajax({
                        url:$(opt.panel.component).find('input[name=id]').length?'/commodity/modify':'/commodity/save',
                        type:'post',
                        data:$('.'+opt.panel.component.className.split(' ')[0]).find('form').serialize() + richText,//+userType,
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
