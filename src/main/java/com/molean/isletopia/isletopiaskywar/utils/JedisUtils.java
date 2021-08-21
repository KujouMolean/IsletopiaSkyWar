package com.molean.isletopia.isletopiaskywar.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.net.URI;

public class JedisUtils {
    private static JedisPool jedisPool;
    public static Jedis getJedis() {
        if (jedisPool == null) {
            jedisPool = new JedisPool(URI.create("redis://localhost:6379/"), 2000);
        }
        return jedisPool.getResource();
    }
}
