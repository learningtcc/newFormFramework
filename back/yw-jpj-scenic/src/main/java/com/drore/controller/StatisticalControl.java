package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.domain.vo.ClicksCountVo;
import com.drore.domain.vo.TradeCountVo;
import com.drore.service.OrderInfoService;
import com.drore.service.StatisticalService;
import com.drore.util.DateTimeUtil;
import com.drore.util.JSONObjResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:    统计分析相关的  <br/>
 * 项目名称: yw-jpj-service <br/>
 * 创建日期:  2017/9/6 11:53  <br/>
 * 作者:    wdz
 */
@Api(value = "统计分析相关接口  wdz")
@RestController
@RequestMapping("/statistical/")
public class StatisticalControl {
    @Autowired
    StatisticalService statisticalService;
    @Autowired
    OrderInfoService orderInfoService;

    @ApiOperation(value = "街区文化活动点击数量 已测 ", notes = "街区文化活动点击数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期，格式为yyyy-MM-dd", paramType = "query", required = false, dataType = "string"),
            @ApiImplicitParam(name = "endDate", value = "结束日期，格式为yyyy-MM-dd", paramType = "query", required = false, dataType = "string")
    })
    @GetMapping("countStreetCulture")
    public JSONObject countStreetCulture(@RequestParam  String startDate, @RequestParam  String endDate)throws Exception {
        String logMsg = "街区文化统计";
        LogbackLogger.info(logMsg);
        if(StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)){
            //return  JSONObjResult.toJSONObj("开始日期结束日期不能为空!");
            //默认获取30天数据
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            endDate = simpleDateFormat.format(new Date());
            startDate = DateTimeUtil.DateBefore(endDate, 30);
        }

        DateTimeUtil.checkStrFormat(startDate,DateTimeUtil.NORMALFORMAT);
        DateTimeUtil.checkStrFormat(endDate,DateTimeUtil.NORMALFORMAT);
        List<ClicksCountVo> vos = statisticalService.streetCulture(startDate,endDate);
        return JSONObjResult.toJSONObj(vos, true, "");
    }


    @ApiOperation(value = "主题活动点击数量 已测", notes = "主题活动点击数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期，格式为yyyy-MM-dd", paramType = "query", required = false, dataType = "string"),
            @ApiImplicitParam(name = "endDate", value = "结束日期，格式为yyyy-MM-dd", paramType = "query", required = false, dataType = "string")
    })
    @GetMapping("countThemeActivities")
    public JSONObject countThemeActivities(@RequestParam  String startDate, @RequestParam  String endDate)throws Exception {
        String logMsg = "主题活动点击数量";
        LogbackLogger.info(logMsg);
        if(StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)){
            //return  JSONObjResult.toJSONObj("开始日期结束日期不能为空!");
            //默认获取30天数据
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            endDate = simpleDateFormat.format(new Date());
            startDate = DateTimeUtil.DateBefore(endDate, 30);
        }

        DateTimeUtil.checkStrFormat(startDate,DateTimeUtil.NORMALFORMAT);
        DateTimeUtil.checkStrFormat(endDate,DateTimeUtil.NORMALFORMAT);
        List<ClicksCountVo> vos = statisticalService.themeActivities(startDate,endDate);
        return JSONObjResult.toJSONObj(vos, true, "");
    }


    @ApiOperation(value = "商品访问量分析 已测", notes = "商品访问量分析")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期，格式为yyyy-MM-dd", paramType = "query", required = false, dataType = "string"),
            @ApiImplicitParam(name = "endDate", value = "结束日期，格式为yyyy-MM-dd", paramType = "query", required = false, dataType = "string")
    })
    @GetMapping("countCommodity")
    public JSONObject countCommodity(@RequestParam  String startDate, @RequestParam  String endDate)throws Exception {
        String logMsg = "商品访问量分析";
        LogbackLogger.info(logMsg);
        if(StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)){
            //return  JSONObjResult.toJSONObj("开始日期结束日期不能为空!");
            //默认获取30天数据
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            endDate = simpleDateFormat.format(new Date());
            startDate = DateTimeUtil.DateBefore(endDate, 30);
        }

        DateTimeUtil.checkStrFormat(startDate,DateTimeUtil.NORMALFORMAT);
        DateTimeUtil.checkStrFormat(endDate,DateTimeUtil.NORMALFORMAT);
        List<ClicksCountVo> vos = statisticalService.commodity(startDate,endDate);
        return JSONObjResult.toJSONObj(vos, true, "");
    }

    @ApiOperation(value = "商品销量排行分析 已测", notes = "商品销量排行分析")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期，格式为yyyy-MM-dd", paramType = "query", required = false, dataType = "string"),
            @ApiImplicitParam(name = "endDate", value = "结束日期，格式为yyyy-MM-dd", paramType = "query", required = false, dataType = "string")
    })
    @GetMapping("countCommoditySales")
    public JSONObject countCommoditySales(@RequestParam  String startDate, @RequestParam  String endDate)throws Exception {
        String logMsg = "商品销量分析";
        LogbackLogger.info(logMsg);
        if(StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)){
            //return  JSONObjResult.toJSONObj("开始日期结束日期不能为空!");
            //默认获取30天数据
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            endDate = simpleDateFormat.format(new Date());
            startDate = DateTimeUtil.DateBefore(endDate, 30);
        }
        DateTimeUtil.checkStrFormat(startDate,DateTimeUtil.NORMALFORMAT);
        DateTimeUtil.checkStrFormat(endDate,DateTimeUtil.NORMALFORMAT);
        List<ClicksCountVo> vos = orderInfoService.countCommondiy(startDate,endDate);
        return JSONObjResult.toJSONObj(vos, true, "");
    }

    @ApiOperation(value = "街区交易量分析 已测", notes = "街区交易量分析")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期，格式为yyyy-MM-dd", paramType = "query", required = false, dataType = "string"),
            @ApiImplicitParam(name = "endDate", value = "结束日期，格式为yyyy-MM-dd", paramType = "query", required = false, dataType = "string")
    })
    @GetMapping("totalTrade")
    public JSONObject totalTrade(@RequestParam  String startDate, @RequestParam  String endDate) throws Exception {
        String logMsg = "街区交易量分析";
        LogbackLogger.info(logMsg);
        if(StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)){
            //return  JSONObjResult.toJSONObj("开始日期结束日期不能为空!");
            //默认获取30天数据
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            endDate = simpleDateFormat.format(new Date());
            startDate = DateTimeUtil.DateBefore(endDate, 30);
        }
        DateTimeUtil.checkStrFormat(startDate,DateTimeUtil.NORMALFORMAT);
        DateTimeUtil.checkStrFormat(endDate,DateTimeUtil.NORMALFORMAT);
        int day = DateTimeUtil.daysBetween(startDate,endDate);
        if(day<0 || day>90){
            return  JSONObjResult.toJSONObj("此处最大只支持90天!");
        }
        List<TradeCountVo> vos = orderInfoService.totalTrade(startDate,endDate);
        return JSONObjResult.toJSONObj(vos, true, "");
    }


    @ApiOperation(value = "商铺交易量 已测", notes = "商铺交易量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期，格式为yyyy-MM-dd", paramType = "query", required = false, dataType = "string"),
            @ApiImplicitParam(name = "endDate", value = "结束日期，格式为yyyy-MM-dd", paramType = "query", required = false, dataType = "string")
    })
    @GetMapping("totalTradeStore")
    public JSONObject totalTradeStore(@RequestParam  String startDate, @RequestParam  String endDate) throws Exception {
        String logMsg = "商品销量分析";
        LogbackLogger.info(logMsg);
        if(StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)){
            //return  JSONObjResult.toJSONObj("开始日期结束日期不能为空!");
            //默认获取30天数据
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            endDate = simpleDateFormat.format(new Date());
            startDate = DateTimeUtil.DateBefore(endDate, 30);
        }
        DateTimeUtil.checkStrFormat(startDate,DateTimeUtil.NORMALFORMAT);
        DateTimeUtil.checkStrFormat(endDate,DateTimeUtil.NORMALFORMAT);
        List<TradeCountVo> vos = orderInfoService.totalTradeStore(startDate,endDate);
        return JSONObjResult.toJSONObj(vos, true, "");
    }

    @ApiOperation(value = "商铺访问量", notes = "商铺访问量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期，格式为yyyy-MM-dd", paramType = "query", required = false, dataType = "string"),
            @ApiImplicitParam(name = "endDate", value = "结束日期，格式为yyyy-MM-dd", paramType = "query", required = false, dataType = "string")
    })
    @GetMapping("storeCommodity")
    public JSONObject storeCommodity(@RequestParam  String startDate, @RequestParam  String endDate) throws Exception {
        String logMsg = "商品销量分析";
        LogbackLogger.info(logMsg);
        if(StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)){
            //return  JSONObjResult.toJSONObj("开始日期结束日期不能为空!");
            //默认获取30天数据
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            endDate = simpleDateFormat.format(new Date());
            startDate = DateTimeUtil.DateBefore(endDate, 30);
        }
        DateTimeUtil.checkStrFormat(startDate,DateTimeUtil.NORMALFORMAT);
        DateTimeUtil.checkStrFormat(endDate,DateTimeUtil.NORMALFORMAT);
        List<ClicksCountVo> vos = statisticalService.storeCommodity(startDate,endDate);
        return JSONObjResult.toJSONObj(vos, true, "");
    }



}
