package cn.itxsl.kernel.model.dto;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/13 13:07
 * @description：返回结果
 */
public  class Result{

    private Integer code=200;

    private String message;

    private Object data;

    private Integer totalSize;

    public Result() {
    }

    public Result(String message) {
        this.message = message;
    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public Result(Integer code, String message, Object data, Integer totalSize) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.totalSize = totalSize;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }
}
