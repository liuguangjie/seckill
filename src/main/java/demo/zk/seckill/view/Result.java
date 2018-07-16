package demo.zk.seckill.view;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by free on 18-7-14.
 */
public class Result implements Serializable {


    private String code;

    private String msg;

    private Object data;

    public Result(){}
    public Result(String code, String msg){
        this.code = code;
        this.msg = msg;
        this.data = "";
    }
    public Result(String code, String msg, Object data){
        this.code = code;
        this.msg = msg;
        if (data == null) {
            this.data = "";
        } else {
            this.data = data;
        }

    }

    public static Result ok(Object... data) {
        LinkedList<Object> ldata = new LinkedList<Object>();
        for (Object o : data) {
            if (o != null && !"".equals(o)) {
                ldata.addLast(o);
            }
        }

        return new Result("200", "ok", ldata);
    }

    /*public static void main(String[] args) {
        Result.ok(null, "", null, null, "sssss", new Object());
    }*/

    public static Result error() {

        return new Result("500", "error");
    }

    public static Result none() {

        return new Result("404", "none");
    }

    public static Result already() {
        return new Result("300", "Please line up");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
