package demo.zk.seckill.redis;

import java.util.Map;

/**
 * Created by free on 18-7-15.
 */
public interface ICacheMangerTarget {

    /**
     * 缓存字符串数据结构-根据key获取值
     * @param key
     * @return
     */
    public String getWithkey(String key);
    /**
     * 缓存字符串数据结构-存储
     * @param key
     * @param value
     * @return
     */
    public String setKeyValue(String key, String value);
    /**
     * 缓存字符串数据结构-删除
     * @param key
     * @return
     */
    public long delWithkey(String key);
    /**
     * 缓存Hash(散列)数据结构-获取值
     * @param hkey
     * @param key
     * @return
     */
    public String getHashWithKey(String hkey, String key);
    /**
     * 缓存Hash(散列)数据结构-根据hashKey获取所有值
     * @param hkey
     * @return
     */
    public Map<String,String> getAllWithHashKey(String hkey);
    /**
     * 缓存Hash(散列)数据结构-存放值
     * @param hkey
     * @param key
     * @param value
     * @return
     */
    public long setHashKeyValue(String hkey, String key, String value);
    /**
     *
     * setrange:设置失效时间 <br/>
     * @author hadoop
     * @param hkey
     * @param key
     * @param value
     * @param time 失效时间 秒为单位
     * @return
     * @since JDK 1.8
     */
    public String setExpxKeyCache(String key, String value,long timeout);
    /**
     * 缓存Hash(散列)数据结构-存放值Map
     * @param hkey
     * @param map
     * @return
     */
    public String setHashKeyValues(String hkey, Map<String,String> map);
    /**
     * 缓存Hash(散列)数据结构-删除
     * @param hkey
     * @param key
     * @return
     */
    public long delHashWithKey(String hkey, String key);
    /**
     *
     * setValueByprotostuff:set值通过Protobuff序列化. <br/>
     *
     * @author hadoop
     * @param key
     * @param bytes
     * @return
     * @since JDK 1.8
     */
    public String setValueByprotostuff(String key ,byte[] bytes);
    /**
     *
     * getValueByprotostuff:获取值通过Protobuff序列化的. <br/>
     *
     * @author hadoop
     * @param key
     * @return
     * @since JDK 1.8
     */
    public byte[] getValueByprotostuff(String key);

    public long delByprotostuff(String key);
}
