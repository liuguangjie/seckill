package demo.zk.seckill.service;

import demo.zk.seckill.entity.ShopItem;
import demo.zk.seckill.entity.User;

import java.util.List;

/**
 * Created by free on 18-7-14.
 */
public interface SeckillService {

    Object fastOrders(User user);

    List<ShopItem> listShopItem();

    void updateShopItem(ShopItem shopItem);
}
