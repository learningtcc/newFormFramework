/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */
package com.drore.cloud.service;

import com.drore.cloud.sdk.domain.metadata.io.MaterialInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;

/***
 * MaterialService
 * @since:cloud-service 1.0 
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2016年7月11日 下午3:32:41
 */
public interface MaterialService {
	
	/***
	 * 根据parentId查询资源明细
	 * @param parentId
	 * @return
	 */
	public List<Map> findMaterialsByParentId(String parentId);
	
	/***
	 * 上传图片
	 * @param file
	 * @return
	 */
	public MaterialInfo upload(MultipartFile file, HttpServletRequest request);


	public MaterialInfo uploadFile(File file);
	/***
	 * 上传url流图片
	 * @param url
	 * @return
	 */
	public MaterialInfo uploadUrl(String url);

}
