package demo.zk.seckill.util;

import demo.zk.seckill.entity.LockType;
import demo.zk.seckill.redis.lock.FairLock;
import demo.zk.seckill.redis.lock.ReadLock;
import demo.zk.seckill.redis.lock.ReentrantLock;
import demo.zk.seckill.redis.lock.WriteLock;

/**
 * Created by free on 18-7-17.
 * 基于redis 实现 锁
 */
public class RedissonLock {


    /**重入锁 */
    private static ReentrantLock reentrantLock;

    /**公平锁*/
    private static FairLock fairLock;
    /**读锁*/
    private static ReadLock  readLock;
    /**写锁*/
    private static WriteLock writeLock;


    public static  boolean getLock() {

        return reentrantLock.acquire();
    }

    public static void releaseLock() {
        reentrantLock.release();
    }

}
