import $ from 'jquery';
import layer from 'static/layer/layer'
import { btnPlusClass,btnReloadClass,windowCenter,setConfig } from 'common/commonClass';
import GridPanel from 'component/gridPanel';
import adForm from 'code/menu/business/ad/adForm';
import { getFormatDate } from 'common/method'


export default function adGrid(opt){
    var btns = {
        plus:{
            extraClass: btnPlusClass,
            events:{
                click:function(opt){
                    opt.panel.items['adForm'] = adForm({
                        owner:opt.panel,
                        ownerBtn:opt.panel,
                    });
                },
                mouseenter:function(opt){
                    opt.btn.tips = layer.tips('新增广告图', opt.btn, {
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
                    opt.panel.items['adForm'] = adForm({
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
                        url:'/advertising/deleteById',
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
            text:'广告图名称',
            name:'name'
        },{
            text:'所放位置',
            name:'tableName'
        },{
            text:'链接地址',
            name:'linkUrl'
        },{
            text:'添加时间',
            name:'createTime',
        },{
            type:'operations',
            text:'操作',
            style:'width:160px;'
        }
    ];

    return new GridPanel(setConfig({
        opt:opt,
        fnName:'adGrid',
        btns:btns,
        operationBtns:operationBtns,
        classGroup:'GridPanel',
        config:{
            container:windowCenter,
            url:"/advertising/queryPage",
            type:'post',
            pageSize:10,
            pageNo:1,
            fields:fields,
            title:{
                innerHTML:(opt.title&&opt.title.innerHTML)||'广告管理'
            },
            boxExtraClass:['col-sm-12'],
            render:(opt)=>{
                let datas = opt.response.data.root
                $(opt.table).find('tbody').children().map((index,item)=>{
                    $(item).children().eq($(item).parent().prev().find('[name="createTime"]').index()).text(getFormatDate({
                        timeStamp: datas[index].createTime,
                        format: 'yyyy-MM-dd HH:mm:ss'
                    }))
                })
            },
        }
    }));
}

