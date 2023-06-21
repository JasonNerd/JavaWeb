package com.rainbow.emplista.pojo;
/**
 * 用于统一的返回结果
 * code, Integer, 0: failure 1: success
 * msg, String, message
 * data, Object, response to the front end
 */
public class Result {
    private Integer code;
    private String msg;
    private Object data;

    // factory
    public static Result success(){
        return new Result(1, "success", null);
    }
    public static Result success(Object data){
        return new Result(1, "success", data);
    }
    public static Result error(){
        return new Result(0, "error", null);
    }

    // getter setter constructor
    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result() {
    }
}
