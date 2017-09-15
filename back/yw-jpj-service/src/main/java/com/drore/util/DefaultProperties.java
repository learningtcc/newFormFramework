package com.drore.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DefaultProperties {


    /**
     * 缓存前缀
     */
    public static final String redisCode = "redisCode";



    // 根据Key读取Value
    public static String getValueByKey(String key) {
        Properties pps = new Properties();
        String filePath = DefaultProperties.class.getClassLoader()
                .getResource("default.properties").getPath();
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


    public static void main(String[] args) {
        System.out.println();
    }

}
