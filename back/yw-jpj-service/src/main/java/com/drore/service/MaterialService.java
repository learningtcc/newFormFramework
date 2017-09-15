/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */
package com.drore.service;

import com.drore.cloud.sdk.domain.metadata.io.MaterialInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/***
 * MaterialService
 * @since:cloud-service 1.0
 * @author <a href="mailto:baoec@drore.com">baoec@drore.com </a>
 * 2017年9月4日 下午3:32:41
 */
public interface MaterialService {
	/***
	 * 上传图片
	 * @param file
	 * @return
	 */
	public MaterialInfo upload(MultipartFile file, HttpServletRequest request);


}
