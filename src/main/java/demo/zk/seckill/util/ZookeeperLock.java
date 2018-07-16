package demo.zk.seckill.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/**
 * zookeeper 多功能客户端 已经实现 分布式锁:
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
    <version>?.?.?</version>
 */
public class ZookeeperLock {

    /***
     * 分布式 zookeeper, ip地址逗号分割
     */
    private static String address = "192.168.56.10:2181,192.168.56.11:5181,192.168.56.11:7181";

    public static CuratorFramework client;

    static{
        /**
         * 第一个参数 重试连接 间隔毫秒
         * 第二个参数 重试几次
         */
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(address, retryPolicy);
        client.start();
    }
    /**
     * 私有的默认构造子，保证外界无法直接实例化
     */
    private ZookeeperLock(){}
    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
     * 针对一件商品实现，多件商品同时秒杀建议实现一个map
     */
    private static class SingletonHolder {

        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private  static InterProcessMutex mutex = new InterProcessMutex(client, "/curator/lock");
    }


    public static InterProcessMutex getMutex(){
        return SingletonHolder.mutex;
    }


    /**
     * 获得锁
     */
    public static boolean getLock(){
        try {
            /**
             源码解释 2个参数的意思

             org.apache.curator.framework.recipes.locks.LockInternals 源码204行

             其实就是 millisToWait = TimeUnit.SECONDS.toMillis(3);

             org.apache.curator.framework.recipes.locks.LockInternals 源码294行
             if ( millisToWait != null ) {
                 millisToWait -= (System.currentTimeMillis() - startMillis);
                 startMillis = System.currentTimeMillis();
             if ( millisToWait <= 0 ) {
                 doDelete = true;    // timed out - delete our node
                 break;
             }
                wait(millisToWait);
             }
             else {
                wait();
             }

             */
            return getMutex().acquire(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 释放锁
     */
    public static void releaseLock(){
        try {
            getMutex().release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
