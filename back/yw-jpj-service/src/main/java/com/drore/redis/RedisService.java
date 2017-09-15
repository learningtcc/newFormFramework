package com.drore.redis;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明:封装redis 缓存服务器服务接口 <br/>
 * 项目名称: cloud-uc <br/>
 * 创建日期: 2016年7月28日 下午2:23:14 <br/>
 * 作者: wdz
 */


import com.drore.util.DefaultProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

@Service
public class RedisService {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 通过key删除（字节）
     *
     * @param key
     */
    public void del(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.del(key);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 通过key删除
     *
     * @param key
     */
    public void del(String key) {
        Jedis jedis = null;
        try {
            key = DefaultProperties.getValueByKey(DefaultProperties.redisCode) + key;
            jedis = getJedis();
            jedis.del(key);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 功能描述：初始化的时候删除                    <br/>
     * 作    者： wdz                  <br/>
     * 创建时间：2016年12月7日  下午3:14:07            <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                           <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     *
     * @param key
     */
    public void delInit(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.del(key);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }


    /**
     * 功能描述：刷新缓存                    <br/>
     * 作    者： wdz                  <br/>
     * 创建时间：2016年12月1日  下午5:35:24            <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                           <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     */
    public void flushAll() {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.flushAll();
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 添加key value 并且设置存活时间(byte)
     *
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(byte[] key, byte[] value, int liveTime) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            set(key, value);
            jedis.expire(key, liveTime);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 添加key value 并且设置存活时间
     *
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(String key, String value, int liveTime) {
        Jedis jedis = null;
        try {
            key = DefaultProperties.getValueByKey(DefaultProperties.redisCode) + key;
            jedis = getJedis();
            jedis.setex(key, liveTime, value);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 添加key value
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            key = DefaultProperties.getValueByKey(DefaultProperties.redisCode) + key;
            jedis = getJedis();
            jedis.set(key, value);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 添加key value (字节)(序列化)
     *
     * @param key
     * @param value
     */
    public void set(byte[] key, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(key, value);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 获取redis value (String)
     *
     * @param key
     * @return
     */
    public String get(String key) {
        Jedis jedis = null;
        try {
            key = DefaultProperties.getValueByKey(DefaultProperties.redisCode) + key;
            jedis = getJedis();
            String value = jedis.get(key);
            return value;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 获取redis value (byte [] )(反序列化)
     *
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.get(key);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 通过正则匹配keys
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.keys(pattern);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 检查key是否已经存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        Jedis jedis = null;
        try {
            key = DefaultProperties.getValueByKey(DefaultProperties.redisCode) + key;
            jedis = getJedis();
            return jedis.exists(key);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 清空redis 所有数据
     *
     * @return
     */
    public String flushDB() {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.flushDB();
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 查看redis里有多少数据
     */
    public long dbSize() {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.dbSize();
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 检查是否连接成功
     *
     * @return
     */
    public String ping() {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.ping();
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 获取一个jedis 客户端
     *
     * @return
     */
    private Jedis getJedis() {
        return jedisPool.getResource();
    }


    private RedisService() {

    }

    // 操作redis客户端
//    private static Jedis jedis;

}
