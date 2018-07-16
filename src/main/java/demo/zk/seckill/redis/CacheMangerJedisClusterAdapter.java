package demo.zk.seckill.redis;

import redis.clients.jedis.JedisCluster;

import java.util.Map;

/**
 * Created by free on 18-7-15.
 */
public class CacheMangerJedisClusterAdapter implements ICacheMangerTarget {

    private JedisCluster jedisCluster;

    public CacheMangerJedisClusterAdapter(){

    }
    /**
     * 构造函数初始化 jedisCluster对象
     * @param jedisCluster
     */
    public CacheMangerJedisClusterAdapter(JedisCluster jedisCluster){
        this.jedisCluster = jedisCluster;

    }

    public String getWithkey(String key) {
        return jedisCluster.get(key);
    }

    public String setKeyValue(String key, String value) {
        return jedisCluster.set(key, value);
    }

    public long delWithkey(String key) {

        return jedisCluster.del(key);
    }

    public String getHashWithKey(String hkey, String key) {

        return jedisCluster.hget(hkey, key);
    }

    public Map<String, String> getAllWithHashKey(String hkey) {

        return jedisCluster.hgetAll(hkey);
    }

    @Override
    public long setHashKeyValue(String hkey, String key, String value) {
        return jedisCluster.hset(hkey, key, value);
    }

    public String setHashKeyValues(String hkey, Map<String, String> map) {

        return jedisCluster.hmset(hkey, map);
    }

    public long delHashWithKey(String hkey, String key) {

        return jedisCluster.hdel(hkey, key);
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }
    public String setExpxKeyCache(String key, String value, long time) {
        return "";
    }
    public String setValueByprotostuff(String key, byte[] bytes) {
        return null;
    }
    public byte[] getValueByprotostuff(String key) {
        return null;
    }
    public long delByprotostuff(String key) {
        return 0;
    }


}
