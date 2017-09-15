/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */
package com.drore.cloud.controller;

import com.drore.cloud.exception.MacroApiException;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.common.util.GsonUtil;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.domain.PaginationMap;
import com.drore.cloud.sdk.domain.metadata.PackageResources;
import com.drore.cloud.sdk.domain.metadata.ResourceDetail;
import com.drore.cloud.sdk.domain.metadata.query.Relation;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.cloud.util.PackageConfigUtils;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/***
 * @since:cloud-ims 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2016/8/5 15:47
 * service接口,该接口是统一接口
 * {service:
 *      {"resource_name":"add",
 *       "sort":[{column:value,rule:"desc"}],
 *       current_page:1,
 *       page_size:10,
 *
 *       display_fields:"id,xx,dd,xx"
 *       ,alias:[{
 *          "rel_resource_name":"",
 *          "rel_field":"",
 *          "main_field":"",
 *       }]
 *       query:{
            "must":[
            {
            prefix:{
            "hotel_name":"h"
            }
            }
            ],
            "must_not":[],
            "should":[]
}
 *       }
 *      }
 *
 */
@Api("公共服务接口(基本的list,detail接口)")
@RestController
@RequestMapping("/api/cms/service")
public class ServiceController {

    @Autowired
    private CloudQueryRunner run;

    @ApiOperation(value = "列表接口",notes = "{service:\n" +
            "       {\"resource_name\":\"add\",\n" +
            "        \"sort\":[{column:value,rule:\"desc\"}],\n" +
            "        current_page:1,\n" +
            "        page_size:10,\n" +
            " \n" +
            "        display_fields:\"id,xx,dd,xx\"\n" +
            "        ,alias:[{\n" +
            "           \"rel_resource_name\":\"\",\n" +
            "           \"rel_field\":\"\",\n" +
            "           \"main_field\":\"\",\n" +
            "        }]\n" +
            "        query:{\n" +
            "            \"must\":[\n" +
            "            {\n" +
            "            prefix:{\n" +
            "            \"hotel_name\":\"h\"\n" +
            "            }\n" +
            "            }\n" +
            "            ],\n" +
            "            \"must_not\":[],\n" +
            "            \"should\":[]\n" +
            "}\n" +
            "        }\n" +
            "       }")
    @PostMapping("/list")
    public PaginationMap<Map<String,Object>> list(@RequestParam(value = "service") String services){
        LogbackLogger.info("services::"+services);
        JsonElement jsonElement=new JsonParser().parse(services);
        if (jsonElement==null||jsonElement.isJsonNull()||!jsonElement.isJsonObject()){
            throw new MacroApiException("请求参数异常");
        }
        JsonObject service=jsonElement.getAsJsonObject();
        //获取resource_name
        String resource_name= GsonUtil.toStringValue(service.get("resource_name"));
        //判断资源是否在exclude_api_resources中
        if (PackageConfigUtils.getSystemConfig().getExcludeApiResources().contains(resource_name)){
            throw new MacroApiException("请求参数非法!!!!!");
        }
        //获取query参数
        String query=GsonUtil.toStringValue(service.get("query"));

        if (StringUtils.isBlank(resource_name)){
            throw new MacroApiException("请求参数异常");
        }
        //查询元数据
        PackageResources pr=run.findOneResourceByName(resource_name);
        if (pr==null){
            throw new MacroApiException("资源不存在");
        }
        //筛选需要的元数据字段
        List<Map<String ,Object>> columns=Lists.transform(pr.getResourceDetails(), new Function<ResourceDetail, Map<String, Object>>() {
            @Override
            public Map<String, Object> apply(ResourceDetail detail) {
                Map<String,Object> column=Maps.newHashMap();
                column.put("rd_name",detail.getRdName());
                column.put("rd_cn_name",detail.getRdCnName());
                column.put("show_name",detail.getShowName());
                column.put("media_type",detail.getMediaType());
                column.put("is_required",detail.getIsRequired());
                column.put("field_type",detail.getFieldType());
                column.put("rd_length",detail.getRdLength());
                column.put("rd_precision",detail.getRdPrecision());
                column.put("is_form_show",detail.getIsFormShow());
                column.put("is_grid_show",detail.getIsGridShow());
                column.put("form_sort",detail.getFormSort());
                column.put("grid_sort",detail.getGridSort());
                column.put("sort",detail.getSort());
                return column;
            }
        });

        //获取分页对象
        Integer current_page=1,page_size=10;
        String cp=GsonUtil.toStringValue(service.get("current_page"));
        String ps=GsonUtil.toStringValue(service.get("page_size"));
        if (StringUtils.isNotBlank(cp)){
            current_page=new BigDecimal(cp).intValue();
        }
        if (StringUtils.isNotBlank(ps)){
            page_size=new BigDecimal(ps).intValue();
        }
        //获取排序字段对象
        JsonElement se=service.get("sort");
        Map<String,String> sort= Maps.newHashMap();
        if(se!=null&&!se.isJsonNull()&&se.isJsonArray()){
            //正确的sort请求字段
            JsonArray sortArr=se.getAsJsonArray();
            List<String> rules= Lists.newArrayList("asc","desc");
            for (JsonElement st:sortArr){
                JsonObject stobj=st.getAsJsonObject();
                String column=GsonUtil.toStringValue(stobj.get("column"));
                String rule=GsonUtil.toStringValue(stobj.get("rule"));
                if (StringUtils.isBlank(rule)){
                    rule="asc";
                }
                if (!rules.contains(StringUtils.lowerCase(rule))){
                    rule="asc";
                }
                sort.put(column,rule);
            }
        }
        //获取关联对象
        JsonElement alias=service.get("alias");
        //创建request对象
        RequestExample req=new RequestExample(page_size,current_page);
        if (StringUtils.isNotBlank(query)){
            req.setQuery(GsonUtil.create().fromJson(query,HashMap.class));
        }
        Pagination<Map> pas=null;
        //判断是否多表关联
        if(alias!=null&&!alias.isJsonNull()&&alias.isJsonArray()){
            //多表关联
            List<Relation> rels=Lists.newArrayList();

            req.setAlias(resource_name);
            JsonArray relsArr=alias.getAsJsonArray();
            PackageResources resource=null;
            for(JsonElement relEle:relsArr){
                JsonObject rel=relEle.getAsJsonObject();
                String rel_resource_name=GsonUtil.toStringValue(rel.get("rel_resource_name"));
                String relField=GsonUtil.toStringValue(rel.get("rel_field"));
                String mainField=GsonUtil.toStringValue(rel.get("main_field"));
                /* 查询关联 */
                resource=run.findOneResourceByName(rel_resource_name);
                Relation relf=new Relation();
                relf.setRelResName(rel_resource_name);
                relf.setRelResAlias(rel_resource_name);
                relf.setRelResField(relField);
                relf.setOnField(rel_resource_name+"."+mainField);
                req.addRelations(relf);
                rels.add(relf);
            }
            //加入排序字段
            req.getSort().putAll(sort);
            pas=run.queryListByExample(resource_name,req);
        }else{
            //加入排序字段
            req.getSort().putAll(sort);
            pas=run.queryListByExample(resource_name,req);
        }
        PaginationMap<Map<String,Object>> pmap=new PaginationMap<Map<String,Object>>();
        pmap.setColumns(columns);
        pmap.setMessage(pas.getMessage());
        pmap.setSuccess(pas.getSuccess());
        pmap.setData(pas.getData());
        pmap.setCount(pas.getCount());
        pmap.setErrCode(pas.getErrCode());
        pmap.setPage_size(pas.getPage_size());
        pmap.setCurrent_page(pas.getCurrent_page());
        pmap.setTotal_page(pas.getTotal_page());
        return pmap;
    }


