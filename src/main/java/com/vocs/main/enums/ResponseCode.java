package com.vocs.main.enums;

/**
 * @Description: 枚举
 */
public enum ResponseCode {
    SUCCESS("000", "成功"),
    ILLEGAL_ARGUMENT("001", "参数错误"),
    RATELIMITED("997", "服务过载，当前请求已被限流"),
    DEGRADATION("998", "服务已降级"),
    FAILURE("999", "失败"),
    ILLEGAL_REQUEST("003", "未知请求"),
    INVALID_PARAMS_LENGTH("004", "参数长度错误"),
    INVALID_PARAMETER_VALUE("005", "参数值非法"),
    PARAMS_IS_EMPTY("006", "参数不能为空"),
    REQUEST_TIMEOUT("007", "请求超时"),
    AUTH_ACCESS_DENIED("008", "拒绝访问"),
    MISS_REQUIRED_PARAMETER("009", "缺失必选参数"),
    DATA_NOT_EXISTS("010", "数据不存在"),
    SYSTEM_IS_BUSY("011", "系统繁忙"),
    DUPLICATE_REQUEST("012", "重复的请求");

    private final String code;
    private final String msg;

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public String toString() {
        return "ResponseCode(code=" + this.getCode() + ", msg=" + this.getMsg() + ")";
    }

    private ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}