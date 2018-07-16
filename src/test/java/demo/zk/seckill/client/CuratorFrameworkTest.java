package demo.zk.seckill.client;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 列出了一些基本的增删改查
 * 更多的特新需要看官方文档
 */
public class CuratorFrameworkTest {



    String url = "192.168.56.10:2181,192.168.56.11:5181,192.168.56.11:7181";

    /**
     * https://blog.csdn.net/qq_15370821/article/details/73692288
     *
     * @throws Throwable
     */
    @Test
    public void test1() throws Throwable {


        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);//刚开始重试间隔为1秒，之后重试间隔逐渐增加，最多重试不超过三次
        /*RetryPolicy retryPolicy1 = new RetryNTimes(3, 1000);//最大重试次数，和两次重试间隔时间
        RetryPolicy retryPolicy2 = new RetryUntilElapsed(5000, 1000);//会一直重试直到达到规定时间，第一个参数整个重试不能超过时间，第二个参数重试间隔
        //第一种方式
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.0.3:2181", 5000,5000,retryPolicy);//最后一个参数重试策略
        */

        //第二种方式
        CuratorFramework client1 = CuratorFrameworkFactory.builder().connectString(url)
                .sessionTimeoutMs(5000)//会话超时时间
                .connectionTimeoutMs(5000)//连接超时时间
                .retryPolicy(retryPolicy)
                .build();

        client1.start();

        String path = client1.create().creatingParentsIfNeeded()//若创建节点的父节点不存在会先创建父节点再创建子节点
                .withMode(CreateMode.EPHEMERAL)//withMode节点类型，
                .forPath("/curator/3", "131".getBytes());
        System.out.println(path);

        List<String> list = client1.getChildren().forPath("/");
        System.out.println(list);

        //String re = new String(client1.getData().forPath("/curator/3"));//只获取数据内容
        /*Stat stat = new Stat();
        String re = new String(client1.getData().storingStatIn(stat)//在获取节点内容的同时把状态信息存入Stat对象
                .forPath("/curator/3/2/1"));
        System.out.println(re);
        System.out.println(stat);*/


        /*client1.delete().guaranteed()//保障机制，若未删除成功，只要会话有效会在后台一直尝试删除
                .deletingChildrenIfNeeded()//若当前节点包含子节点
                .withVersion(-1)//指定版本号
                .forPath("/curator");*/

        //Thread.sleep(Integer.MAX_VALUE);

        /**
         Stat stat = new Stat();
         String re = new String(client1.getData().storingStatIn(stat)//在获取节点内容的同时把状态信息存入Stat对象
         .forPath("/curator/3"));
         System.out.println(re);
         System.out.println(stat);

         Thread.sleep(10000);
         client1.setData().withVersion(stat.getVersion())//修改前获取一次节点数据得到版本信息
         .forPath("/curator/3", "111".getBytes());

         若线程在sleep时，在另一个客户端修改了该节点数据，会抛出异常：



         */

    }

    /**
     * 异步调用
     */
    @Test
    public void test2() throws Exception {
        ExecutorService es = Executors.newFixedThreadPool(5);//线程池

        RetryPolicy retry = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(url, 5000, 5000, retry);
        client.start();

        //Stat stat = client.checkExists().forPath("/node_1");//同步调用
        client.checkExists().inBackground(new BackgroundCallback() {

            public void processResult(CuratorFramework curator, CuratorEvent event) throws Exception {//传入客户端对象和事件
                System.out.println(event.getType());
                int re = event.getResultCode();//执行成功为0
                System.out.println(re);

                String path = event.getPath();
                System.out.println(path);
                Stat stat = event.getStat();
                System.out.println(stat);

            }
        }, "123", es).forPath("/node_1");//把线程池es传给异步调用

        List<String> list = client.getChildren().forPath("/");
        System.out.println(list);

        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 事件监听：
     */
    @Test
    public void test3() throws Exception {
        RetryPolicy retry = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(url, 5000, 5000, retry);
        client.start();

        final NodeCache cache = new NodeCache(client, "/node_1");
        cache.start();

        cache.getListenable().addListener(new NodeCacheListener() {

            public void nodeChanged() throws Exception {
                byte[] res = cache.getCurrentData().getData();
                System.out.println("data: " + new String(res));
            }
        });

        Thread.sleep(Integer.MAX_VALUE);


        //子节点监听
        /*@SuppressWarnings("resource")
        final PathChildrenCache cache = new PathChildrenCache(client,"/node_1",true);
        cache.start();

        cache.getListenable().addListener(new PathChildrenCacheListener() {

            @Override
            public void childEvent(CuratorFramework curator, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED:
                        System.out.println("add:" + event.getData());
                        break;
                    case CHILD_UPDATED:
                        System.out.println("update:" + event.getData());
                        break;
                    case CHILD_REMOVED:
                        System.out.println("remove:" + event.getData());
                        break;
                    default:
                        break;
                }
            }
        });*/


    }

    /**
     * ACL权限：
     */
    @Test
    public void test4() throws Exception {
        RetryPolicy retry = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(url, 5000, 5000, retry);
        client.start();

        //ACL有IP授权和用户名密码访问的模式
        ACL aclRoot = new ACL(ZooDefs.Perms.ALL, new Id("digest", DigestAuthenticationProvider.generateDigest("root:root")));
        List<ACL> aclList = new ArrayList<ACL>();
        aclList.add(aclRoot);

        String path = client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                .withACL(aclList)
                .forPath("/node_3/node_ACL", "2".getBytes());
        System.out.println(path);

        CuratorFramework client1 = CuratorFrameworkFactory.builder().connectString("192.168.0.3:2181")
                .sessionTimeoutMs(5000)//会话超时时间
                .connectionTimeoutMs(5000)//连接超时时间
                .authorization("digest", "root:root".getBytes())//权限访问
                .retryPolicy(retry)
                .build();

        client1.start();

        String re = new String(client1.getData().forPath("/node_3/node_ACL"));
        System.out.println(re);

    }
}

