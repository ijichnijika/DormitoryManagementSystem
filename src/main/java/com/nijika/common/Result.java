package com.nijika.common;

import lombok.Data;

/**
 * 统一响应结果封装
 * 前端统一处理：{ code: 200, message: "操作成功", data: {...} }
 */
@Data
public class Result<T> {
    private Integer code; // 状态码：200成功，400客户端错误，500服务器错误
    private String message; // 提示信息
    private T data; // 响应数据（泛型）

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
