import $ from 'jquery';
import layer from 'static/layer/layer'
import { btnPlusClass,btnReloadClass,windowCenter,setConfig } from 'common/commonClass';
import GridPanel from 'component/gridPanel';
import { getFormatDate } from 'common/method'
import accountForm from 'code/menu/business/business/accountForm'
import detailForm from 'code/menu/business/business/detailForm'

export default function businessGrid(opt){
    var btns = {
        plus:{
            extraClass: btnPlusClass,
            events:{
                click:function(opt){
                    opt.panel.items['accountForm'] = accountForm({
                        owner:opt.panel,
                        ownerBtn:opt.panel,
                    });
                },
                mouseenter:function(opt){
                    opt.btn.tips = layer.tips('新增商家', opt.btn, {
                        tips: [1, '#286090']
                    });
                },
                mouseleave:function(opt){
                    layer.close(opt.btn.tips);
                }
            }
        },
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
        info:{
            extraClass:['fa','fa-pencil-square','btn','btn-sm','btn-info','btn-space'],
            events:{
                mouseenter:function(opt){
                    opt.btn.tips = layer.tips('查看账户信息', opt.btn, {
                        tips: [1, '#5bc0de']
                    });
                },
                mouseleave:function(opt){
                    layer.close(opt.btn.tips);
                },
                click:function(opt){
                    opt.panel.items['accountForm'] = accountForm({
                        owner:opt.panel,
                        ownerBtn:opt.panel,
                        data:{
                            id:opt.tr.getAttribute('uid')
                        }
                    });
                }
            }
        },
        edit:{
            extraClass:['fa','fa-edit','btn','btn-sm','btn-warning','btn-space'],
            events:{
                mouseenter:function(opt){
                    opt.btn.tips = layer.tips('编辑', opt.btn, {
                        tips: [1, '#ec971f']
                    });
                },
                mouseleave:function(opt){
                    layer.close(opt.btn.tips);
                },
                click:function(opt){
                    opt.panel.items['detailForm'] = detailForm({
                        owner:opt.panel,
                        ownerBtn:opt.panel,
                        data:{
                            id:opt.tr.getAttribute('uid')
                        }
                    });
                }
            }
        },
        top:{
            extraClass:['fa','fa-hand-pointer-o','btn','btn-sm','btn-success','btn-space'],
            events:{
                mouseenter:function(opt){
                    opt.btn.tips = layer.tips('置顶热门商铺，只能置顶一个商铺', opt.btn, {
                        tips: [1, '#5cb85c']
                    });
                },
                mouseleave:function(opt){
                    layer.close(opt.btn.tips);
                },
                click:function(opt){
                    $.ajax({
                        url:'/store/updateHot',
                        type:'post',
                        data:{
                            id:opt.tr.getAttribute('uid')
                        },
                        success:function(response){
                            if(response.isSuccess){
                                layer.msg('设置热门成功!');
                                opt.panel.refresh();
                            }else{
                                layer.alert(response.errorMessage,function(){});
                            }
                        },
                        error:function(response){
                            layer.alert(response.errorMessage,function(){});
                        }
                    })
                }
            }
        }
    }
    var fields=[
        {
            text:'序号',
            type:'increment',
            style:'width:45px;'
        },{
            text:'商家名称',
            name:'name'
        },{
            text:'联系人',
            name:'contactPerson'
        },{
            text:'联系方式',
            name:'contactPhone'
        },{
            text:'是否是热门商铺',
            name:'isHot',
            data:{
                Y:'是',
                N:'否'
            }

        },{
            text:'联系地址',
            name:'address',
        },{
            text:'发布状态',
            name:'isRelease',
            data:{
                Y:'已发布',
                N:'待发布'
            }
        },{
            text:'发布时间',
            name:'createTime',
        },{
            type:'operations',
            text:'操作',
            style:'width:160px;'
        }
    ];

    return new GridPanel(setConfig({
        opt:opt,
        fnName:'businessGrid',
        btns:btns,
        operationBtns:operationBtns,
        classGroup:'GridPanel',
        config:{
            container:windowCenter,
            url:"/store/queryPage",
            type:'post',
            pageSize:10,
            pageNo:1,
            fields:fields,
            title:{
                innerHTML:(opt.title&&opt.title.innerHTML)||'商家管理'
            },
            boxExtraClass:['col-sm-12'],
            render:(opt)=>{
                let datas = opt.response.data.root
                $(opt.table).find('tbody').children().map((index,item)=>{
                    if(datas[index].isHot === 'Y'){
                        $(item).find('[methodname="top"]').attr('disabled','disabled')
                    }
                    $(item).children().eq($(item).parent().prev().find('[name="createTime"]').index()).text(getFormatDate({
                        timeStamp: datas[index].createTime,
                        format: 'yyyy-MM-dd HH:mm:ss'
                    }))
                })
            },
            search:{
                fields:[
                    {
                        name:'name',
                        text:'商家名称',//placeholder
                    },
                    {
                        name:'contactPerson',
                        text:'负责人',//placeholder
                    },
                    {
                        name:'contactPhone',
                        text:'负责人联系方式',//placeholder
                    },{
                        name:'startTime',
                        text:'请选择起始日期',
                        type:'date'
                    },{
                        name:'endTime',
                        text:'结束日期',
                        type:'date'
                    }
                ]
            }
        }
    }));
}

