package com.phoenix.oauth2.base.result;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * 自定义响应结构
 */
@Data
public class RequestResult implements Serializable {

    // 响应业务状态
    private Integer code;

    // 响应消息
    private String message;

    // 响应中的数据
    private Object data;

    public RequestResult() {
    }

    public RequestResult(Object data) {
        this.code = 200;
        this.message = "OK";
        this.data = data;
    }

    public RequestResult(String message, Object data) {
        this.code = 200;
        this.message = message;
        this.data = data;
    }

    public RequestResult(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static RequestResult ok() {
        return new RequestResult(null);
    }

    public static RequestResult ok(String message) {
        return new RequestResult(message, null);
    }

    public static RequestResult ok(Object data) {
        return new RequestResult(data);
    }

    public static RequestResult ok(String message, Object data) {
        return new RequestResult(message, data);
    }

    public static RequestResult build(Integer code, String message) {
        return new RequestResult(code, message, null);
    }

    public static RequestResult build(Integer code, String message, Object data) {
        return new RequestResult(code, message, data);
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }


    /**
     * JSON字符串转成 RequestResult 对象
     *
     * @param json json
     * @return RequestResult
     */
    public static RequestResult format(String json) {
        try {
            return JSON.parseObject(json, RequestResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
