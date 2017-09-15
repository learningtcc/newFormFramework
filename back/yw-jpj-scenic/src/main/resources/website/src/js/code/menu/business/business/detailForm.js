import { setConfig } from 'common/commonClass';
import WindowPanel from 'component/windowPanel';

export default function detailForm(opt){
    var fields=[
        {
            column:[
               {
                    label:'商家名称',
                    readonly: true,
                    name:'name',
                    validator:{
                        type:['validateLength'],
                        validateRule:{
                            min:1,
                            max:50
                        }
                    }
                }, {
                    label:'地址',
                    name:'address',
                    readonly:true,
                },{
                    label:'店铺类型',
                    name:'type',
                    readonly:true,
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label:'联系人姓名',
                    name:'name',
                    readonly:true,
                },{
                    label:'联系人电话',
                    name:'name',
                    readonly:true,
                },{
                    label:'营业执照',
                    name:'businessLicenseUrl',
                    readonly:true,
                    type:'img'
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label:'经度',
                    name:'longitude',
                    readonly:true,
                },{
                    label:'纬度',
                    name:'dimension',
                    readonly:true,
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column: [
                {
                    label: '地图',
                    name: '',
                    readonly: true,
                    type: 'map',
                    location: {
                        longitude: 'longitude',
                        latitude: 'latitude'// 地图中心点取得是同一接口中的经纬度字段，此处设置接口经纬度字段至地图
                    },
                    myEvent (opt) {
                        // alert(opt.e.point.lng + "," + opt.e.point.lat);
                    },
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
                    label:'微信公众号二维码',
                    name:'weixinUrl',
                    readonly:true,
                    type:'img'
                },{
                    label:'支付宝微信支付二维码',
                    name:'alipayUrl',
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
                    label:'银行收款开户行',
                    name:'bankAddress',
                    readonly:true,
                },{
                    label:'银行卡卡号',
                    name:'bankCard',
                    readonly:true,
                },{
                    label:'持卡人',
                    name:'payee',
                    readonly:true,
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label: '店铺图集(450*360)',
                    name: 'pics',
                    type: 'multigraph',
                    imgListWidth: '78%',
                    readonly: true,
                    extraClass: {
                        boxDiv: ['new-col-width'],
                        label: ['new-label-width'],
                        inputText: ['bigest-input-width']
                    }
                }
            ]
        },{
            column:[
                {
                    label:'商铺简介',
                    readonly:true,
                    name:'remark',
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
        // edit:{
        //     html:'确认',
        //     extraClass:['layui-layer-btn0'],
        //     events:{
        //         click:function(opt){
        //             if(opt.panel.items['detailForm'].validatesFn({windowBtn:opt.btn})){
        //                 var loading = layer.load(1,{shade: [0.1,'#fff'] });
        //                 $.ajax({
        //                     url:$(opt.panel.component).find('input[name=id]').length?'/store/update':'/store/add',
        //                     type:'post',
        //                     data:$('.'+opt.panel.component.className.split(' ')[0]).find('form').serialize(),//+userType,
        //                     success:function(response){
        //                         if(response.isSuccess){
        //                             layer.closeAll();
        //                             layer.msg('保存成功!');
        //                             opt.panel.owner.refresh();
        //                         }else{
        //                             layer.close(loading);
        //                             layer.msg(response.errorMessage,function(){});
        //                         }
        //                     },
        //                     error:function(response){
        //                         layer.close(loading);
        //                         layer.alert('错误代码:'+response.status+'，请联系管理员',{title:'错误提示',shadeClose: true});
        //                     }
        //                 });
        //             }
        //         }
        //     }
        // },
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
        },
        // Withdraw:{
        //     html:'撤下',
        //     extraClass:['layui-layer-btn0'],
        //     events:{
        //         click:function(opt){
        //             for(var i in opt.panel.items){
        //                 if(opt.panel.items[i].refresh)
        //                     opt.panel.items[i].refresh();
        //             }
        //         }
        //     }
        // }
    }
    return new WindowPanel({
        owner:opt.owner,
        layerConfig:{
            area:'1000px',
            title:'商家详情'
        },
        btns:btns,
        items:[
            setConfig({
                opt:opt,
                fnName:'detailForm',
                config:{
                    xtype:'FormPanel',
                    data:opt.data,
                    fields:fields,
                    queryUrl:'/store/queryById',
                    queryType:'post'
                }
            })
        ]
    });
}
