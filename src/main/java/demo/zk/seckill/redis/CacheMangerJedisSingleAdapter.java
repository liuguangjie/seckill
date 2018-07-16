package demo.zk.seckill.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by free on 18-7-15.
 */
public class CacheMangerJedisSingleAdapter implements ICacheMangerTarget {

    private JedisPool jedisPool = null;

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }



    public Long zadd(String key, Map<String, Double> members) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.zadd(key, members);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Double zscore(String key, String member) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.zscore(key, member);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 减少库存
     * @return
     */
    public Long InventoryReduction(String key, String member) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();

            Double num = jedis.zscore(key, member);
            Map<String, Double> members = new LinkedHashMap<String, Double>();
            members.put(member, --num);

            return jedis.zadd(key, members);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public String getWithkey(String key) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    @Override
    public String setKeyValue(String key, String value) {

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.set(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    @Override
    public long delWithkey(String key) {

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.del(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    @Override
    public String getHashWithKey(String hkey, String key) {

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.hget(hkey, key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public Map<String, String> getAllWithHashKey(String hkey) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.hgetAll(hkey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public long setHashKeyValue(String hkey, String key, String value) {

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.hset(hkey, key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    @Override
    public String setExpxKeyCache(String key, String value, long timeout) {
        return "";
    }

    @Override
    public String setHashKeyValues(String hkey, Map<String, String> map) {
        return "";
    }

    @Override
    public long delHashWithKey(String hkey, String key) {
        return 0;
    }

    @Override
    public String setValueByprotostuff(String key, byte[] bytes) {
        return null;
    }

    @Override
    public byte[] getValueByprotostuff(String key) {
        return new byte[0];
    }

    @Override
    public long delByprotostuff(String key) {
        return 0;
    }

    /**
     * 获取 Jedis
     */
    public Jedis getJedis() {
        return jedisPool.getResource();
    }
}
