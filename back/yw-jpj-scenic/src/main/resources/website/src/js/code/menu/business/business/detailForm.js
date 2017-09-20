import { setConfig } from 'common/commonClass';
import WindowPanel from 'component/windowPanel';

export default function detailForm(opt){
    var fields=[
        {
            column: [
                {
                    label: '店铺状态',
                    name: 'isRelease',
                    type: 'radio',
                    readonly: true,
                    option: [
                        {
                            html: '发布',
                            value: 'Y'
                        },
                        {
                            html: '暂不发布',
                            value: 'N'
                        }
                    ]
                },
                {
                    label: '店铺名称',
                    name: 'name',
                    readonly: true,

                },
                {
                    label: '店铺类型',
                    name: 'type',
                    readonly: true,

                }
            ],
            extraClass: {
                boxDiv: ['col-sm-4']
            }
        },
        {
            column: [
                {
                    label: '店铺图片',
                    name: '',
                    type: 'img',
                    readonly: true,

                },
                {
                    label: '面积',
                    name: 'area',
                    readonly: true,

                },
                {
                    label: '运费',
                    name: 'fee',
                    readonly: true,

                }
            ],
            extraClass: {
                boxDiv: ['col-sm-4']
            }
        },
        {
            column: [
                {
                    label: '客服电话',
                    name: 'serviceTel',
                    readonly: true,

                },
                {
                    label: '联系人姓名',
                    name: 'contactPerson',
                    readonly: true,

                },
                {
                    label: '联系人电话',
                    name: 'contactPhone',
                    readonly: true,

                }
            ],
            extraClass: {
                boxDiv: ['col-sm-4']
            }
        },
        {
            column: [
                {
                    label: '地址',
                    name: 'address',
                    readonly: true,

                },
                {
                    label: '地址经度',
                    name: 'longitude',
                    readonly: true,
                    events: {
                        blur: function (opt) {
                            let longitude = $(opt.component).find('input[name="longitude"]').val() ? $(opt.component).find('input[name="longitude"]').val() : 116.404
                            let latitude = $(opt.component).find('input[name="dimension"]').val() ? $(opt.component).find('input[name="dimension"]').val() : 39.915
                            opt.this.MAP.clearOverlays()
                            let point = new opt.this.BMAP.Point(longitude, latitude);
                            opt.this.MAP.centerAndZoom(point, 16);
                            let marker = new opt.this.BMAP.Marker(point);
                            opt.this.MAP.addOverlay(marker);
                        }
                    }
                },
                {
                    label: '地址纬度',
                    name: 'dimension',
                    readonly: true,
                    events: {
                        blur: function (opt) {
                            let longitude = $(opt.component).find('input[name="longitude"]').val() ? $(opt.component).find('input[name="longitude"]').val() : 116.404
                            let latitude = $(opt.component).find('input[name="dimension"]').val() ? $(opt.component).find('input[name="dimension"]').val() : 39.915
                            opt.this.MAP.clearOverlays()

                            let point = new opt.this.BMAP.Point(longitude, latitude);
                            opt.this.MAP.centerAndZoom(point, 16);
                            let marker = new opt.this.BMAP.Marker(point);
                            opt.this.MAP.addOverlay(marker);
                        }
                    }
                }
            ],
            extraClass: {
                boxDiv: ['col-sm-4']
            }
        },
        {
            column: [
                {
                    label: '地图',
                    name: '',
                    type: 'map',
                    readonly: true,
                    location: {
                        longitude: 'longitude',
                        latitude: 'dimension'// 地图中心点取得是同一接口中的经纬度字段，此处设置接口经纬度字段至地图
                    },
                    area: {
                        width: '120%', // 字符串类型
                        height: '360px'
                    },
                    myEvent (opt) {
                        $(opt.component).find('input[name="longitude"]').val(opt.e.point.lng)
                        $(opt.component).find('input[name="dimension"]').val(opt.e.point.lat)
                        // alert(opt.e.point.lng + "," + opt.e.point.lat);
                    },
                    extraClass: {
                        boxDiv: ['form-group'],
                        label: ['col-sm-1', 'control-label'],
                        inputText: ['col-sm-11']
                    }
                }
            ]
        },
        {
            column: [
                {
                    label: '微信/公众号二维码',
                    name: 'wxUrl',
                    type: 'img',
                    readonly: true,

                },
                {
                    label: '收款二维码(微信)',
                    name: 'weixinUrl',
                    type: 'img',
                    readonly: true,

                },
                {
                    label: '收款二维码(支付宝)',
                    name: 'alipayUrl',
                    type: 'img',
                    readonly: true,

                }
            ],
            extraClass: {
                boxDiv: ['col-sm-4']
            }
        },
        {
            column: [
                {
                    label: '银行收款',
                    name: 'bankAddress',
                    readonly: true,

                },
                {
                    label: '银行卡号',
                    name: 'bankCard',
                    readonly: true,

                },
                {
                    label: '持卡人',
                    name: 'payee',
                    readonly: true,

                }
            ],
            extraClass: {
                boxDiv: ['col-sm-4']
            }
        },
        {
            column: [
                {
                    label: '店铺图集',
                    name: 'pics',
                    type: 'multigraph',
                    readonly: true,
                    extraClass: {
                        boxDiv: ['form-group'],
                        label: ['col-sm-1', 'control-label'],
                        inputText: ['col-sm-11']
                    }
                }
            ]
        },
        {
            column: [
                {
                    label: '店铺介绍',
                    name: 'description',
                    type: 'richTextEditor',
                    readonly: true,
                    area: {
                        width: '122%', // 字符串类型
                        height: '360px'
                    },
                    extraClass: {
                        boxDiv: ['form-group'],
                        label: ['col-sm-1', 'control-label'],
                        inputText: ['col-sm-11']
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
