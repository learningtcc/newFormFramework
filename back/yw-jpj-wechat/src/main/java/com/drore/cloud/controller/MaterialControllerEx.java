/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */
package com.drore.cloud.controller;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.metadata.io.MaterialInfo;
import com.drore.cloud.sdk.domain.metadata.io.MaterialUpload;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.cloud.wx.client.CloudWeixinService;
import com.drore.cloud.wx.model.WxMediaAPI;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * @since:cloud-ims 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2016/8/2 11:15
 */
@Api(value = "微信端上传图片")
@RequestMapping("/wechat/material")
@Controller
public class MaterialControllerEx {
    private static final NumberFormat nf = new DecimalFormat("00000");

    @Autowired
    private CloudQueryRunner run;
    @Autowired
    private CloudWeixinService wxService;

    /**
     * 微信图片上传
     * @param serverids
     * @param stuffix
     * @return
     */
    @RequestMapping("upload")
    @ApiImplicitParam(name = "serverids",value = "微信图片的id，如果有多张图片请使用[\"id1\",\"id2\",\"id3\"]",required = true,dataType = "String[]")
    @ResponseBody
    public RestMessage upload(@RequestParam("serverids[]") String serverids, String stuffix) {
        LogbackLogger.info("stuffix:" + stuffix);
        LogbackLogger.info("serverids:" + serverids);
        RestMessage rm = new RestMessage();
        String[]  serveridArry = serverids.replace("[","").replace("]","").split(",");
        List<MaterialInfo> mis = new ArrayList<>();
        String accesstokend = "";
        String strBase64 = "";
        try {
            accesstokend = wxService.getAccessToken();
            LogbackLogger.info("accesstoken:" + accesstokend);
            LogbackLogger.info("appid:" + wxService.getWeixinToken().getAppid());
            LogbackLogger.info("appsecret:" + wxService.getWeixinToken().getAppsecret());
            for (String serverid : serveridArry) {
                LogbackLogger.info("=================>>serverid:" + serverid);
                String newServerid = serverid.replaceAll("\"","");
                strBase64 = WxMediaAPI.downMedia(accesstokend, newServerid, stuffix);
                MaterialUpload upload = new MaterialUpload();
                upload.setOriginName(initUploadFileName(stuffix));
                upload.setMediaType(stuffix);
                upload.setFile(strBase64);
                MaterialInfo mi = run.upload(upload);
                if (mi!=null) {
                    //上传成功
                    Map cmsMaterial= Maps.newHashMap();
                    cmsMaterial.put("url",mi.getUrl());
                    RestMessage rmd =run.insert("cms_material_info", cmsMaterial);
                    mi.setId(rmd.getId());
                }
                //添加到集合
                mis.add(mi);
            }
            rm.setData(mis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogbackLogger.info("rm:" + new Gson().toJsonTree(rm));
        return rm;
    }

    private String initUploadFileName(String stuffix) {
        String fileName = "";
        String random = nf.format((int) (Math.random() * 100000));
        fileName = new StringBuffer().append(random).append(".").append(stuffix).toString();
        return fileName;
    }


    //base64字符串转化成图片
    public static boolean GenerateImage(String imgStr){   //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try
        {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            //生成jpeg图片
            String imgFilePath = "/home/222.jpg";//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public static void main(String[] args) {
        String ss = "[\"1237378768e7q8e7r8qwesafdasdfasdfaxss111\",\"1237378768e7q8e7r8qwesafdasdfasdfaxss111\"]\n";
        String hh = ss.replace("[","").replace("]","");
        String[] dd =  hh.split(",");
        for(String aa : dd){
            String newServerid = aa.replaceAll("\"","");
            System.out.println(newServerid);
        }
    }
}
