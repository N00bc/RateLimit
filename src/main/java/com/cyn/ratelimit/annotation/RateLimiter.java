package com.cyn.ratelimit.annotation;

import com.cyn.ratelimit.enums.LimitType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Godc
 * @description:
 * @date 2023/1/18 16:19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiter {
    /**
     * 限流的key的前缀
     *
     * @return
     */
    String key() default "rate_limit:";

    /**
     * 限流时间窗,默认60s
     *
     * @return
     */
    int time() default 60;

    /**
     * 限流次数,在时间窗内的限流次数
     */
    int count() default 100;

    /**
     * 限流的类型
     *
     * @return
     */
    LimitType limitType() default LimitType.DEFAULT;
}
