package com.leyou.response;

import lombok.Data;

/**
 * @Author: 98050
 * @Time: 2018-11-24 21:41
 * @Feature: 返回结果
 */
@Data
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    /**
     *  成功时候的调用
     * */
    public static  <T> Result<T> success(T data){
        Result<T> result = new Result<T>(data);
        result.code = CodeMsg.SUCCESS.getCode();
        return new Result<T>(data);
    }

    /**
     *  失败时候的调用
     * */
    public static  <T> Result<T> error(CodeMsg codeMsg){
        return new Result<T>(codeMsg);
    }




    private Result(T data) {
        this.data = data;
    }

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }



    private Result(CodeMsg codeMsg) {
        if(codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }


}
