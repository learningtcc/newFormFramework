import $ from 'jquery';
import layer from 'static/layer/layer'
import { btnPlusClass,btnReloadClass,windowCenter,setConfig } from 'common/commonClass';
import GridPanel from 'component/gridPanel';
import shopsForm from 'code/menu/business/classify/shopsForm';


export default function shopsGrid(opt){
    var btns = {
        plus:{
            extraClass: btnPlusClass,
            events:{
                click:function(opt){
                    opt.panel.items['shopsForm'] = shopsForm({
                        owner:opt.panel,
                        ownerBtn:opt.panel,
                        parentId: '0bb9fdd1a3174d4587431eee4932f347',
                    });
                },
                mouseenter:function(opt){
                    opt.btn.tips = layer.tips('新增商家类型', opt.btn, {
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
                    opt.panel.items['shopsForm'] = shopsForm({
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
        fnName:'cdGrid',
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
                innerHTML:(opt.title&&opt.title.innerHTML)||'商家类型管理'
            },
            boxExtraClass:['col-sm-12'],
            search:{
                fields:[
                    {
                        name:'parentId',
                        text:'父键Id',//placeholder
                        value:'0bb9fdd1a3174d4587431eee4932f347',
                        hidden:true
                    }
                ]
            }
        }
    }));
}

