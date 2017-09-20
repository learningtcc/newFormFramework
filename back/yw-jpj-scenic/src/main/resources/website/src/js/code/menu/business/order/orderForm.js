import { setConfig } from 'common/commonClass';
import WindowPanel from 'component/windowPanel';

export default function orderForm(opt){
    var temp_json = opt;
    var fields=[
        {
            column:[
                {
                    label:'订单编号',
                    readonly:true,
                    name:'orderNo'
                },{
                    label:'商家名称',
                    name:'storeName',
                    readonly:true,
                },{
                    label:'商家编号',
                    name:'storeId',
                    readonly:true,
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label:'运费',
                    name:'fee',
                    readonly:true,
                },{
                    label:'商品编号',
                    name:'typeIdName',
                    readonly:true
                },{
                    label:'商品名称',
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
                    label:'购买数量',
                    name:'speciesIdName',
                    readonly:true
                },{
                    label:'商品单价',
                    name:'speciesIdName',
                    readonly:true
                },{
                    label:'现场自提/快递',
                    name:'price',
                    readonly:true
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
              {
                    label:'收货人姓名',
                    name:'receiptPeople',
                    readonly:true,
                },{
                    label:'收货人联系方式',
                    name:'receiptPhone',
                    readonly:true
                },{
                    label:'收货地址',
                    name:'receiptAdd',
                    readonly:true,
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label:'下单时间',
                    name:'orderTime',
                    readonly:true
                },{
                    label:'支付时间',
                    name:'payTime',
                    readonly:true,
                },{
                    label:'订单总价',
                    name:'total',
                    readonly:true
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label:'优惠活动',
                    name:'offerVoucherName',
                    readonly:true,
                },
                {
                    label:'支付金额',
                    name:'disbursements',
                    readonly:true
                },{
                    label:'订单状态',
                    name:'orderStatus',
                    readonly:true,
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label:'物流公司',
                    name:'receiptWay',
                    readonly:true
                },{
                    label:'运单编号',
                    name:'trackingNum',
                    readonly:true,
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        }
    ];
    var btns={
        closeBtn:{
            html:'关闭',
            extraClass:['layui-layer-btn0'],
            events:{
                click:function(opt){
                    layer.closeAll();
                }
            }
        }
    }
    return new WindowPanel({
        owner:opt.owner,
        layerConfig:{
            area:'1200px',
            title:'查看订单信息'
        },
        btns:btns,
        items:[
            setConfig({
                opt:opt,
                fnName:'orderForm',
                config:{
                    xtype:'FormPanel',
                    data:opt.data,
                    fields:fields,
                    queryUrl:'/order/queryById',
                    queryType:'post',
                    render: opt => {
                        let  temp_orderStatus = '';
                        if($(opt.component).find('input[name="orderStatus"]').val() == 'HasCancel'){
                            temp_orderStatus = '已取消';
                        }else if($(opt.component).find('input[name="orderStatus"]').val() == 'NoPay'){
                            temp_orderStatus = '待付款';
                        }else if($(opt.component).find('input[name="orderStatus"]').val() == 'HasPay'){
                            temp_orderStatus = '已付款';
                        }else if($(opt.component).find('input[name="orderStatus"]').val() == 'NoReceive'){
                            temp_orderStatus = '待收货';
                        }else if($(opt.component).find('input[name="orderStatus"]').val() == 'Received'){
                            temp_orderStatus = '已收货';
                        }else if($(opt.component).find('input[name="orderStatus"]').val() == 'Cancel'){
                            temp_orderStatus = '订单过期';
                        }else {
                            temp_orderStatus = '交易完成';
                        }
                        $(opt.component).find('input[name="orderStatus"]').val(temp_orderStatus);

                    }
                }
            })
        ]
    });
}
