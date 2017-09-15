package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.model.TeaSpecies;
import com.drore.service.TeaSpeciesService;
import com.drore.util.JSONObjResult;
import com.drore.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:   茶种类信息控制器  <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/9/1 14:08  <br/>
 * 作者:    wdz
 */
@Api("商品种类信息")
@RestController
@RequestMapping("/teaSpecies/")
@Controller
public class TeaSpeciesControl {
    @Autowired
    TeaSpeciesService teaSpeciesService;


    /**
     * 查询详情
     *
     * @param id
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
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
        TeaSpecies teaSpecies = teaSpeciesService.queryDetailById(id);
        return JSONObjResult.toJSONObj(teaSpecies,true,null);
    }


    @ApiOperation(value = "删除", notes = "根据主键删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "string")
    })
    @GetMapping("deleteById")
    public JSONObject deleteById(@RequestParam String id) {
        String logMsg = "查询信息";
        LogbackLogger.info(logMsg + id);
        if (StringUtils.isEmpty(id)) {
            return JSONObjResult.toJSONObj("主键不能为空!");
        }
        RestMessage rm = teaSpeciesService.removeById(id);
        return JSONObjResult.toJSONObj(rm);

    }


    /**
     * 保存
     *
     * @param teaSpecies
     * @return
     */
    @ApiOperation(value = "新增", notes = "保存信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "priceLow", value = "最低价,0-10000000", required = true, dataType = "int"),
            @ApiImplicitParam(name = "priceHigh", value = "最高价,0-10000000", required = true, dataType = "int"),
            @ApiImplicitParam(name = "typeId", value = "类型主键，数据字典，长度1-50", required = true, dataType = "string"),
            @ApiImplicitParam(name = "priceUnit", value = "单位，长度1-50", required = true, dataType = "string"),
            @ApiImplicitParam(name = "pics", value = "图集，集合", required = true, dataType = "string"),
            @ApiImplicitParam(name = "themePic", value = "主图", required = true, dataType = "string"),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "string")
    })
    @PostMapping("save")
    public JSONObject save(@Valid TeaSpecies teaSpecies) {
        String logMsg = "保存信息";
        LogbackLogger.info(logMsg);


       /* if (CollectionUtils.isEmpty(teaSpecies.getPics())) {
            return JSONObjResult.toJSONObj("图集不能为空");
        }*/
        RestMessage rm = teaSpeciesService.save(teaSpecies);
        return JSONObjResult.toJSONObj(rm);
    }


    /**
     * 修改
     *
     * @param teaSpecies
     * @return
     */
    @ApiOperation(value = "修改", notes = "修改信息")

    @PostMapping("modify")
    public JSONObject modify(@Valid TeaSpecies teaSpecies) {
        String logMsg = "修改信息";
        LogbackLogger.info(logMsg);

        if (StringUtils.isEmpty(teaSpecies.getId())) {
            return JSONObjResult.toJSONObj("主键不能为空");
        }
       /* if (CollectionUtils.isEmpty(teaSpecies.getPics())) {
            return JSONObjResult.toJSONObj("图集不能为空");
        }*/
        RestMessage rm = teaSpeciesService.update(teaSpecies);
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
        List<TeaSpecies> list = teaSpeciesService.queryListByTypeId(typeId);
        return JSONObjResult.toJSONObj(list, true, null);
    }

    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = true, dataType = "int")
    })
    @PostMapping(value = "queryPage")
    public JSONObject queryPage(@RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "10") int pageSize, TeaSpecies teaSpecies) {
        String logMsg = "分页查询";
        LogbackLogger.info(logMsg);
        Pagination pagination  = new Pagination(pageSize, pageNo);
        pagination = teaSpeciesService.queryByPage(pagination, teaSpecies);
        return JSONObjResult.toJSONObj(new PageUtil(pagination), true, "查询成功");
    }


}
