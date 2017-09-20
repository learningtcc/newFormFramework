import { setConfig } from 'common/commonClass';
import WindowPanel from 'component/windowPanel';

export default function commentForm(opt){
    var temp_json = opt;
    var fields=[
        {
            column:[
                {
                    label:'订单编号',
                    readonly:true,
                    name:'orderNo'
                },{
                    label:'商品编号',
                    name:'commodityId',
                    readonly:true,
                },{
                    label:'商品名称',
                    name:'commodityName',
                    readonly:true,
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label:'用户ID',
                    name:'userId',
                    readonly:true,
                },{
                    label:'评论内容',
                    name:'offerVoucherName',
                    readonly:true
                },{
                    label:'评论时间',
                    name:'createTime',
                    readonly:true
                }
            ],
            extraClass:{
                boxDiv:['col-sm-4']
            }
        },{
            column:[
                {
                    label:'审核状态',
                    name:'auditStatus',
                    readonly:true
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
            area:'1000px',
            title:'查看订单信息'
        },
        btns:btns,
        items:[
            setConfig({
                opt:opt,
                fnName:'commentForm',
                config:{
                    xtype:'FormPanel',
                    data:opt.data,
                    fields:fields,
                    queryUrl:'/commodityEvaluation/queryById',
                    queryType:'post',
                    render: opt => {
                        let  temp_auditStatus = '';
                        if($(opt.component).find('input[name="auditStatus"]').val() == 'PendingAudit'){
                            temp_auditStatus = '待审核';
                        }else if($(opt.component).find('input[name="auditStatus"]').val() == 'AuditFail'){
                            temp_auditStatus = '审核未通过';
                        }else {
                            temp_auditStatus = '审核通过';
                        }
                        $(opt.component).find('input[name="auditStatus"]').val(temp_auditStatus);

                    }
                }
            })
        ]
    });
}
