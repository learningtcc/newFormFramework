package com.drore.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:   支付相关属性的  <br/>
 * 项目名称: spring-boot-base <br/>
 * 创建日期:  2017/5/3 11:02  <br/>
 * 作者:    wdz
 */
public class PayProperties {

    // 根据Key读取Value
    public static String getValueByKey(String key) {
        Properties pps = new Properties();
        String filePath = PayProperties.class.getClassLoader()
                .getResource("pay.properties").getPath();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(
                    filePath));
            pps.load(in);
            String value = pps.getProperty(key);
            in.close();
            return value;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
