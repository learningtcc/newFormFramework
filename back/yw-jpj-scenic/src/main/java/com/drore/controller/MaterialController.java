/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */
package com.drore.controller;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.metadata.io.MaterialInfo;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.service.MaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***
 * @since:cloud-ims 1.0
 * @author <a href="mailto:baoec@drore.com">baoec@drore.com </a>
 * 2017/9/4 11:15
 */
@Api(value = "素材")
@RequestMapping("/cms/material")
@Controller
public class MaterialController {

    @Autowired
    MaterialService materialService;

    @ApiOperation(value = "文件素材上传接口")
    @ApiImplicitParam(name = "file",value = "文件流对象,接收数组格式",required = true,dataType = "MultipartFile")
    @RequestMapping(value="/uploadMaterial",method = RequestMethod.POST)
    @ResponseBody
    public RestMessage uploadMaterial(@RequestParam(value="file") MultipartFile[] files, HttpServletRequest request) throws IOException {
        List<MaterialInfo> maters=new ArrayList<MaterialInfo>();
        LogbackLogger.info("进入图片上传接口:"+files.length +"张");
        for (MultipartFile file : files) {
            MaterialInfo mat=materialService.upload(file, request);
            if (mat!=null) {
                maters.add(mat);
            }
        }
        RestMessage rm=new RestMessage();
        rm.setData(maters);
        return rm;
    }
}
