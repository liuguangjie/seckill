package demo.zk.seckill.redis.lock;


/**
 * Created by kl on 2017/12/29.
 */
public interface Lock {
    Lock setLockInfo(LockInfo lockInfo);

    boolean acquire();

    void release();
}
