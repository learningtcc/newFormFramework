package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.constant.CommonConstant;
import com.drore.domain.DataDictionary;
import com.drore.service.DataDictionaryService;
import com.drore.util.JSONObjResult;
import com.drore.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明: 数据字典控制器<br/>
 * 项目名称: guided-assistant-manage <br/>
 * 创建日期: 2016年8月15日 上午9:20:24 <br/>
 * 作者: wdz
 */
@Api("数据字典")
@RestController
@RequestMapping("/dict/")
public class DataDictionaryController {
    @Autowired
    DataDictionaryService dataDictionaryService;

    /**
     * 校验编码唯一性
     * 
     * @param code
     * @return
     */
    @ApiOperation(value="校验编码唯一性",notes="校验编码唯一性")
    @ApiImplicitParams({
            @ApiImplicitParam(name="code",value="编码,长度50",required=true,dataType="string")
    })
    @PostMapping(value = "checkUniqueCode")
    public JSONObject checkUniqueCode(@RequestParam String code) {
        String logMsg = "校验编码唯一性";
        LogbackLogger.info(logMsg + code);
        RestMessage rm = new RestMessage();
        rm.setSuccess(false);
        if (StringUtils.isEmpty(code)) {
            return JSONObjResult.toJSONObj("代码不能为空");
        }
        if (!dataDictionaryService.checkUniqueCode(null, code)) {
            return JSONObjResult.toJSONObj("系统中已存在此编号");
        } else {
            rm.setSuccess(true);
        }
        return JSONObjResult.toJSONObj(rm);

    }

