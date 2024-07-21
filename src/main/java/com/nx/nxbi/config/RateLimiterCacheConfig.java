package com.nx.nxbi.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 限流器缓存配置类
 *
 * @author Ni Xiang
 */
@Configuration
public class RateLimiterCacheConfig {
    @Value("${rate-limiter.initial-capacity}")
    private int initialCapacity;
    @Value("${rate-limiter.maximum-size}")
    private int maximumSize;
    @Value("${rate-limiter.permits-per-second}")
    private double permitsPerSecond;

    @Bean
    public LoadingCache<String, RateLimiter> rateLimiterCache() {
        return Caffeine.newBuilder()
                .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .build(key -> RateLimiter.create(permitsPerSecond));
    }
}