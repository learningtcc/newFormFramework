package com.drore.redis;

import com.drore.util.RedisProperties;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author vic
 * @desc redis config bean
 */
@Configuration
@EnableAutoConfiguration(exclude={WebMvcAutoConfiguration.class})
@ConfigurationProperties(prefix = "spring.redis", locations = "classpath:redis.properties")
public class RedisConfig {

    private static Logger logger = Logger.getLogger(RedisConfig.class);


    @Bean
    public JedisPoolConfig getRedisConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        return config;
    }

    @Bean
    public JedisPool getJedisPool() {
        JedisPoolConfig config = getRedisConfig();
        JedisPool pool = new JedisPool(config, RedisProperties.getValueByKey("spring.redis.hostName"),
                Integer.valueOf(RedisProperties.getValueByKey("spring.redis.port")), Integer.valueOf(RedisProperties.getValueByKey("spring.redis.timeout")));
        logger.info("init JredisPool ...");
        return pool;
    }


}
