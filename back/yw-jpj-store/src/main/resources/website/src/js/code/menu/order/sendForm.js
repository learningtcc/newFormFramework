
import { setConfig } from 'common/commonClass';
import WindowPanel from 'component/windowPanel';
import { getFormatDate } from 'common/method';


export default function sendForm(opt){
    var temp_json = opt;
    var fields=[
        {
            column:[
                {
                    label:'订单编号',
                    readonly:true,
                    name:'orderNo',
                    disabled:true
                },{
                    label:'商家名称',
                    name:'storeName',
                    readonly:true,
                    disabled:true

                },{
                    label:'商家编号',
                    name:'storeId',
                    readonly:true,
                    disabled:true

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
                    disabled:true
                },{
                    label:'商品编号',
                    name:'typeIdName',
                    readonly:true,
                    disabled:true
                },{
                    label:'商品名称',
                    name:'speciesIdName',
                    readonly:true,
                    disabled:true
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
                    readonly:true,
                    disabled:true

                },{
                    label:'商品单价',
                    name:'speciesIdName',
                    readonly:true,
                    disabled:true

                },{
                    label:'现场自提/快递',
                    name:'receiptWay',
                    readonly:true,
                    disabled:true

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
                    disabled:true

                },{
                    label:'收货人联系方式',
                    name:'receiptPhone',
                    readonly:true,
                    disabled:true

                },{
                    label:'收货地址',
                    readonly:true,
                    name: 'receiptAdd',
                    tagName: 'textarea',
                    disabled:true,
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
                    label:'下单时间',
                    name:'orderTime',
                    readonly:true,
                    disabled:true

                },{
                    label:'支付时间',
                    name:'payTime',
                    readonly:true,
                },{
                    label:'订单总价',
                    name:'total',
                    readonly:true,
                    disabled:true

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
                    disabled:true

                },{
                    label:'支付金额',
                    name:'disbursements',
                    readonly:true
                },{
                    label:'订单状态',
                    name:'orderStatus',
                    readonly:true,
                    disabled:true
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label:'物流公司',
                    name:'logisticsCompany',
                    required:true
                },{
                    label:'运单编号',
                    name:'trackingNum',
                    required:true,
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        }
    ];
    var btns={
        edit:{
            html:'确定',
            extraClass:['layui-layer-btn0'],
            events:{
                click:function(opt){
                    var loading = layer.load(1,{shade: [0.1,'#fff'] });
                    $.ajax({
                        url:'/order/send',
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
        },
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
        items:[
            setConfig({
                opt:opt,
                fnName:'sendForm',
                config:{
                    xtype:'FormPanel',
                    data:opt.data,
                    fields:fields,
                    queryUrl:'/order/queryById',
                    queryType:'post',
                    render: opt => {
                        $(opt.component).find('input[name="orderTime"]').val(getFormatDate({
                            timeStamp: parseInt($(opt.component).find('input[name="orderTime"]').val()),
                            format: 'yyyy-MM-dd HH:mm:ss'
                        }));
                        let  temp_orderStatus = '';
                        if($(opt.component).find('input[name="orderTime"]').val() == 'NoPay'){
                            temp_orderStatus = '未付款';
                        }else if($(opt.component).find('input[name="orderTime"]').val() == 'HasPay'){
                            temp_orderStatus = '已付款';
                        }else {
                            temp_orderStatus = '已收货';
                        }
                        $(opt.component).find('input[name="orderStatus"]').val(temp_orderStatus);

                        if($(opt.component).find('input[name="receiptWay"]').val() == '现场自提'){
                            $(opt.component).find('input[name="logisticsCompany"]').attr('readonly','true');
                            $(opt.component).find('input[name="trackingNum"]').attr('readonly','true');
                            $('.defineBtns').find('.edit').html('已提货');
                        }
                    }
                }
            })
        ],
        btns:btns,
    });
}

