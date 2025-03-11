package com.example.jwt_token.exception;

public enum ErrorCode implements  ErrorHandle{
    EXP_1001("1001","Invalid Input"),
    EXP_1002("1002","User Access Denied Exception"),
    EXP_1003("1003","ROLES ARE NULL"),
    EXP_1004("1004","Role is not found exception"),
    EXP_1005("1005","users are null "),
    EXP_1006("1006","user is UnAuthorized"),
    EXP_1007("1007","")

    ;

    private final String message;
    private final String errorsCode;

    ErrorCode(String message, String errorsCode) {
        this.message = message;
        this.errorsCode = errorsCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getErrorCode() {
        return this.errorsCode;
    }
}
