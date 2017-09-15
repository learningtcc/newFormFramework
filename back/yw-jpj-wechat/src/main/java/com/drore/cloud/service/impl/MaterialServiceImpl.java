/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */
package com.drore.cloud.service.impl;

import com.drore.cloud.constant.ConstantEnum;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.common.util.CommonUtil;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.domain.metadata.io.MaterialInfo;
import com.drore.cloud.sdk.domain.metadata.io.MaterialThumb;
import com.drore.cloud.sdk.domain.metadata.io.MaterialUpload;
import com.drore.cloud.sdk.init.RestBuilder;
import com.drore.cloud.sdk.init.RestBuilderFactory;
import com.drore.cloud.sdk.util.FileUtils;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.cloud.service.MaterialService;
import com.drore.cloud.util.MD5Util;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * MaterialServiceImpl
 * @since:cloud-service 1.0 
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2016年7月11日 下午3:33:12
 */
@Service
public class MaterialServiceImpl implements MaterialService {

	@Autowired
	private CloudQueryRunner run;

	@Override
	public List<Map> findMaterialsByParentId(String parentId) {
		//查询图集
		String sql="select * from %s where is_deleted='%s' and parent_id='%s' order by show_sort asc";
		Pagination<Map> maters=run.sql(String.format(sql, "cms_material_info", ConstantEnum.ToggleEnum.N.name(),parentId), 10000, 1);
		if (maters!=null&&maters.getCount()>0) {
			return maters.getData();
		}
		return new ArrayList<Map>();
	}

	@Override
	public MaterialInfo upload(MultipartFile file, HttpServletRequest request) {
		try {
			System.out.println("file is null;;"+file==null);
			int mul=2*1024*1024;
			MaterialUpload mupload=new MaterialUpload();
			//CommonsMultipartFile cmf = (CommonsMultipartFile) file;
			//String fileType = cmf.getFileItem().getName().substring(cmf.getFileItem().getName().indexOf(".")+1,cmf.getFileItem().getName().length());
			String fileType = file.getOriginalFilename().substring(StringUtils.lastIndexOf(file.getOriginalFilename(), ".")+1);
			System.out.println("fieldType:"+fileType);
			System.out.println("fieldName:"+file.getName());
			System.out.println("OriginalFilename:"+file.getOriginalFilename());
			mupload.setOriginName(file.getName());
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
				//"cms_material_info"图集表
				RestMessage rm=run.insert("cms_material_info", cmsMaterial);
				mat.setId(rm.getId());
			}
			return mat;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public MaterialInfo uploadFile(File file) {
		try {
			RestBuilder rb=RestBuilderFactory.newBuilder();
			int mul=1*1024*1024;
			MaterialUpload mupload=new MaterialUpload();
			String fileType = file.getName().substring(StringUtils.lastIndexOf(file.getName(), ".")+1);
			mupload.setOriginName(file.getName());
			mupload.setMediaType(fileType);
			//判断文件类型,图片添加缩略图
			if(CommonUtil.isFileType(fileType.toUpperCase())){
				MaterialThumb thumb=new MaterialThumb("1080x960","660x_","_x200");
				mupload.setThumb(thumb);
			}
			mupload.setFile(Base64.encodeBase64String(FileUtils.toByte(file)));
			MaterialInfo mat=run.upload(mupload);
			if (mat!=null) {
				//上传成功
				mat.setMediaType(fileType);
				mat.setMediaSize(file.length());
				mat.setOriginalName(file.getName());
				mat.setMediaName(StringUtils.substring(mat.getUrl(), StringUtils.lastIndexOf(mat.getUrl(), "/")+1));
				mat.setMediaSizeMb((file.length()/mul)+"MB");
				mat.setMediaSizeKb(file.length()/1024+"kb");
				mat.setFromIp("127.0.0.1");
				//RestMessage rm=rb.newRecordBuilder().createByRName(BaseTableConstant.material_info, mat);
				//mat.setId(rm.getId());
			}
			return mat;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 对url接口 输出流图片进行上传
	 * */
	@Override
	public MaterialInfo uploadUrl(String url) {
		try {
			int mul=1*1024*1024;
			MaterialUpload mupload=new MaterialUpload();
			System.out.println("url********"+url);
			URL _url = new URL(url);
			InputStream input = _url.openStream();
			String fileType = "jpg";
			mupload.setOriginName("input");
			mupload.setMediaType("jpg");
			//判断文件类型,图片添加缩略图
			if(CommonUtil.isFileType(fileType.toUpperCase())){
				MaterialThumb thumb=new MaterialThumb("1080x960","660x_","_x200");
				mupload.setThumb(thumb);
			}
			byte[] bytes = toByte(input);
			String md5String = MD5Util.getMD5String(bytes);
			System.out.println(md5String);
			mupload.setFile(Base64.encodeBase64String(bytes));
			MaterialInfo mat=run.upload(mupload);
			if (mat!=null) {
				//上传成功
				mat.setMediaType(fileType);
				//mat.setMediaSize(file.length());
				//mat.setOriginalName(file.getName());
				mat.setMediaName(StringUtils.substring(mat.getUrl(), StringUtils.lastIndexOf(mat.getUrl(), "/")+1));
				//mat.setMediaSizeMb((file.length()/mul)+"MB");
				//mat.setMediaSizeKb(file.length()/1024+"kb");
				mat.setFromIp("127.0.0.1");
				//RestMessage rm=rb.newRecordBuilder().createByRName(BaseTableConstant.material_info, mat);
				//mat.setId(rm.getId());
			}
			return mat;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static byte[] toByte(InputStream input) {
		{
			byte[] buffer = null;
			try {
				//FileInputStream fis = new FileInputStream(file);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] b = new byte[1024 * 1024];
				int n;
				while ((n = input.read(b)) != -1) {
					bos.write(b, 0, n);
				}
				input.close();
				bos.close();
				buffer = bos.toByteArray();
			} catch (FileNotFoundException e) {
				LogbackLogger.error(e);
			} catch (IOException e) {
				LogbackLogger.error(e);
			}
			return buffer;
		}
	}
}
