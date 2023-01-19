package com.cyn.ratelimit.enums;

/**
 * @author Godc
 * @description: 限流类型
 * @date 2023/1/18 16:17
 */
public enum LimitType {

    /**
     * 默认限流规则，针对接口进行限流
     */
    DEFAULT,
    /**
     * 针对IP地址进行限流
     */
    IP
}