    /***
     * 查询详情
     * {
     *     service:{
     *         resource_name:"name",
     *         id:"xxxxxxxxxxx",
     *         rels:[
     *              {
     *              rel_type:"o2o",
     *              rel_res_name:"cms_material_info",
     *              rel_res_field:"parent_id"
     *              }
     *         ]
     *     }
     * }
     * @param services
     * @return
     */
    @ApiOperation(value = "列表详情",notes = "{\n" +
            "          service:{\n" +
            "              resource_name:\"name\",\n" +
            "              id:\"xxxxxxxxxxx\",\n" +
            "              rels:[\n" +
            "                   {\n" +
            "                   rel_type:\"o2o\",\n" +
            "                   rel_res_name:\"cms_material_info\",\n" +
            "                   rel_res_field:\"parent_id\"\n" +
            "                   }\n" +
            "              ]\n" +
            "          }\n" +
            "      }")
    @PostMapping("/detail")
    public RestMessage detail(@RequestParam(value = "service") String services){
        JsonElement jsonElement=new JsonParser().parse(services);
        if (jsonElement==null||jsonElement.isJsonNull()||!jsonElement.isJsonObject()){
            throw new MacroApiException("请求参数异常");
        }
        JsonObject service=jsonElement.getAsJsonObject();
        //主表获取resource_name
        String resource_name= GsonUtil.toStringValue(service.get("resource_name"));
        //获取主键id
        String pkid=GsonUtil.toStringValue(service.get("id"));
        Map<String,Object> detail=run.queryOne(resource_name,pkid);
        //获取外键集合
        JsonElement relsobj=service.get("rels");
        //不为空，且必须是数组
        if (relsobj!=null&&!relsobj.isJsonNull()&&relsobj.isJsonArray()){
            JsonArray jsonArray=relsobj.getAsJsonArray();
            Pagination<Map> pagination=null;
            for (JsonElement rel:jsonArray){
                JsonObject relobj=rel.getAsJsonObject();
                String rel_res_name=GsonUtil.toStringValue(relobj.get("rel_res_name"));
                String rel_res_field=GsonUtil.toStringValue(relobj.get("rel_res_field"));
                RequestExample requestExample=new RequestExample(10000,1);
                RequestExample.Criteria rc=requestExample.create();
                RequestExample.Param param=requestExample.createParam();
                param.addTerm(rel_res_field,pkid);
                rc.getMust().add(param);
                //是否有rels
                JsonElement rels=relobj.get("rels");
                if (rels!=null&&!rels.isJsonNull()&&rels.isJsonArray()&&rels.getAsJsonArray().size()>0){
                    requestExample.setAlias(rel_res_name);
                    JsonArray jarrs=rels.getAsJsonArray();
                    for (JsonElement rl:jarrs){
                        String inner_rel_res_name=GsonUtil.toStringValue(rl.getAsJsonObject().get("rel_res_name"));
                        String inner_rel_res_field=GsonUtil.toStringValue(rl.getAsJsonObject().get("rel_res_field"));
                        String inner_main_field=GsonUtil.toStringValue(rl.getAsJsonObject().get("main_field"));
                        //关联表
                        Relation relation=new Relation();
                        relation.setRelResName(inner_rel_res_name);
                        relation.setRelResAlias(inner_rel_res_name);
                        relation.setOnField(rel_res_name+"."+inner_main_field);
                        relation.setRelResField(inner_rel_res_field);
                        requestExample.addRelations(relation);
                    }
                }
                pagination=run.queryListByExample(rel_res_name,requestExample);
                detail.put(rel_res_name,pagination.getData());
            }
        }
        return new RestMessage(detail);
    }



}
