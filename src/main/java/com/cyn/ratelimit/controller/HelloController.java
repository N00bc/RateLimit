package com.cyn.ratelimit.controller;

import com.cyn.ratelimit.annotation.RateLimiter;
import com.cyn.ratelimit.enums.LimitType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Godc
 * @description:
 * @date 2023/1/18 23:46
 */
@RestController
public class HelloController {
    /**
     * 限流: 10秒内，可以访问3次
     *
     * @return
     */
    @GetMapping("/hello")
    @RateLimiter(time = 60, count = 3,limitType = LimitType.IP)
    public String hello() {
        return "hello world";
    }
}
