package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.domain.metadata.io.MaterialInfo;
import com.drore.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 说明: 公共接口
 * 项目名称: taihu-rent
 * 创建时间: 2017/5/19 16:46
 * 作者: xiangwb
 */
@Controller
@RequestMapping("/common")
public class CommonControl {
    @Autowired
    MaterialService materialService;
    /**
     * 上传图片
     */
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject uploadImage(
            @RequestParam("imageFile") MultipartFile resourceFile,HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        boolean success = false;
        String errorMessage = "";
        String resourceUrl = null;
        String resourceId = null;
        try {
            if (!resourceFile.isEmpty()) {
                byte[] fileBytes = resourceFile.getBytes();
                String fileName = resourceFile.getOriginalFilename();
                String[] names = fileName.split("\\.");
//				if(!"jpe".equalsIgnoreCase(names[names.length-1])){
//					jsonObject.put("success", false);
//					jsonObject.put("errorMessage", "格式不正确");
//					jsonObject.put("resourceUrl", "");
//					return jsonObject;
//				}
                MaterialInfo materialInfo = materialService.upload(resourceFile,request);
                if (materialInfo != null) {
                    resourceUrl = materialInfo.getUrl();
                    resourceId = materialInfo.getId();
                    success = true;
                } else {
                    errorMessage = "上传失败";
                }
            } else
                errorMessage = "未发现文件";
        } catch (Exception e) {
            errorMessage = "上传失败";
        }
        jsonObject.put("isSuccess", success);
        jsonObject.put("errorMessage", errorMessage);
        jsonObject.put("resourceUrl", resourceUrl);
        jsonObject.put("resourceId", resourceId);
        return jsonObject;
    }


    /**
     * 上传视频
     */
    @RequestMapping(value = "/uploadVideo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject uploadVideo(
            @RequestParam("imageVideo") MultipartFile resourceFile,HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        boolean success = false;
        String errorMessage = "";
        String resourceUrl = null;
        String resourceId = null;
        try {
            if (!resourceFile.isEmpty()) {
                byte[] fileBytes = resourceFile.getBytes();
                String fileName = resourceFile.getOriginalFilename();
                String[] names = fileName.split("\\.");
//				if(!"jpe".equalsIgnoreCase(names[names.length-1])){
//					jsonObject.put("success", false);
//					jsonObject.put("errorMessage", "格式不正确");
//					jsonObject.put("resourceUrl", "");
//					return jsonObject;
//				}
                MaterialInfo materialInfo = materialService.upload(resourceFile,request);
                if (materialInfo != null) {
                    resourceUrl = materialInfo.getUrl();
                    resourceId = materialInfo.getId();
                    success = true;
                } else {
                    errorMessage = "上传失败";
                }
            } else
                errorMessage = "未发现文件";
        } catch (Exception e) {
            errorMessage = "上传失败";
        }
        jsonObject.put("isSuccess", success);
        jsonObject.put("errorMessage", errorMessage);
        jsonObject.put("resourceUrl", resourceUrl);
        jsonObject.put("resourceId", resourceId);
        return jsonObject;
    }
}
