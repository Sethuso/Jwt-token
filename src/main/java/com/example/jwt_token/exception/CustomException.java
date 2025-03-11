package com.example.jwt_token.exception;

public class CustomException extends  RuntimeException {
    private final   ErrorCode errorCode;

    private  static String getMessage(ErrorCode errorCode){
        if(errorCode.getMessage() != null){
            return errorCode.getMessage();
        }else{
            return null;
        }
    }

    public CustomException( ErrorCode errorCode) {
        super(getMessage(errorCode));
        this.errorCode = errorCode;

    }
}
