import $ from 'jquery';
import layer from 'static/layer/layer'
import { btnPlusClass,btnReloadClass,windowCenter,setConfig } from 'common/commonClass';
import GridPanel from 'component/gridPanel';
import goodsForm from 'code/menu/goods/goodsForm';
import detailForm from 'code/menu/goods/detailForm';

export default function goodsGrid(opt){
    var btns = {
        plus:{
            extraClass: btnPlusClass,
            events:{
                click:function(opt){
                    opt.panel.items['goodsForm'] = goodsForm({
                        owner:opt.panel,
                        ownerBtn:opt.panel,
                    });
                },
                mouseenter:function(opt){
                    opt.btn.tips = layer.tips('新增商品信息', opt.btn, {
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
                    console.log(opt.tr.getAttribute('uid'));
                    opt.panel.items['goodsForm'] = goodsForm({
                        owner:opt.panel,
                        ownerBtn:opt.panel,
                        data:{
                            id:opt.tr.getAttribute('uid')
                        }
                    });
                }
            }
        },
        see:{
            extraClass:['fa','fa-eye','btn','btn-sm','btn-success','btn-space'],
            events:{
                mouseenter:function(opt){
                    opt.btn.tips = layer.tips('查看', opt.btn, {
                        tips: [1, '#5cb85c']
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
        },
        // top:{
        //     extraClass:['fa','fa-hand-pointer-o','btn','btn-sm','btn-success','btn-space'],
        //     events:{
        //         mouseenter:function(opt){
        //             opt.btn.tips = layer.tips('特色商品设置', opt.btn, {
        //                 tips: [1, '#5cb85c']
        //             });
        //         },
        //         mouseleave:function(opt){
        //             layer.close(opt.btn.tips);
        //         },
        //         click:function(opt){
        //             $.ajax({
        //
        //             })
        //         }
        //     }
        // },
        isEnable:{
            extraClass:['fa','fa-hand-pointer-o','btn','btn-sm','btn-info','btn-space'],
            events:{
                mouseenter:function(opt){
                    opt.btn.tips = layer.tips('上下架设置', opt.btn, {
                        tips: [1, '#5bc0de']
                    });
                },
                mouseleave:function(opt){
                    layer.close(opt.btn.tips);
                },
                click:function(opt){
                    $.ajax({
                        url:'/commodity/onOffShelves',
                        type:'get',
                        data:{
                            id:opt.tr.getAttribute('uid')
                        },
                        success:function(response){
                            if(response.isSuccess){
                                layer.msg('设置成功!');
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
                        url:'/commodity/deleteById',
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
            text:'商品序号',
            type:'increment',
            style:'width:45px;'
        },{
            text:'商品名称',
            name:'name'
        },{
            text:'商品类别',
            name:'typeIdName'
        },{
            text:'商品种类',
            name:'speciesIdName'
        },{
            text:'是否是特色商品',
            name:'isFeatures',
            data:{
                Y:'是',
                N:'否'
            }
        },{
            text:'上下架',
            name:'isShelves',
            data:{
                Y:'上架',
                N:'下架'
            }
        },{
            type:'operations',
            text:'操作',
            style:'width:260px;'
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
            url:"/commodity/queryPage",
            type:'post',
            pageSize:10,
            pageNo:1,
            fields:fields,
            title:{
                innerHTML:(opt.title&&opt.title.innerHTML)||'商品管理'
            },
            render:(opt)=>{
                let datas = opt.response.data.root
                $(opt.table).find('tbody').children().map((index,item)=>{
                    if(datas.length != 0){
                        if(datas[index].isShelves === 'Y'){
                            $(item).find('[methodname="edit"]').attr('disabled','disabled')
                        }
                    }
                })
            },
            search:{
                fields:[
                    {
                        name:'name',
                        text:'商品名称',//placeholder
                    },
                    {
                        name:'typeId',
                        text:'商品类别',//placeholder
                        type: 'select',
                        async: {
                            url: '/dict/queryByCode',
                            type: 'post',
                            data: {
                                send: {
                                    code: 'goods_category'
                                },
                                set: 'id'
                            }
                        }
                    }
                ]
            },
            boxExtraClass:['col-sm-12']
        }
    }));
}

