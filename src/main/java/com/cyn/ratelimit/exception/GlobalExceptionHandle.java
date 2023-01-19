package com.cyn.ratelimit.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Godc
 * @description:
 * @date 2023/1/18 17:29
 */
@RestControllerAdvice
public class GlobalExceptionHandle {
    /**
     * 全局异常处理。
     *
     * @param e
     * @return
     */
    @ExceptionHandler(RateLimitException.class)
    public Map<String, Object> rateLimitException(Exception e) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("msg", e.getMessage());
        return result;
    }
}
