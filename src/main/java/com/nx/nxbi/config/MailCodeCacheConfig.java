package com.nx.nxbi.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


/**
 * 邮件验证码缓存配置类
 *
 * @author Ni Xiang
 */
@Configuration
public class MailCodeCacheConfig {
    @Value("${email-code.cache.initial-capacity}")
    private int initialCapacity;
    @Value("${email-code.cache.maximum-size}")
    private long maximumSize;
    @Value("${email-code.cache.time-out}")
    private long timeOut;

    @Bean
    public Cache<String, Integer> mailCodeCache() {
        return Caffeine.newBuilder()
                //初始大小
                .initialCapacity(initialCapacity)
                //最大条数
                .maximumSize(maximumSize)
                //过期时间
                .expireAfterWrite(timeOut, TimeUnit.SECONDS)
                .build();
    }
}
