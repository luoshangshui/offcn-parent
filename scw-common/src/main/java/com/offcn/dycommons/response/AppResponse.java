package com.offcn.dycommons.response;

import com.offcn.dycommons.enums.ResponseEnum;

public class AppResponse<T> {
    private Integer code;
    private String msg;
    private T data;

    public AppResponse() {

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public AppResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> AppResponse<T> ok(T data){
        AppResponse<T> response = new AppResponse<>();
        response.setCode(ResponseEnum.SUCCESS.getCode());
        response.setMsg(ResponseEnum.SUCCESS.getMes());
        response.setData(data);
        return response;
    }
    public static <T> AppResponse<T> fail(T data){
        AppResponse<T> response = new AppResponse<>();
        response.setCode(ResponseEnum.FAIL.getCode());
        response.setMsg(ResponseEnum.FAIL.getMes());
        response.setData(data);
        return response;
    }
}
