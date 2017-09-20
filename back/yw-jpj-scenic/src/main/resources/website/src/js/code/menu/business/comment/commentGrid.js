import $ from 'jquery';
import layer from 'static/layer/layer'
import { btnPlusClass,btnReloadClass,windowCenter,setConfig } from 'common/commonClass';
import GridPanel from 'component/gridPanel';
import commentForm from 'code/menu/business/comment/commentForm';
import { getFormatDate } from 'common/method';

export default function commentGrid(opt){
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
                    opt.panel.items['commentForm'] = commentForm({
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
            text:'商品编号',
            name:'commodityId'
        },{
            text:'商品名称',
            name:'commodityName'
        },{
            text:'用户ID',
            name:'userId'
        },{
            text:'评论内容',
            name:'offerVoucherName',
        },{
            text:'评论时间',
            name:'createTime',
        },{
            text:'审核状态',
            name:'auditStatus',
            data:{
                PendingAudit:'待审核',
                AuditFail:'审核未通过',
                Audited:'审核通过'
            }
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
            url:"/commodityEvaluation/queryPage",
            type:'post',
            pageSize:10,
            pageNo:1,
            fields:fields,
            title:{
                innerHTML:(opt.title&&opt.title.innerHTML)||'评价管理'
            },
            render:(opt)=>{
                let datas = opt.response.data.root
                $(opt.table).find('tbody').children().map((index,item)=>{
                    $(item).children().eq($(item).parent().prev().find('[name="createTime"]').index()).text(getFormatDate({
                        timeStamp: datas[index].createTime,
                        format: 'yyyy-MM-dd HH:mm:ss'
                    }))
                })
            },
            search:{
                fields:[
                    {
                        name:'orderNo',
                        text:'订单编号',//placeholder
                    },
                    {
                        name:'auditStatus',
                        text:'审核状态',//placeholder
                        type: 'select',
                        async: {
                            url: '/auditManagement/getAuditStatusList',
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

