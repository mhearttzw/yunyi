package com.zheng.ucenter.common.constant;

/**
 * ucenter系统接口结果常量枚举类
 * Created by shuzheng on 2017/4/26.
 */
public enum UcenterResultConstant {

    /**
     * 成功
     */
    SUCCESS(10001, "success"),
    USER_EXISTS(10002,"用户已存在！"),
    NO_MORE_PAGE(1005, "没有下一页了！"),



    /**
     * 失败
     */
    FAILED(10101, "failed"),
    REPEAT_REQUEST(10102, "验证码有效时间内不需重复请求！"),
    USER_NOT_EXISTS(10103, "用户不存在！"),
    SEND_FAIL(10104, "发送验证码失败！请稍后重试！"),
    CODE_ERROR(10105, "验证码不正确！"),
    CODE_INVALID(10106, "验证码已失效！请重新请求验证码！"),
    PASSWORD_ERROR(10107, "密码不正确！"),
    IMG_UPLOAD_FAILED(10108, "图片处理失败！"),
    NOT_LOGIN(10109, "未登录"),
    NICKNAME_REPEAT(10110, "昵称重复");

    /**
     * 无效长度
     */
    //INVALID_LENGTH(10001, "Invalid length"),

    /**
     * 用户名不能为空
     */
    //EMPTY_USERNAME(10101, "Username cannot be empty"),

    /**
     * 密码不能为空
     */
    //EMPTY_PASSWORD(10102, "Password cannot be empty"),

    /**
     * 帐号不存在
     */
    //INVALID_USERNAME(10103, "Account does not exist"),

    /**
     * 密码错误
     */
    //INVALID_PASSWORD(10104, "Password error"),

    /**
     * 无效帐号
     */
    //INVALID_ACCOUNT(10105, "Invalid account");

    public int code;

    public String message;

    UcenterResultConstant(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
