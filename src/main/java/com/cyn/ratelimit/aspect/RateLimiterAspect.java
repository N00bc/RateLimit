package com.cyn.ratelimit.aspect;

import com.cyn.ratelimit.annotation.RateLimiter;
import com.cyn.ratelimit.enums.LimitType;
import com.cyn.ratelimit.exception.RateLimitException;
import com.cyn.ratelimit.utils.IpUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;

/**
 * @author Godc
 * @description:
 * @date 2023/1/18 17:39
 */
@Aspect
@Component
public class RateLimiterAspect {
    private static final Logger logger = LoggerFactory.getLogger(RateLimiterAspect.class);

    @Autowired
    RedisTemplate<Object, Object> template;

    @Autowired
    RedisScript<Long> redisScript;

    @Pointcut("@annotation(com.cyn.ratelimit.annotation.RateLimiter)")
    public void pc() {
    }

    /**
     * 期望kv格式
     * rate_limit:ip-全类名-方法
     * rate_limit:全类名-方法
     *
     * @param pjp
     */
    @Around("pc()")
    public void around(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        // 获取注解
        RateLimiter annotation = AnnotationUtils.findAnnotation(signature.getMethod(), RateLimiter.class);
        StringBuffer key = new StringBuffer(annotation.key());
        if (annotation.limitType() == LimitType.IP) {
            // 如果需要对ip限流 需要append ip地址 rate_limit:ip-
            key.append(IpUtils.getIpAddr(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest())).append("-");
        }
        // append 全类名 ---------> rate_limit:ip-全类名-
//        key.append(signature.getDeclaringTypeName()).append("-");  getDeclaringTypeName()返回当前执行的方法所在类的全包名
        key.append(signature.getMethod().getDeclaringClass().getName()).append("-");
        // append 方法名 ---------> rate_limit:ip-全类名-方法名
        key.append(signature.getMethod().getName());
        int time = annotation.time();
        int count = annotation.count();
        try {
            Long current = template.execute(redisScript, Collections.singletonList(key.toString()), time, count);
            if (current == null || current.intValue() > count) {
                // 超过限流阈值
                logger.info("当前接口已达到最大限流次数");
                throw new RateLimitException("访问过于频繁，请稍后访问");
            }
            logger.info("当前接口阈值请求数:{},当前请求次数:{},缓存的key为:{}",count,current,key.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
