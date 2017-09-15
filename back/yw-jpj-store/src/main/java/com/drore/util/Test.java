package com.drore.util;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Administrator on 2017/9/11 0011.
 */
public class Test {

    @Value("${spring.redis.port}")
    private int port;
}
