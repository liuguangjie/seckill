package demo.zk.seckill.entity;

/**
 * Created by free on 18-7-15.
 */
public class ShopItem {

    /**
     * 商品id
     */
    private Integer id;
    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品库存
     */
    private Integer num;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
