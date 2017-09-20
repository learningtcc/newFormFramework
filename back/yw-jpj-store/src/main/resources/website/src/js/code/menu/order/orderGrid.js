import $ from 'jquery';
import layer from 'static/layer/layer'
import { btnPlusClass,btnReloadClass,windowCenter,setConfig } from 'common/commonClass';
import GridPanel from 'component/gridPanel';
import orderForm from 'code/menu/order/orderForm';
import detailForm from 'code/menu/order/detailForm';
import sendForm from 'code/menu/order/sendForm';
import { getFormatDate } from 'common/method'


export default function orderGrid(opt){
    var btns = {
        reload:{
            extraClass: btnReloadClass,
            events:{
                click:function(opt){
                    opt.panel.refresh();
                    opt.panel.clearAllItem();
                },
                mouseenter:function(opt){
                    $(opt.btn).addClass('fa-spin');
                },
                mouseleave:function(opt){
                    $(opt.btn).removeClass('fa-spin');
                }
            }
        }
    }
    var operationBtns = {
        edit:{
            extraClass:['fa','fa-eye','btn','btn-sm','btn-warning','btn-space'],
            events:{
                mouseenter:function(opt){
                    opt.btn.tips = layer.tips('查看', opt.btn, {
                        tips: [1, '#ec971f']
                    });
                },
                mouseleave:function(opt){
                    layer.close(opt.btn.tips);
                },
                click:function(opt){
                    console.log(opt.tr.getAttribute('uid'));
                    opt.panel.items['orderForm'] = orderForm({
                        owner:opt.panel,
                        ownerBtn:opt.panel,
                        data:{
                            id:opt.tr.getAttribute('uid')
                        }
                    });
                }
            }
        },
        express:{
            extraClass:['fa','fa-car','btn','btn-sm','btn-success','btn-space'],
            events:{
                mouseenter:function(opt){
                    opt.btn.tips = layer.tips('发货', opt.btn, {
                        tips: [1, '#5cb85c']
                    });
                },
                mouseleave:function(opt){
                    layer.close(opt.btn.tips);
                },
                click:function(opt){
                    console.log(opt.tr.getAttribute('uid'));
                    opt.panel.items['sendForm'] = sendForm({
                        owner:opt.panel,
                        ownerBtn:opt.panel,
                        data:{
                            id:opt.tr.getAttribute('uid')
                        }
                    });
                }
            }
        },
        cash:{
            extraClass:['fa','fa-money','btn','btn-sm','btn-info','btn-space'],
            events:{
                mouseenter:function(opt){
                    opt.btn.tips = layer.tips('收款', opt.btn, {
                        tips: [1, '#5bc0de']
                    });
                },
                mouseleave:function(opt){
                    layer.close(opt.btn.tips);
                },
                click:function(opt){
                    console.log(opt.tr.getAttribute('uid'));
                    opt.panel.items['detailForm'] = detailForm({
                        owner:opt.panel,
                        ownerBtn:opt.panel,
                        data:{
                            id:opt.tr.getAttribute('uid')
                        }
                    });
                }
            }
        }
    }
    var fields=[
        {
            text:'订单编号',
            name:'orderNo',
        },{
            text:'订单商品',
            name:'storeName'
        },{
            text:'商品序号',
            name:'storeId'
        },{
            text:'商品原价',
            name:'total'
        },{
            text:'参与活动',
            name:'offerVoucherName',
        },{
            text:'下单时间',
            name:'createTime',
        },{
            text:'订单状态',
            name:'orderStatus',
            data:{
                HasCancel:'已取消',
                NoPay:'待付款',
                HasPay:'已付款',
                NoReceive:'待收货',
                Received:'已收货',
                Cancel:'订单过期',
                Finished:'交易完成'
            }
        },{
            text:'支付时间',
            name:'payTime',

        },{
            type:'operations',
            text:'操作',
            style:'width:160px;'
        }
    ];

    return new GridPanel(setConfig({
        opt:opt,
        fnName:'cdGrid',
        btns:btns,
        operationBtns:operationBtns,
        classGroup:'GridPanel',
        config:{
            container:windowCenter,
            url:"/order/queryPage",
            type:'get',
            pageSize:10,
            pageNo:1,
            fields:fields,
            title:{
                innerHTML:(opt.title&&opt.title.innerHTML)||'订单管理'
            },
            render:(opt)=>{
                let datas = opt.response.data.root
                $(opt.table).find('tbody').children().map((index,item)=>{
                    if(datas.length != 0){
                        if(datas[index].orderStatus === 'NoPay'){
                            $(item).find('[methodname="express"]').attr('disabled','disabled');
                        }else if(datas[index].orderStatus === 'HasPay'){
                            $(item).find('[methodname="cash"]').attr('disabled','disabled');
                        }else {
                            $(item).find('[methodname="cash"]').attr('disabled','disabled');
                            $(item).find('[methodname="express"]').attr('disabled','disabled');
                        }
                        $(item).children().eq($(item).parent().prev().find('[name="createTime"]').index()).text(getFormatDate({
                            timeStamp: datas[index].createTime,
                            format: 'yyyy-MM-dd HH:mm:ss'
                        }))
                    }
                })
            },
            search:{
                fields:[
                    {
                        name:'orderNo',
                        text:'订单编号',//placeholder
                    },
                    {
                        name:'orderStatus',
                        text:'订单状态',//placeholder
                        type: 'select',
                        async: {
                            url: '/order/getOrderStatusList',
                            type: 'post',
                            data: {
                                send: {
                                    code: 'goods_category'
                                },
                                set: 'value'
                            }
                        }
                    }
                ]
            },
            boxExtraClass:['col-sm-12']
        }
    }));
}

