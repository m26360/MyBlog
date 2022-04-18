package com.mana.blogapi.vo;

public enum ErrorCode {

    PARAMS_ERROR(10001, "参数有误"),
    TOKEN_ILLEGAL(10003, "token不合法"),
    ACCOUNT_PWD_NOT_EXIST(10002, "用户名或密码不存在"),
    //TOKEN_NOT_EXIST(10002,"用户名或密码不存在"),
    ACCOUNT_EXIST(10004, "账户已经被注册了"),
    NO_PERMISSION(70001, "无访问权限"),
    SESSION_TIME_OUT(90001, "会话超时"),
    NO_LOGIN(90002, "未登录"),
    FAIL_UPLOAD(20001, "上传失败");


    private int code;
    private String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
