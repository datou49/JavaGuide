package com.javaguide.spring.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Spring - 全局异常处理
 * <p>@RestControllerAdvice 统一拦截异常，@ExceptionHandler 按异常类型分发。
 * 避免每个 Controller 重复 try-catch，是规范开发（谷粒商城全局异常）的通用做法。</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return error(400, msg);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleBiz(IllegalStateException ex) {
        return error(400, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleOther(Exception ex) {
        return error(500, "系统异常: " + ex.getClass().getSimpleName());
    }

    private Map<String, Object> error(int code, String msg) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("code", code);
        m.put("message", msg);
        return m;
    }
}
