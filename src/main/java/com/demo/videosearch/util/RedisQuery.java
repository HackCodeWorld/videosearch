package com.demo.videosearch.util;

import com.demo.videosearch.db.JedisDBPool;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Redis数据库操作方法封装
 */
public class RedisQuery {
    /**
     * Redis zset操作：获取指定区间内的元素，从高到低排序
     * reverseRange()方法使用了Jedis的zrevrangeWithScores()方法，
     * 该方法会返回指定区间内的元素，从高到低排序，并带有score值。我们可以将返回结果转换为Set<String>类型，并只保留元素值，不需要score值。
     */
    public static Set<String> reverseRange(String key, long start, long end) {
        Jedis jedis = JedisDBPool.getConnectJedis();
        Set<Tuple> tuples = jedis.zrevrangeWithScores(key, start, end);
        jedis.close();
        Set<String> result = new HashSet<>();
        for (Tuple tuple : tuples) {
            result.add(tuple.getElement());
        }
        return result;
    }

    /**
     * Redis zset操作：增加指定的score值到指定的成员中
     * 其中，incrementScore()方法使用了Jedis的zincrby()方法，
     * 该方法会自动为成员增加指定的score值，如果成员不存在，则会被添加并将其score值设置为给定值。
     */
    public static double incrementScore(String key, String member, double score) {
        try {
            Jedis jedis = JedisDBPool.getConnectJedis();
            double newScore = jedis.zincrby(key, score, member);
        } catch (JedisConnectionException e) {
            System.err.println("Failed to connect to Redis, skipping increment score.");
            return score;
        }

        return score;
    }

    /**
     * Redis list操作：将所有指定的值插入存储在键位于key的列表的头部
     * leftPush()方法使用了Jedis的lpush()方法，
     * 该方法可以将所有指定的值插入存储在键位于key的列表的头部。
     */
    public static long leftPush(String key, String... values) {
        try {
            Jedis jedis = JedisDBPool.getConnectJedis();
            long result = jedis.lpush(key, values);
            jedis.close();
            return result;
        } catch (JedisConnectionException e) {
            System.err.println("Failed to connect to Redis, proceeding with normal KV search operation");
            // call your HashMap fallback method here
        }
        return 0;  // return a default value or handle this case as per your requirement
    }

    /**
     * 添加数据,相同key会覆盖数据，所以也相当于改
     */
    public static void set(String key, String value) {
        Jedis jedis = JedisDBPool.getConnectJedis();
        jedis.set(key, value);
        //Jedis3.0版本后jedisPool.returnResource(final Jedis resource)方法已被弃用，用jedis.close()代替 来归还连接
        jedis.close();
    }

    /**
     * 添加数据，带超时时间，超时自动销毁
     */
    public static void setex(String key, String value, int seconds) {
        Jedis jedis = JedisDBPool.getConnectJedis();
        jedis.setex(key, seconds, value);
        jedis.close();
    }

    /**
     * 根据key删除数据
     */
    public static void deleteByKey(String key) {
        Jedis jedis = JedisDBPool.getConnectJedis();
        jedis.del(key);
        jedis.close();
    }

    /**
     * 根据key查询
     */
    public static String getByKey(String key) {
        Jedis jedis = JedisDBPool.getConnectJedis();
        String value = jedis.get(key);
        jedis.close();
        return value;
    }

    /**
     * 查询某条数据是否存在
     */
    public static boolean isExist(String key) {
        Jedis jedis = JedisDBPool.getConnectJedis();
        boolean exist = jedis.exists(key);
        jedis.close();
        return exist;
    }

//    public static void main(String[] args) {
////        set("测试key", "测试value");
////        //通过给key添加冒号可以使数据在Redis Desktop manager中以文件夹的形式展示
////        set("User:测试key", "测试value");
//    }
}

