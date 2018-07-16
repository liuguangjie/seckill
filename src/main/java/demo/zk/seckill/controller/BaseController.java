package demo.zk.seckill.controller;

import demo.zk.seckill.redis.CacheMangerJedisSingleAdapter;
import demo.zk.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  公共注入 Service
 */
public abstract class BaseController {

    @Autowired
    protected SeckillService seckillService;

    @Autowired
    protected CacheMangerJedisSingleAdapter cacheManger;

}