    /**
     * 根据主键删除
     * 
     * @param id
     * @return
     */
    @ApiOperation(value="删除",notes="根据主键删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键",required=true,dataType="string")
    })
    @GetMapping("removeById")
    public JSONObject removeById(@RequestParam String id) {
        String logMsg = "查询信息";
        LogbackLogger.info(logMsg + id);
        RestMessage rm=new RestMessage();
        if (StringUtils.isEmpty(id)) {
            rm.setMessage("主键不能为空");
            rm.setSuccess(false);
            return JSONObjResult.toJSONObj(rm);
        }
        rm = dataDictionaryService.deleteById(id);
        return JSONObjResult.toJSONObj(rm);

    }

    /**
     * 查询详情 已测试
     * 
     * @param id
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @ApiOperation(value="查询详情",notes="根据主键查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键",required=true,dataType="string")
    })
    @GetMapping("queryById")
    public JSONObject queryById(@RequestParam String id) {
        String logMsg = "查询信息";
        LogbackLogger.info(logMsg + id);
        RestMessage rm = new RestMessage();
        if (StringUtils.isEmpty(id)) {
            return JSONObjResult.toJSONObj("主键不能为空!");
        }
        DataDictionary dataDictionary = dataDictionaryService.findById(id);
        rm.setData(dataDictionary);
        return JSONObjResult.toJSONObj(rm);

    }

    /**
     * 保存 已测试
     * 
     * @param dictionary
     * @return
     */
    @ApiOperation(value="新增",notes="保存信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name="code",value="编码,长度50",required=true,dataType="string"),
        @ApiImplicitParam(name="name",value="名称，长度50",required=true,dataType="string"),
        @ApiImplicitParam(name="value",value="字典值，长度100",required=true,dataType="string"),
        @ApiImplicitParam(name="description",value="描述，长度255",required=true,dataType="string"),
        @ApiImplicitParam(name="parentId",value="父键，长度50",required=true,dataType="string"),
        @ApiImplicitParam(name="isEnable",value="是否启用，Y|N",required=true,dataType="string")
    })
    @PostMapping("save")
    public JSONObject save(@Valid  DataDictionary dictionary) {
        String logMsg = "保存信息";
        LogbackLogger.info(logMsg);
        RestMessage rm = new RestMessage();
        rm.setSuccess(false);
        if (StringUtils.isEmpty(dictionary.getCode())) {
            return JSONObjResult.toJSONObj("编码不能为空");
        }
        if (!dictionary.getCode().matches(".{0,50}")) {
            return JSONObjResult.toJSONObj("字典编码校验失败");
        }
        if (StringUtils.isNotEmpty(dictionary.getParentId())) {
            if (!dictionary.getParentId().matches(".{0,50}")) {
                return JSONObjResult.toJSONObj("父节点校验失败");
            }
        }
        rm = dataDictionaryService.save(dictionary);
          return JSONObjResult.toJSONObj(rm);
    }

    /**
     * 更新 已测试
     * 
     * @param dictionary
     * @return
     */
    @ApiOperation(value="修改信息",notes="修改信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name="code",value="编码,长度50",required=true,dataType="string"),
        @ApiImplicitParam(name="name",value="名称，长度50",required=true,dataType="string"),
        @ApiImplicitParam(name="value",value="字典值，长度100",required=true,dataType="string"),
        @ApiImplicitParam(name="description",value="描述，长度255",required=true,dataType="string"),
        @ApiImplicitParam(name="parentId",value="父键，长度50",required=true,dataType="string"),
        @ApiImplicitParam(name="isEnable",value="是否启用，Y|N",required=true,dataType="string")
    })
    @PostMapping("update")
    public JSONObject update(@Valid  DataDictionary dictionary) {
        String logMsg = "修改信息";
        LogbackLogger.info(logMsg);
        RestMessage rm = new RestMessage();
        rm.setSuccess(false);
        if (StringUtils.isEmpty(dictionary.getId())) {
            return JSONObjResult.toJSONObj("主键不能为空");
        }
        rm = dataDictionaryService.update(dictionary);
        return JSONObjResult.toJSONObj(rm);
    }

    /**
     * 根据是否启用状态查询 已测试
     * 
     * @param enable
     * @return
     */
    @ApiOperation(value="根据是否启用状态查询",notes="根据是否启用状态查询")
    @ApiImplicitParams({
        @ApiImplicitParam(name="parentId",value="父键，长度50",required=true,dataType="string"),
        @ApiImplicitParam(name="enable",value="是否启用，Y|N",required=true,dataType="string")
    })
    @PostMapping("queryListByParentEnable")
    public JSONObject queryListByParentEnable(@RequestParam String parentId,
            @RequestParam String enable) {
        String logMsg = "根据是否启用状态查询";
        LogbackLogger.info(logMsg);
        List<DataDictionary> list = dataDictionaryService
                .findListByParentEnable(parentId, enable);
        RestMessage rm = new RestMessage();
        rm.setData(list);
        return JSONObjResult.toJSONObj(rm);
    }

    /**
     * 分页查询 已测试
     * 
     * @param dataDictionary
     * @return
     */
    @ApiOperation(value="分页查询",notes="分页查询")
    @ApiImplicitParams({
        @ApiImplicitParam(name="parentId",value="父键，长度50",required=true,dataType="string"),
        @ApiImplicitParam(name = "pageNo", value = "第几页", required = true, dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int")
    })
    @PostMapping("queryListByPage")
    public JSONObject queryListByPage(int pageNo, int pageSize,
            DataDictionary dataDictionary) {
        String logMsg = "分页查询";
        LogbackLogger.info(logMsg);
        PageUtil pageUtil = new PageUtil(pageSize,pageNo);
          pageUtil = dataDictionaryService.findListByPage(pageUtil
                ,dataDictionary);
        RestMessage rm = new RestMessage();
        rm.setData(pageUtil);
        return JSONObjResult.toJSONObj(rm);
    }

    /**
     * 根据编码查询 已测试
     * 
     * @param code
     * @param enable
     * @return
     */
    @ApiOperation(value="根据编码查询",notes="根据编码查询列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name="code",value="编码,长度50",required=true,dataType="string"),
        @ApiImplicitParam(name="enable",value="是否启用，Y|N",required=true,dataType="string")
    })
    @PostMapping("queryByCode")
    public JSONObject queryByCode(@RequestParam String code, String enable) {
        String logMsg = "根据编码查询列表";
        LogbackLogger.info(logMsg);
        List<DataDictionary> list = dataDictionaryService.findListByCode(code,
                enable);
        RestMessage rm = new RestMessage();
        rm.setData(list);
        return JSONObjResult.toJSONObj(rm);
    }

    /**
     * 根据父节点查询子节点（非递归）
     * 
     * @param parentId
     * @return
     */
    @ApiOperation(value="根据父id查询",notes="根据父id查询列表")
    @ApiImplicitParam(name="parentId",value=" 父鍵，长度50",required=true,dataType="string")
    @PostMapping("queryByParentId")
    public JSONObject queryByParentId(String parentId) {
        String logMsg = "根据父节点查询子节点（非递归）";
        LogbackLogger.info(logMsg);
        if (StringUtils.isEmpty(parentId)) {
            parentId = CommonConstant.ROOT;
        }
        List<DataDictionary> queryByParentId = dataDictionaryService
                .queryByParentId(parentId, null);
        RestMessage rm=new RestMessage();
        rm.setData(queryByParentId);
        return JSONObjResult.toJSONObj(rm);
    }

}
