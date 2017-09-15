/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */
package com.drore.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.common.util.CommonUtil;
import com.drore.cloud.sdk.domain.metadata.io.MaterialInfo;
import com.drore.cloud.sdk.domain.metadata.io.MaterialThumb;
import com.drore.cloud.sdk.domain.metadata.io.MaterialUpload;
import com.drore.service.MaterialService;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/***
 * MaterialServiceImpl
 * @since:cloud-service 1.0
 * @author <a href="mailto:baoec@drore.com">baoec@drore.com </a>
 * 2017年9月4日 下午3:33:12
 */
@Service
public class MaterialServiceImpl implements MaterialService {

	@Autowired
	private CloudQueryRunner run;


	@Override
	public MaterialInfo upload(MultipartFile file, HttpServletRequest request) {
		try {
			System.out.println("file is null;;"+file==null);
			int mul=1*1024*1024;
			MaterialUpload mupload=new MaterialUpload();
			System.out.println(file.getOriginalFilename());
			String fileType = file.getOriginalFilename().substring(StringUtils.lastIndexOf(file.getOriginalFilename(), ".")+1);
			System.out.println("fieldType:"+fileType);
			System.out.println("fieldName:"+file.getName());
			mupload.setOriginName(file.getOriginalFilename());
			//针对手机上传的情况
			mupload.setMediaType(StringUtils.equalsIgnoreCase(fileType, "jpeg")==true?"jpg":fileType);
			System.out.println("fie" +
					"ldType:"+mupload.getMediaType());
			//判断文件类型,图片添加缩略图
			if(CommonUtil.isFileType(fileType.toUpperCase())){
				MaterialThumb thumb=new MaterialThumb("1080x960","660x_","_x200");
				mupload.setThumb(thumb);
			}
			mupload.setFile(Base64.encodeBase64String(file.getBytes()));
			//MaterialInfo mat=mb.upload(mupload);
			MaterialInfo mat=run.upload(mupload);
			if (mat!=null) {
				//上传成功
				Map cmsMaterial= Maps.newHashMap();
				cmsMaterial.put("media_type",fileType);
				cmsMaterial.put("media_size",file.getSize());
				cmsMaterial.put("original_name",file.getOriginalFilename());
				cmsMaterial.put("media_name",StringUtils.substring(mat.getUrl(), StringUtils.lastIndexOf(mat.getUrl(), "/")+1));
				cmsMaterial.put("media_size_mb",(file.getSize()/mul)+"MB");
				cmsMaterial.put("media_size_kb",file.getSize()/1024+"kb");
				cmsMaterial.put("from_ip",CommonUtil.getIpAddr(request));
				cmsMaterial.put("url",mat.getUrl());
				//针对图片处理
				if (mat.getThumb()!=null){
					cmsMaterial.put("thumb_fullsize",mat.getThumb().getFullsize());
					cmsMaterial.put("thumb_medium",mat.getThumb().getMedium());
					cmsMaterial.put("thumb_small",mat.getThumb().getSmall());
				}
				RestMessage rm=run.insert("cms_material_info", cmsMaterial);
				mat.setId(rm.getId());
			}
			return mat;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



}
