package demo.zk.seckill.controller;

import demo.zk.seckill.view.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by free on 18-7-15.
 * redis 操作
 */
/*@RestController*/
@RequestMapping("/redis")
public class RedisController extends BaseController {

    /*@RequestMapping("/{option}/{data}")
    public Object CRUD_zookeeper(
            @PathVariable String option,
            @PathVariable String data,
            @RequestParam(required = false) String change
    ) {

        try {
            System.out.println(cacheManger.getWithkey("name"));


            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        } finally {

        }
    }*/

}
