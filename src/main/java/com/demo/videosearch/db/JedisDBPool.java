package com.demo.videosearch.db;

import com.demo.videosearch.util.CommonUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Redis连接池
 */
public class JedisDBPool {
    public static JedisPool jedisPool;
    static {
        try {
            Properties properties = new Properties();
            InputStream in = CommonUtils.readResourceFile("redis.properties");
            properties.load(in);
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(Integer.valueOf(properties.getProperty("maxTotal")));
            jedisPoolConfig.setMaxIdle(Integer.valueOf(properties.getProperty("maxIdle")));
            jedisPoolConfig.setMinIdle(Integer.valueOf(properties.getProperty("minIdle")));
            jedisPoolConfig.setMaxWaitMillis(Integer.valueOf(properties.getProperty("maxWaitMillis")));
            jedisPoolConfig.setTestOnBorrow(Boolean.valueOf(properties.getProperty("testOnBorrow")));
            jedisPoolConfig.setTestOnReturn(Boolean.valueOf(properties.getProperty("testOnReturn")));
            jedisPoolConfig.setTimeBetweenEvictionRunsMillis(Integer.valueOf(properties.getProperty("timeBetweenEvictionRunsMillis")));
            jedisPoolConfig.setMinEvictableIdleTimeMillis(Integer.valueOf(properties.getProperty("minEvictableIdleTimeMillis")));
            jedisPoolConfig.setNumTestsPerEvictionRun(Integer.valueOf(properties.getProperty("numTestsPerEvictionRun")));
            jedisPool = new JedisPool(jedisPoolConfig,properties.getProperty("host"),Integer.valueOf(properties.getProperty("port")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Jedis getConnectJedis() {
        return jedisPool.getResource();
    }
}
