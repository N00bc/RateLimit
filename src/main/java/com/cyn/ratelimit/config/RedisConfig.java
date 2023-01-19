package com.cyn.ratelimit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @author Godc
 * @description:
 * @date 2023/1/18 16:23
 */
@Configuration
public class RedisConfig {
    @Bean
    RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 设置kv序列化器
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        template.setKeySerializer(serializer);
        template.setHashKeySerializer(serializer);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        return template;
    }

    /**
     * 配置lua脚本
     * @return
     */
    @Bean
    DefaultRedisScript<Long> limitScript(){
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        // 设置返回值类型
        script.setResultType(Long.class);
        // 设置lua脚本路径
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/limit.lua")));
        return script;
    }
}
