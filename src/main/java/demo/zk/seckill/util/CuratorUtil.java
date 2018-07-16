package demo.zk.seckill.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by free on 18-7-15.
 */
public class CuratorUtil {
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

    public static CuratorFramework getClient() {
        return client;
    }

}
