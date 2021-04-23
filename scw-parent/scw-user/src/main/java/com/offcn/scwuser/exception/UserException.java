package com.offcn.scwuser.exception;

import com.offcn.scwuser.enums.UserExceptionEnum;

public class UserException extends RuntimeException{
    public UserException(UserExceptionEnum userExceptionEnum) {
        super(userExceptionEnum.getMsg());
    }
}
