/**
 * Created by Administrator on 2017/1/12.
 */

var ywData = (function($){
    return {
        //读取列表
        list:function(param,callback){
            var resource_name = param.resource_name,
                curpage = param.curpage,
                pagesize = parseInt(param.pagesize);

            var sort = param.sort;
            var dataType =  param.typeAll;
            var qu = new Query(resource_name);
            qu.setCurrentPage(curpage);
            qu.setPageSize(pagesize);
            if (typeof dataType){
                $.each(dataType, function(i,o) {
                    qu.getMust().addFuzzy(i,o);
                });
            }
            if(sort != null){
                qu.addSort(sort.column,sort.rule);
            }

            $.ajax({
                url: "/api/cms/service/list",
                type:"post",
                data: {
                    service: qu.toString()
                },
                dataType: "json",
                success: function(data) {
                    if(typeof(callback) == "function"){
                        try{
                            callback(data);
                            return data;
                        }
                        catch(e){}
                    }
                }
            });
        },
        //读取详情
        detail:function(param,callback){
            var resource_name = param.resource_name,
                id = param.id,
                relations = param.relations;
            var detail = new Query(resource_name);
            detail.setPkId(id);
            detail.addRelation(new Relation("cms_material_info", "parent_id"));

            if (relations != null) {
                for(var i in relations){
                     detail.addRelation(relations[i]);
                }
            }

            $.ajax({
                url: "/api/cms/service/detail",
                type:"post",
                data: {
                    service: detail.toString()
                },
                dataType: "json",
                success: function(data) {
                    if(typeof(callback) == "function"){
                        try{
                            callback(data);
                        }
                        catch(e){}
                    }
                }
            });
        }
    }
})(jQuery);
