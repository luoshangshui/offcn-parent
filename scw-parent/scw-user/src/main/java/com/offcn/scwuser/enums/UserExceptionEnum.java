package com.offcn.scwuser.enums;

public enum UserExceptionEnum {
    //账号存在
    LOGINACCT_EXIST(1,"账号已存在"),
    //邮箱存在
    EMAIL_EXIST(2,"邮箱已存在"),
    //账号锁定
    LOGINACCT_LOCK(3,"账号锁定");
    private Integer code;
    private String msg;

    UserExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
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
}
