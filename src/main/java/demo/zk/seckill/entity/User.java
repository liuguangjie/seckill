package demo.zk.seckill.entity;

/**
 * Created by free on 18-7-14.
 */
public class User {
    private String userId;

    private String itemId;
    public String getUserId() {
        return userId;
    }


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
