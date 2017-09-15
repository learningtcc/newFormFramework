/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */
package com.drore.cloud.util;

import com.drore.cloud.model.config.SystemConfig;
import com.drore.cloud.sdk.common.util.GsonUtil;
import com.drore.cloud.sdk.util.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/***
 * 读取package.json配置文件
 * @since:cloud-ims 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2016/9/12 20:09
 */
public class PackageConfigUtils {

    public static SystemConfig getSystemConfig(){
        SystemConfig config=null;
        //读取配置文件
        String packageConfig= FileUtils.readLinesToString("package.json");
        if (StringUtils.isNotBlank(packageConfig)) {
            //不为空
            Type t = new com.google.gson.reflect.TypeToken<List<SystemConfig>>() {
            }.getType();
            List<SystemConfig> systemConfigList = GsonUtil.create().fromJson(packageConfig, t);
            for (SystemConfig cf : systemConfigList) {
                if (cf.getEnable()) {
                    config = cf;
                    break;
                }
            }
        }
        if (config.getExcludeApiResources()==null){
            config.setExcludeApiResources(new ArrayList<String>());
        }
        return config;
    }
}
