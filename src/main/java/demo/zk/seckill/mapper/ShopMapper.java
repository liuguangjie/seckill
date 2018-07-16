package demo.zk.seckill.mapper;

import demo.zk.seckill.entity.ShopItem;

import java.util.List;

/**
 * Created by free on 18-7-15.
 */
public interface ShopMapper {
    List<ShopItem> selectShopItemList();

    void updateShopItem(ShopItem shopItem);
}
