package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.exception.CustomException;
import com.drore.model.CommodityInfo;
import com.drore.model.TeaSpecies;
import com.drore.service.CommodityService;
import com.drore.util.JSONObjResult;
import com.drore.util.LoginStoreUserUtil;
import com.drore.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * 浙江卓锐科技股份有限公司 版权所有 ? Copyright 2017<br/>
 * 说明: 商品信息，商家可以进行商品的新增、修改、删除、上下架操作<br/>
 * 项目名称:  <br/>
 * 创建日期: 2017/9/4 14:18<br/>
 * 作者: wdz
 */

@Api(value = "商品管理 wdz 已完成")
@RestController
@RequestMapping("/commodity")
public class CommodityControl {

    @Autowired
    private CommodityService commodityService;

    @ApiOperation(value = "查询详情", notes = "根据主键查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "string")
    })
    @GetMapping("queryById")
    public JSONObject queryById(@RequestParam String id) {
        String logMsg = "查询信息";
        LogbackLogger.info(logMsg + id);
        if (StringUtils.isEmpty(id)) {
            return JSONObjResult.toJSONObj("主键不能为空!");
        }
        CommodityInfo commodityInfo = commodityService.queryDetailById(id);
        return JSONObjResult.toJSONObj(commodityInfo,true,null);
    }


    @ApiOperation(value = "上下架", notes = "上下架详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "string")
    })
    @GetMapping("onOffShelves")
    public JSONObject onOffShelves(@RequestParam String id) {
        String logMsg = "上下架";
        LogbackLogger.info(logMsg + id);
        if (StringUtils.isEmpty(id)) {
            return JSONObjResult.toJSONObj("主键不能为空!");
        }
       RestMessage restMessage  = commodityService.onOffShelves(id,LoginStoreUserUtil.getUserId());
        return JSONObjResult.toJSONObj(restMessage);
    }


    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int"),
            @ApiImplicitParam(name = "name", value = "名称",  dataType = "String"),
            @ApiImplicitParam(name = "typeId", value = "分类主键，下拉框",   dataType = "String")
    })
    @PostMapping(value = "queryPage")
    public JSONObject queryPage(@RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "10") int pageSize, CommodityInfo commodityInfo) {
        String logMsg = "分页查询";
        LogbackLogger.info(logMsg);
        Pagination pagination = new Pagination(pageSize, pageNo);
        commodityInfo.setStoreId(LoginStoreUserUtil.getStoreId());//传入商家信息
        pagination = commodityService.queryByPage(pagination, commodityInfo);
        return JSONObjResult.toJSONObj(new PageUtil(pagination), true, "查询成功");
    }


    /**
     * 保存 已测试
     *
     * @param commodityInfo
     * @return
     */
    @ApiOperation(value="新增",notes="保存信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="price",value="商品价格,0,100000000范围",required=true,dataType="double"),
            @ApiImplicitParam(name="details",value="详情",dataType="string"),
            @ApiImplicitParam(name="standard",value="规格",required=true,dataType="string"),
            @ApiImplicitParam(name="typeId",value="类别，字典下拉框",required=true,dataType="string"),
            @ApiImplicitParam(name="isShelves",value="是否上下架，上架Y，Y|N",required=true,dataType="string"),
            @ApiImplicitParam(name="priceId",value="价格单位，字典下拉框",required=true,dataType="string"),
            @ApiImplicitParam(name="isFeatures",value="是否特色商品，是Y，Y|N",required=true,dataType="string"),
            @ApiImplicitParam(name="name",value="商品名称，1-100",required=true,dataType="string"),
            @ApiImplicitParam(name="speciesId",value="种类，茶种类 根据类型主键加载列表 下拉框",required=true,dataType="string"),
            @ApiImplicitParam(name = "pics",value = "图集，多个逗号隔开",dataType = "string" )
    })
    @PostMapping("save")
    public JSONObject save(@Valid CommodityInfo commodityInfo) {
        String logMsg = "保存信息";
        LogbackLogger.info(logMsg);
        RestMessage rm   = commodityService.save(commodityInfo);
        return JSONObjResult.toJSONObj(rm);
    }


    @ApiOperation(value="修改",notes="修改信息")
    @PostMapping("modify")
    public JSONObject modify(@Valid CommodityInfo commodityInfo) {
        String logMsg = "修改信息";
        LogbackLogger.info(logMsg);
        if(StringUtils.isEmpty(commodityInfo.getId())){
            throw  new CustomException("主键不能为空!");
        }
         RestMessage  rm = commodityService.update(commodityInfo);
        return JSONObjResult.toJSONObj(rm);
    }

    @ApiOperation(value="删除",notes="删除信息")
    @GetMapping("deleteById")
    public JSONObject deleteById(@RequestParam String id) {
        String logMsg = "删除信息";
        LogbackLogger.info(logMsg);
        if(StringUtils.isEmpty(id)){
            throw  new CustomException("主键不能为空!");
        }
        RestMessage  rm = commodityService.deleteById(id);
        return JSONObjResult.toJSONObj(rm);
    }

    @ApiOperation(value = "根据类型主键加载列表", notes = "根据类型主键加载列表")
    @GetMapping("findListByTypeId")
    public JSONObject findListByTypeId(@RequestParam String typeId) {
        String logMsg = "修改信息";
        LogbackLogger.info(logMsg);
        if (StringUtils.isEmpty(typeId)) {
            return JSONObjResult.toJSONObj("类型不能为空");
        }
        List<TeaSpecies> list = commodityService.queryListByTypeId(typeId);
        return JSONObjResult.toJSONObj(list, true, null);
    }


}
