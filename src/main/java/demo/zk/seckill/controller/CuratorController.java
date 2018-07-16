package demo.zk.seckill.controller;

import demo.zk.seckill.util.CuratorUtil;
import demo.zk.seckill.view.Result;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * zookeeper Curator 客服端操作
 * 增删改查 的列子
 */
@RestController
@RequestMapping("/curator")
public class CuratorController {


    /**
     * http://localhost:8080/curator/add/111?change=3
     * http://localhost:8080/curator/delete/111?change=3
     * http://localhost:8080/curator/changeData/111?change=5555
     * http://localhost:8080/curator/get/111
     * @param option
     * @param data
     * @param change
     * @return
     */
    @RequestMapping("/{option}/{data}")
    public Object CRUD_zookeeper(
            @PathVariable String option,
            @PathVariable String data,
            @RequestParam(required = false) String change
    ) {

        try {

            Invoker invoker = new Invoker();
            Object retObj = invoker.lookupAndExcute(option, data, change);

            return Result.ok(retObj);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    private static class Invoker {

        private static final Map<String, Method> methodHashMap = new HashMap<String, Method>();
        static {

            try {
                Method[] methods = Invoker.class.getMethods();
                for (Method method : methods) {
                    if (!ReflectionUtils.isObjectMethod(method)) {
                        methodHashMap.put(method.getName(), method);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }



        private CuratorFramework client = CuratorUtil.client;

        private static final String ROOT = "/curator";

        public void setClient(CuratorFramework client) {
            this.client = client;
        }

        public Object lookupAndExcute(String name, String data, String change) {
            Method method = methodHashMap.get(name);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }

            return ReflectionUtils.invokeMethod(method,
                        this, data, change);

        }

        public void add(String data, String change) {
            try {
                String join = ROOT + "/" + data;
                Stat stat = client.checkExists().forPath(join);
                if (stat == null) {
                    client.create().withMode(CreateMode.PERSISTENT).forPath(join,
                            change == null? "".getBytes() : change.getBytes());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void delete(String data, String change) {
            try {
                String join = ROOT + "/" + data;
                Stat stat = client.checkExists().forPath(join);
                if (stat != null) {
                    client.delete().forPath(join);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public void changeData(String data, String change) {
            try {
                String join = ROOT + "/" + data;
                Stat stat = client.checkExists().forPath(join);
                if (stat != null) {
                    client.setData().forPath(join,
                            change == null? "".getBytes() : change.getBytes());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Object get(String data, String change) {
            try {
                String join = ROOT + "/" + data;
                Stat stat = client.checkExists().forPath(join);
                if (stat != null) {
                    return new String(client.getData().forPath(join));
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
            return "";
        }

    }

}
