import $ from 'jquery';
import layer from 'static/layer/layer'
import { btnPlusClass,btnReloadClass,windowCenter,setConfig } from 'common/commonClass';
import { checkActive } from 'common/method';
import { getFormatDate } from 'common/method'
import GridPanel from 'component/gridPanel';
import majorForm from 'code/menu/business/classify/majorForm'

export default function shopsGrid(opt){
    var btns = {
        // plus:{
        //     extraClass: btnPlusClass,
        //     events:{
        //         click:function(opt){
        //             opt.panel.items['majorForm'] = majorForm({
        //                 owner:opt.panel,
        //                 ownerBtn:opt.panel,
        //                 parentId: '0bb9fdd1a3174d4587431eee4932f347',
        //             });
        //         },
        //         mouseenter:function(opt){
        //             opt.btn.tips = layer.tips('新增商家类型', opt.btn, {
        //                 tips: [1, '#286090']
        //             });
        //         },
        //         mouseleave:function(opt){
        //             layer.close(opt.btn.tips);
        //         }
        //     }
        // },
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
                    checkActive({
                        table:opt.panel.table,
                        tr:opt.tr,
                        backgroundColor:'#286090'
                    });
                    opt.panel.clearAllItem();
                    opt.panel.hideOtherRows({
                        tr:opt.tr
                    });
                    opt.panel.items['shopsChildGrid'] = shopsChildGrid({
                        owner:opt.panel,
                        ownerBtn:opt.panel,
                        data:{
                            id:opt.tr.getAttribute('uid')
                        },
                        goodsName:$(opt.tr).children().eq($(opt.tr).parent().prev().find('td[name="name"]').index()).text()
                    });
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
            text:'类型名称',
            name:'name'
        },{
            text:'创建时间',
            name:'create_time'
        },{
            type:'operations',
            text:'操作',
            style:'width:160px;'
        }
    ];

    return new GridPanel(setConfig({
        opt:opt,
        fnName:'shopsGrid',
        btns:btns,
        operationBtns:operationBtns,
        classGroup:'GridPanel',
        config:{
            container:windowCenter,
            url:"/dict/queryListByPage",
            type:'post',
            pageSize:10,
            pageNo:1,
            fields:fields,
            title:{
                innerHTML:(opt.title&&opt.title.innerHTML)||'主营类型管理'
            },
            boxExtraClass:['col-sm-12'],
            search:{
                fields:[
                    {
                        name:'parentId',
                        text:'父键Id',//placeholder
                        value:'0097ed7a03d34b738fe94a828c88ca46',
                        hidden:true
                    }
                ]
            }
        }
    }));
}

export function shopsChildGrid(opt){
    var json = opt;
    var btns = {
        plus:{
            extraClass: btnPlusClass,
            events:{
                click:function(opt){
                    console.log(json.data.id);
                    opt.panel.items['majorForm'] = majorForm({
                        owner:opt.panel,
                        ownerBtn:opt.panel,
                        typeId:json.data.id,
                    });
                },
                mouseenter:function(opt){
                    opt.btn.tips = layer.tips('新增商品种类', opt.btn, {
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
                    opt.panel.items['majorForm'] = majorForm({
                        owner:opt.panel,
                        ownerBtn:opt.panel,
                        data:{
                            id:opt.tr.getAttribute('uid')
                        }
                    });
                }
            }
        },
        delete:{
            extraClass:['fa','fa-trash','btn','btn-sm','btn-danger','btn-space'],
            events:{
                mouseenter:function(opt){
                    opt.btn.tips = layer.tips('删除', opt.btn, {
                        tips: [1, '#d9534f']
                    });
                },
                mouseleave:function(opt){
                    layer.close(opt.btn.tips);
                },
                click:function(opt){
                    $.ajax({
                        url:'/teaSpecies/deleteById',
                        type:'get',
                        data:{
                            id:opt.tr.getAttribute('uid')
                        },
                        success:function(response){
                            if(response.isSuccess){
                                layer.msg('删除成功');
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
            text:'类型名称',
            name:'name'
        },{
            text:'创建时间',
            name:'createTime'
        },{
            type:'operations',
            text:'操作',
            style:'width:160px;'
        }
    ];

    return new GridPanel(setConfig({
        opt:opt,
        fnName:'shopsChildGrid',
        btns:btns,
        operationBtns:operationBtns,
        classGroup:'GridPanel',
        config:{
            container:windowCenter,
            url:"/teaSpecies/findListByTypeId",
            type:'get',
            pageSize:10,
            pageNo:1,
            fields:fields,
            title:{
                innerHTML:(opt.title&&opt.title.innerHTML)||'商品种类管理'
            },
            boxExtraClass:['col-sm-12'],
            render:(opt)=>{
                let datas = opt.response.data
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
                        name:'typeId',
                        text:'Id',//placeholder
                        value:opt.data.id,
                        hidden:true
                    }
                ]
            }
        }
    }));
}
