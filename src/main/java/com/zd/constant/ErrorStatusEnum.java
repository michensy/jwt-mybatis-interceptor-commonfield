package com.zd.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ErrorStatusEnum {
    DEFAULT_ERRROR("-1", "服务异常"),
    UNKNOWN_ERRROR("-999", "未知错误"),
    SERVICE_CURRENTLY_UNAVAILABLE("901", "服务不可用"),
    INSUFFICIENT_USER_PERMISSIONS("902", "没有权限"),
    INVALID_ENCODING("903", "编码错误"),
    SERVICE_CURRENTLY_NOTFOUND("904", "服务不存在"),
    FORBIDDEN_REQUEST("905", "请求被禁止"),
    METHOD_OBSOLETED("906", "服务已经作废"),
    DATA_NOTFUND("907", " 资料不存在"),
    BUSINESS_ERROR("910", "业务异常"),
    IDEMPOTENCY_ERROR("911", "请求已失效或重复提交"),
    MISSING_SESSION("920", "缺少认证参数 "),
    INVALID_SESSION("921", "会话不存在或者已过期"),
    MISSING_APP_KEY("922", "缺少 appKey 参数"),
    INVALID_APP_KEY("923", "无效的 appKey 参数"),
    MISSING_SIGNATURE("924", "缺少签名参数"),
    INVALID_SIGNATURE("925", "无效签名"),
    INVALID_ARGUMENTS("926", "参数无效或缺失"),
    INVALID_FORMAT("927", "无效的格式类型"),
    FAILD_DECRYPT("928", "解密失败"),
    EXCEED_USER_INVOKE_LIMITED("940", "用户调用服务的次数超限"),
    EXCEED_SESSION_INVOKE_LIMITED("941", "会话调用服务的次数超限"),
    EXCEED_APP_INVOKE_LIMITED("942", "应用调用服务的次数超限"),
    EXCEED_APP_INVOKE_FREQUENCY_LIMITED("943", "应用调用服务的频率超限");

    private String value;
    private String text;

    private ErrorStatusEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return this.value;
    }

    public String getText() {
        return this.text;
    }

    public static ErrorStatusEnum getInstance(String status) {
        ErrorStatusEnum[] allStatus = values();
        ErrorStatusEnum[] var2 = allStatus;
        int var3 = allStatus.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            ErrorStatusEnum ws = var2[var4];
            if (ws.getValue().equalsIgnoreCase(status)) {
                return ws;
            }
        }

        throw new IllegalArgumentException("值非法，没有符合的枚举对象");
    }

    public static List<Map> getAll() {
        List<Map> ls = new ArrayList();
        ErrorStatusEnum[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            ErrorStatusEnum et = var1[var3];
            Map<String, Object> m = new HashMap();
            m.put("value", et.value);
            m.put("text", et.text);
            ls.add(m);
        }

        return ls;
    }
}